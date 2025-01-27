package br.com.josenaldo.codeflix.application.category.create;

import static io.vavr.API.Try;

import br.com.josenaldo.codeflix.domain.category.Category;
import br.com.josenaldo.codeflix.domain.category.CategoryGateway;
import br.com.josenaldo.codeflix.domain.validation.handler.Notification;
import io.vavr.control.Either;
import java.util.Objects;

public class DefaultCreateCategoryUseCase extends CreateCategoryUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultCreateCategoryUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

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

    private Either<Notification, CreateCategoryOutput> createCategory(Category category) {
        return Try(() -> categoryGateway.create(category))
            .toEither()
            .bimap(Notification::create, CreateCategoryOutput::from);
    }
}
