package br.com.josenaldo.codeflix.catalog.application.category.retrieve.list;

import br.com.josenaldo.codeflix.catalog.domain.category.Category;
import br.com.josenaldo.codeflix.catalog.domain.category.CategoryID;
import java.time.Instant;

/**
 * Represents the output data for a category in a list.
 * <p>
 * This record encapsulates the essential details of a category, including its unique identifier,
 * timestamps for creation, update, deletion, and descriptive attributes such as name, description,
 * and active status.
 * <p>
 * It is typically used to transfer data from the domain layer to the application or presentation
 * layer.
 *
 * @param id          the unique identifier of the category.
 * @param createdAt   the timestamp when the category was created.
 * @param deletedAt   the timestamp when the category was deleted, or {@code null} if not deleted.
 * @param name        the name of the category.
 * @param description the description of the category.
 * @param isActive    the active status of the category.
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public record CategoryListOutput(
    CategoryID id,
    Instant createdAt,
    Instant updatedAt,
    Instant deletedAt,
    String name,
    String description,
    boolean isActive
) {

    /**
     * Creates a new {@code CategoryListOutput} instance from the given {@link Category}.
     * <p>
     * This method maps the attributes of the provided {@code Category} to a new instance of
     * {@code CategoryListOutput}.
     *
     * @param category the {@code Category} object from which to create the output.
     * @return a new {@code CategoryListOutput} instance containing the category's data.
     */
    public static CategoryListOutput from(final Category category) {
        return new CategoryListOutput(
            category.getId(),
            category.getCreatedAt(),
            category.getUpdatedAt(),
            category.getDeletedAt(),
            category.getName(),
            category.getDescription(),
            category.isActive()
        );
    }
}
