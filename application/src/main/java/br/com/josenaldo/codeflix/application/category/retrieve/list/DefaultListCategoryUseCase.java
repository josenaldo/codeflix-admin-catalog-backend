package br.com.josenaldo.codeflix.application.category.retrieve.list;

import br.com.josenaldo.codeflix.domain.category.Category;
import br.com.josenaldo.codeflix.domain.category.CategoryGateway;
import br.com.josenaldo.codeflix.domain.category.CategorySearchQuery;
import br.com.josenaldo.codeflix.domain.exceptions.DomainException;
import br.com.josenaldo.codeflix.domain.pagination.Pagination;
import java.util.Objects;

/**
 * Default implementation of the use case for listing categories.
 * <p>
 * This class extends {@link ListCategoryUseCase} to provide the business logic for retrieving a
 * paginated list of categories based on a search query. It utilizes a {@link CategoryGateway} to
 * fetch the category data from the underlying data source and maps the domain category objects to
 * {@link CategoryListOutput} objects.
 * <p>
 * In case of any runtime exceptions during the retrieval process, a {@link DomainException} is
 * thrown with the corresponding error message.
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public class DefaultListCategoryUseCase extends ListCategoryUseCase {

    /**
     * The gateway used to perform category retrieval operations.
     * <p>
     * This gateway is responsible for accessing the underlying data source and returning a
     * paginated list of categories.
     */
    private final CategoryGateway categoryGateway;

    /**
     * Constructs a new {@code DefaultListCategoryUseCase} with the specified
     * {@link CategoryGateway}.
     * <p>
     * The provided gateway must not be {@code null} and is used to retrieve category data.
     *
     * @param categoryGateway the gateway responsible for category retrieval.
     * @throws NullPointerException if {@code categoryGateway} is {@code null}.
     */
    public DefaultListCategoryUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    /**
     * Executes the use case for listing categories based on the given search query.
     * <p>
     * This method uses the {@link CategoryGateway} to fetch a paginated list of {@link Category}
     * objects according to the provided {@link CategorySearchQuery}. The retrieved categories are
     * then mapped to a {@link CategoryListOutput} format.
     * <p>
     * If any runtime exception occurs during the retrieval process, it is caught and rethrown as a
     * {@link DomainException} with the appropriate error message.
     *
     * @param query the search query containing filtering and pagination parameters.
     * @return a {@link Pagination} containing a paginated list of {@link CategoryListOutput}
     * objects.
     * @throws DomainException if an error occurs during the category retrieval process.
     */
    @Override
    public Pagination<CategoryListOutput> execute(CategorySearchQuery query) {
        try {
            Pagination<Category> categoryPagination = categoryGateway.findAll(query);
            return categoryPagination.map(CategoryListOutput::from);
        } catch (RuntimeException e) {
            throw DomainException.with(e.getMessage());
        }
    }
}
