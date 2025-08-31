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

    /**
     * Defines an error message indicating that the {@code categoryGateway} dependency must not be
     * null.
     * <p>
     * This constant is used primarily for validation purposes within the {@link CreateGenreUseCase}
     * to signal a configuration or dependency injection error when the required
     * {@code categoryGateway} dependency is absent.
     * <p>
     * Expected usage includes internal validation, where an exception might be raised with this
     * message to help identify and resolve misconfigurations.
     */
    public static final String CATEGORY_GATEWAY_NULL_ERROR = "categoryGateway must not be null";

    /**
     * Represents the error message used to signify that the genre gateway instance is
     * {@code null}.
     * <p>
     * This constant provides a predefined error message to be used when the required genre gateway
     * dependency is not initialized or injected. It ensures consistency of the error message across
     * different parts of the application.
     * <p>
     * This message is typically used in validation scenarios where the genre gateway is expected to
     * be non-null before proceeding with an operation.
     */
    public static final String GENRE_GATEWAY_NULL_ERROR = "genreGateway must not be null";

    /**
     * Represents the error message template used when genre creation validation fails.
     * <p>
     * This constant defines a generic error message indicating that the system could not create the
     * aggregate for the genre. The message is used as part of the error handling mechanism during
     * validation and execution of the genre creation process.
     * <p>
     * It may be referenced in exception handling, logging, or client-facing error responses to
     * provide a standardized error description.
     */
    public static final String CREATE_GENRE_VALIDATION_ERROR_MESSAGE_TEMPLATE = "Could not create aggregate Genre";

    /**
     * Template message for errors when specific categories cannot be found during the genre
     * creation process.
     * <p>
     * This constant is used to generate an error message that includes a list of missing category
     * identifiers when a genre is being created. The `%s` placeholder is replaced with the list of
     * categories that could not be located.
     * <p>
     * Responsibilities:
     * <ul>
     *   <li>Provides a consistent format for reporting missing categories in genre creation.</li>
     *   <li>Helps in identifying invalid or missing category dependencies during validation.</li>
     * </ul>
     * <p>
     * Errors generated using this template are typically handled by the application to ensure proper
     * feedback is provided to the user or calling system.
     */
    public static final String GENRE_CATEGORIES_NOT_FOUND_ERROR_TEMPLATE = "Some categories could not be found: %s";

    /**
     * Represents the error message displayed when the categories associated with a genre are null.
     * <p>
     * This constant is used to indicate that the categories parameter provided to a genre creation
     * operation must not be null. It serves as a predefined error message to be used in validation
     * scenarios, ensuring consistency throughout the application.
     */
    public static final String CATEGORIES_NULL_ERROR_MESSAGE = "categories must not be null";
}
