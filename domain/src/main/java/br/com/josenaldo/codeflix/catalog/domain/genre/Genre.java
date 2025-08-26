package br.com.josenaldo.codeflix.catalog.domain.genre;

import br.com.josenaldo.codeflix.catalog.domain.AggregateRoot;
import br.com.josenaldo.codeflix.catalog.domain.category.CategoryID;
import br.com.josenaldo.codeflix.catalog.domain.exceptions.NotificationException;
import br.com.josenaldo.codeflix.catalog.domain.utils.InstantUtils;
import br.com.josenaldo.codeflix.catalog.domain.validation.ValidationHandler;
import br.com.josenaldo.codeflix.catalog.domain.validation.handler.Notification;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a genre in the application's domain layer. A {@code Genre} has a name, and an active
 * status indicating whether it is enabled or disabled.
 * <p>
 * This class extends {@link AggregateRoot} to include common attributes and behaviors used by
 * entities considered to be the root of their aggregates. It also implements the {@link Cloneable}
 * interface, allowing safe cloning of its instances.
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public class Genre extends AggregateRoot<GenreID> implements Cloneable {

    /**
     * A constant string used to represent the error message for validation failures during the
     * validation of a {@code Genre}.
     *
     * <p>
     * This message is intended to be used in scenarios where validation errors prevent the
     * successful instantiation or modification of a {@code Genre}. It provides a user-friendly
     * explanation of the failure reason, which can be logged or displayed in error responses.
     */
    public static final String GENRE_VALIDATION_ERROR_MESSAGE =
        "Genre has validation errors";

    /**
     * The name of the genre.
     */
    private String name;

    /**
     * Indicates whether the genre is active ({@code true}) or inactive ({@code false}).
     */
    private boolean active;

    /**
     * The list of categories associated with this genre.
     */
    private List<CategoryID> categories;

    /**
     * Internal constructor for creating a {@code Genre} instance with all necessary fields.
     * <p>
     * The validation of the provided attributes is performed by calling the
     * {@link #selfValidate()}. If validation fails, an exception will be thrown.
     *
     * @param id         The unique identifier of this genre.
     * @param createdAt  The date/time when the genre was created.
     * @param updatedAt  The date/time when the genre was last updated.
     * @param deletedAt  The date/time when the genre was deactivated, or {@code null} if it is
     *                   still active.
     * @param name       The name of the genre.
     * @param active     {@code true} if the genre is active; {@code false} otherwise.
     * @param categories The list of categories associated with this genre.
     * @throws NotificationException If the provided attributes do not pass validation.
     */
    private Genre(
        final GenreID id,
        final Instant createdAt,
        final Instant updatedAt,
        final Instant deletedAt,
        final String name,
        final boolean active,
        final List<CategoryID> categories
    ) {
        super(id, createdAt, updatedAt, deletedAt);
        this.name = name;
        this.active = active;
        this.categories = new ArrayList<>(
            categories != null ? categories : Collections.emptyList());

        this.selfValidate();
    }

    /**
     * Creates a new {@code Genre} instance. If the genre is active, {@code deletedAt} will be set
     * to {@code null}; otherwise, it will be set to the current time.
     *
     * @param name       The name of the genre.
     * @param active     {@code true} if the genre is active; {@code false} otherwise.
     * @param categories The list of categories associated with this genre.
     * @return A new {@code Genre} instance.
     * @throws NotificationException If the provided attributes do not pass validation.
     */
    public static Genre newGenre(
        final String name,
        final boolean active,
        final List<CategoryID> categories
    ) {
        final var id = GenreID.unique();
        final var now = InstantUtils.now();
        final var deletedAt = active ? null : now;

        return new Genre(
            id,
            now,
            now,
            deletedAt,
            name,
            active,
            categories
        );
    }

    /**
     * Creates a new {@code Genre} instance. If the genre is active, {@code deletedAt} will be set
     * to {@code null}; otherwise, it will be set to the current time.
     *
     * @param name   The name of the genre.
     * @param active {@code true} if the genre is active; {@code false} otherwise.
     * @return A new {@code Genre} instance.
     * @throws NotificationException If the provided attributes do not pass validation.
     */
    public static Genre newGenre(
        final String name,
        final boolean active
    ) {
        return newGenre(name, active, null);
    }

    /**
     * Creates a new {@code Genre} instance with the specified attributes. The {@code deletedAt}
     * field will be set to {@code null} if the genre is active; otherwise, it will be set to the
     * current time.
     *
     * @param genreID    The unique identifier of the genre.
     * @param createdAt  The date/time when the genre was created.
     * @param updatedAt  The date/time when the genre was last updated.
     * @param deletedAt  The date/time when the genre was deactivated, or {@code null} if it is
     *                   still active.
     * @param name       The name of the genre.
     * @param active     {@code true} if the genre is active; {@code false} otherwise.
     * @param categories The list of categories associated with this genre.
     * @return A new {@code Genre} instance.
     * @throws NotificationException If the provided attributes do not pass validation.
     */
    public static Genre with(
        final GenreID genreID,
        final Instant createdAt,
        final Instant updatedAt,
        final Instant deletedAt,
        final String name,
        final boolean active,
        final List<CategoryID> categories
    ) {
        return new Genre(genreID, createdAt, updatedAt, deletedAt, name, active, categories);
    }

    /**
     * Creates a new {@code Genre} instance with the same attributes as the specified genre.
     *
     * @param genre The genre to copy.
     * @return A new {@code Genre} instance.
     */
    public Genre with(final Genre genre) {
        return with(
            genre.id,
            genre.createdAt,
            genre.updatedAt,
            genre.deletedAt,
            genre.name,
            genre.active,
            genre.categories
        );
    }

    /**
     * Validates the current state of this {@code Genre} instance by checking if its attributes meet
     * the required conditions and business rules. If the validation detects any errors, a
     * {@link NotificationException} is thrown with detailed information about the issues.
     * <p>
     * This method internally creates a {@link Notification} object to collect validation errors and
     * calls the {@link #validate(ValidationHandler)} method to perform the actual validation.
     * <p>
     * If the {@link Notification} contains errors after validation, a {@link NotificationException}
     * is raised with the error details.
     *
     * <p>The validation ensures that the genre's current attributes are consistent and conform
     * to expected constraints before proceeding with subsequent operations.
     *
     * @throws NotificationException If validation errors are detected, containing the details of
     *                               the issues.
     */
    protected void selfValidate() {
        Notification notification = Notification.create();
        this.validate(notification);

        if (notification.hasErrors()) {
            throw new NotificationException(GENRE_VALIDATION_ERROR_MESSAGE, notification);
        }
    }

    /**
     * Validates the current state of this genre, checking whether the required fields are correctly
     * set and meeting any business rules.
     *
     * @param validationHandler The {@link ValidationHandler} that will collect any validation
     *                          errors.
     */
    @Override
    public void validate(final ValidationHandler validationHandler) {
        new GenreValidator(this, validationHandler).validate();
    }

    /**
     * Deactivates this genre by setting its {@code active} property to {@code false} and assigning
     * the current time to {@code deletedAt}, if it is not already set.
     *
     * @return This {@code Genre} instance, for a fluent interface.
     */
    public Genre deactivate() {
        if (getDeletedAt() == null) {
            this.deletedAt = InstantUtils.now();
        }
        this.active = false;
        this.touch();
        return this;
    }

    /**
     * Activates this genre by setting its {@code active} property to {@code true} and resetting
     * {@code deletedAt} to {@code null}.
     *
     * @return This {@code Genre} instance, for a fluent interface.
     */
    public Genre activate() {
        this.deletedAt = null;
        this.active = true;
        this.touch();
        return this;
    }

    /**
     * Updates the name, active status, and categories of this genre.
     * <p>
     * If {@code active} is {@code true}, the genre is activated; otherwise, it is deactivated  (and
     * {@code deletedAt} is set to the current time).
     * <p>
     * If categories are provided, they will replace the existing ones, ven if the list is empty. If
     * no categories are null, the existing list of categories will replaced by an empty list.
     *
     * @param aName      the new name of the genre; must not be {@code null} or blank and must
     *                   respect length constraints.
     * @param isActive   {@code true} to activate the genre; {@code false} to deactivate.
     * @param categories the new list of associated category identifiers; if {@code null}, an empty
     *                   list is used.
     * @return This {@code Genre} instance, for a fluent interface.
     * @throws NotificationException if the new state violates validation constraints.
     */
    public Genre update(
        final String aName,
        final boolean isActive,
        final List<CategoryID> categories
    ) {
        this.name = aName;
        if (isActive) {
            this.activate();
        } else {
            this.deactivate();
        }

        this.categories = new ArrayList<>(
            categories != null ? categories : Collections.emptyList());

        this.touch();
        this.selfValidate();
        return this;
    }

    /**
     * Adds a new category to the list of categories associated with this {@code Genre}.
     * <p>
     * If the provided {@code categoryID} is {@code null}, the method returns the current instance
     * of the genre without making any changes. Otherwise, the category is added to the genre, and
     * the last updated timestamp is adjusted.
     *
     * @param categoryID The {@link CategoryID} to add to this genre. Must not be {@code null} to
     *                   take effect.
     * @return This {@code Genre} instance, allowing for method chaining. If {@code categoryID} is
     * {@code null}, no changes are made, and the current instance is returned as is.
     */
    public Genre addCategory(final CategoryID categoryID) {
        if (categoryID == null) {
            return this;
        }

        this.categories.add(categoryID);
        this.touch();
        return this;
    }

    /**
     * Removes a specified category from the list of categories associated with this {@code Genre}.
     * <p>
     * If the given {@link CategoryID} is {@code null}, the method does nothing and returns the
     * current instance of the {@code Genre}. Otherwise, the category is removed from the genre, and
     * the last updated timestamp is adjusted.
     *
     * @param aCategoryID The {@link CategoryID} to remove from this genre. If {@code null}, no
     *                    changes are made.
     * @return This {@code Genre} instance, enabling method chaining. If the specified category is
     * not found, it is simply ignored.
     */
    public Genre removeCategory(final CategoryID aCategoryID) {
        if (aCategoryID == null) {
            return this;
        }

        this.categories.remove(aCategoryID);
        this.touch();
        return this;
    }

    /**
     * Retrieves the unique identifier of this genre.
     *
     * @return A {@link GenreID} representing the unique identifier.
     */
    @Override
    public GenreID getId() {
        return id;
    }

    /**
     * Retrieves the name of this genre.
     *
     * @return The genre name.
     */
    public String getName() {
        return name;
    }

    /**
     * Indicates whether this genre is currently active.
     *
     * @return {@code true} if the genre is active; {@code false} otherwise.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Retrieves the list of category identifiers associated with this genre.
     *
     * @return an unmodifiable list of {@link CategoryID}; never {@code null}.
     */

    public List<CategoryID> getCategories() {
        return Collections.unmodifiableList(categories);
    }

    /**
     * Creates and returns an exact copy (clone) of this genre, preserving all its attributes.
     *
     * @return A new {@code Genre} instance that is a clone of this one.
     * @throws IllegalStateException If the cloning process fails.
     */
    @Override
    public Genre clone() {
        try {
            Genre clone = (Genre) super.clone();
            clone.categories = new ArrayList<>(this.categories);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("The Genre object could not be cloned.", e);
        }
    }
}
