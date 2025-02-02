package br.com.josenaldo.codeflix.domain.validation;

import java.util.List;

/**
 * Defines a contract for handling and aggregating validation errors within the domain.
 * <p>
 * Implementations of this interface allow for appending individual errors or errors from another
 * {@code ValidationHandler}, executing validation logic, and retrieving a list of accumulated
 * errors. This abstraction facilitates a flexible validation mechanism, enabling the combination
 * and processing of multiple validation error sources.
 * <p>
 * The interface also contains a nested {@link Validation} interface, which should be implemented by
 * classes that encapsulate specific validation logic to be executed by a
 * {@code ValidationHandler}.
 * <p>
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public interface ValidationHandler {

    /**
     * Represents a contract for encapsulating a validation process.
     * <p>
     * Implementations of this interface should define the logic within the {@code validate()}
     * method to perform specific validation tasks.
     */
    interface Validation {

        /**
         * Executes the validation logic.
         */
        void validate();
    }

    /**
     * Appends a single {@link Error} to the current validation handler.
     *
     * @param error The error to be appended.
     * @return The current instance of {@code ValidationHandler} with the appended error.
     */
    ValidationHandler append(Error error);

    /**
     * Appends errors from another {@code ValidationHandler} to this handler.
     *
     * @param validationHandler The validation handler whose errors are to be appended.
     * @return The current instance of {@code ValidationHandler} with the combined errors.
     */
    ValidationHandler append(ValidationHandler validationHandler);

    /**
     * Executes the provided validation logic and appends any errors encountered to this handler.
     *
     * @param validation An instance of {@link Validation} containing the validation logic.
     * @return The current instance of {@code ValidationHandler} after performing validation.
     */
    ValidationHandler validate(Validation validation);

    /**
     * Checks whether there are any errors accumulated in this validation handler.
     *
     * @return {@code true} if at least one error exists; {@code false} otherwise.
     */
    default boolean hasErrors() {
        return getErrors() != null && !getErrors().isEmpty();
    }

    /**
     * Retrieves a list of all errors accumulated during the validation process.
     *
     * @return A list of {@link Error} objects representing the validation errors.
     */
    List<Error> getErrors();

    /**
     * Retrieves the first error from the list of accumulated errors.
     *
     * @return The first {@link Error} if present; otherwise, {@code null} if there are no errors.
     */
    default Error fisrtError() {
        return getErrors().stream().findFirst().orElse(null);
    }
}
