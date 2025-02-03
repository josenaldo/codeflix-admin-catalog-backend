package br.com.josenaldo.codeflix.application.category.retrieve.get;

import static br.com.josenaldo.codeflix.application.category.exceptions.CategoryExceptions.categoryNotFoundException;

import br.com.josenaldo.codeflix.application.category.retrieve.CategoryOutput;
import br.com.josenaldo.codeflix.domain.category.CategoryGateway;
import br.com.josenaldo.codeflix.domain.category.CategoryID;
import io.vavr.API;

/**
 * Default implementation of the use case for retrieving a category by its identifier.
 * <p>
 * This class extends {@link GetCategoryByIdUseCase} and provides the business logic to fetch a
 * category using its unique identifier. It converts the input string into a {@link CategoryID} and
 * retrieves the category data through the {@link CategoryGateway}.
 * <p>
 * If the input is invalid or the category is not found, an exception is thrown using the helper
 * method from
 * {@link br.com.josenaldo.codeflix.application.category.exceptions.CategoryExceptions}.
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public class DefaultGetCategoryByIdUseCase extends GetCategoryByIdUseCase {

    /**
     * The gateway used to perform operations related to categories.
     * <p>
     * This gateway provides the methods to retrieve category data from the underlying data store.
     */
    private final CategoryGateway categoryGateway;

    /**
     * Constructs a new {@code DefaultGetCategoryByIdUseCase} with the specified
     * {@link CategoryGateway}.
     * <p>
     * The provided gateway is used to retrieve and convert category information based on the input
     * identifier.
     *
     * @param categoryGateway the gateway responsible for accessing category data.
     */
    public DefaultGetCategoryByIdUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    /**
     * Executes the use case for retrieving a category by its identifier.
     * <p>
     * This method attempts to convert the provided string input into a {@link CategoryID}. If the
     * conversion fails or the category does not exist, an exception is thrown using
     * {@code categoryNotFoundException}. Otherwise, it retrieves the category data and maps it into
     * a {@link CategoryOutput} object.
     *
     * @param input the unique identifier of the category as a {@code String}.
     * @return a {@link CategoryOutput} containing the details of the retrieved category.
     * @throws br.com.josenaldo.codeflix.domain.exceptions.DomainException if the category is not
     *                                                                     found or if the
     *                                                                     identifier is invalid.
     */
    @Override
    public CategoryOutput execute(String input) {
        final CategoryID categoryId = API.Try(() -> CategoryID.fromString(input))
                                         .getOrElseThrow(categoryNotFoundException(input));

        return categoryGateway.findById(categoryId)
                              .map(CategoryOutput::from)
                              .orElseThrow(categoryNotFoundException(input));
    }
}
