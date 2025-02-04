package br.com.josenaldo.codeflix.domain.exceptions;

import br.com.josenaldo.codeflix.domain.validation.Error;
import java.util.List;

/**
 * Represents a domain exception that encapsulates one or more validation errors.
 * <p>
 * This exception extends {@link NoStackTraceException} and is used to indicate domain-specific
 * validation issues. It carries a list of {@link Error} instances that provide detailed error
 * information.
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public class DomainException extends NoStackTraceException {

    /**
     * The list of errors associated with this exception.
     */
    private final List<Error> errors;

    /**
     * Private constructor to create a new {@code DomainException} with a message and a list of
     * errors.
     *
     * @param message The detail message for this exception.
     * @param errors  The list of {@link Error} objects associated with this exception.
     */
    private DomainException(String message, List<Error> errors) {
        super(message);
        this.errors = errors;
    }

    /**
     * Creates a new {@code DomainException} with the specified list of errors.
     *
     * @param errors The list of {@link Error} objects to associate with this exception.
     * @return A new {@code DomainException} containing the provided errors.
     */
    public static DomainException with(final List<Error> errors) {
        return new DomainException(errors.getFirst().message(), errors);
    }

    /**
     * Creates a new {@code DomainException} with the specified error.
     *
     * @param error The {@link Error} object to associate with this exception.
     * @return A new {@code DomainException} containing the provided error.
     */
    public static DomainException with(final Error error) {
        return new DomainException(error.message(), List.of(error));
    }

    /**
     * Creates a new {@code DomainException} with the specified message.
     *
     * @param message The detail message for this exception.
     * @return A new {@code DomainException} containing the provided message.
     */
    public static DomainException with(final String message) {
        return new DomainException(message, List.of(new Error(message)));
    }
    /**
     * Retrieves the list of errors associated with this exception.
     *
     * @return The list of {@link Error} objects.
     */
    public List<Error> getErrors() {
        return errors;
    }
}
