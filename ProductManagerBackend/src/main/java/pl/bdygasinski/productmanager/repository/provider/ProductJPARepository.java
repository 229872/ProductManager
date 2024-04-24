package pl.bdygasinski.productmanager.repository.provider;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.bdygasinski.productmanager.model.Product;
import pl.bdygasinski.productmanager.repository.api.ProductRepository;

import java.util.List;

@Repository
interface ProductJPARepository extends JpaRepository<Product, Long>, ProductRepository {

}
