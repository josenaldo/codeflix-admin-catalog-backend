package br.com.josenaldo.codeflix.catalog.application.category.update;

import br.com.josenaldo.codeflix.catalog.domain.category.Category;
import br.com.josenaldo.codeflix.catalog.domain.category.CategoryGateway;
import br.com.josenaldo.codeflix.catalog.domain.category.CategoryID;
import br.com.josenaldo.codeflix.catalog.domain.exceptions.NotFoundException;
import br.com.josenaldo.codeflix.catalog.domain.validation.handler.Notification;
import io.vavr.API;
import io.vavr.control.Either;
import java.util.Objects;

/**
 * Default implementation of the {@link UpdateCategoryUseCase} for updating an existing category.
 * <p>
 * This class handles the update operation by retrieving the category by its identifier, applying
 * the update, validating the updated category, and then persisting the changes via the
 * {@link CategoryGateway}.
 * <p>
 * If the category is not found, an exception is thrown. In case of validation errors, a
 * {@link Notification} containing the errors is returned as the left value of an {@link Either};
 * otherwise, the successful result is wrapped in an {@link UpdateCategoryOutput} as the right
 * value.
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public class DefaultUpdateCategoryUseCase extends UpdateCategoryUseCase {

    /**
     * The gateway used for performing operations related to categories.
     */
    private final CategoryGateway categoryGateway;

    /**
     * Constructs a new {@code DefaultUpdateCategoyUseCase} with the specified
     * {@link CategoryGateway}.
     * <p>
     * The provided gateway is used to retrieve and update category data in the underlying data
     * store.
     *
     * @param categoryGateway the category gateway; must not be {@code null}.
     */
    public DefaultUpdateCategoryUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    /**
     * Executes the update category use case.
     * <p>
     * It extracts the necessary fields from the {@link UpdateCategoryCommand}, retrieves the
     * corresponding category, and applies the updates. After validation, if there are errors, it
     * returns a {@link Notification} wrapped in an {@link Either} as the left value; otherwise, it
     * persists the updated category and returns an {@link UpdateCategoryOutput} wrapped in an
     * {@link Either}.
     *
     * @param command the update category command containing the new data.
     * @return an {@link Either} containing either a {@link Notification} with validation errors or
     * an {@link UpdateCategoryOutput} with the updated category.
     */
    @Override
    public Either<Notification, UpdateCategoryOutput> execute(final UpdateCategoryCommand command) {
        final CategoryID id = CategoryID.fromString(command.id());
        final String name = command.name();
        final String description = command.description();
        final boolean isActive = command.isActive();

        Category category = this.categoryGateway
            .findById(id)
            .orElseThrow(NotFoundException.supplierOf(Category.class, id));

        final var notification = Notification.create();

        category.update(name, description, isActive)
                .validate(notification);

        return notification.hasErrors() ? Either.left(notification) : update(category);
    }

    /**
     * Persists the updated category and returns the result.
     * <p>
     * This method uses the {@link CategoryGateway} to update the category. It leverages Vavr's
     * {@code Try} construct to handle potential exceptions, mapping them to a {@link Notification}
     * if necessary.
     *
     * @param category the updated category.
     * @return an {@link Either} containing a {@link Notification} in case of errors or an
     * {@link UpdateCategoryOutput} upon success.
     */
    private Either<Notification, UpdateCategoryOutput> update(final Category category) {
        return API.Try(() -> this.categoryGateway.update(category))
                  .toEither()
                  .bimap(Notification::create, UpdateCategoryOutput::from);
    }
}
