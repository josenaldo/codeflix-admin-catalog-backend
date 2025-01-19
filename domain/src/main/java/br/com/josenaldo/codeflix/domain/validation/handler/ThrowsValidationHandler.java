package br.com.josenaldo.codeflix.domain.validation.handler;

import br.com.josenaldo.codeflix.domain.exceptions.DomainException;
import br.com.josenaldo.codeflix.domain.validation.Error;
import br.com.josenaldo.codeflix.domain.validation.ValidationHandler;
import java.util.List;

public class ThrowsValidationHandler implements ValidationHandler {

  @Override
  public ValidationHandler append(Error error) {
    List<Error> errorList = List.of(error);
    throw DomainException.with(errorList);
  }

  @Override
  public ValidationHandler append(ValidationHandler validationHandler) {
    throw DomainException.with(validationHandler.getErrors());
  }

  @Override
  public ValidationHandler validate(Validation validation) {
    try {
      validation.validate();
    } catch (final Exception exception) {
      String message = exception.getMessage();
      List<Error> errors = List.of(new Error(message));
      throw DomainException.with(errors);
    }

    return this;
  }

  @Override
  public boolean hasErrors() {
    return ValidationHandler.super.hasErrors();
  }

  @Override
  public List<Error> getErrors() {
    return List.of();
  }
}
