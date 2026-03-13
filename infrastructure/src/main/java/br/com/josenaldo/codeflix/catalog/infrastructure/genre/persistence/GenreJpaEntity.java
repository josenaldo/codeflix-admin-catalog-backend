package br.com.josenaldo.codeflix.catalog.infrastructure.genre.persistence;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.EAGER;

import br.com.josenaldo.codeflix.catalog.domain.category.CategoryID;
import br.com.josenaldo.codeflix.catalog.domain.genre.Genre;
import br.com.josenaldo.codeflix.catalog.domain.genre.GenreID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jspecify.annotations.NonNull;

/**
 * Represents the JPA entity for the "Genre" domain, used to persist genre-related data in the
 * database. This class maps to the "genres" table and handles the representation of genres,
 * including their relationships and attributes.
 *
 * <p>The GenreJpaEntity ensures that the domain model is accurately represented
 * in a relational database context. It supports operations such as mapping between domain-specific
 * entities and persistence entities.
 *
 * <p>It also maintains the associations with related categories, reflecting
 * the many-to-many relationship in the form of a Set of {@code GenreCategoryJpaEntity}. Categories
 * can be added to or removed from this entity using the appropriate methods to maintain
 * bidirectional integrity.
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
@Entity
@Table(name = "genres")
public class GenreJpaEntity {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant updatedAt;

    @Column(name = "deleted_at", columnDefinition = "DATETIME(6)")
    private Instant deletedAt;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "active", nullable = false)
    private boolean active;

    @OneToMany(mappedBy = "genre", cascade = ALL, fetch = EAGER, orphanRemoval = true)
    private Set<GenreCategoryJpaEntity> categories;

    /**
     * Default constructor for the {@code GenreJpaEntity} class.
     *
     * <p>Creates a new instance of {@code GenreJpaEntity} with no initialized data.
     *
     * <p>This constructor is primarily used by frameworks such as JPA that
     * require a no-argument constructor to instantiate objects dynamically.
     */
    public GenreJpaEntity() {
    }

    /**
     * Constructs a new {@code GenreJpaEntity} with the provided attributes.
     * <p>
     * This private constructor initializes the entity with the specified properties and an empty
     * set of categories. Direct usage is restricted, and it is typically invoked internally within
     * the class.
     *
     * @param id        The unique identifier of the genre. Must not be {@code null}.
     * @param createdAt The timestamp indicating when the genre was created. Must not be
     *                  {@code null}.
     * @param updatedAt The timestamp indicating when the genre was last updated. Must not be
     *                  {@code null}.
     * @param deletedAt The timestamp indicating when the genre was deleted, or {@code null} if the
     *                  genre is active.
     * @param name      The name of the genre. Must not be {@code null}.
     * @param active    A boolean indicating whether the genre is active.
     */
    private GenreJpaEntity(
        final String id,
        final Instant createdAt,
        final Instant updatedAt,
        final Instant deletedAt,
        final String name,
        final boolean active
    ) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.name = name;
        this.active = active;
        this.categories = new HashSet<>();
    }

    /**
     * Converts a {@link Genre} domain object into a {@link GenreJpaEntity} instance for persistence
     * purposes.
     *
     * <p>This method maps the attributes and associated categories of the provided
     * {@link Genre} object into a new {@link GenreJpaEntity}. Associations are stored as a
     * bidirectional relationship to ensure consistency between the domain object and the
     * persistence layer.
     *
     * @param aGenre The {@link Genre} domain object to be converted. Must not be null.
     * @return A new {@link GenreJpaEntity} instance representing the provided {@link Genre}. This
     * includes all attributes and associated categories.
     * @throws NullPointerException If the provided {@code aGenre} is null.
     */
    public static GenreJpaEntity from(final Genre aGenre) {
        final var anEntity = new GenreJpaEntity(
            aGenre.getId().getValue(),
            aGenre.getCreatedAt(),
            aGenre.getUpdatedAt(),
            aGenre.getDeletedAt(),
            aGenre.getName(),
            aGenre.isActive()
        );

        aGenre.getCategories().forEach(anEntity::addCategory);

        return anEntity;
    }


    /**
     * Converts the current object into a Genre aggregate, encapsulating its properties and
     * relationships.
     * <p>
     * This method aggregates the relevant fields and transforms them into a {@code Genre} instance.
     * The returned {@code Genre} object represents the current state of the entity in a form suited
     * for domain logic operations.
     *
     * @return A {@code Genre} object containing the aggregated properties of the current entity.
     * Special cases include handling null values for nullable fields, which will translate to null
     * values in the returned {@code Genre}.
     */
    public Genre toAggregate() {

        return Genre.with(
            new GenreID(this.getId()),
            this.getCreatedAt(),
            this.getUpdatedAt(),
            this.getDeletedAt(),
            this.getName(),
            this.isActive(),
            this.getCategoryIDS()
        );

    }

    /**
     * Adds a new category association to this {@code GenreJpaEntity}.
     *
     * <p>This method creates a mapping between the current {@code GenreJpaEntity} instance
     * and the provided category, represented by its {@code CategoryID}, and saves this relationship
     * in the set of categories managed by this entity.
     *
     * <p>This operation ensures bidirectional integrity for the many-to-many relationship
     * between genres and categories.
     *
     * @param anId The unique identifier of the category to be associated with this genre. Must not
     *             be {@code null}.
     * @throws NullPointerException If the provided {@code anId} is {@code null}.
     */
    private void addCategory(CategoryID anId) {
        this.categories.add(GenreCategoryJpaEntity.from(this, anId));
    }

    /**
     * Removes a category association from the genre's list of categories.
     * <p>
     * This method is used to disassociate a category identified by the specified {@code CategoryID}
     * from this genre. The removal ensures that the relationship between the genre and the category
     * is no longer persisted.
     * </p>
     *
     * @param anId The unique identifier of the category to be removed. Must not be null.
     */
    private void removeCategory(CategoryID anId) {
        this.categories.remove(GenreCategoryJpaEntity.from(this, anId));
    }

    /**
     * Retrieves the unique identifier of the genre.
     *
     * <p>This identifier typically represents the {@code id} field of the genre,
     * which is a unique string defined at the time of persistence.
     *
     * @return The unique identifier of the genre entity. Returns {@code null} if the entity has not
     * yet been persisted or the {@code id} is not set.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the identifier for this genre entity.
     *
     * <p>This method assigns the specified identifier to this entity. The identifier
     * is used to uniquely distinguish this entity within the database and other systems interacting
     * with this object.
     *
     * @param id the unique identifier to be assigned to this entity. It should not be null or empty
     *           and must conform to the expected identifier format.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Retrieves the timestamp indicating when this entity was initially created.
     *
     * <p>This value is typically populated at the time of persistence and
     * remains constant throughout the lifecycle of the entity, serving as an immutable reference to
     * the creation moment.
     *
     * @return An {@code Instant} representing the creation date and time of this entity. If the
     * entity has not been initialized or persisted, this value may be {@code null}.
     */
    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation timestamp for this entity.
     *
     * <p>This method is used to define the precise {@code Instant}
     * representing when the entity was created. It is typically used during entity initialization
     * or updates to accurately record the creation time.
     *
     * @param createdAt The {@code Instant} to set as the entity's creation time. Must not be null.
     */
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Retrieves the timestamp of the last update made to this entity.
     *
     * <p>This method returns the value of the {@code updatedAt} field, which represents
     * the most recent modification time for this entity. It is typically used to track the last
     * modification in auditing or synchronization processes.
     *
     * @return An {@code Instant} object representing the timestamp of the most recent update. If no
     * updates have been made, this value may correspond to the creation time or remain
     * uninitialized, depending on the context.
     */
    public Instant getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Updates the timestamp that represents when this entity was last modified.
     * <p>
     * This method sets the {@code updatedAt} field with a new timestamp, indicating the most recent
     * update to this genre entity. This timestamp is used to track changes in the entity over
     * time.
     *
     * @param updatedAt The new timestamp of the last update. This value must not be {@code null}.
     */
    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Retrieves the timestamp indicating when the entity was marked as deleted.
     *
     * <p>If the entity has not been deleted, this method returns {@code null}.
     *
     * @return an {@code Instant} representing the deletion timestamp, or {@code null} if the entity
     * is not deleted.
     */
    public Instant getDeletedAt() {
        return deletedAt;
    }

    /**
     * Sets the timestamp indicating when this entity was marked as deleted.
     *
     * <p>The {@code deletedAt} value represents the point in time the entity
     * was logically deleted in the system. This allows retaining the entity in the database without
     * permanently removing it, supporting soft-deletion patterns.
     *
     * @param deletedAt The {@link Instant} when the entity was marked as deleted. If {@code null},
     *                  it indicates the entity is not deleted.
     */
    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    /**
     * Retrieves the name of the genre.
     *
     * <p>The name represents the identifier or title associated with this genre entity.
     * This value is a required attribute and cannot be null.
     *
     * @return the name of the genre as a non-null {@code String}.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the genre.
     *
     * <p>This method updates the name attribute of a {@code GenreJpaEntity} with the
     * specified value. The name is used to identify and represent the genre in the persistence
     * layer and in application logic.
     *
     * @param name The name to set for the genre. It must not be null or empty to ensure valid
     *             representation of a genre.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Checks whether the current genre is active.
     *
     * <p>Active genres are considered valid and usable within
     * the application context. This property provides essential status information about the
     * genre.
     *
     * @return {@code true} if the genre is active; {@code false} otherwise.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the active status of the genre.
     *
     * <p>This method updates the active state of the genre to either active
     * or inactive. The active state determines whether the genre is considered enabled or disabled
     * in the system.
     *
     * @param active a boolean value representing the new active status. {@code true} to set the
     *               genre as active, or {@code false} to set it as inactive.
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Retrieves the set of category associations linked to this genre.
     *
     * <p>This method returns the {@code Set} of {@code GenreCategoryJpaEntity}
     * objects that represent the many-to-many relationship between this genre and its associated
     * categories.
     *
     * <p>The returned set reflects the categories currently associated
     * with the genre in the database.
     *
     * @return A {@code Set} of {@code GenreCategoryJpaEntity} representing the categories
     * associated with this genre. If no categories are associated, the set will be empty.
     */
    public Set<GenreCategoryJpaEntity> getCategories() {
        return categories;
    }

    /**
     * Retrieves a list of {@link CategoryID} instances associated with this genre.
     *
     * <p>This method processes the genre's associated categories and extracts
     * their respective {@code CategoryID} objects. It applies transformation logic to convert
     * category data into a collection of domain-specific {@code CategoryID}.
     *
     * @return A non-null {@link List} of {@link CategoryID} representing the identifiers of all
     * categories linked to this genre. If no categories are associated, an empty list is returned.
     */
    public @NonNull List<CategoryID> getCategoryIDS() {
        return this.getCategories().stream()
                   .map(it -> CategoryID.fromString(it.getId().getCategoryId()))
                   .toList();
    }

    /**
     * Sets the collection of genre-category associations for this genre entity.
     *
     * <p>This method replaces the current set of associated categories with the provided set.
     * Each entry in the set represents a relationship between the genre and a category,
     * encapsulated as a {@code GenreCategoryJpaEntity}.
     *
     * <p>Use this method to overwrite all associations with categories when updating
     * the relationships in bulk. Ensure the provided set is not null to avoid unintended
     * assignments.
     *
     * @param categories A set of {@code GenreCategoryJpaEntity} objects representing the
     *                   associations between this genre and its related categories. Pass
     *                   {@code Collections.emptySet()} if no categories should be associated.
     */
    public void setCategories(Set<GenreCategoryJpaEntity> categories) {
        this.categories = categories;
    }
}
