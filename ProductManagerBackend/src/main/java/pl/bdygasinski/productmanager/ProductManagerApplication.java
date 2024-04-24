package pl.bdygasinski.productmanager;

import com.github.javafaker.Faker;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.bdygasinski.productmanager.model.Money;
import pl.bdygasinski.productmanager.model.Product;
import pl.bdygasinski.productmanager.repository.api.ProductRepository;

import java.math.BigDecimal;
import java.util.stream.IntStream;

@SpringBootApplication
public class ProductManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductManagerApplication.class, args);
    }

    private final ProductRepository productRepository;

    public ProductManagerApplication(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Value("${init-data}")
    private boolean initData;

    @PostConstruct
    void initApplicationData() {
        Faker faker = new Faker();
        if (initData) {
            IntStream.rangeClosed(1, 100)
                .boxed()
                .map(i -> Product.builder()
                    .name(faker.regexify("[A-Z][A-Za-z]{1,100}"))
                    .price(new Money(BigDecimal.valueOf(Double.parseDouble(faker.commerce().price(120.0, 5000.0)))))
                    .build())
                .forEach(productRepository::save);
        }
    }
}
