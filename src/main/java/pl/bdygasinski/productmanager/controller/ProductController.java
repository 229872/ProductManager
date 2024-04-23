package pl.bdygasinski.productmanager.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.bdygasinski.productmanager.logic.api.ProductService;
import pl.bdygasinski.productmanager.model.Product;

import static java.util.Objects.requireNonNull;
import static pl.bdygasinski.productmanager.controller.ApiVersion.API_ROOT_PATH;

@RestController
@RequestMapping(API_ROOT_PATH + "/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        requireNonNull(productService, "ProductController requires non null ProductService");
        this.productService = productService;
    }

    @GetMapping
    Page<Product> findAll(Pageable pageable) {
        return productService.findAll(pageable);
    }
}
