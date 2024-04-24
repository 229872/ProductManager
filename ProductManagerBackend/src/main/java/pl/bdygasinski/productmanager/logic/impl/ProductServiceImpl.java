package pl.bdygasinski.productmanager.logic.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.bdygasinski.productmanager.logic.api.ProductService;
import pl.bdygasinski.productmanager.logic.exception.ExceptionFactory;
import pl.bdygasinski.productmanager.model.Product;
import pl.bdygasinski.productmanager.repository.api.ProductRepository;

import static java.util.Objects.requireNonNull;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
@Slf4j
class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        requireNonNull(productRepository, "ProductService requires non null ProductRepository");
        this.productRepository = productRepository;
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Product findById(Long id) {
        return productRepository.findById(id)
            .orElseThrow(ExceptionFactory::createProductNotFoundException);
    }

    @Override
    public Product create(Product product) {
        try {
            return productRepository.save(product);

        } catch (DataAccessException e) {
            log.warn("Database exception: ", e);
            throw ExceptionFactory.createUnknownException();
        }
    }

    @Override
    public Product update(Product product, Long id) {
        Product foundProduct = productRepository.findById(id)
            .orElseThrow(ExceptionFactory::createProductNotFoundException);

        foundProduct.setName(product.getName());
        foundProduct.setPrice(product.getPrice());

        return productRepository.save(foundProduct);
    }

    @Override
    public void deleteById(Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(ExceptionFactory::createProductNotFoundException);

        productRepository.delete(product);
    }
}
