package br.com.josenaldo.codeflix.catalog.domain.validation.handler;

import br.com.josenaldo.codeflix.catalog.domain.exceptions.DomainException;
import br.com.josenaldo.codeflix.catalog.domain.validation.Error;
import br.com.josenaldo.codeflix.catalog.domain.validation.ValidationHandler;
import java.util.List;

/**
 * A {@link ValidationHandler} implementation that immediately throws a {@link DomainException} when
 * validation errors are encountered.
 * <p>
 * This handler is designed to propagate errors by throwing exceptions rather than accumulating them
 * for later inspection. It is useful in scenarios where immediate failure is desired upon
 * validation errors.
 * <p>
 * Note: All methods that attempt to append errors will result in a {@code DomainException} being
 * thrown.
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public class ThrowsValidationHandler implements ValidationHandler {

    /**
     * Appends a single validation error by throwing a {@link DomainException} with the given
     * error.
     *
     * @param error The validation error to be appended.
     * @return This method never returns normally.
     * @throws DomainException Always thrown with the provided error.
     */
    @Override
    public ValidationHandler append(Error error) {
        throw DomainException.with(error);
    }

    /**
     * Appends errors from another {@link ValidationHandler} by throwing a {@link DomainException}
     * with the errors from the provided handler.
     *
     * @param validationHandler The validation handler whose errors are to be appended.
     * @return This method never returns normally.
     * @throws DomainException Always thrown with the errors from the provided validation handler.
     */
    @Override
    public ValidationHandler append(ValidationHandler validationHandler) {
        throw DomainException.with(validationHandler.getErrors());
    }

    /**
     * Executes the provided validation logic and throws a {@link DomainException} if any exception
     * occurs during the validation process.
     * <p>
     * The method tries to execute the validation logic defined in the provided {@link Validation}
     * instance. If an exception occurs, it captures the exception message, wraps it into an
     * {@link Error}, and throws a {@code DomainException} with that error.
     *
     * @param validation The validation logic to execute.
     * @return This {@link ValidationHandler} instance if no exception occurs.
     * @throws DomainException If an exception is thrown during validation.
     */
    @Override
    public <T> T validate(Validation<T> validation) {
        try {
            return validation.validate();
        } catch (final Exception exception) {
            String message = exception.getMessage();
            List<Error> errors = List.of(new Error(message));
            throw DomainException.with(errors);
        }
    }

    /**
     * Checks whether there are any accumulated validation errors.
     * <p>
     * Since this handler throws exceptions immediately on error occurrences, it never accumulates
     * errors and this method will always indicate no errors.
     *
     * @return {@code false} as no errors are stored.
     */
    @Override
    public boolean hasErrors() {
        return ValidationHandler.super.hasErrors();
    }

    /**
     * Retrieves the list of accumulated validation errors.
     * <p>
     * As this handler is designed to throw exceptions instead of accumulating errors, this method
     * always returns an empty list.
     *
     * @return An empty list of errors.
     */
    @Override
    public List<Error> getErrors() {
        return List.of();
    }
}
