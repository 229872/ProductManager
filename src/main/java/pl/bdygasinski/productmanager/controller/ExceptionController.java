package pl.bdygasinski.productmanager.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import pl.bdygasinski.productmanager.dto.ResponseExceptionBody;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toMap;

@RestControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {

        log.warn("Validation exception occurred: ", ex);
        Map<String, String> violations = ex.getFieldErrors().stream()
            .collect(toMap(
                FieldError::getField,
                fieldError -> Optional.ofNullable(fieldError.getDefaultMessage()).orElse("Not valid")
            ));

        return ResponseEntity.badRequest().body(violations);
    }

    @ExceptionHandler(ResponseStatusException.class)
    ResponseEntity<?> handleResponseStatusException(ResponseStatusException e) {

        log.warn("Application exception occurred: ", e);
        ResponseExceptionBody responseBody = ResponseExceptionBody.builder()
            .timestamp(LocalDateTime.now())
            .message(e.getReason())
            .build();

        return ResponseEntity.status(e.getStatusCode().value()).body(responseBody);
    }
}
