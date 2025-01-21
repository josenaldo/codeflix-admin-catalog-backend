package br.com.josenaldo.codeflix.domain;

import java.time.Instant;
import java.util.Objects;

import br.com.josenaldo.codeflix.domain.validation.ValidationHandler;

public abstract class Entity<ID extends Identifier> {

  protected final ID id;
  protected Instant createdAt;
  protected Instant updatedAt;
  protected Instant deletedAt;

  protected Entity(final ID id) {
    Objects.requireNonNull(id, "id must not be null");
    this.id = id;
    this.createdAt = Instant.now();
    this.updatedAt = Instant.now();
  }

  protected Entity(final ID id, final Instant createdAt, final Instant updatedAt, final Instant deletedAt) {
    this.id = id;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.deletedAt = deletedAt;
  }

  public abstract void validate(ValidationHandler validationHandler);

  public ID getId() {
    return id;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public Instant getDeletedAt() {
    return deletedAt;
  }

  protected void touch() {
    this.updatedAt = Instant.now();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Entity<?> entity = (Entity<?>) o;
    return Objects.equals(getId(), entity.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }
}
