package br.com.josenaldo.codeflix.catalog.infrastructure.api.controllers;

import br.com.josenaldo.codeflix.catalog.domain.exceptions.DomainException;
import br.com.josenaldo.codeflix.catalog.domain.validation.Error;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<?> handleDomainException(final DomainException exception) {
        // Log the exception and return a generic error response
        return ResponseEntity.unprocessableEntity().body(ApiError.from(exception));
    }

    static record ApiError(
        String message,
        List<Error> errors
    ) {

        static ApiError from(final DomainException exception) {
            return new ApiError(
                exception.getMessage(),
                exception.getErrors()
            );
        }
    }
}
