package br.com.josenaldo.codeflix.domain.category;

import java.time.Instant;

import br.com.josenaldo.codeflix.domain.AggregateRoot;
import br.com.josenaldo.codeflix.domain.validation.ValidationHandler;

public class Category extends AggregateRoot<CategoryID> {

  private String name;
  private String description;
  private boolean active;
  private Instant createdAt;
  private Instant updatedAt;
  private Instant deletedAt;

  private Category(
      final CategoryID id,
      final String name,
      final String description,
      final boolean active,
      final Instant createdAt,
      final Instant updatedAt,
      final Instant deletedAt) {
    super(id);

    this.name = name;
    this.description = description;
    this.active = active;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.deletedAt = deletedAt;
  }

  public static Category newCategory(
      final String name,
      final String description,
      final boolean active) {
    final var id = CategoryID.unique();
    final var now = Instant.now();
    final var deletedAt = active ? null : now;

    return new Category(
        id,
        name,
        description,
        active,
        now,
        now,
        deletedAt);
  }

  @Override
  public void validate(ValidationHandler validationHandler) {
    // TODO: usar uma fábrica de validador
    new CategoryValidator(this, validationHandler).validate();
  }

  public CategoryID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public boolean isActive() {
    return active;
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
}
