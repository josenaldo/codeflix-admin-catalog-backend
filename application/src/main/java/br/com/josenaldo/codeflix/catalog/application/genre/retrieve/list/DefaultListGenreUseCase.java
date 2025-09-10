package br.com.josenaldo.codeflix.catalog.application.genre.retrieve.list;

import br.com.josenaldo.codeflix.catalog.domain.genre.GenreGateway;
import br.com.josenaldo.codeflix.catalog.domain.pagination.Pagination;
import br.com.josenaldo.codeflix.catalog.domain.pagination.SearchQuery;
import java.util.Objects;

/**
 * Default implementation of the {@link ListGenreUseCase}.
 *
 * <p>The {@code DefaultListGenreUseCase} is responsible for handling the logic to retrieve a
 * paginated list of genres based on a given search query. It uses a {@link GenreGateway} to
 * interact with the underlying data storage mechanisms, ensuring proper data retrieval and
 * transformation into the required output format.
 *
 * <p>This implementation ensures that the {@code GenreGateway} dependency is not null and
 * delegates the search execution to the gateway, transforming the retrieved results into
 * {@link GenreListOutput} objects.
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public class DefaultListGenreUseCase extends ListGenreUseCase {

    private final GenreGateway genreGateway;

    /**
     * Constructs a new {@code DefaultListGenreUseCase} with the specified {@link GenreGateway}.
     *
     * <p>The {@code DefaultListGenreUseCase} relies on a {@link GenreGateway} instance to handle
     * data retrieval operations for listing genres. This dependency must not be {@code null}.
     *
     * @param genreGateway The {@link GenreGateway} instance to be used for retrieving genre data.
     *                     Must not be {@code null}.
     * @throws NullPointerException If the provided {@code genreGateway} is {@code null}.
     */
    public DefaultListGenreUseCase(final GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway, GENRE_GATEWAY_NULL_ERROR);
    }

    /**
     * Executes the use case to retrieve a paginated list of genres.
     *
     * <p>This method processes a search query to fetch a list of genres that match the query's
     * criteria. It utilizes the {@link GenreGateway} to access the underlying data store and maps
     * the retrieved results into the {@link GenreListOutput} format for standardized output.
     *
     * <p>The search results include pagination metadata such as total pages, total items, and the
     * list of genres that satisfy the query parameters.
     *
     * @param aQuery The search query containing the criteria for filtering genres. Must not be
     *               {@code null}.
     * @return A {@link Pagination} object containing the paginated list of {@link GenreListOutput}
     * objects that match the search criteria.
     * @throws NullPointerException If {@code aQuery} is {@code null}.
     */
    @Override
    public Pagination<GenreListOutput> execute(final SearchQuery aQuery) {
        return this.genreGateway.findAll(aQuery).map(GenreListOutput::from);
    }
}
