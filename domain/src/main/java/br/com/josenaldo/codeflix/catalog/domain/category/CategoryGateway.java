package br.com.josenaldo.codeflix.catalog.domain.category;

import br.com.josenaldo.codeflix.catalog.domain.pagination.Pagination;
import br.com.josenaldo.codeflix.catalog.domain.pagination.SearchQuery;
import java.util.Optional;


/**
 * The {@code CategoryGateway} interface defines the contract for data access operations related to
 * the {@link Category} entity within the application.
 * <p>
 * Implementations of this interface are responsible for interacting with the underlying data
 * storage mechanism, ensuring proper handling of {@link Category} entities.
 * <p>
 * This interface supports:
 * <ul>
 *   <li>Creating a new category.</li>
 *   <li>Updating an existing category.</li>
 *   <li>Deleting a category by its unique identifier.</li>
 *   <li>Finding a category by its unique identifier.</li>
 *   <li>Searching and paginating categories based on specific criteria.</li>
 * </ul>
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public interface CategoryGateway {

    /**
     * Creates a new {@link Category} in the underlying data store.
     *
     * @param aCategory The aCategory object to be created.
     * @return The newly created {@code Category} with any assigned IDs or additional properties.
     */
    Category create(Category aCategory);

    /**
     * Deletes the {@link Category} identified by the given {@link CategoryID} from the underlying
     * data store.
     *
     * @param id The unique identifier of the category to be deleted.
     */
    void deleteById(CategoryID id);

    /**
     * Retrieves the {@link Category} identified by the given {@link CategoryID}.
     *
     * @param id The unique identifier of the category to be retrieved.
     * @return An {@link Optional} containing the {@code Category} if found, or empty if not found.
     */
    Optional<Category> findById(CategoryID id);

    /**
     * Updates an existing {@link Category} in the underlying data store.
     *
     * @param aCategory The aCategory object with updated information.
     * @return The updated {@code Category}, reflecting any changes persisted.
     */
    Category update(Category aCategory);

    /**
     * Retrieves a paginated list of {@link Category} objects based on the provided search
     * criteria.
     *
     * @param aSearchQuery A {@link SearchQuery} containing parameters to filter and sort
     *                     categories.
     * @return A {@link Pagination} of categories that match the specified search query.
     */
    Pagination<Category> findAll(SearchQuery aSearchQuery);
}
