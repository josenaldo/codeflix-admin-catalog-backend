package br.com.josenaldo.codeflix.domain;

import br.com.josenaldo.codeflix.domain.exceptions.InvalidIdException;
import com.github.f4b6a3.ulid.Ulid;
import com.github.f4b6a3.ulid.UlidCreator;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class UlidId extends Identifier implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private final String value;

  private UlidId() {
    Ulid ulid = UlidCreator.getUlid();
    this.value = ulid.toString();
  }

  private UlidId(String value) {
    try {
      Ulid ulid = Ulid.from(value);
      this.value = ulid.toString();
    } catch (IllegalArgumentException e) {
      throw new InvalidIdException();
    }
  }

  public static UlidId generate() {
    return new UlidId();
  }

  public static UlidId from(String aValue) {
    return new UlidId(aValue);
  }

  public String getValue() {
    return value;
  }

  public String toString() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UlidId ulidId = (UlidId) o;
    return Objects.equals(value, ulidId.value);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(value);
  }
}
