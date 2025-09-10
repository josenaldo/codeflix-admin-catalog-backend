package br.com.josenaldo.codeflix.catalog.infrastructure.api.controllers;

import br.com.josenaldo.codeflix.catalog.domain.exceptions.DomainException;
import br.com.josenaldo.codeflix.catalog.domain.exceptions.NotFoundException;
import br.com.josenaldo.codeflix.catalog.domain.validation.Error;
import java.time.Instant;
import java.util.List;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Provides a centralized mechanism to handle exceptions across the application.
 * <p>
 * This class uses Spring’s {@code @RestControllerAdvice} to globally manage and transform
 * exceptions into uniform responses, ensuring consistent error handling. Specifically, it captures
 * specific exceptions and converts them to meaningful responses following the problem details
 * standard (RFC 9457).
 * <p>
 * The main exceptions handled include:
 * <ul>
 *   <li>{@link MethodArgumentNotValidException} for validation errors.</li>
 *   <li>{@link DomainException} for domain-specific validation issues.</li>
 *   <li>{@link NotFoundException} for resource not found scenarios.</li>
 * </ul>
 * <p>
 * Each exception is mapped to an appropriate HTTP status code and a structured
 * response that follows a clear and predictable format.
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    private static final String VALIDATION_ERROR_TITLE = "Validation error";
    private static final String RESOURCE_NOT_FOUND_TITLE = "Resource not found";
    private static final String PROP_ERRORS = "errors";
    private static final String PROP_TIMESTAMP = "timestamp";
    public static final String VALIDATION_ERROR_DETAIL = "The provided data is not valid";

    /**
     * Handles exceptions of type {@code MethodArgumentNotValidException} that occur when method
     * arguments fail validation.
     * <p>
     * This method processes validation errors, extracts relevant field error information, and
     * constructs a {@code ProblemDetail} object containing the details of the validation failure.
     * The response includes:
     * <ul>
     *   <li>A status indicating the error (HTTP 422 Unprocessable Entity).</li>
     *   <li>A descriptive title for the validation error.</li>
     *   <li>Details of the validation failure.</li>
     *   <li>A list of specific validation errors, including field names and error messages.</li>
     * </ul>
     *
     * @param exception the {@code MethodArgumentNotValidException} that encapsulates the validation
     *                  errors. It contains details about the fields that failed validation and
     *                  their respective messages.
     * @return a {@code ProblemDetail} object with structured information about the validation
     * error. Includes HTTP status, error title, error detail, and a list of validation-specific
     * error messages.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValidException(final MethodArgumentNotValidException exception) {
        final List<Error> errors = exception
            .getBindingResult()
            .getFieldErrors()
            .stream()
            .map(fe -> new Error("field %s: %s".formatted(fe.getField(), fe.getDefaultMessage())))
            .toList();

        return newProblemDetail(
            HttpStatus.UNPROCESSABLE_ENTITY,
            VALIDATION_ERROR_TITLE,
            VALIDATION_ERROR_DETAIL,
            errors
        );
    }

    /**
     * Handles exceptions of type {@code DomainException} that occur during domain-specific
     * validation.
     * <p>
     * This method processes the provided domain exception, extracts relevant details, and
     * constructs a {@code ProblemDetail} object. The response includes:
     * <ul>
     *   <li>A status indicating the error (HTTP 422 Unprocessable Entity).</li>
     *   <li>A descriptive title for the validation error.</li>
     *   <li>The message detailing the validation failure.</li>
     *   <li>A list of specific domain-related validation errors, if available.</li>
     * </ul>
     *
     * @param exception the {@code DomainException} containing information about domain-specific
     *                  validation errors. It includes a message and a list of associated error
     *                  details.
     * @return a {@code ProblemDetail} object encapsulating structured information about the domain
     * validation error, including HTTP status, error title, a detailed description of the error,
     * and a list of related validation issues.
     * @throws NullPointerException if the {@code exception} parameter is null.
     */
    @ExceptionHandler(DomainException.class)
    public ProblemDetail handleDomainException(final DomainException exception) {
        return newProblemDetail(
            HttpStatus.UNPROCESSABLE_ENTITY,
            VALIDATION_ERROR_TITLE,
            exception.getMessage(),
            exception.getErrors()
        );

    }

    /**
     * Handles exceptions of type {@code NotFoundException} indicating that a requested resource
     * could not be found.
     *
     * <p>
     * This method processes the provided {@code NotFoundException} and constructs a
     * {@code ProblemDetail} object. The response includes:
     * <ul>
     *   <li>A status indicating the error (HTTP 404 Not Found).</li>
     *   <li>A descriptive title for the resource not found error.</li>
     *   <li>The message providing details of the missing resource.</li>
     *   <li>A list of specific errors, if available.</li>
     * </ul>
     * </p>
     *
     * @param exception the {@code NotFoundException} containing information about the missing
     *                  resource, including a message and any associated error details.
     * @return a {@code ProblemDetail} object encapsulating structured information about the
     * resource not found error, including HTTP status, error title, a detailed description of the
     * error, and a list of related issues if provided.
     */
    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleNotFoundException(final NotFoundException exception) {
        return newProblemDetail(
            HttpStatus.NOT_FOUND,
            RESOURCE_NOT_FOUND_TITLE,
            exception.getMessage(),
            exception.getErrors()
        );
    }

    /**
     * Creates a new {@code ProblemDetail} object encapsulating structured information about an
     * error.
     * <p>
     * This method constructs a {@code ProblemDetail} object based on the provided status, title,
     * detail, and a list of errors. It also adds additional properties like a timestamp.
     *
     * @param status the HTTP status code associated with the problem. Must not be {@code null}.
     * @param title  the short, human-readable summary of the problem. Must not be {@code null}.
     * @param detail a detailed explanation of the problem. Can be {@code null} if not necessary.
     * @param errors a list of specific error details related to the problem. Can be {@code null},
     *               in which case an empty list will be associated with the problem.
     * @return a {@code ProblemDetail} object populated with the provided attributes and additional
     * properties, including a timestamp. If {@code errors} is {@code null}, an empty list is
     * included instead.
     * @throws NullPointerException if {@code status} or {@code title} is {@code null}.
     */
    private static ProblemDetail newProblemDetail(
        final HttpStatus status,
        final String title,
        final String detail,
        final List<Error> errors
    ) {
        final var pd = ProblemDetail.forStatusAndDetail(status, detail);
        pd.setTitle(title);
        pd.setDetail(detail);
        pd.setProperty(PROP_ERRORS, errors == null ? List.of() : errors);
        pd.setProperty(PROP_TIMESTAMP, Instant.now());

        return pd;
    }
}
