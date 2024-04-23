package pl.bdygasinski.productmanager.logic.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionFactory {

    public static ResponseStatusException createProductNotFoundException() {
        return new ProductNotFoundException(HttpStatus.NOT_FOUND, ExceptionMessage.PRODUCT_NOT_FOUND);
    }

    public static ResponseStatusException createUnknownException() {
        return new UnknownException(HttpStatus.CONFLICT, ExceptionMessage.UNKNOWN);
    }
}
