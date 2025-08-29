package br.com.josenaldo.codeflix.catalog.application.genre.update;

import br.com.josenaldo.codeflix.catalog.domain.genre.Genre;
import java.util.Objects;

/**
 * Represents the output of an operation that updates a genre.
 * <p>
 * This record encapsulates the result of a genre update operation, specifically containing the ID
 * of the updated genre. It simplifies the communication and transfer of this information between
 * different parts of the application.
 * </p>
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public record UpdateGenreOutput(
    String id
) {

    /**
     * Creates an instance of {@code UpdateGenreOutput} from the provided {@code Genre}.
     * <p>
     * This method constructs an {@code UpdateGenreOutput} using the identifier of the given
     * {@code Genre}, encapsulating the result of the genre update operation.
     * </p>
     *
     * @param aGenre the genre object used as the source for creating the update output. Must not be
     *               null, and the genre must contain a valid ID.
     * @return an instance of {@code UpdateGenreOutput} containing the ID of the given
     * {@code Genre}.
     * @throws NullPointerException if {@code aGenre} or its ID is null.
     */
    public static UpdateGenreOutput from(final Genre aGenre) {
        Objects.requireNonNull(aGenre, "the Genre must not be null");
        return new UpdateGenreOutput(aGenre.getId().getValue());
    }
}
