package br.com.josenaldo.codeflix.catalog.application.genre.retrieve.list;

import br.com.josenaldo.codeflix.catalog.domain.category.CategoryID;
import br.com.josenaldo.codeflix.catalog.domain.genre.Genre;
import java.time.Instant;
import java.util.List;

/**
 * Represents the output structure for a genre, including its metadata and associated categories.
 *
 * <p>This record encapsulates information about a genre, such as its creation and deletion
 * timestamps, name, status (active or inactive), and a list of related category IDs. It simplifies
 * transferring genre data while maintaining clarity and consistency.
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public record GenreListOutput(
    String id,
    Instant createdAd,
    Instant deletedAt,
    String nme,
    boolean isActive,
    List<String> categories
) {

    /**
     * Converts a {@link Genre} object into a {@link GenreListOutput} object.
     *
     * <p>This method extracts data from the given {@link Genre} instance and creates a
     * new {@link GenreListOutput} object containing relevant information such as the creation and
     * deletion timestamps, name, active status, and a list of category IDs.
     *
     * @param genre The {@link Genre} object to be converted. Must not be {@code null}.
     * @return A {@link GenreListOutput} object populated with the corresponding data from the given
     * {@link Genre}. Returns {@code null} if the input is {@code null}.
     */
    public static GenreListOutput from(final Genre genre) {

        return new GenreListOutput(
            genre.getId().getValue(),
            genre.getCreatedAt(),
            genre.getDeletedAt(),
            genre.getName(),
            genre.isActive(),
            genre.getCategories()
                 .stream()
                 .map(CategoryID::getValue)
                 .toList()
        );
    }
}
