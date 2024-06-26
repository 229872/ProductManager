package pl.bdygasinski.productmanager;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.bdygasinski.productmanager.model.Money;
import pl.bdygasinski.productmanager.model.Product;

import java.math.BigDecimal;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestData {
    public static final String NAME = "TV";
    public static final Money PRICE = new Money(BigDecimal.valueOf(1000.0));
    private static Long counter = 0L;

    public static void resetCounter() {
        counter = 0L;
    }

    public static Product.ProductBuilder<?, ?> getDefaultBuilder() {
        counter++;
        return Product.builder()
            .id(counter)
            .version(0L)
            .name("%s%d".formatted(NAME, counter))
            .price(PRICE);
    }

    public static Product getDefaultProduct() {
        return getDefaultBuilder().build();
    }
}
