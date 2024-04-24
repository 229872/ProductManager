package pl.bdygasinski.productmanager.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.bdygasinski.productmanager.logic.api.ProductService;
import pl.bdygasinski.productmanager.model.Product;

import java.net.URI;

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

    @GetMapping("/id/{id}")
    Product findById(@PathVariable Long id) {
        return productService.findById(id);
    }

    @PostMapping
    ResponseEntity<Product> create(@RequestBody @Valid Product product) {
        Product responseBody = productService.create(product);
        URI uri = URI.create("/id/%d".formatted(product.getId()));
        return ResponseEntity.created(uri).body(responseBody);
    }

    @DeleteMapping("/id/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/id/{id}")
    Product update(@PathVariable Long id, @RequestBody @Valid Product product) {
        return productService.update(product, id);
    }
}
