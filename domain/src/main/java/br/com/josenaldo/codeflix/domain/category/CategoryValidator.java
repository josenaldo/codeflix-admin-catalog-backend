package br.com.josenaldo.codeflix.domain.category;

import br.com.josenaldo.codeflix.domain.validation.Error;
import br.com.josenaldo.codeflix.domain.validation.ValidationHandler;
import br.com.josenaldo.codeflix.domain.validation.Validator;

public class CategoryValidator extends Validator {

  private static final int NAME_MAX_LENGTH = 255;
  private static final int NAME_MIN_LENGTH = 3;

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
      this.validationHandler().append(new Error("'name' should not be null"));
      return;
    }

    if (name.isBlank()) {
      this.validationHandler().append(new Error("'name' should not be empty"));
      return;
    }

    int length = name.trim().length();
    if (length < NAME_MIN_LENGTH || length > NAME_MAX_LENGTH) {
      this.validationHandler().append(new Error("'name' length must be between 3 and 255 characters"));
      return;
    }
  }
}
