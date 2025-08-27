package br.com.josenaldo.codeflix.catalog.application.category.create;

import br.com.josenaldo.codeflix.catalog.domain.category.Category;
import br.com.josenaldo.codeflix.catalog.domain.category.CategoryID;
import java.util.Objects;

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
    String id
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
        Objects.requireNonNull(category, "the Category must not be null");
        return new CreateCategoryOutput(category.getId().getValue());
    }

    /**
     * Creates a new {@code CreateCategoryOutput} instance from the provided {@link CategoryID}.
     * <p>
     * This method extracts the unique identifier from the given {@code CategoryID} and returns a
     * new output record containing it.
     *
     * @param anId the {@link CategoryID} containing the unique identifier of a category. Must not
     *             be {@code null}.
     * @return a new {@code CreateCategoryOutput} instance containing the unique identifier of the
     * category.
     * @throws NullPointerException if {@code anId} is {@code null}.
     */
    public static CreateCategoryOutput from(final CategoryID anId) {
        Objects.requireNonNull(anId, "the CategoryID must not be null");
        return new CreateCategoryOutput(anId.getValue());
    }
}
