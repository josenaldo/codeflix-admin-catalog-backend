package br.com.josenaldo.codeflix.catalog.domain.category;

import br.com.josenaldo.codeflix.catalog.domain.validation.Error;
import br.com.josenaldo.codeflix.catalog.domain.validation.ValidationHandler;
import br.com.josenaldo.codeflix.catalog.domain.validation.Validator;

/**
 * Validator for Category entities that checks if the category's name meets specific constraints.
 * <p>
 * This class verifies that the category name is not null, not blank, and that its trimmed length is
 * within the allowed range. It collects any validation errors through the provided
 * {@link ValidationHandler}.
 * <p>
 * The defined name constraints include a minimum length of 3 characters and a maximum length of 255
 * characters.
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public class CategoryValidator extends Validator {

    /**
     * Maximum allowed length for a category name.
     */
    public static final int NAME_MAX_LENGTH = 255;

    /**
     * Minimum required length for a category name.
     */
    public static final int NAME_MIN_LENGTH = 3;

    /**
     * Error message indicating that the name is null.
     */
    public static final String NULL_NAME_ERROR = "'name' should not be null";

    /**
     * Error message indicating that the name is empty.
     */
    public static final String EMPTY_NAME_ERROR = "'name' should not be empty";

    /**
     * Error message indicating that the name's length is out of the allowed range.
     */
    public static final String NAME_LENGTH_OUT_OF_RANGE_ERROR = "'name' length must be between 3 and 255 characters";

    /**
     * The Category instance to be validated.
     */
    private final Category category;

    /**
     * Constructs a new CategoryValidator with the specified Category and ValidationHandler.
     * <p>
     * The provided category is the object whose name will be validated, and the validationHandler
     * is used to collect any validation errors encountered during the process.
     *
     * @param category          the Category to validate.
     * @param validationHandler the handler used to collect validation errors.
     */
    public CategoryValidator(final Category category, final ValidationHandler validationHandler) {
        super(validationHandler);
        this.category = category;
    }

    /**
     * Executes the validation logic for the category.
     * <p>
     * This method triggers the internal validation process by calling the checkNameConstraints method that
     * performs the name constraint checks.
     */
    @Override
    public void validate() {
        checkNameConstraints();
    }

    /**
     * Checks if the category name adheres to the required constraints.
     * <p>
     * The validation includes:
     * <ul>
     *   <li>Ensuring the name is not null.</li>
     *   <li>Ensuring the name is not blank.</li>
     *   <li>Ensuring the trimmed name length is between {@code NAME_MIN_LENGTH} and {@code NAME_MAX_LENGTH}.</li>
     * </ul>
     * If any of these checks fail, an appropriate error is appended to the {@link ValidationHandler}.
     */
    private void checkNameConstraints() {
        final String name = category.getName();

        if (name == null) {
            this.validationHandler().append(new Error(NULL_NAME_ERROR));
            return;
        }

        if (name.isBlank()) {
            this.validationHandler().append(new Error(EMPTY_NAME_ERROR));
            return;
        }

        int length = name.trim().length();
        if (length < NAME_MIN_LENGTH || length > NAME_MAX_LENGTH) {
            this.validationHandler().append(new Error(NAME_LENGTH_OUT_OF_RANGE_ERROR));
        }
    }
}
