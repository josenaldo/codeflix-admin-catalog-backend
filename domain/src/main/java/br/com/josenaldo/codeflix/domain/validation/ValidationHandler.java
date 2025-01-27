package br.com.josenaldo.codeflix.domain.validation;

import java.util.List;

public interface ValidationHandler {

  ValidationHandler append(Error error);

  ValidationHandler append(ValidationHandler validationHandler);

  ValidationHandler validate(Validation validation);

  default boolean hasErrors() {
    return getErrors() != null && !getErrors().isEmpty();
  }

  List<Error> getErrors();

  interface Validation {

    void validate();
  }

    default Error fisrtError() {
        return getErrors().stream().findFirst().orElse(null);
    }
}
