package br.com.josenaldo.codeflix.catalog.domain.genre;

import br.com.josenaldo.codeflix.catalog.domain.AggregateRoot;
import br.com.josenaldo.codeflix.catalog.domain.category.Category;
import br.com.josenaldo.codeflix.catalog.domain.exceptions.NotificationException;
import br.com.josenaldo.codeflix.catalog.domain.validation.ValidationHandler;
import br.com.josenaldo.codeflix.catalog.domain.validation.handler.Notification;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
     * creation of a {@code Genre}.
     *
     * <p>
     * This message is intended to be used in scenarios where validation errors prevent the
     * successful instantiation or modification of a {@code Genre}. It provides a user-friendly
     * explanation of the failure reason, which can be logged or displayed in error responses.
     */
    public static final String GENRE_VALIDATION_ERROR_MESSAGE = "The Genre could not be created because of validation errors.";

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
    private final List<Category> categories;

    /**
     * Internal constructor for creating a {@code Genre} instance with all necessary fields.
     * <p>
     * The validation of the provided attributes is performed using the provided
     * {@link ValidationHandler}. If validation fails, an exception will be thrown.
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
        List<Category> categories
    ) {
        super(id, createdAt, updatedAt, deletedAt);
        this.name = name;
        this.active = active;
        this.categories = categories;

        Notification notification = Notification.create();
        this.validate(notification);

        if (notification.hasErrors()) {
            throw new NotificationException(
                GENRE_VALIDATION_ERROR_MESSAGE
                , notification
            );
        }
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
        final List<Category> categories
    ) {
        final var id = GenreID.unique();
        final var now = Instant.now();
        final var deletedAt = active ? null : now;

        return new Genre(
            id,
            now,
            now,
            deletedAt,
            name,
            active,
            Objects.requireNonNullElseGet(categories, ArrayList::new)
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
        List<Category> categories
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
            new ArrayList<>(genre.categories)
        );
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
     * Retrieves the list of categories associated with this genre.
     *
     * @return The list of {@link Category} objects.
     */
    public List<Category> getCategories() {
        return Collections.unmodifiableList(categories);
    }

    /**
     * Deactivates this genre by setting its {@code active} property to {@code false} and assigning
     * the current time to {@code deletedAt}, if it is not already set.
     *
     * @return This {@code Genre} instance, for a fluent interface.
     */
    public Genre deactivate() {
        if (getDeletedAt() == null) {
            this.deletedAt = Instant.now();
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
     * Updates the name, description, and active status of this genre. If {@code active} is
     * {@code true}, the genre is activated; otherwise, it is deactivated.
     *
     * @param name   The new name of the genre.
     * @param active {@code true} to activate the genre; {@code false} to deactivate.
     * @return This {@code Genre} instance, for a fluent interface.
     */
    public Genre update(final String name, final boolean active) {
        this.name = name;
        if (active) {
            this.activate();
        } else {
            this.deactivate();
        }
        this.touch();
        return this;
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
            return (Genre) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("The Genre object could not be cloned.", e);
        }
    }
}
