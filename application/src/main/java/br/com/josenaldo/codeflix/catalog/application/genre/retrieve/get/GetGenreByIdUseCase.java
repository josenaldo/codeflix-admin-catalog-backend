package br.com.josenaldo.codeflix.catalog.application.genre.retrieve.get;

import br.com.josenaldo.codeflix.catalog.application.UseCase;
import br.com.josenaldo.codeflix.catalog.domain.genre.Genre;

/**
 * Abstract use case for retrieving a genre by its unique identifier.
 * <p>
 * This class extends the generic {@link UseCase} to define a specific contract for fetching a
 * {@link Genre} entity and mapping it into a {@link GenreOutput} representation. Concrete
 * implementations must provide the logic for interacting with the underlying data source and
 * handling specific scenarios like missing genres.
 *
 * <p>
 * The input to this use case is expected to be the genre's unique identifier (String), and the
 * output is a {@link GenreOutput} instance encapsulating the genre's details.
 *
 * <p>
 * If the required dependencies, such as a {@code GenreGateway}, are not properly initialized, an
 * error message constant {@code GENRE_GATEWAY_NULL_ERROR} is available to communicate such errors.
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public abstract class GetGenreByIdUseCase extends UseCase<String, GenreOutput> {

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
