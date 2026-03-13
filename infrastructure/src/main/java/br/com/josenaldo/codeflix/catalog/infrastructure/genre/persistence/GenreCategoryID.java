package br.com.josenaldo.codeflix.catalog.infrastructure.genre.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a composite key for associating a {@code Genre} with a {@code Category}.
 *
 * <p>This class is used primarily within JPA entities to create a composite primary key
 * for the relationship between genres and categories in the persistence layer. It holds the
 * identification fields for both entities, which together uniquely identify the relationship.
 *
 * <p>The {@code GenreCategoryID} class is immutable once created, ensuring the integrity
 * of the composite key.
 *
 * <p>It implements {@code Serializable} to ensure compatibility with JPA requirements for
 * primary key classes.
 *
 * <p>The equality and hash code are based on the values of {@code genreId} and {@code categoryId}.
 * This ensures that two instances with the same keys are considered equal.
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
@Embeddable
public class GenreCategoryID implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "genre_id", nullable = false)
    private String genreId;

    @Column(name = "category_id", nullable = false)
    private String categoryId;

    /**
     * Constructs an empty {@code GenreCategoryID} instance.
     *
     * <p>This default, no-argument constructor is required by the JPA framework for instantiating
     * the composite key. It initializes a {@code GenreCategoryID} object with no predefined values
     * for {@code genreId} and {@code categoryId}.
     *
     * <p>Instances created using this constructor need to have their fields set appropriately
     * before being used as a valid composite key.
     */
    public GenreCategoryID() {
    }

    /**
     * Constructs a new {@code GenreCategoryID} instance with the specified genre and category
     * identifiers.
     *
     * <p>This constructor initializes a composite key that uniquely associates a genre
     * with a category. It requires both {@code genreId} and {@code categoryId} to be non-null and
     * form the composite key that guarantees uniqueness within a persistence context.
     *
     * @param genreId    the unique identifier for the genre, representing the primary key of the
     *                   genre entity.
     * @param categoryId the unique identifier for the category, representing the primary key of the
     *                   category entity.
     * @throws IllegalArgumentException if either {@code genreId} or {@code categoryId} is null or
     *                                  blank.
     */
    private GenreCategoryID(final String genreId, final String categoryId) {
        this.genreId = genreId;
        this.categoryId = categoryId;
    }

    /**
     * Creates a new {@code GenreCategoryID} instance from the given genre and category
     * identifiers.
     * <p>
     * This method serves as a factory for constructing a {@code GenreCategoryID} object, which
     * represents a composite key uniquely associating a genre with a category. It requires both
     * {@code genreId} and {@code categoryId} to be provided and valid.
     *
     * @param genreId    the unique identifier of the genre, corresponding to the primary key of the
     *                   genre entity. It must not be {@code null} or blank.
     * @param categoryId the unique identifier of the category, corresponding to the primary key of
     *                   the category entity. It must not be {@code null} or blank.
     * @return a {@code GenreCategoryID} object that encapsulates the provided genre and category
     * identifiers.
     * @throws IllegalArgumentException if {@code genreId} or {@code categoryId} is {@code null} or
     *                                  blank.
     */
    public static GenreCategoryID from(final String genreId, final String categoryId) {
        return new GenreCategoryID(genreId, categoryId);
    }

    /**
     * Compares this object with the specified object for equality.
     * <p>
     * This method determines if the given object is equivalent to this {@code GenreCategoryID}
     * instance. Two {@code GenreCategoryID} objects are considered equal if their {@code genreId}
     * and {@code categoryId} values are both non-null and identical.
     *
     * @param o the object to compare with this {@code GenreCategoryID} instance. This can be
     *          {@code null} or of a different class.
     * @return {@code true} if the specified object is equal to this {@code GenreCategoryID},
     * {@code false} otherwise.
     */
    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final GenreCategoryID that = (GenreCategoryID) o;
        return Objects.equals(getGenreId(), that.getGenreId()) && Objects.equals(
            getCategoryId(),
            that.getCategoryId()
        );
    }

    /**
     * Computes the hash code for this {@code GenreCategoryID} instance.
     *
     * <p>The hash code is generated based on the values of the {@code genreId}
     * and {@code categoryId} fields. This ensures that instances with the same field values produce
     * the same hash code, which is crucial for the correct functioning of hash-based collections
     * such as {@link java.util.HashMap} or {@link java.util.HashSet}.
     *
     * @return an integer representing the hash code of this {@code GenreCategoryID} instance,
     * derived from the {@code genreId} and {@code categoryId} fields.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getGenreId(), getCategoryId());
    }

    /**
     * Retrieves the unique identifier of the genre.
     *
     * <p>This method returns the {@code genreId} associated with the current instance,
     * representing the primary key of the genre entity in the database.
     *
     * @return the unique identifier of the genre as a {@code String}. Returns {@code null} if the
     * identifier has not been set.
     */
    public String getGenreId() {
        return genreId;
    }

    /**
     * Sets the unique identifier for the genre.
     *
     * <p>This method is used to assign a value to the {@code genreId} field, representing the
     * primary key for a specific genre. The {@code genreId} is crucial for associating the genre
     * with other entities within the application or persistence context.
     *
     * @param genreId the unique identifier for the genre. It must not be {@code null} or blank.
     *                Passing a {@code null} or blank value may result in improper behavior or
     *                validation errors.
     */
    public void setGenreId(String genreId) {
        this.genreId = genreId;
    }

    /**
     * Retrieves the unique identifier for the category associated with this instance.
     *
     * <p>The category identifier represents the primary key of the category entity
     * that is part of the composite key for this object. It is a non-null value that uniquely
     * identifies the category.
     *
     * @return the unique identifier for the associated category. Returns {@code null} if the
     * category identifier is not set.
     */
    public String getCategoryId() {
        return categoryId;
    }

    /**
     * Sets the unique identifier for the category associated with this composite key.
     *
     * <p>This method updates the {@code categoryId} field of this instance with the provided
     * value.
     *
     * @param categoryId the unique identifier for the category. It should be a non-null, non-empty
     *                   string representing the primary key of the associated category entity.
     * @throws IllegalArgumentException if {@code categoryId} is null or empty.
     */
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }


}
