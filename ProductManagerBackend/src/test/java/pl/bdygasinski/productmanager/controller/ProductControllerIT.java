package pl.bdygasinski.productmanager.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import pl.bdygasinski.productmanager.TestData;
import pl.bdygasinski.productmanager.logic.api.ProductService;
import pl.bdygasinski.productmanager.logic.exception.ExceptionFactory;
import pl.bdygasinski.productmanager.model.Product;

import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Integration tests between controller context and ProductController")
@WebMvcTest(ProductController.class)
class ProductControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @BeforeEach
    void setUp() {
        TestData.resetCounter();
    }

    private static Stream<List<Product>> provideList() {
        TestData.resetCounter();
        return Stream.of(
            List.of(TestData.getDefaultProduct()),
            List.of(TestData.getDefaultProduct(), TestData.getDefaultProduct()),
            List.of(TestData.getDefaultProduct(), TestData.getDefaultProduct(), TestData.getDefaultProduct())
        );
    }

    @ParameterizedTest
    @MethodSource("provideList")
    @DisplayName("Should return ok with whole data")
    void findAll_positive1(List<Product> data) throws Exception {
        PageRequest givenPageRequest = PageRequest.ofSize(data.isEmpty() ? 1 : data.size());
        Page<Product> givenPage = new PageImpl<>(data, givenPageRequest, data.size());
        given(productService.findAll(any())).willReturn(givenPage);

        mockMvc.perform(get("/api/v1/products"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(data.size())))
            .andExpect(jsonPath("$.content[*].name", containsInAnyOrder(data.stream()
                .map(Product::getName)
                .toArray())))
            .andExpect(jsonPath("$.content[*].price", containsInAnyOrder(data.stream()
                .map(p -> p.getPrice().getValue())
                .toArray())));
    }

    @ParameterizedTest
    @MethodSource("provideList")
    @DisplayName("Should return requested page with requested size")
    void findAll_positive2(List<Product> data) throws Exception {
        int givenSize = 1;
        int givenPageNr = 0;
        PageRequest givenPageRequest = PageRequest.of(givenPageNr, givenSize);
        Page<Product> givenPage = new PageImpl<>(data.stream().limit(givenSize).toList(), givenPageRequest, data.size());
        given(productService.findAll(givenPageRequest)).willReturn(givenPage);

        mockMvc.perform(get("/api/v1/products?page=%d&size=%d".formatted(givenPageNr, givenSize)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(givenSize)))
            .andExpect(jsonPath("$.content[*].name", contains(data.stream()
                .limit(givenSize)
                .map(Product::getName)
                .toArray())))
            .andExpect(jsonPath("$.content[*].price", contains(data.stream()
                .limit(givenSize)
                .map(p -> p.getPrice().getValue())
                .toArray())));
    }

    @Test
    @DisplayName("Should find product when service has it")
    void findById_positive1() throws Exception {
        Product givenProduct = TestData.getDefaultProduct();
        Long givenId = givenProduct.getId();
        given(productService.findById(givenId)).willReturn(givenProduct);

        mockMvc.perform(get("/api/v1/products/id/%d".formatted(givenId)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is((givenId.intValue()))))
            .andExpect(jsonPath("$.name", is(givenProduct.getName())))
            .andExpect(jsonPath("$.price", is(givenProduct.getPrice().getValue())));
    }


    @Test
    @DisplayName("Should invoke deleteById method in service and returns not content")
    void delete_positive1() throws Exception {
        Product givenProduct = TestData.getDefaultProduct();
        Long givenId = givenProduct.getId();

        mockMvc.perform(delete("/api/v1/products/id/%d".formatted(givenId)))
            .andDo(print())
            .andExpect(status().isNoContent());

        then(productService).should().deleteById(givenId);
    }

    @Test
    @DisplayName("Should invoke deleteById and return 404")
    void delete_negative1() throws Exception {
        Long givenId = 1L;
        doThrow(ExceptionFactory.createProductNotFoundException()).when(productService).deleteById(givenId);

        mockMvc.perform(delete("/api/v1/products/id/%d".formatted(givenId)))
            .andDo(print())
            .andExpect(status().isNotFound());

        then(productService).should().deleteById(givenId);
    }
}