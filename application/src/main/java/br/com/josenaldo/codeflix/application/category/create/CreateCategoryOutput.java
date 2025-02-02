package br.com.josenaldo.codeflix.application.category.create;

import br.com.josenaldo.codeflix.domain.category.Category;
import br.com.josenaldo.codeflix.domain.category.CategoryID;

/**
 * Represents the output of the create category use case.
 * <p>
 * This record encapsulates the result of a category creation operation by holding the unique
 * identifier of the newly created category. It is used to transfer the result from the domain layer
 * to the application or presentation layer.
 * <p>
 *
 * @param id the unique identifier of the created category.
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public record CreateCategoryOutput(
    CategoryID id
) {

    /**
     * Creates an instance of {@code CreateCategoryOutput} from the provided {@link Category}.
     * <p>
     * This method extracts the category's unique identifier and returns a new output record
     * containing that identifier.
     *
     * @param category the category from which the identifier is extracted.
     * @return a new {@code CreateCategoryOutput} instance containing the unique identifier of the
     * category.
     */
    public static CreateCategoryOutput from(final Category category) {
        return new CreateCategoryOutput(category.getId());
    }
}
