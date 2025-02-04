package br.com.josenaldo.codeflix.domain.category;

import br.com.josenaldo.codeflix.domain.pagination.Pagination;
import java.util.Optional;

/**
 * Defines an abstraction layer for performing operations related to {@link Category} entities. This
 * interface allows creating, updating, and deleting categories, as well as retrieving individual
 * categories or paginated lists of categories based on search criteria.
 * <p>
 * Implementations of this interface should handle the necessary infrastructure details such as
 * persistence mechanisms, ensuring that the domain logic remains decoupled from those details.
 * <p>
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public interface CategoryGateway {

    /**
     * Creates a new {@link Category} in the underlying data store.
     *
     * @param category The category object to be created.
     * @return The newly created {@code Category} with any assigned IDs or additional properties.
     */
    Category create(Category category);

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
     * @param category The category object with updated information.
     * @return The updated {@code Category}, reflecting any changes persisted.
     */
    Category update(Category category);

    /**
     * Retrieves a paginated list of {@link Category} objects based on the provided search
     * criteria.
     *
     * @param searchQuery A {@link CategorySearchQuery} containing parameters to filter and sort
     *                    categories.
     * @return A {@link Pagination} of categories that match the specified search query.
     */
    Pagination<Category> findAll(CategorySearchQuery searchQuery);
}
