package pl.bdygasinski.productmanager.logic.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class UnknownException extends ResponseStatusException {

    UnknownException(HttpStatusCode status, String reason) {
        super(status, reason);
    }
}
