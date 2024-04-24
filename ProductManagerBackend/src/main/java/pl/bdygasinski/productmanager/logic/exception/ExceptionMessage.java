package pl.bdygasinski.productmanager.logic.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionMessage {

    public static final String PRODUCT_NOT_FOUND = "product.not.found";
    public static final String UNKNOWN = "unknown.exception";
}
