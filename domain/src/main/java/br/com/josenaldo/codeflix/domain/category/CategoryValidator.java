package br.com.josenaldo.codeflix.domain.category;

import br.com.josenaldo.codeflix.domain.validation.Error;
import br.com.josenaldo.codeflix.domain.validation.ValidationHandler;
import br.com.josenaldo.codeflix.domain.validation.Validator;

public class CategoryValidator extends Validator {

    public static final int NAME_MAX_LENGTH = 255;
    public static final int NAME_MIN_LENGTH = 3;
    public static final String NULL_NAME_ERROR = "'name' should not be null";
    public static final String EMPTY_NAME_ERROR = "'name' should not be empty";
    public static final String NAME_LENGTH_OUT_OF_RANGE_ERROR = "'name' length must be between 3 and 255 characters";

    private final Category category;

    public CategoryValidator(final Category category, final ValidationHandler validationHandler) {
        super(validationHandler);
        this.category = category;
    }

    @Override
    public void validate() {
        extracted();
    }

    private void extracted() {
        checkNameConstraints();
    }

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
            return;
        }
    }
}
