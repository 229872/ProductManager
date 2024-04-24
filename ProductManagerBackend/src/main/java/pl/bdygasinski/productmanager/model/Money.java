package pl.bdygasinski.productmanager.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

@Embeddable
public record Money(@JsonProperty("price") BigDecimal value) {

    public Money {
        if (value.doubleValue() < 0) {
            throw new IllegalArgumentException("Value must be positive");
        }
    }

    public Double getValue() {
        return value.doubleValue();
    }

}
