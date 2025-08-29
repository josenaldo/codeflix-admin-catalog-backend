package br.com.josenaldo.codeflix.catalog.application.genre.update;

import br.com.josenaldo.codeflix.catalog.application.UseCase;

/**
 * Defines the use case for updating a genre within the system.
 * <p>
 * This abstract class operates as a specialized extension of {@link UseCase}, where the input is an
 * {@link UpdateGenreCommand} containing the necessary data to update an existing genre, and the
 * output is an {@link UpdateGenreOutput} providing the result of the operation.
 * </p>
 *
 * <p>
 * Concrete implementations must encapsulate the business logic required to validate the input,
 * update the genre, and handle any related domain-specific rules or constraints.
 * </p>
 *
 * <p>
 * The use case ensures:
 * <ul>
 *   <li>Validation of all input data, such as ensuring the existence of the genre and
 *       the referenced categories.</li>
 *   <li>Updating the genre's attributes (name, active status, associated categories) as requested.</li>
 *   <li>Handling errors via appropriate exceptions when the operation fails.</li>
 * </ul>
 * </p>
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public abstract class UpdateGenreUseCase extends UseCase<UpdateGenreCommand, UpdateGenreOutput> {

}
