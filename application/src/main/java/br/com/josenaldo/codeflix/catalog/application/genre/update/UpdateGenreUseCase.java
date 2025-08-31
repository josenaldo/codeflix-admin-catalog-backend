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

    /**
     * Represents an error message indicating that the category gateway is null.
     * <p>
     * This constant is used to signal that a required dependency for accessing category-related
     * data, the {@code categoryGateway}, has not been properly initialized or provided. It is
     * intended to assist in identifying and diagnosing configuration or runtime issues in the
     * application.
     * </p>
     */
    public static final String CATEGORY_GATEWAY_NULL_ERROR = "categoryGateway must not be null";

    /**
     * Represents an error message indicating that the genre gateway is null.
     * <p>
     * This constant is used to signal that a null value was encountered for the required dependency
     * `genreGateway` during the execution of the genre update use case. It ensures that proper
     * error handling can be implemented when the gateway is not provided, preventing unexpected
     * null pointer issues.
     * </p>
     */
    public static final String GENRE_GATEWAY_NULL_ERROR = "genreGateway must not be null";

    /**
     * Template for error messages indicating that some referenced categories could not be found.
     * <p>
     * This constant defines the error message structure used when an update operation involving a
     * genre fails due to missing category references. The message includes placeholders (e.g.,
     * "%s") for dynamically inserting a list of the missing category identifiers.
     * </p>
     *
     * <p>
     * It is used in scenarios where input validation or domain-specific constraints check whether
     * all categories associated with a genre update request exist in the system. If any category is
     * missing, this error message is used to provide clear feedback regarding the issue.
     * </p>
     */
    public static final String GENRE_CATEGORIES_NOT_FOUND_ERROR_TEMPLATE = "Some categories could not be found: %s";

    /**
     * A template for validation error messages encountered during the update of a genre aggregate.
     * <p>
     * This constant defines a formatted string used to generate descriptive error messages when the
     * validation process for updating a genre fails. It incorporates placeholders to dynamically
     * include the details of the genre being updated, enabling more context-aware error reporting.
     * </p>
     */
    public static final String UPDATE_GENRE_VALIDATION_ERROR_MESSAGE_TEMPLATE = "Could not update Aggregate Genre %s";
}
