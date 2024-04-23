package pl.bdygasinski.productmanager.logic.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.bdygasinski.productmanager.model.Product;

public interface ProductService {

    Page<Product> findAll(Pageable pageable);

    Product findById(Long id);

    Product create(Product product);

    Product update(Product product, Long id);

    void deleteById(Long id);
}
