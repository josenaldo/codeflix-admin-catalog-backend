package br.com.josenaldo.codeflix.application.category.create;

import br.com.josenaldo.codeflix.domain.category.Category;
import br.com.josenaldo.codeflix.domain.category.CategoryGateway;
import br.com.josenaldo.codeflix.domain.validation.handler.ThrowsValidationHandler;
import java.util.Objects;

public class DefaultCreateCategoryUseCase extends CreateCategoryUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultCreateCategoryUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public CreateCategoryOutput execute(final CreateCategoryCommand command) {
        final var name = command.name();
        final var description = command.description();
        final var isActive = command.isActive();

        final var category = Category.newCategory(name, description, isActive);

        category.validate(new ThrowsValidationHandler());
        this.categoryGateway.create(category);

        return CreateCategoryOutput.from(category);
    }
}
