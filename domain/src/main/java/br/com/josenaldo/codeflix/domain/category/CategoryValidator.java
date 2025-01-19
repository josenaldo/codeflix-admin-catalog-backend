package br.com.josenaldo.codeflix.domain.category;

import br.com.josenaldo.codeflix.domain.validation.Error;
import br.com.josenaldo.codeflix.domain.validation.ValidationHandler;
import br.com.josenaldo.codeflix.domain.validation.Validator;

public class CategoryValidator extends Validator {

  private final Category category;

  public CategoryValidator(final Category category, final ValidationHandler validationHandler) {
    super(validationHandler);
    this.category = category;
  }

  @Override
  public void validate() {
    if (category.getName() == null || category.getName().isEmpty()) {
      this.validationHandler().append(new Error("'name' should not be empty"));
    }
  }
}
