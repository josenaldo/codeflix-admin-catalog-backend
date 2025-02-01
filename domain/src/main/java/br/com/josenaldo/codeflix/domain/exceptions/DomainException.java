package br.com.josenaldo.codeflix.domain.exceptions;

import br.com.josenaldo.codeflix.domain.validation.Error;
import java.util.List;

public class DomainException extends NoStackTraceException {

  private final List<Error> errors;

  private DomainException(String message, List<Error> errors) {
    super(message);
    this.errors = errors;
  }

  public static DomainException with(final List<Error> errors) {
      return new DomainException(errors.getFirst().message(), errors);
  }

  public static DomainException with(final Error error) {
    return new DomainException(error.message(), List.of(error));
  }

  public List<Error> getErrors() {
    return errors;
  }

}
