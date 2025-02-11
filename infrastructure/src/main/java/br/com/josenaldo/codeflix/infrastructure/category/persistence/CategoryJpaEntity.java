package br.com.josenaldo.codeflix.infrastructure.category.persistence;

import br.com.josenaldo.codeflix.domain.category.Category;
import br.com.josenaldo.codeflix.domain.category.CategoryID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * Represents the JPA entity for the "category" table.
 * <p>
 * This class maps the Category domain object to a relational database table using JPA annotations.
 * It defines the fields corresponding to the database columns and provides utility methods to
 * convert between the JPA entity and the domain object.
 * <p>
 * The entity is used by the persistence layer to perform CRUD operations on category data.
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
@Entity
@Table(name = "category")
public class CategoryJpaEntity {

    /**
     * Unique identifier of the category.
     * <p>
     * Mapped to the "id" column in the database. The length is fixed at 26 characters because the
     * ID is generated using the ULID algorithm.
     */
    @Id
    @Column(name = "id", nullable = false, length = 26)
    private String id;

    /**
     * Timestamp when the category was created.
     * <p>
     * Mapped to the "created_at" column with microsecond precision.
     */
    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant createdAt;

    /**
     * Timestamp when the category was last updated.
     * <p>
     * Mapped to the "updated_at" column with microsecond precision.
     */
    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant updatedAt;

    /**
     * Timestamp when the category was deleted.
     * <p>
     * Mapped to the "deleted_at" column with microsecond precision. This value is null if the
     * category is active.
     */
    @Column(name = "deleted_at", columnDefinition = "DATETIME(6)")
    private Instant deletedAt;

    /**
     * Name of the category.
     * <p>
     * Mapped to the "name" column with a maximum length of 255 characters.
     */
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    /**
     * Description of the category.
     * <p>
     * Mapped to the "description" column with a maximum length of 4000 characters.
     */
    @Column(name = "description", length = 4000)
    private String description;

    /**
     * Indicates whether the category is active.
     * <p>
     * Mapped to the "active" column. A default value of false is assigned.
     */
    @Column(name = "active", nullable = false)
    private boolean active = false;

    /**
     * Default constructor required by JPA.
     */
    public CategoryJpaEntity() {
    }

    /**
     * Constructs a new {@code CategoryJpaEntity} with the specified values.
     * <p>
     * This constructor is used to initialize all fields of the entity.
     *
     * @param id          the unique identifier of the category.
     * @param createdAt   the creation timestamp.
     * @param updatedAt   the last update timestamp.
     * @param deletedAt   the deletion timestamp, or null if not deleted.
     * @param name        the name of the category.
     * @param description the description of the category.
     * @param active      the active status of the category.
     */
    public CategoryJpaEntity(
        final String id,
        final Instant createdAt,
        final Instant updatedAt,
        final Instant deletedAt,
        final String name,
        final String description,
        final boolean active
    ) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.name = name;
        this.description = description;
        this.active = active;
    }

    /**
     * Converts a {@link Category} domain object to a {@code CategoryJpaEntity}.
     * <p>
     * This method extracts the relevant fields from the domain object and creates a new JPA entity
     * instance.
     *
     * @param category the domain object to be converted.
     * @return a new {@code CategoryJpaEntity} representing the provided category.
     */
    public static CategoryJpaEntity from(final Category category) {
        return new CategoryJpaEntity(
            category.getId().getValue(),
            category.getCreatedAt(),
            category.getUpdatedAt(),
            category.getDeletedAt(),
            category.getName(),
            category.getDescription(),
            category.isActive()
        );
    }

    /**
     * Converts this {@code CategoryJpaEntity} to a {@link Category} domain object.
     * <p>
     * The conversion maps all entity fields to the corresponding domain object fields.
     *
     * @return a {@code Category} domain object representing this entity.
     */
    public Category to() {
        return Category.with(
            CategoryID.fromString(getId()),
            getCreatedAt(),
            getUpdatedAt(),
            getDeletedAt(),
            getName(),
            getDescription(),
            isActive()
        );
    }

    /**
     * Returns the active status of the category.
     *
     * @return true if the category is active; false otherwise.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the active status of the category.
     *
     * @param active the new active status.
     */
    public void setActive(final boolean active) {
        this.active = active;
    }

    /**
     * Returns the description of the category.
     *
     * @return the category description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the category.
     *
     * @param description the new description.
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Returns the name of the category.
     *
     * @return the category name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the category.
     *
     * @param name the new name.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Returns the deletion timestamp of the category.
     *
     * @return the deletion timestamp, or null if the category is not deleted.
     */
    public Instant getDeletedAt() {
        return deletedAt;
    }

    /**
     * Sets the deletion timestamp of the category.
     *
     * @param deletedAt the new deletion timestamp.
     */
    public void setDeletedAt(final Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    /**
     * Returns the last update timestamp of the category.
     *
     * @return the timestamp of the last update.
     */
    public Instant getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Sets the last update timestamp of the category.
     *
     * @param updatedAt the new update timestamp.
     */
    public void setUpdatedAt(final Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Returns the creation timestamp of the category.
     *
     * @return the creation timestamp.
     */
    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation timestamp of the category.
     *
     * @param createdAt the new creation timestamp.
     */
    public void setCreatedAt(final Instant createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Returns the unique identifier of the category.
     *
     * @return the category ID as a String.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the category.
     *
     * @param id the new category ID.
     */
    public void setId(final String id) {
        this.id = id;
    }
}
