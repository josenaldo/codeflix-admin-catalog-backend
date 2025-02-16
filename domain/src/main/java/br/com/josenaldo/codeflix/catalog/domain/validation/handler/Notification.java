package br.com.josenaldo.codeflix.catalog.domain.validation.handler;

import br.com.josenaldo.codeflix.catalog.domain.exceptions.DomainException;
import br.com.josenaldo.codeflix.catalog.domain.validation.Error;
import br.com.josenaldo.codeflix.catalog.domain.validation.ValidationHandler;
import java.util.ArrayList;
import java.util.List;

/**
 * A concrete implementation of the {@link ValidationHandler} interface that collects and manages
 * validation errors during the validation process.
 * <p>
 * The {@code Notification} class allows appending individual errors or errors from another
 * validation handler, executing validation logic, and checking whether any errors have been
 * collected. It is useful in scenarios where validation errors need to be aggregated and processed
 * after validation is complete.
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public class Notification implements ValidationHandler {

    /**
     * The list that holds the collected validation errors.
     */
    private final List<Error> errors;

    /**
     * Private constructor to initialize the {@code Notification} with a given list of errors.
     *
     * @param errors the list of errors used to initialize the notification.
     */
    private Notification(final List<Error> errors) {
        this.errors = errors;
    }

    /**
     * Creates a new {@code Notification} instance with an empty list of errors.
     *
     * @return a new {@code Notification} instance with no errors.
     */
    public static Notification create() {
        return new Notification(new ArrayList<>());
    }

    /**
     * Creates a new {@code Notification} instance initialized with a single error.
     *
     * @param error the error with which to initialize the notification.
     * @return a new {@code Notification} instance containing the specified error.
     */
    public static Notification create(final Error error) {
        return new Notification(new ArrayList<>()).append(error);
    }

    /**
     * Creates a new {@code Notification} instance initialized with an error derived from a
     * throwable.
     *
     * @param throwable the throwable from which the error message will be extracted.
     * @return a new {@code Notification} instance containing the error message from the throwable.
     */
    public static Notification create(Throwable throwable) {
        return create(new Error(throwable.getMessage()));
    }

    /**
     * Appends a new error to the notification.
     *
     * @param error the error to append.
     * @return the current {@code Notification} instance with the appended error.
     */
    @Override
    public Notification append(final Error error) {
        this.errors.add(error);
        return this;
    }

    /**
     * Appends all errors from another {@link ValidationHandler} to the current notification.
     *
     * @param validationHandler the validation handler whose errors are to be appended.
     * @return the current {@code Notification} instance with the combined errors.
     * <p>
     * <strong>Note:</strong> The current implementation erroneously returns {@code null}.
     * This may be a mistake, and typically the method should return the current instance.
     */
    @Override
    public Notification append(final ValidationHandler validationHandler) {
        this.errors.addAll(validationHandler.getErrors());
        return this;
    }

    /**
     * Executes the provided validation logic. If a {@link DomainException} is thrown, its errors
     * are added to the notification. If any other exception is thrown, its message is added as a
     * new error.
     *
     * @param validation the validation logic to execute.
     * @return the current {@code Notification} instance after attempting validation.
     */
    @Override
    public Notification validate(Validation validation) {
        try {
            validation.validate();
        } catch (final DomainException e) {
            this.errors.addAll(e.getErrors());
        } catch (final Exception e) {
            this.errors.add(new Error(e.getMessage()));
        }
        return this;
    }

    /**
     * Checks whether the notification has any collected errors.
     *
     * @return {@code true} if there is at least one error; {@code false} otherwise.
     */
    @Override
    public boolean hasErrors() {
        return !this.errors.isEmpty();
    }

    /**
     * Retrieves the list of errors collected in the notification.
     *
     * @return a list of {@link Error} objects representing the validation errors.
     */
    @Override
    public List<Error> getErrors() {
        return this.errors;
    }
}
