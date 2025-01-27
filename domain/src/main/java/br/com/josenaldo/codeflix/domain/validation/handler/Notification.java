package br.com.josenaldo.codeflix.domain.validation.handler;

import br.com.josenaldo.codeflix.domain.exceptions.DomainException;
import br.com.josenaldo.codeflix.domain.validation.Error;
import br.com.josenaldo.codeflix.domain.validation.ValidationHandler;
import java.util.ArrayList;
import java.util.List;

/**
 * The Notification class is a concrete implementation of the ValidationHandler interface. It is
 * used to collect and manage validation errors during the validation process. This class provides
 * methods to append errors, validate objects, and check for the presence of errors.
 */
public class Notification implements ValidationHandler {

    private final List<Error> errors;

    /**
     * Private constructor to initialize the Notification with a list of errors.
     *
     * @param errors the list of errors to initialize the Notification with.
     */
    private Notification(final List<Error> errors) {
        this.errors = errors;
    }

    /**
     * Creates a new Notification instance with an empty list of errors.
     *
     * @return a new Notification instance.
     */
    public static Notification create() {
        return new Notification(new ArrayList<>());
    }

    /**
     * Creates a new Notification instance with a single error.
     *
     * @param error the error to initialize the Notification with.
     * @return a new Notification instance.
     */
    public static Notification create(final Error error) {
        return new Notification(new ArrayList<>()).append(error);
    }

    /**
     * Creates a new Notification instance with a single error.
     *
     * @param throwable the throwable to initialize the Notification with.
     * @return a new Notification instance.
     */
    public static Notification create(Throwable throwable) {
        return create(new Error(throwable.getMessage()));
    }

    /**
     * Appends a new error to the Notification.
     *
     * @param error the error to append.
     * @return the current Notification instance.
     */
    @Override
    public Notification append(final Error error) {
        this.errors.add(error);
        return this;
    }

    /**
     * Appends all errors from another ValidationHandler to the current Notification.
     *
     * @param validationHandler the ValidationHandler whose errors are to be appended.
     * @return the current Notification instance.
     */
    @Override
    public Notification append(final ValidationHandler validationHandler) {
        this.errors.addAll(validationHandler.getErrors());
        return null;
    }

    /**
     * Validates an object using the provided validation logic. If a DomainException is thrown, its
     * errors are added to the Notification. If any other exception is thrown, its message is added
     * as an error.
     *
     * @param validation the validation logic to execute.
     * @return the current Notification instance.
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
     * Checks if the Notification has any errors.
     *
     * @return true if there are errors, false otherwise.
     */
    @Override
    public boolean hasErrors() {
        return !this.errors.isEmpty();
    }

    /**
     * Returns the list of errors in the Notification.
     *
     * @return the list of errors.
     */
    @Override
    public List<Error> getErrors() {
        return this.errors;
    }


}
