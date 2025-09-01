package br.com.josenaldo.codeflix.catalog.application.genre.delete;

import br.com.josenaldo.codeflix.catalog.domain.exceptions.DomainException;
import br.com.josenaldo.codeflix.catalog.domain.genre.Genre;
import br.com.josenaldo.codeflix.catalog.domain.genre.GenreGateway;
import br.com.josenaldo.codeflix.catalog.domain.genre.GenreID;
import java.util.Objects;

/**
 * Provides the default implementation for the {@link DeleteGenreUseCase} use case, enabling the
 * deletion of a genre from the underlying data storage.
 * <p>
 * The {@code DefaultDeleteGenreUseCase} encapsulates the logic required to remove a genre by
 * delegating the operation to a {@link GenreGateway}. It ensures the required dependency is
 * provided at the time of instantiation and facilitates the domain-driven design principle of
 * separating responsibilities.
 * <p>
 * The primary goal of this class is to handle the application layer logic for genre deletion while
 * relying on the {@link GenreGateway} for the actual data access operations.
 * <p>
 * This class is part of the application layer and adheres to the principles of clean architecture
 * by decoupling the use case logic from specific data implementations.
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public class DefaultDeleteGenreUseCase extends DeleteGenreUseCase {

    private final GenreGateway genreGateway;

    /**
     * Constructs an instance of {@code DefaultDeleteGenreUseCase}.
     * <p>
     * This constructor ensures that a valid {@link GenreGateway} dependency is provided. The
     * {@code GenreGateway} is responsible for executing the actual deletion of a {@link Genre} by
     * its unique identifier in the underlying data store.
     *
     * @param genreGateway The {@link GenreGateway} implementation that provides the operations
     *                     required for managing {@link Genre} entities, including deletion. Must
     *                     not be {@code null}.
     * @throws NullPointerException If the {@code genreGateway} parameter is {@code null}.
     */
    public DefaultDeleteGenreUseCase(final GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway, GENRE_GATEWAY_NULL_ERROR);
    }

    /**
     * Deletes the genre associated with the provided identifier.
     * <p>
     * This method delegates the deletion operation to the {@link GenreGateway}, ensuring that the
     * genre corresponding to the given identifier is removed from the underlying data store. The
     * identifier is converted to a {@link GenreID} instance before initiating the deletion
     * process.
     *
     * @param aGenreId The unique identifier of the genre to be deleted. Must not be {@code null}.
     *                 This parameter is expected to be a valid string representation of a
     *                 {@link GenreID}.
     * @throws NullPointerException If {@code aGenreId} is {@code null}.
     * @throws DomainException      If the provided {@code aGenreId} is not a valid ULID string
     *                              representation.
     */
    @Override
    public void execute(String aGenreId) {
        Objects.requireNonNull(aGenreId, GENRE_ID_NULL_ERROR);
        this.genreGateway.deleteById(GenreID.fromString(aGenreId));
    }
}
