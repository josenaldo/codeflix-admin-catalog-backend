package br.com.josenaldo.codeflix.catalog.application.genre.update;

import br.com.josenaldo.codeflix.catalog.domain.category.Category;
import br.com.josenaldo.codeflix.catalog.domain.category.CategoryGateway;
import br.com.josenaldo.codeflix.catalog.domain.category.CategoryID;
import br.com.josenaldo.codeflix.catalog.domain.exceptions.DomainException;
import br.com.josenaldo.codeflix.catalog.domain.exceptions.NotFoundException;
import br.com.josenaldo.codeflix.catalog.domain.exceptions.NotificationException;
import br.com.josenaldo.codeflix.catalog.domain.genre.Genre;
import br.com.josenaldo.codeflix.catalog.domain.genre.GenreGateway;
import br.com.josenaldo.codeflix.catalog.domain.genre.GenreID;
import br.com.josenaldo.codeflix.catalog.domain.validation.Error;
import br.com.josenaldo.codeflix.catalog.domain.validation.ValidationHandler;
import br.com.josenaldo.codeflix.catalog.domain.validation.handler.Notification;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A default implementation of the {@link UpdateGenreUseCase} that provides functionality to update
 * an existing genre within the system.
 * <p>
 * This use case manages the following responsibilities:
 * <ul>
 *   <li>Validating whether the genre exists in the system.</li>
 *   <li>Updating the genre's attributes, including its name, active status, and associated
 *       categories.</li>
 *   <li>Validating the existence of the associated categories before proceeding with the update.</li>
 *   <li>Throwing appropriate exceptions if validation fails or the requested genres or categories
 *       are not found.</li>
 * </ul>
 * <p>
 * The concrete behavior relies on the provided {@link CategoryGateway} and {@link GenreGateway},
 * which manage interactions with the persistence layer for {@link Category} and {@link Genre}
 * entities, respectively.
 * <p>
 * This implementation ensures that domain rules and integrity constraints are maintained during
 * the update process.
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public class DefaultUpdateGenreUseCase extends UpdateGenreUseCase {

    private final CategoryGateway categoryGateway;

    private final GenreGateway genreGateway;

    /**
     * Constructs a new instance of {@code DefaultUpdateGenreUseCase}.
     * <p>
     * This constructor initializes the use case with the necessary gateways to interact with
     * categories and genres. These gateways are required for operations related to updating genres,
     * such as validating associated categories and updating the genre details.
     *
     * @param aCategoryGateway The gateway for performing operations related to {@link Category}.
     *                         Must not be {@code null}.
     * @param aGenreGateway    The gateway for performing operations related to {@link Genre}. Must
     *                         not be {@code null}.
     * @throws NullPointerException if {@code aCategoryGateway} or {@code aGenreGateway} is null.
     */
    public DefaultUpdateGenreUseCase(
        final CategoryGateway aCategoryGateway,
        final GenreGateway aGenreGateway
    ) {
        categoryGateway = Objects.requireNonNull(aCategoryGateway, CATEGORY_GATEWAY_NULL_ERROR);
        genreGateway = Objects.requireNonNull(aGenreGateway, GENRE_GATEWAY_NULL_ERROR);
    }

    /**
     * Executes the use case to update a genre in the system.
     * <p>
     * This method processes the provided {@code UpdateGenreCommand} to update a genre's attributes,
     * such as its name, active status, and associated categories. It validates the input, ensures
     * the genre exists, and ensures all associated categories are valid. If the operation
     * encounters errors during validation or updating, an exception is thrown.
     * </p>
     *
     * <p>
     * Upon successful execution, the updated genre's details are returned encapsulated in an
     * {@link UpdateGenreOutput} object.
     * </p>
     *
     * @param aCommand The command containing information about the genre update, including its ID,
     *                 new name, active status, and associated categories. Must not be
     *                 {@code null}.
     * @return An {@link UpdateGenreOutput} containing the ID of the updated genre.
     * @throws NullPointerException  If {@code aCommand} is {@code null}.
     * @throws NotFoundException     If the genre with the given ID does not exist.
     * @throws NotificationException If validation errors occur during the update.
     */
    @Override
    public UpdateGenreOutput execute(final UpdateGenreCommand aCommand) {
        final var anId = GenreID.fromString(aCommand.id());
        final var aName = aCommand.name();
        final var isActive = aCommand.isActive();
        final var categories = toCategoryId(aCommand.categories());

        var aGenre = genreGateway
            .findById(anId)
            .orElseThrow(NotFoundException.supplierOf(Genre.class, anId));

        final var notification = Notification.create();

        notification.append(validateCategories(categories));
        notification.validate(() -> aGenre.update(aName, isActive, categories));

        if (notification.hasErrors()) {
            var errorMessage = UPDATE_GENRE_VALIDATION_ERROR_MESSAGE_TEMPLATE
                .formatted(anId.getValue());
            throw new NotificationException(errorMessage, notification);
        }

        return UpdateGenreOutput.from(this.genreGateway.update(aGenre));
    }

    /**
     * Converts a list of category ID strings into a list of {@link CategoryID} objects.
     * <p>
     * This method maps each string in the provided list to a {@code CategoryID} instance using the
     * {@link CategoryID#fromString(String)} method. The resulting list contains corresponding
     * {@code CategoryID} objects for valid strings.
     * </p>
     *
     * @param categories A list of strings representing category IDs. Must not be {@code null}.
     * @return A list of {@code CategoryID} objects corresponding to the provided strings. If the
     * input list is empty, an empty list is returned.
     * @throws NullPointerException If the input {@code categories} list is {@code null}.
     * @throws DomainException      If any string in the list is not a valid ULID.
     */
    private List<CategoryID> toCategoryId(final List<String> categories) {
        return categories.stream().map(CategoryID::fromString).toList();
    }

    /**
     * Validates the provided list of category IDs.
     * <p>
     * This method checks if the provided category IDs are valid and exist in the system. If any of
     * the IDs are missing or invalid, an error is added to a {@code ValidationHandler}. This
     * ensures that all referenced categories are verified before proceeding with further
     * operations.
     * </p>
     *
     * <p>
     * Validation involves:
     * <ul>
     *   <li>Checking if the list of IDs is {@code null} or empty, returning an empty {@code ValidationHandler} in such cases.</li>
     *   <li>Verifying the existence of each category ID using the category gateway, and collecting missing IDs in error messages.</li>
     * </ul>
     * </p>
     *
     * @param ids A list of {@link CategoryID} instances representing the categories to validate.
     *            May be {@code null} or empty.
     * @return A {@link ValidationHandler} containing any validation errors. If all IDs are valid,
     * the handler will contain no errors.
     * @throws NullPointerException If internal operations encounter {@code null} improperly.
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

            final var missingIdsMessage = missingIds
                .stream()
                .map(CategoryID::getValue)
                .collect(Collectors.joining(", "));

            final Error missingIdsError = new Error(
                GENRE_CATEGORIES_NOT_FOUND_ERROR_TEMPLATE.formatted(missingIdsMessage)
            );

            notification.append(missingIdsError);
        }

        return notification;
    }
}
