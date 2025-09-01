package br.com.josenaldo.codeflix.catalog.application.genre.delete;

import br.com.josenaldo.codeflix.catalog.application.UnitUseCase;

/**
 * Represents a use case for deleting a genre by its unique identifier.
 * <p>
 * This use case extends {@link UnitUseCase}, specifying a {@link String} as its input type. The
 * input represents the unique identifier of the genre to be deleted. Implementations of this class
 * are responsible for defining the logic to remove the genre from the underlying data storage.
 * <p>
 * The {@link DeleteGenreUseCase} serves as an abstraction to ensure a single responsibility for
 * handling deletions in the application layer, adhering to clean architecture principles.
 * <p>
 * It also provides a constant error message to signal situations where the required
 * {@code GenreGateway} dependency is not provided.
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public abstract class DeleteGenreUseCase extends UnitUseCase<String> {

    /**
     * Error message indicating that the {@code GenreGateway} dependency must not be {@code null}.
     * <p>
     * This constant is utilized to enforce the validation of the {@code GenreGateway} being
     * properly injected when creating or initializing instances of classes, such as
     * {@code DefaultDeleteGenreUseCase}, which rely on this dependency.
     * <p>
     * Failing to provide a valid {@code GenreGateway} will typically result in an exception being
     * thrown, preventing further execution.
     */
    public static final String GENRE_GATEWAY_NULL_ERROR = "the GenreGateway must not be null";

    /**
     * Error message indicating that the {@code GenreID} must not be {@code null}.
     * <p>
     * This constant is used to enforce validation of the input parameter in operations requiring a
     * genre identifier, such as deleting a genre.
     * <p>
     * If a {@code null} {@code GenreID} is provided, an exception is typically thrown to prevent
     * invalid operations in the application layer and to ensure data integrity.
     */
    public static final String GENRE_ID_NULL_ERROR = "the GenreID must not be null";
}
