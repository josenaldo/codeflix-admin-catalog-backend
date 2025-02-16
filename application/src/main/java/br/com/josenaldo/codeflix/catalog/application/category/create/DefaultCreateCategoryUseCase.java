package br.com.josenaldo.codeflix.catalog.application.category.create;

import static io.vavr.API.Try;

import br.com.josenaldo.codeflix.catalog.domain.category.Category;
import br.com.josenaldo.codeflix.catalog.domain.category.CategoryGateway;
import br.com.josenaldo.codeflix.catalog.domain.validation.handler.Notification;
import io.vavr.control.Either;
import java.util.Objects;

/**
 * Default implementation of {@link CreateCategoryUseCase} that handles the creation of a new
 * category.
 * <p>
 * This use case validates the input command and uses a {@link CategoryGateway} to persist the
 * category. If validation errors occur, a {@link Notification} with the errors is returned;
 * otherwise, a successful result is wrapped in a {@link CreateCategoryOutput}.
 * <p>
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public class DefaultCreateCategoryUseCase extends CreateCategoryUseCase {

    /**
     * The gateway used to persist categories.
     */
    private final CategoryGateway categoryGateway;

    /**
     * Constructs a new {@code DefaultCreateCategoryUseCase} with the specified
     * {@link CategoryGateway}.
     * <p>
     * The provided {@code categoryGateway} is used to persist the created category.
     *
     * @param categoryGateway the gateway used for category persistence; must not be {@code null}.
     */
    public DefaultCreateCategoryUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    /**
     * Executes the category creation use case.
     * <p>
     * It extracts the name, description, and active status from the command, creates a new
     * {@link Category}, and validates it using a {@link Notification}. If validation errors are
     * found, it returns an {@link Either} containing a {@link Notification} with the errors.
     * Otherwise, it persists the category using the {@link CategoryGateway} and returns a
     * successful {@link CreateCategoryOutput}.
     *
     * @param command the command containing the data for creating a category.
     * @return an {@link Either} containing either a {@link Notification} with errors or a
     * {@link CreateCategoryOutput} upon success.
     */
    @Override
    public Either<Notification, CreateCategoryOutput> execute(final CreateCategoryCommand command) {
        final var name = command.name();
        final var description = command.description();
        final var isActive = command.isActive();

        final var notification = Notification.create();

        final var category = Category.newCategory(name, description, isActive);
        category.validate(notification);

        return notification.hasErrors() ? Either.left(notification) : createCategory(category);
    }

    /**
     * Persists the provided category using the {@link CategoryGateway}.
     * <p>
     * This method attempts to create the category and maps any exceptions to a {@link Notification}
     * using {@code Notification::create}. Otherwise, it transforms the created category into a
     * {@link CreateCategoryOutput}.
     *
     * @param category the category to persist.
     * @return an {@link Either} with a {@link Notification} if an error occurred or a
     * {@link CreateCategoryOutput} upon success.
     */
    private Either<Notification, CreateCategoryOutput> createCategory(Category category) {
        return Try(() -> categoryGateway.create(category))
            .toEither()
            .bimap(Notification::create, CreateCategoryOutput::from);
    }
}
