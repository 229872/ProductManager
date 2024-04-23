package pl.bdygasinski.productmanager.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SuperBuilder
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)

@Entity
@Table(name = "products")
public class Product extends AbstractEntity {

    @NotBlank @Pattern(regexp = "^[A-Z][A-Za-z]{1,100}$")
    @Column(nullable = false, length = 100)
    private String name;

    @NotNull @Positive
    @AttributeOverride(name = "value", column = @Column(name = "price", nullable = false))
    private Money price;

}
