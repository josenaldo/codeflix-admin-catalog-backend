package br.com.josenaldo.codeflix.catalog.application.genre.create;

import br.com.josenaldo.codeflix.catalog.domain.category.Category;
import br.com.josenaldo.codeflix.catalog.domain.category.CategoryGateway;
import br.com.josenaldo.codeflix.catalog.domain.category.CategoryID;
import br.com.josenaldo.codeflix.catalog.domain.exceptions.DomainException;
import br.com.josenaldo.codeflix.catalog.domain.exceptions.NotificationException;
import br.com.josenaldo.codeflix.catalog.domain.genre.Genre;
import br.com.josenaldo.codeflix.catalog.domain.genre.GenreGateway;
import br.com.josenaldo.codeflix.catalog.domain.validation.Error;
import br.com.josenaldo.codeflix.catalog.domain.validation.ValidationHandler;
import br.com.josenaldo.codeflix.catalog.domain.validation.handler.Notification;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Default implementation of {@link CreateGenreUseCase} that handles the creation of a new genre.
 * <p>
 * This use case:
 * <ul>
 *   <li>Receives a {@link CreateGenreCommand} with the genre data.</li>
 *   <li>Validates the provided category identifiers by checking their existence via the {@link CategoryGateway}.</li>
 *   <li>Aggregates validation  errors into a {@link Notification}.</li>
 *   <li>Delegates persistence to the {@link GenreGateway} when validation succeeds, returning a {@link CreateGenreOutput}.</li>
 * </ul>
 * <p>
 * Validation contract:
 * <ul>
 *   <li>If any referenced categories do not exist, an error describing the missing identifiers is appended.</li>
 *   <li>When there are no categories, the categories' validation passes without adding errors.</li>
 * </ul>
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public class DefaultCreateGenreUseCase extends CreateGenreUseCase {

    /**
     * Represents the gateway for interacting with {@link Category} entities within the use case.
     * <p>
     * The {@code categoryGateway} is used to perform various operations related to categories, such
     * as retrieval, validation, and existence checks. It acts as an abstraction over the underlying
     * data persistence mechanism to facilitate operations required by the
     * {@code DefaultCreateGenreUseCase}.
     */
    private final CategoryGateway categoryGateway;

    /**
     * Represents the gateway used for performing operations related to the {@link Genre} entity.
     * <p>
     * This field provides access to the {@link GenreGateway} dependency required for handling
     * persistence and retrieval of genre data within the use case. It supports operations such as:
     * <ul>
     *   <li>Creating a new genre.</li>
     *   <li>Updating an existing genre.</li>
     *   <li>Deleting a genre by its unique identifier.</li>
     *   <li>Fetching a genre by its unique identifier.</li>
     *   <li>Searching through genres with pagination and filtering criteria.</li>
     * </ul>
     * <p>
     * It is intended to be used within the {@code DefaultCreateGenreUseCase} to fulfill
     * genre-related interactions with the underlying data store.
     */
    private final GenreGateway genreGateway;

    /**
     * Constructs a new {@code DefaultCreateGenreUseCase} with the required gateways.
     *
     * @param categoryGateway the gateway used for category queries; must not be {@code null}.
     * @param genreGateway    the gateway used for genre persistence; must not be {@code null}.
     */
    public DefaultCreateGenreUseCase(
        final CategoryGateway categoryGateway,
        final GenreGateway genreGateway
    ) {
        this.categoryGateway = Objects.requireNonNull(
            categoryGateway,
            "categoryGateway must not be null"
        );
        this.genreGateway = Objects.requireNonNull(genreGateway, "genreGateway must not be null");
    }

    /**
     * Executes the "create genre" use case with the provided command.
     * <p>
     * This method processes the input command to create a new genre. It validates the input data,
     * particularly the associated categories, and ensures all required fields are valid before
     * attempting to create the genre. If validation is successful, a new genre is created and
     * persisted through the appropriate gateway.
     * <p>
     * Validation errors or issues during input processing are accumulated and, if any exist, a
     * {@link NotificationException} is thrown detailing the issues.
     *
     * @param aCommand the {@link CreateGenreCommand} containing all the input data needed to create
     *                 a genre. It includes the genre's name, activation status, and associated
     *                 category identifiers. Must not be {@code null}.
     * @return a {@link CreateGenreOutput} containing the unique identifier of the newly created
     * genre. If creation fails, no value is returned (a runtime exception is thrown instead).
     * @throws NotificationException if the input command fails validation, or if there are issues
     *                               with the associated categories. The exception contains the
     *                               specific validation errors encountered.
     */
    @Override
    public CreateGenreOutput execute(final CreateGenreCommand aCommand) {

        final var aName = aCommand.name();
        final var isActive = aCommand.isActive();
        final List<CategoryID> categories = toCategoryID(aCommand.categories());

        final Notification notification = Notification.create();
        notification.append(validateCategories(categories));

        final var aGenre = notification.validate(() -> Genre.newGenre(aName, isActive, categories));

        if (notification.hasErrors()) {
            throw new NotificationException(GENRE_CREATION_ERROR, notification);
        }

        return CreateGenreOutput.from(genreGateway.create(aGenre));
    }

    /**
     * Validates a list of category IDs against the existing categories in the system.
     * <p>
     * This method checks whether the provided list of {@code CategoryID}s is valid by ensuring that
     * all referenced IDs exist in the system's database. If any IDs are missing, the validation
     * process appends an error to the returned {@code ValidationHandler}.
     *
     * @param ids the list of {@code CategoryID}s to be validated. It can be {@code null} or empty,
     *            in which case no validation is performed and an empty notification is returned.
     * @return a {@link ValidationHandler} containing validation results. It may contain errors for
     * any missing {@code CategoryID}s or be empty if all IDs are valid.
     * @throws NullPointerException if {@code ids} contains {@code null} elements (inaccessible if
     *                              the system strictly enforces non-null lists).
     */
    private ValidationHandler validateCategories(final List<CategoryID> ids) {
        final var notification = Notification.create();

        if (ids == null || ids.isEmpty()) {
            return notification;
        }

        final List<CategoryID> retrievedIds = categoryGateway.existsByIds(ids);
        if (ids.size() != retrievedIds.size()) {
            final var missingIds = new ArrayList<>(ids);
            missingIds.removeAll(retrievedIds);
            final var missingIdsMessage = missingIds.stream()
                                                    .map(CategoryID::getValue)
                                                    .collect(Collectors.joining(", "));

            Error missingIdsError = new Error("Some categories could not be found: %s".formatted(
                missingIdsMessage));
            notification.append(missingIdsError);
        }

        return notification;
    }

    /**
     * Converts a list of string representations of category identifiers into a list of
     * {@link CategoryID} instances.
     * <p>
     * Each string in the input list is processed to create a corresponding {@code CategoryID}
     * object. If any string is invalid or null, an exception is thrown by the
     * {@link CategoryID#fromString(String)} method.
     *
     * @param categories the list of string representations of category identifiers. Must not be
     *                   {@code null}. An empty list results in an empty return value.
     * @return a list of {@link CategoryID} instances created from the provided strings. If the
     * input list is empty, an empty list is returned.
     * @throws NullPointerException if {@code categories} is null or contains null entries.
     * @throws DomainException      if any string in the list is not a valid category identifier.
     */
    private List<CategoryID> toCategoryID(final List<String> categories) {
        Objects.requireNonNull(categories, "categories must not be null");
        return categories.stream().map(CategoryID::fromString).toList();
    }
}
