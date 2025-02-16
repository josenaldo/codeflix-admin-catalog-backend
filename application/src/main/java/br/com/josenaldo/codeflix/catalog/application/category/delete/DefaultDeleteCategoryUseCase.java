package br.com.josenaldo.codeflix.catalog.application.category.delete;

import br.com.josenaldo.codeflix.catalog.domain.category.CategoryGateway;
import br.com.josenaldo.codeflix.catalog.domain.category.CategoryID;
import java.util.Objects;

/**
 * Default implementation of the delete category use case.
 * <p>
 * This class handles the deletion of a category by converting a string identifier into a
 * {@link CategoryID} and delegating the deletion operation to the provided
 * {@link CategoryGateway}.
 * <p>
 * Concrete implementations for deleting a category should extend this class.
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public class DefaultDeleteCategoryUseCase extends DeleteCategoryUseCase {

    /**
     * The gateway used to perform operations related to categories.
     */
    private final CategoryGateway categoryGateway;

    /**
     * Constructs a new {@code DefaultDeleteCategoryUseCase} with the specified
     * {@link CategoryGateway}.
     * <p>
     * The provided gateway is used to delete the category based on its identifier.
     *
     * @param categoryGateway the gateway responsible for category persistence and deletion.
     */
    public DefaultDeleteCategoryUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    /**
     * Executes the deletion of a category.
     * <p>
     * This method converts the provided string identifier into a {@link CategoryID} and delegates
     * the deletion process to the {@link CategoryGateway}.
     *
     * @param input the unique identifier of the category to be deleted, represented as a
     *              {@code String}.
     */
    @Override
    public void execute(final String input) {
        this.categoryGateway.deleteById(CategoryID.fromString(input));
    }
}
