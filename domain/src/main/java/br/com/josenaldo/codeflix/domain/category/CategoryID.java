package br.com.josenaldo.codeflix.domain.category;

import br.com.josenaldo.codeflix.domain.Identifier;
import br.com.josenaldo.codeflix.domain.exceptions.InvalidIdException;
import com.github.f4b6a3.ulid.Ulid;
import com.github.f4b6a3.ulid.UlidCreator;

import java.util.Objects;

public class CategoryID extends Identifier {

  private final String value;

  private CategoryID(String value) {
    Objects.requireNonNull(value, "value must not be null");
    this.value = value;
  }

  public static CategoryID unique() {
    Ulid ulid = UlidCreator.getUlid();
    return new CategoryID(ulid.toString().toLowerCase());
  }

  public static CategoryID fromString(String value) {
    try {
      Ulid ulid = Ulid.from(value.toLowerCase());
      return new CategoryID(ulid.toString().toLowerCase());
    } catch (IllegalArgumentException e) {
      throw new InvalidIdException();
    }
  }

  @Override
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

    CategoryID categoryID = (CategoryID) o;
    return Objects.equals(value, categoryID.value);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(value);
  }
}
