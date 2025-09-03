package br.com.josenaldo.codeflix.catalog.application.genre.retrieve.get;

import br.com.josenaldo.codeflix.catalog.domain.exceptions.NotFoundException;
import br.com.josenaldo.codeflix.catalog.domain.genre.Genre;
import br.com.josenaldo.codeflix.catalog.domain.genre.GenreGateway;
import br.com.josenaldo.codeflix.catalog.domain.genre.GenreID;
import java.util.Objects;

/**
 * Provides the default implementation of the {@link GetGenreByIdUseCase} for retrieving a genre by
 * its unique identifier.
 * <p>
 * This class uses a {@link GenreGateway} to interact with the underlying data source and fetch a
 * {@link Genre} entity. If the genre is found, it is mapped to a {@link GenreOutput} instance which
 * represents its external use. If the genre is not found, an exception is thrown.
 * <p>
 * The {@code GenreGateway} is a required dependency, and its absence will result in an exception
 * during instantiation.
 *
 * <p>Behavior:
 * <ul>
 *   <li>Delegates the genre lookup to the {@link GenreGateway}.</li>
 *   <li>Maps the found genre to a {@link GenreOutput} if it exists.</li>
 *   <li>Throws {@code NotFoundException} with detailed information if the genre is not found.</li>
 * </ul>
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public class DefaultGetGenreByIdUseCase extends GetGenreByIdUseCase {

    private final GenreGateway genreGateway;

    /**
     * Constructs a new instance of {@code DefaultGetGenreByIdUseCase} with the specified
     * {@code GenreGateway}.
     * <p>
     * This constructor ensures that a valid {@link GenreGateway} is provided, as it is a required
     * dependency for the use case. If the {@code GenreGateway} is null, an exception is thrown.
     *
     * <p>The {@code DefaultGetGenreByIdUseCase} leverages the {@code GenreGateway} to fetch
     * {@link Genre} entities by their unique identifier and map them into {@link GenreOutput}
     * representations for external use.
     *
     * @param genreGateway The gateway responsible for accessing the {@link Genre} entities. Must
     *                     not be null.
     * @throws NullPointerException If the given {@code genreGateway} is null.
     */
    public DefaultGetGenreByIdUseCase(final GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway, GENRE_GATEWAY_NULL_ERROR);
    }

    /**
     * Executes the use case to retrieve a {@link Genre} based on the provided unique identifier.
     * <p>
     * This method interacts with the {@code GenreGateway} to locate the {@code Genre} corresponding
     * to the given ID. If the genre is found, it is transformed into a {@link GenreOutput}. If not,
     * a {@link NotFoundException} is thrown, indicating that no matching genre exists.
     *
     * @param anId The unique identifier of the genre to be retrieved. Must not be null or invalid.
     * @return The {@link GenreOutput} representing the retrieved genre.
     * @throws NotFoundException    If no genre is found with the given identifier.
     * @throws NullPointerException If the provided {@code anId} is null.
     */
    @Override
    public GenreOutput execute(final String anId) {
        return genreGateway.findById(GenreID.fromString(anId))
                           .map(GenreOutput::from)
                           .orElseThrow(NotFoundException.supplierOf(Genre.class, anId));

    }
}
