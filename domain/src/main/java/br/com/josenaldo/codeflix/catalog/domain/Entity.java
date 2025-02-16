package br.com.josenaldo.codeflix.catalog.domain;

import br.com.josenaldo.codeflix.catalog.domain.validation.ValidationHandler;
import java.time.Instant;
import java.util.Objects;

/**
 * Serves as the base class for all entities in the domain. It provides common fields and behaviors
 * such as a unique identifier, creation timestamp, update timestamp, and optional deletion
 * timestamp. Classes extending {@code Entity} must implement domain-specific validation by
 * overriding the {@link #validate(ValidationHandler)} method.
 *
 * @param <ID> The type of the entity's identifier, which must extend {@link Identifier}.
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public abstract class Entity<ID extends Identifier> {

    /**
     * The unique identifier of the entity.
     */
    protected final ID id;

    /**
     * The timestamp when the entity was created.
     */
    protected final Instant createdAt;

    /**
     * The timestamp when the entity was last updated.
     */
    protected Instant updatedAt;

    /**
     * The timestamp when the entity was logically deleted, or {@code null} if the entity is still
     * considered active.
     */
    protected Instant deletedAt;

    /**
     * Constructs a new {@code Entity} with the provided identifier. Initializes
     * {@code createdAt} and {@code updatedAt} to the current time.
     *
     * @param id The unique identifier of the entity. Must not be {@code null}.
     * @throws NullPointerException If the {@code id} is {@code null}.
     */
    protected Entity(final ID id) {

        this.id = Objects.requireNonNull(id, "id must not be null");

        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    /**
     * Constructs a new {@code Entity} with the provided identifier and timestamps.
     *
     * @param id         The unique identifier of the entity. Must not be {@code null}.
     * @param createdAt  The creation timestamp of the entity.
     * @param updatedAt  The last updated timestamp of the entity.
     * @param deletedAt  The deletion timestamp of the entity, or {@code null} if the entity
     *                   has not been logically deleted.
     */
    protected Entity(
        final ID id,
        final Instant createdAt,
        final Instant updatedAt,
        final Instant deletedAt
    ) {
        this.id = Objects.requireNonNull(id, "id must not be null");

        this.createdAt = Objects.requireNonNull(createdAt, "createdAt must not be null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt must not be null");
        this.deletedAt = deletedAt;
    }

    /**
     * Validates the current state of the entity, ensuring it conforms to
     * any necessary business rules or constraints.
     *
     * @param validationHandler A {@link ValidationHandler} to which any validation
     *                          errors should be reported.
     */
    public abstract void validate(ValidationHandler validationHandler);

    /**
     * Returns the unique identifier of the entity.
     *
     * @return The entity's identifier.
     */
    public ID getId() {
        return id;
    }

    /**
     * Returns the timestamp indicating when the entity was created.
     *
     * @return The creation timestamp.
     */
    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     * Returns the timestamp indicating when the entity was last updated.
     *
     * @return The last update timestamp.
     */
    public Instant getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Returns the timestamp indicating when the entity was logically deleted,
     * or {@code null} if the entity is still considered active.
     *
     * @return The deletion timestamp, or {@code null} if not deleted.
     */
    public Instant getDeletedAt() {
        return deletedAt;
    }

    /**
     * Updates the {@code updatedAt} timestamp to the current time, typically
     * called whenever an entity is modified.
     */
    protected void touch() {
        this.updatedAt = Instant.now();
    }

    /**
     * Determines whether this entity is equal to the specified object. Two entities
     * are considered equal if they have the same identifier.
     *
     * @param o The object to compare with this entity.
     * @return {@code true} if both entities share the same identifier; otherwise {@code false}.
     */
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

    /**
     * Returns a hash code for this entity, derived from its identifier.
     *
     * @return A hash code value for this entity.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
