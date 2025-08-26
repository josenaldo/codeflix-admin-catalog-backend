package br.com.josenaldo.codeflix.catalog.application.category.retrieve.list;

import br.com.josenaldo.codeflix.catalog.application.UseCase;
import br.com.josenaldo.codeflix.catalog.domain.pagination.Pagination;
import br.com.josenaldo.codeflix.catalog.domain.pagination.SearchQuery;

/**
 * Represents the abstract use case for listing categories.
 * <p>
 * This use case accepts a {@link SearchQuery} containing the search criteria and pagination
 * parameters, and returns a {@link Pagination} of {@link CategoryListOutput} representing the
 * paginated list of categories.
 * <p>
 * Concrete implementations should override the {@code execute} method to provide the business logic
 * for retrieving and filtering categories from the underlying data source.
 * <p>
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public abstract class ListCategoryUseCase extends
    UseCase<SearchQuery, Pagination<CategoryListOutput>> {

}
