package br.com.josenaldo.codeflix.domain.validation;

/**
 * Abstract base class for domain validators.
 * <p>
 * This class provides a common foundation for all validator implementations by encapsulating a
 * {@link ValidationHandler} to collect and manage validation errors. Subclasses must implement the
 * {@link #validate()} method to perform specific validation logic.
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public abstract class Validator {

    /**
     * The handler used to collect validation errors.
     */
    private final ValidationHandler validationHandler;

    /**
     * Constructs a new {@code Validator} with the specified {@link ValidationHandler}.
     *
     * @param validationHandler The validation handler to which validation errors will be appended.
     */
    protected Validator(ValidationHandler validationHandler) {
        this.validationHandler = validationHandler;
    }

    /**
     * Returns the {@link ValidationHandler} associated with this validator.
     *
     * @return The validation handler used for collecting errors.
     */
    protected ValidationHandler validationHandler() {
        return validationHandler;
    }

    /**
     * Executes the validation logic.
     * <p>
     * Subclasses must override this method to implement domain-specific validation rules.
     */
    public abstract void validate();
}
