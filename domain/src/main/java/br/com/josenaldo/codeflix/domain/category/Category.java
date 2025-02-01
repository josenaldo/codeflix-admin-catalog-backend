package br.com.josenaldo.codeflix.domain.category;

import br.com.josenaldo.codeflix.domain.AggregateRoot;
import br.com.josenaldo.codeflix.domain.validation.ValidationHandler;
import java.time.Instant;

public class Category extends AggregateRoot<CategoryID> implements Cloneable {

  private String name;
  private String description;
  private boolean active;

  private Category(
      final CategoryID id,
      final String name,
      final String description,
      final boolean active,
      final Instant createdAt,
      final Instant updatedAt,
      final Instant deletedAt) {
    super(id, createdAt, updatedAt, deletedAt);

    this.name = name;
    this.description = description;
    this.active = active;
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

  public Category deactivate() {
    if (getDeletedAt() == null) {
      this.deletedAt = Instant.now();
    }

    this.active = false;
    this.touch();

    return this;
  }

  public Category activate() {
    this.deletedAt = null;
    this.active = true;
    this.touch();

    return this;
  }

  public Category update(final String name, final String description, final boolean active) {
    this.name = name;
    this.description = description;

    if (active) {
      this.activate();
    } else {
      this.deactivate();
    }

    this.touch();

    return this;
  }

    /**
     * @return
     * @throws CloneNotSupportedException
     */
    @Override
    public Category clone() {
        try {
            return (Category) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("The Category object could not be cloned.", e);
        }
    }
}
