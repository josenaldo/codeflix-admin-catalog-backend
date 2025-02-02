package br.com.josenaldo.codeflix.domain.category;

import br.com.josenaldo.codeflix.domain.AggregateRoot;
import br.com.josenaldo.codeflix.domain.validation.ValidationHandler;
import java.time.Instant;

/**
 * Represents a category in the application's domain layer. A {@code Category} has a name,
 * description, and an active status indicating whether it is enabled or disabled.
 * <p>
 * This class extends {@link AggregateRoot} to include common attributes and behaviors used by
 * entities considered to be the root of their aggregates. It also implements the {@link Cloneable}
 * interface, allowing safe cloning of its instances.
 *
 * @author YourName
 * @version 1.0
 */
public class Category extends AggregateRoot<CategoryID> implements Cloneable {

    /**
     * The name of the category.
     */
    private String name;

    /**
     * A textual description of the category.
     */
    private String description;

    /**
     * Indicates whether the category is active ({@code true}) or inactive ({@code false}).
     */
    private boolean active;

    /**
     * Internal constructor for creating a {@code Category} instance with all necessary fields.
     *
     * @param id          The unique identifier of this category.
     * @param name        The name of the category.
     * @param description A textual description of the category.
     * @param active      {@code true} if the category is active; {@code false} otherwise.
     * @param createdAt   The date/time when the category was created.
     * @param updatedAt   The date/time when the category was last updated.
     * @param deletedAt   The date/time when the category was deactivated, or {@code null} if it is
     *                    still active.
     */
    private Category(
        final CategoryID id,
        final String name,
        final String description,
        final boolean active,
        final Instant createdAt,
        final Instant updatedAt,
        final Instant deletedAt
    ) {
        super(id, createdAt, updatedAt, deletedAt);
        this.name = name;
        this.description = description;
        this.active = active;
    }

    /**
     * Creates a new {@code Category} instance. If the category is active, {@code deletedAt} will be
     * set to {@code null}; otherwise, it will be set to the current time.
     *
     * @param name        The name of the category.
     * @param description A textual description of the category.
     * @param active      {@code true} if the category is active; {@code false} otherwise.
     * @return A new {@code Category} instance.
     */
    public static Category newCategory(
        final String name,
        final String description,
        final boolean active
    ) {
        final var id = CategoryID.unique();
        final var now = Instant.now();
        final var deletedAt = active ? null : now;

        return new Category(id, name, description, active, now, now, deletedAt);
    }

    /**
     * Validates the current state of this category, checking whether the required fields are
     * correctly set and meeting any business rules.
     *
     * @param validationHandler The {@link ValidationHandler} that will collect any validation
     *                          errors.
     */
    @Override
    public void validate(ValidationHandler validationHandler) {
        new CategoryValidator(this, validationHandler).validate();
    }

    /**
     * Retrieves the unique identifier of this category.
     *
     * @return A {@link CategoryID} representing the unique identifier.
     */
    public CategoryID getId() {
        return id;
    }

    /**
     * Retrieves the name of this category.
     *
     * @return The category name.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the description of this category.
     *
     * @return The category description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Indicates whether this category is currently active.
     *
     * @return {@code true} if the category is active; {@code false} otherwise.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Deactivates this category by setting its {@code active} property to {@code false} and
     * assigning the current time to {@code deletedAt}, if it is not already set.
     *
     * @return This {@code Category} instance, for a fluent interface.
     */
    public Category deactivate() {
        if (getDeletedAt() == null) {
            this.deletedAt = Instant.now();
        }
        this.active = false;
        this.touch();
        return this;
    }

    /**
     * Activates this category by setting its {@code active} property to {@code true} and resetting
     * {@code deletedAt} to {@code null}.
     *
     * @return This {@code Category} instance, for a fluent interface.
     */
    public Category activate() {
        this.deletedAt = null;
        this.active = true;
        this.touch();
        return this;
    }

    /**
     * Updates the name, description, and active status of this category. If {@code active} is
     * {@code true}, the category is activated; otherwise, it is deactivated.
     *
     * @param name        The new name of the category.
     * @param description The new description of the category.
     * @param active      {@code true} to activate the category; {@code false} to deactivate.
     * @return This {@code Category} instance, for a fluent interface.
     */
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
     * Creates and returns an exact copy (clone) of this category, preserving all its attributes.
     *
     * @return A new {@code Category} instance that is a clone of this one.
     * @throws IllegalStateException If the cloning process fails.
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
