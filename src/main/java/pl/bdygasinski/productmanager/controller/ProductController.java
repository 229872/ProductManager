package pl.bdygasinski.productmanager.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.bdygasinski.productmanager.model.Product;
import pl.bdygasinski.productmanager.repository.api.ProductRepository;

import static java.util.Objects.requireNonNull;
import static pl.bdygasinski.productmanager.controller.ApiVersion.API_ROOT_PATH;

@RestController
@RequestMapping(API_ROOT_PATH + "/products")
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        requireNonNull(productRepository, "ProductController requires non null product repository");
        this.productRepository = productRepository;
    }

    @GetMapping
    Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
}
