package br.com.josenaldo.codeflix.domain;

import br.com.josenaldo.codeflix.domain.exceptions.InvalidIdException;
import com.github.f4b6a3.ulid.Ulid;
import com.github.f4b6a3.ulid.UlidCreator;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class EntityId implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private final String value;

  private EntityId() {
    Ulid ulid = UlidCreator.getUlid();
    this.value = ulid.toString();
  }

  private EntityId(String value) {
    try {
      Ulid ulid = Ulid.from(value);
      this.value = ulid.toString();
    } catch (IllegalArgumentException e) {
      throw new InvalidIdException();
    }
  }

  public static EntityId generate() {
    return new EntityId();
  }

  public static EntityId from(String aValue) {
    return new EntityId(aValue);
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
    EntityId entityId = (EntityId) o;
    return Objects.equals(value, entityId.value);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(value);
  }
}
