package br.com.josenaldo.codeflix.application.category.update;

import static br.com.josenaldo.codeflix.application.category.exceptions.CategoryExceptions.categoryNotFoundException;

import br.com.josenaldo.codeflix.domain.category.Category;
import br.com.josenaldo.codeflix.domain.category.CategoryGateway;
import br.com.josenaldo.codeflix.domain.category.CategoryID;
import br.com.josenaldo.codeflix.domain.validation.handler.Notification;
import io.vavr.API;
import io.vavr.control.Either;
import java.util.Objects;

public class DefaultUpdateCategoyUseCase extends UpdateCategoryUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultUpdateCategoyUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public Either<Notification, UpdateCategoryOutput> execute(final UpdateCategoryCommand command) {
        final CategoryID id = CategoryID.fromString(command.id());
        final String name = command.name();
        final String description = command.description();
        final boolean isActive = command.isActive();

        Category category = this.categoryGateway.findById(id).orElseThrow(categoryNotFoundException(
            id));

        final var notification = Notification.create();

        category.update(name, description, isActive).validate(notification);

        return notification.hasErrors() ? Either.left(notification) : update(category);
    }

    private Either<Notification, UpdateCategoryOutput> update(final Category category) {
        return API.Try(() -> this.categoryGateway.update(category))
                  .toEither()
                  .bimap(Notification::create, UpdateCategoryOutput::from);
    }


}
