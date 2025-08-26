package br.com.josenaldo.codeflix.catalog.domain.genre;

import br.com.josenaldo.codeflix.catalog.domain.pagination.Pagination;
import br.com.josenaldo.codeflix.catalog.domain.pagination.SearchQuery;
import java.util.Optional;

/**
 * The {@code GenreGateway} interface defines the contract for data access operations related to *
 * the {@link Genre} entity within the application.
 * <p>
 * Implementations of this interface are responsible for interacting with the underlying data
 * storage mechanism, ensuring proper handling of {@link Genre} entities.
 * <p>
 * This interface supports:
 * <ul>
 *   <li>Creating a new genre.</li>
 *   <li>Updating an existing genre.</li>
 *   <li>Deleting a genre by its unique identifier.</li>
 *   <li>Finding a genre by its unique identifier.</li>
 *   <li>Searching and paginating genres based on specific criteria.</li>
 * </ul>
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public interface GenreGateway {

    /**
     * Creates a new {@link Genre} in the underlying data store.
     *
     * @param aGenre The genre object to be created.
     * @return The newly created {@code Genre} with any assigned IDs or additional properties.
     */
    Genre create(Genre aGenre);

    /**
     * Deletes the {@link Genre} identified by the given {@link GenreID} from the underlying data
     * store.
     *
     * @param id The unique identifier of the genre to be deleted.
     */
    void deleteById(GenreID id);

    /**
     * Retrieves the {@link Genre} identified by the given {@link GenreID}.
     *
     * @param id The unique identifier of the genre to be retrieved.
     * @return An {@link Optional} containing the {@code Genre} if found, or empty if not found.
     */
    Optional<Genre> findById(GenreID id);

    /**
     * Updates an existing {@link Genre} in the underlying data store.
     *
     * @param aGenre The aGenre object with updated information.
     * @return The updated {@code Genre}, reflecting any changes persisted.
     */
    Genre update(Genre aGenre);

    /**
     * Retrieves a paginated list of {@link Genre} objects based on the provided search criteria.
     *
     * @param aSearchQuery A {@link SearchQuery} containing parameters to filter and sort genres.
     * @return A {@link Pagination} of genres that match the specified search query.
     */
    Pagination<Genre> findAll(SearchQuery aSearchQuery);
}
