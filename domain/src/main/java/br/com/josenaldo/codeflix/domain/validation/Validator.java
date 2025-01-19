package br.com.josenaldo.codeflix.domain.validation;

public abstract class Validator {

  private final ValidationHandler validationHandler;

  protected Validator(ValidationHandler validationHandler) {
    this.validationHandler = validationHandler;
  }

  protected ValidationHandler validationHandler() {
    return validationHandler;
  }

  public abstract void validate();
}
