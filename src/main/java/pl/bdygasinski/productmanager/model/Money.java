package pl.bdygasinski.productmanager.model;

import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

@Embeddable
public record Money(BigDecimal value) {

    public Money {
        if (value.doubleValue() < 0) {
            throw new IllegalArgumentException("Value must be positive");
        }
    }
}
