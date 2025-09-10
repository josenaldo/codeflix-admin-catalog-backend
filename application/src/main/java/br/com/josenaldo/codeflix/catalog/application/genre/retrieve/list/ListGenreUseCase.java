package br.com.josenaldo.codeflix.catalog.application.genre.retrieve.list;

import br.com.josenaldo.codeflix.catalog.application.UseCase;
import br.com.josenaldo.codeflix.catalog.domain.pagination.Pagination;
import br.com.josenaldo.codeflix.catalog.domain.pagination.SearchQuery;

/**
 * Abstract use case for listing genres based on search query parameters.
 *
 * <p>The {@code ListGenreUseCase} defines a contract for implementing the use case responsible for
 * retrieving paginated lists of genres according to the given search criteria. It extends the
 * {@link UseCase} class, specifying input as a {@link SearchQuery} and output as a
 * {@link Pagination} containing {@link GenreListOutput}.
 *
 * <p>Concrete implementations must provide the logic for retrieving and processing genre data
 * by overriding the {@link #execute(SearchQuery)} method.
 *
 * <p>This abstraction ensures that genre listing adheres to a standardized processing flow
 * while allowing flexibility in the implementation of data retrieval and business rules.
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 * @see UseCase
 * @see SearchQuery
 * @see Pagination
 * @see GenreListOutput
 */
public abstract class ListGenreUseCase
    extends UseCase<SearchQuery, Pagination<GenreListOutput>> {

    /**
     * Constant error message indicating that the {@code GenreGateway} dependency is null.
     * <p>
     * This constant is used to enforce non-null validation for the {@code GenreGateway}, a required
     * dependency for the {@code GetGenreByIdUseCase}. If the {@code GenreGateway} is not properly
     * initialized or injected, this error message can be used to signal the issue in exception
     * handling or logging.
     * <p>
     * Ensuring the {@code GenreGateway} is provided is critical for the correct functioning of the
     * {@code GetGenreByIdUseCase}, as it provides the access required to interact with the
     * underlying data source for genres.
     */
    public static final String GENRE_GATEWAY_NULL_ERROR = "GenreGateway must not be null";
}
