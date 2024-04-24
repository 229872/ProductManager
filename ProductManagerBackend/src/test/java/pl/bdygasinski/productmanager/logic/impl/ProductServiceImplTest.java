package pl.bdygasinski.productmanager.logic.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.web.server.ResponseStatusException;
import pl.bdygasinski.productmanager.TestData;
import pl.bdygasinski.productmanager.logic.exception.ExceptionMessage;
import pl.bdygasinski.productmanager.logic.exception.ProductNotFoundException;
import pl.bdygasinski.productmanager.logic.exception.UnknownException;
import pl.bdygasinski.productmanager.model.Money;
import pl.bdygasinski.productmanager.model.Product;
import pl.bdygasinski.productmanager.repository.api.ProductRepository;

import java.math.BigDecimal;
import java.rmi.UnknownHostException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("Unit tests for ProductServiceImpl")
@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl underTest;

    private static Stream<List<Product>> provideList() {
        return Stream.of(
            List.of(),
            List.of(TestData.getDefaultProduct()),
            List.of(TestData.getDefaultProduct(), TestData.getDefaultProduct())
        );
    }

    @ParameterizedTest
    @MethodSource("provideList")
    @DisplayName("Should return same as product repository")
    void findAll_positive_1(List<Product> givenList) {
        //given
        Pageable givenPageable = Pageable.unpaged();
        Page<Product> givenRepositoryOutput = new PageImpl<>(givenList, givenPageable, givenList.size());
        given(productRepository.findAll(givenPageable)).willReturn(givenRepositoryOutput);

        //when
        List<Product> result = underTest.findAll(givenPageable).toList();

        //then
        Assertions
            .assertThat(result)
            .isNotNull()
            .isEqualTo(givenList);
    }

    @Test
    @DisplayName("Should return Product when repository contains Product with given id")
    void findById_positive_1() {
        //given
        Product givenProduct = TestData.getDefaultProduct();
        Long givenId = givenProduct.getId();
        given(productRepository.findById(givenId)).willReturn(Optional.of(givenProduct));

        //when
        //then
        Assertions
            .assertThatCode(() -> underTest.findById(givenId))
            .doesNotThrowAnyException();

        Assertions
            .assertThat(underTest.findById(givenId))
            .isEqualTo(givenProduct);
    }

    @Test
    @DisplayName("""
            Should throw ProductNotFoundException with corresponding exception message when repository doesn't contain
            Product with given id
        """)
    void findById_negative_1() {
        //given
        Long givenId = 1L;
        given(productRepository.findById(givenId)).willReturn(Optional.empty());

        //when
        Exception exception = catchException(() -> underTest.findById(givenId));

        //then
        Assertions
            .assertThat(exception)
            .isNotNull()
            .isInstanceOf(ResponseStatusException.class)
            .isExactlyInstanceOf(ProductNotFoundException.class)
            .hasMessageContaining(ExceptionMessage.PRODUCT_NOT_FOUND);
    }

    @Test
    @DisplayName("Should return Product")
    void create_positive_1() {
        //given
        Product givenProduct = TestData.getDefaultProduct();
        given(productRepository.save(givenProduct)).willReturn(givenProduct);

        //when
        Product result = underTest.create(givenProduct);

        //then
        Assertions
            .assertThat(result)
            .isNotNull()
            .isEqualTo(givenProduct);
    }

    @Test
    @DisplayName("Should throw unknown exception when database exception happens")
    void create_negative_1() {
        //given
        Product givenProduct = TestData.getDefaultProduct();
        given(productRepository.save(givenProduct)).willThrow(new DataIntegrityViolationException(""));

        //when
        Exception exception = catchException(() -> underTest.create(givenProduct));

        //then
        Assertions
            .assertThat(exception)
            .isNotNull()
            .isInstanceOf(ResponseStatusException.class)
            .isExactlyInstanceOf(UnknownException.class)
            .hasMessageContaining(ExceptionMessage.UNKNOWN);
    }

    @Test
    @DisplayName("Should update product when repository contains product with given id")
    void update_positive_1() {
        //given
        Product givenProduct = TestData.getDefaultProduct();
        Money oldPrice = givenProduct.getPrice();
        Product newProductData = TestData.getDefaultBuilder()
            .price(new Money(new BigDecimal("1200")))
            .build();
        Long givenId = givenProduct.getId();
        given(productRepository.findById(givenId)).willReturn(Optional.of(givenProduct));
        given(productRepository.save(givenProduct)).willReturn(givenProduct);

        //when
        Product result = underTest.update(newProductData, givenId);

        //then
        Assertions
            .assertThat(result)
            .isNotNull();

        assertThat(result.getName())
            .isEqualTo(givenProduct.getName());
        assertThat(result.getPrice())
            .isEqualTo(newProductData.getPrice())
            .isNotEqualTo(oldPrice);
    }

    @Test
    @DisplayName("Should throw ProductNotFoundException when repository doesn't contain product with given id")
    void update_negative_1() {
        //given
        Product newProductData = TestData.getDefaultBuilder()
            .price(new Money(new BigDecimal("1200")))
            .build();
        Long givenId = 1L;
        given(productRepository.findById(givenId)).willReturn(Optional.empty());

        //when
        Exception exception = catchException(() -> underTest.update(newProductData, givenId));

        //then
        Assertions
            .assertThat(exception)
            .isNotNull()
            .isInstanceOf(ResponseStatusException.class)
            .isExactlyInstanceOf(ProductNotFoundException.class)
            .hasMessageContaining(ExceptionMessage.PRODUCT_NOT_FOUND);

        then(productRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("Should execute repository delete method when product with given id can be found in repository")
    void deleteById_positive_1() {
        //given
        Product givenProduct = TestData.getDefaultProduct();
        Long givenId = givenProduct.getId();
        given(productRepository.findById(givenId)).willReturn(Optional.of(givenProduct));

        //when
        //then
        Assertions
            .assertThatCode(() -> underTest.deleteById(givenId))
            .doesNotThrowAnyException();

        then(productRepository).should().delete(givenProduct);
    }

    @Test
    @DisplayName("""
            Should throw ProductNotFoundException with corresponding exception message when product with given id can't
            be found in repository.
        """)
    void deleteById_negative_1() {
        //given
        Long givenId = 1L;
        given(productRepository.findById(givenId)).willReturn(Optional.empty());

        //when
        Exception exception = catchException(() -> underTest.deleteById(givenId));

        //then
        Assertions
            .assertThat(exception)
            .isNotNull()
            .isInstanceOf(ResponseStatusException.class)
            .isExactlyInstanceOf(ProductNotFoundException.class)
            .hasMessageContaining(ExceptionMessage.PRODUCT_NOT_FOUND);
    }
}