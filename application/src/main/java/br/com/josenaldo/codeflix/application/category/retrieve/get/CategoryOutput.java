package br.com.josenaldo.codeflix.application.category.retrieve.get;

import br.com.josenaldo.codeflix.domain.category.Category;
import br.com.josenaldo.codeflix.domain.category.CategoryID;
import java.time.Instant;

/**
 * Represents the output data of a category retrieval operation.
 * <p>
 * This record encapsulates all the necessary information about a category including its identifier,
 * creation, update and deletion timestamps, as well as its name, description, and active status.
 * <p>
 * It is used to transfer category data from the domain layer to the application or presentation
 * layer.
 *
 * @param id          the unique identifier of the category.
 * @param createdAt   the timestamp when the category was created.
 * @param updatedAt   the timestamp when the category was last updated.
 * @param deletedAt   the timestamp when the category was deleted, or {@code null} if not deleted.
 * @param name        the name of the category.
 * @param description the description of the category.
 * @param isActive    the status indicating whether the category is active.
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public record CategoryOutput(
    CategoryID id,
    Instant createdAt,
    Instant updatedAt,
    Instant deletedAt,
    String name,
    String description,
    boolean isActive
) {

    /**
     * Creates a new {@code CategoryOutput} instance from the given {@link Category}.
     * <p>
     * This method extracts the data from the provided {@code Category} object and maps it to a new
     * instance of {@code CategoryOutput}.
     *
     * @param category the {@code Category} from which to create the output.
     * @return a new {@code CategoryOutput} instance containing the category's data.
     */
    public static CategoryOutput from(final Category category) {
        return new CategoryOutput(
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
