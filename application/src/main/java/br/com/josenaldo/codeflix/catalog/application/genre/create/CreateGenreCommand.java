package br.com.josenaldo.codeflix.catalog.application.genre.create;

import br.com.josenaldo.codeflix.catalog.domain.category.CategoryID;
import java.util.List;

/**
 * Command object that carries the input data required to create a genre.
 * <p>
 * It encapsulates the genre's name, activation status, and the associated category identifiers.
 * Implementations that consume this command are expected to validate the fields according to their
 * own business rules.
 *
 * @param name       the name of the genre.
 * @param isActive   indicates whether the genre is active.
 * @param categories the list of category identifiers associated with the genre; must not be
 *                   {@code null} (an empty list indicates no categories).
 *                   <p>
 *                   Invariants:
 *                   <ul>
 *                     <li>{@code categories} is expected to be non-null.</li>
 *                   </ul>
 *                   <p>
 *                   Special notes:
 *                   <ul>
 *                     <li>Use {@link #with(String, Boolean, List)} when the activation flag may be {@code null}.</li>
 *                   </ul>
 *                   <p>
 *                   The associated categories are represented as their string identifiers (see {@link CategoryID}).
 *                   Missing or unknown identifiers are typically handled by the use case validation phase.
 *                   <p>
 *                   The {@code isActive} component reflects the final resolved state (not nullable).
 *                   When using {@link #with(String, Boolean, List)}, a {@code null} {@code isActive} is treated as {@code true}.
 *                   <p>
 *                   This record only carries data and does not perform validation by itself.
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public record CreateGenreCommand(
    String name,
    boolean isActive,
    List<String> categories
) {

    /**
     * Factory method that builds a {@code CreateGenreCommand} from possibly nullable activation
     * input.
     * <p>
     * The provided {@code isActive} value is interpreted as follows:
     * <ul>
     *   <li>If {@code isActive} is {@code null}, the resulting command sets {@code isActive} to {@code true}.</li>
     *   <li>Otherwise, the provided boolean value is used.</li>
     * </ul>
     *
     * @param name       the name of the genre.
     * @param isActive   the desired activation flag; if {@code null}, it defaults to {@code true}.
     * @param categories the list of category identifiers associated with the genre; must not be
     *                   {@code null} (an empty list indicates no categories).
     * @return a new {@code CreateGenreCommand} initialized with the provided values and default
     * handling.
     */

    public static CreateGenreCommand with(
        final String name,
        final Boolean isActive,
        final List<String> categories
    ) {
        return new CreateGenreCommand(
            name,
            isActive == null || isActive,
            categories
        );
    }
}
