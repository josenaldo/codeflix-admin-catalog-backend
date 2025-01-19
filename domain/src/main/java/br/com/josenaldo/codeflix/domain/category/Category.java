package br.com.josenaldo.codeflix.domain.category;

import br.com.josenaldo.codeflix.domain.UlidId;
import java.time.Instant;

public class Category {

  private UlidId id;
  private String name;
  private String description;
  private boolean active;
  private Instant createdAt;
  private Instant updatedAt;
  private Instant deletedAt;

  private Category(
      final UlidId id,
      final String name,
      final String description,
      final boolean active,
      final Instant createdAt,
      final Instant updatedAt,
      final Instant deletedAt
  ) {
    this.id = id;
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
      final boolean active
  ) {

    UlidId id = UlidId.generate();
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

  public UlidId getId() {
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
