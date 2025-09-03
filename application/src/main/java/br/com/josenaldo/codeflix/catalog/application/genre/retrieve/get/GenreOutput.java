package br.com.josenaldo.codeflix.catalog.application.genre.retrieve.get;

import br.com.josenaldo.codeflix.catalog.domain.category.CategoryID;
import br.com.josenaldo.codeflix.catalog.domain.genre.Genre;
import java.time.Instant;
import java.util.List;

/**
 * Represents the output details of a genre in a format suitable for external use. This record
 * encapsulates information about a genre, including its unique identifier, timestamps, name, active
 * status, and associated categories.
 *
 * <p>This class is immutable and provides a static factory method to create an instance
 * from a {@code Genre} domain object. It is designed to be used in scenarios where the internal
 * representation of a genre entity needs to be transformed into a more suitable representation for
 * external systems or clients.
 *
 * @param id         The unique identifier of the genre.
 * @param createdAt  The instant when the genre was created.
 * @param updatedAt  The instant when the genre was last updated.
 * @param deletedAt  The instant when the genre was deleted, or null if not deleted.
 * @param name       The name of the genre.
 * @param isActive   A flag indicating whether the genre is currently active.
 * @param categories A list of category IDs associated with this genre.
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public record GenreOutput(
    String id,
    Instant createdAt,
    Instant updatedAt,
    Instant deletedAt,
    String name,
    boolean isActive,
    List<String> categories
) {

    /**
     * Creates a {@code GenreOutput} instance from the provided {@code Genre} object.
     * <p>
     * This method transforms a domain-level {@code Genre} into a representation suitable for
     * external use, capturing its details such as ID, creation and update timestamps, deletion
     * timestamp (if applicable), name, active status, and associated category IDs.
     *
     * @param aGenre The genre object containing the domain-level details to be transformed. Must
     *               not be null.
     * @return A {@code GenreOutput} instance representing the given genre, populated with its
     * details.
     * @throws NullPointerException If the provided {@code aGenre} is null.
     */
    public static GenreOutput from(final Genre aGenre) {
        return new GenreOutput(
            aGenre.getId().getValue(),
            aGenre.getCreatedAt(),
            aGenre.getUpdatedAt(),
            aGenre.getDeletedAt(),
            aGenre.getName(),
            aGenre.isActive(),
            aGenre.getCategories().stream().map(CategoryID::getValue).toList()
        );
    }
}
