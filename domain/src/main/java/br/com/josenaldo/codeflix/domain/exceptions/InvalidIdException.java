package br.com.josenaldo.codeflix.domain.exceptions;

public class InvalidIdException extends RuntimeException {

  public InvalidIdException() {
  }

  public InvalidIdException(String message) {
    super(message);
  }

  public InvalidIdException(String message, Throwable cause) {
    super(message, cause);
  }

  public InvalidIdException(Throwable cause) {
    super(cause);
  }
}
