package br.com.josenaldo.codeflix.catalog.application.genre.update;

import java.util.List;

/**
 * Represents a command to update a genre in the system.
 * <p>
 * This record encapsulates the required data to perform an update operation on a genre, such as its
 * unique identifier, name, active status, and associated categories.
 * </p>
 *
 * <p>
 * The {@link #with(String, String, Boolean, List)} factory method provides a convenient way to
 * create instances of this command, ensuring default values for optional parameters (e.g.,
 * {@code isActive} defaults to {@code true} when {@code null}).
 * </p>
 *
 * <p>
 * This command serves as input for the update genre use cases, such as
 * {@code DefaultUpdateGenreUseCase}.
 * </p>
 *
 * @param id         The unique identifier of the genre to be updated.
 * @param name       The new name for the genre.
 * @param isActive   Indicates whether the genre should be active.
 * @param categories The list of category IDs to associate with the genre.
 * @version 1.0 author Josenaldo de Oliveira Matos Filho
 */
public record UpdateGenreCommand(
    String id,
    String name,
    boolean isActive,
    List<String> categories
) {

    /**
     * Creates a new {@link UpdateGenreCommand} instance, ensuring sensible defaults for optional
     * parameters.
     * <p>
     * This factory method provides a convenient way to initialize an update command with the
     * specified parameters, with {@code isActive} defaulting to {@code true} if {@code null}.
     * </p>
     *
     * @param id         The unique identifier of the genre to update. Must not be {@code null}.
     * @param name       The new name for the genre. Must not be {@code null}.
     * @param isActive   A Boolean indicating if the genre is active. If {@code null}, defaults to
     *                   {@code true}.
     * @param categories A list of category identifiers to associate with the genre. Must not be
     *                   {@code null}.
     * @return A new {@link UpdateGenreCommand} instance populated with the specified parameters.
     * @throws NullPointerException if {@code id}, {@code name}, or {@code categories} is
     *                              {@code null}.
     */
    public static UpdateGenreCommand with(
        final String id,
        final String name,
        final Boolean isActive,
        final List<String> categories
    ) {
        return new UpdateGenreCommand(
            id,
            name,
            isActive == null || isActive,
            categories
        );
    }
}
