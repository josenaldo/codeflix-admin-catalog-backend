package br.com.josenaldo.codeflix.catalog.domain.validation;

import java.util.List;

/**
 * Defines a contract for handling and aggregating validation errors within the domain.
 * <p>
 * Implementations of this interface support two common strategies:
 * <ul>
 *   <li>Accumulating errors for later inspection (e.g., a notification-style handler).</li>
 *   <li>Failing fast by throwing a domain exception upon validation problems.</li>
 * </ul>
 * <p>
 * A handler can:
 * <ul>
 *   <li>Append individual errors.</li>
 *   <li>Append errors from another handler.</li>
 *   <li>Execute a unit of validation logic.</li>
 *   <li>Expose collected errors for inspection.</li>
 * </ul>
 * <p>
 * The interface also contains a nested {@link Validation} contract to encapsulate validation logic
 * to be executed by a {@code ValidationHandler}.
 * <p>
 * General contracts:
 * <ul>
 *   <li>{@link #getErrors()} never returns {@code null}; it may return an empty list.</li>
 *   <li>{@link #hasErrors()} reflects whether at least one error is currently available.</li>
 *   <li>Null inputs are not permitted unless explicitly documented; implementations may reject them.</li>
 * </ul>
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public interface ValidationHandler {

    /**
     * Represents a contract for encapsulating a validation process.
     * <p>
     * Implementations provide the validation steps inside {@link #validate()} and may throw
     * exceptions to signal failures. A {@code ValidationHandler} implementation decides whether to
     * aggregate such failures as {@link Error}s or to propagate them as exceptions.
     *
     * @param <T> the type of the result produced by the validation routine.
     */
    interface Validation<T> {

        /**
         * Executes the validation logic.
         *
         * @return the result produced by the validation, which may be {@code null} depending on the
         * use case.
         */
        T validate();
    }

    /**
     * Appends a single {@link Error} to this handler.
     * <p>
     * Implementations that follow a fail-fast strategy may throw an exception instead of storing
     * the error. Accumulating implementations add the error to their internal collection.
     *
     * @param error the error to append; must not be {@code null}.
     * @return this handler instance to enable method chaining in accumulating implementations.
     * @throws RuntimeException if the implementation rejects appending (e.g., fail-fast handlers).
     */
    ValidationHandler append(Error error);

    /**
     * Appends all errors from another handler to this handler.
     * <p>
     * Implementations that follow a fail-fast strategy may throw an exception instead of storing
     * the errors. Accumulating implementations merge the provided errors with their own.
     *
     * @param validationHandler the source handler whose errors are to be appended; must not be
     *                          {@code null}.
     * @return this handler instance to enable method chaining in accumulating implementations.
     * @throws RuntimeException if the implementation rejects appending (e.g., fail-fast handlers).
     */
    ValidationHandler append(ValidationHandler validationHandler);

    /**
     * Executes the provided validation logic.
     * <p>
     * Behavior on failures depends on the implementation:
     * <ul>
     *   <li>Accumulating implementations typically capture failures as {@link Error}s.</li>
     *   <li>Fail-fast implementations typically propagate failures as exceptions.</li>
     * </ul>
     *
     * @param validation the validation routine to execute; must not be {@code null}.
     * @param <T>        the type of the value returned by the validation routine.
     * @return the value returned by the executed validation routine.
     * @throws RuntimeException if the implementation propagates validation failures.
     */
    <T> T validate(Validation<T> validation);

    /**
     * Checks whether there are any errors accumulated in this handler.
     *
     * @return {@code true} if at least one error exists; {@code false} otherwise.
     */
    default boolean hasErrors() {
        return getErrors() != null && !getErrors().isEmpty();
    }

    /**
     * Retrieves all errors accumulated during validation.
     * <p>
     * Contract:
     * <ul>
     *   <li>Never returns {@code null}; may return an empty list when no errors are present.</li>
     *   <li>Mutability of the returned list is implementation-specific and must not be assumed.</li>
     * </ul>
     *
     * @return a list of {@link Error} instances; never {@code null}.
     */
    List<Error> getErrors();

    /**
     * Retrieves the first error from the list of accumulated errors.
     *
     * @return the first {@link Error} if present; otherwise {@code null}.
     */
    default Error fisrtError() {
        return getErrors().stream().findFirst().orElse(null);
    }
}
