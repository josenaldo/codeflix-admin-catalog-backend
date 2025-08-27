package br.com.josenaldo.codeflix.catalog.application.genre.create;

import br.com.josenaldo.codeflix.catalog.application.UseCase;


/**
 * Represents the abstract use case for creating a new genre.
 * <p>
 * This abstract class extends {@link UseCase} to define the contract for executing a genre creation
 * operation. The input is a {@link CreateGenreCommand} and the output is a
 * {@link CreateGenreOutput} that represents the successfully created genre.
 * <p>
 * Implementations validate the input and apply the business rules required to create a genre. They
 * may signal validation failures and other errors according to the application's conventions.
 * <p>
 * Responsibilities of concrete implementations:
 * <ul>
 *   <li>Validate input data (for example, verifying provided categories and fields).</li>
 *   <li>Interact with gateways or repositories to verify dependencies and persist the genre.</li>
 *   <li>Return a {@link CreateGenreOutput} with any generated identifiers.</li>
 * </ul>
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public abstract class CreateGenreUseCase extends
    UseCase<CreateGenreCommand, CreateGenreOutput> {

}
