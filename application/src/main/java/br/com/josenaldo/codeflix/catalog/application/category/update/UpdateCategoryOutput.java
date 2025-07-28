package br.com.josenaldo.codeflix.catalog.application.category.update;

import br.com.josenaldo.codeflix.catalog.domain.category.Category;
import br.com.josenaldo.codeflix.catalog.domain.category.CategoryID;

/**
 * Represents the output of the update category use case.
 * <p>
 * This record encapsulates the unique identifier of a category after an update operation. It is
 * used to transfer the result of an update from the domain layer to the application or presentation
 * layer.
 * <p>
 *
 * @param id the unique identifier of the updated category.
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public record UpdateCategoryOutput(
    String id
) {

    /**
     * Creates an instance of {@code UpdateCategoryOutput} from the given {@link Category}.
     * <p>
     * This method extracts the unique identifier from the updated category and returns a new output
     * record.
     *
     * @param category the updated category from which the identifier is extracted.
     * @return a new {@code UpdateCategoryOutput} instance containing the category's unique
     * identifier.
     */
    public static UpdateCategoryOutput from(Category category) {
        return new UpdateCategoryOutput(category.getId().getValue());
    }


    public static UpdateCategoryOutput from(String anId) {
        return new UpdateCategoryOutput(anId);
    }
}
