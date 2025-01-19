package br.com.josenaldo.codeflix.domain.category;

import br.com.josenaldo.codeflix.domain.AggregateRoot;
import java.time.Instant;

public class Category extends AggregateRoot<CategoryID> {

  private String name;
  private String description;
  private boolean active;
  private Instant createdAt;
  private Instant updatedAt;
  private Instant deletedAt;

  private Category(
      final CategoryID anId,
      final String aName,
      final String aDescription,
      final boolean isActive,
      final Instant aCreatedAt,
      final Instant anUpdatedAt,
      final Instant aDeletedAt
  ) {
    super(anId);

    this.name = aName;
    this.description = aDescription;
    this.active = isActive;
    this.createdAt = aCreatedAt;
    this.updatedAt = anUpdatedAt;
    this.deletedAt = aDeletedAt;
  }

  public static Category newCategory(
      final String name,
      final String description,
      final boolean active
  ) {

    var id = CategoryID.unique();
    var now = Instant.now();

    return new Category(
        id,
        name,
        description,
        active,
        now,
        now,
        null
    );
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
