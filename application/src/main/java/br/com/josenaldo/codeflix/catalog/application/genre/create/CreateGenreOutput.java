package br.com.josenaldo.codeflix.catalog.application.genre.create;

import br.com.josenaldo.codeflix.catalog.domain.genre.Genre;
import br.com.josenaldo.codeflix.catalog.domain.genre.GenreID;
import java.util.Objects;

/**
 * Represents the output of the create genre use case.
 * <p>
 * This record encapsulates the result of a genre creation operation by holding the unique
 * identifier of the newly created genre. It is used to transfer the result from the domain layer to
 * the application or presentation layer.
 * <p>
 *
 * @param id the unique identifier of the created genre.
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public record CreateGenreOutput(
    String id
) {

    /**
     * Creates an instance of {@code CreateGenreOutput} from the provided {@link Genre}.
     * <p>
     * This method extracts the genre's unique identifier and returns a new output record containing
     * that identifier.
     *
     * @param genre the genre from which the identifier is extracted.
     * @return a new {@code CreateGenreOutput} instance containing the unique identifier of the
     * genre.
     */
    public static CreateGenreOutput from(final Genre genre) {
        return new CreateGenreOutput(genre.getId().getValue());
    }

    /**
     * Creates an instance of {@code CreateGenreOutput} from the provided {@link GenreID}.
     * <p>
     * This method extracts the string value of the provided {@code GenreID} and uses it to
     * construct a new {@code CreateGenreOutput} instance, encapsulating the unique identifier of a
     * genre.
     *
     * @param anId the unique identifier of the created genre, encapsulated in a {@link GenreID}
     *             instance. Must not be {@code null}.
     * @return a new {@code CreateGenreOutput} instance containing the unique identifier of the
     * genre.
     * @throws NullPointerException if {@code anId} is {@code null}.
     */
    public static CreateGenreOutput from(final GenreID anId) {
        Objects.requireNonNull(anId, "the GenreID must not be null");
        return new CreateGenreOutput(anId.getValue());
    }
}
