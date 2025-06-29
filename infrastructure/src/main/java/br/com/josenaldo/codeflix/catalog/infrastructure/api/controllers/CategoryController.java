package br.com.josenaldo.codeflix.catalog.infrastructure.api.controllers;

import br.com.josenaldo.codeflix.catalog.application.category.create.CreateCategoryCommand;
import br.com.josenaldo.codeflix.catalog.application.category.create.CreateCategoryOutput;
import br.com.josenaldo.codeflix.catalog.application.category.create.CreateCategoryUseCase;
import br.com.josenaldo.codeflix.catalog.application.category.retrieve.get.CategoryOutput;
import br.com.josenaldo.codeflix.catalog.application.category.retrieve.get.GetCategoryByIdUseCase;
import br.com.josenaldo.codeflix.catalog.domain.pagination.Pagination;
import br.com.josenaldo.codeflix.catalog.domain.validation.handler.Notification;
import br.com.josenaldo.codeflix.catalog.infrastructure.api.CategoryApi;
import br.com.josenaldo.codeflix.catalog.infrastructure.category.models.CategoryApiOutput;
import br.com.josenaldo.codeflix.catalog.infrastructure.category.models.CreateCategoryApiInput;
import br.com.josenaldo.codeflix.catalog.infrastructure.category.presenters.CategoryApiPresenter;
import java.net.URI;
import java.util.Objects;
import java.util.function.Function;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryController implements CategoryApi {

    private final CreateCategoryUseCase createCategoryUseCase;

    private final GetCategoryByIdUseCase getCategoryByIdUseCase;

    public CategoryController(
        CreateCategoryUseCase createCategoryUseCase,
        GetCategoryByIdUseCase getCategoryByIdUseCase
    ) {
        this.createCategoryUseCase = Objects.requireNonNull(createCategoryUseCase);
        this.getCategoryByIdUseCase = Objects.requireNonNull(getCategoryByIdUseCase);
    }

    /**
     * @return
     */
    @Override
    public ResponseEntity<?> createCategory(final CreateCategoryApiInput input) {
        final var aCommand = CreateCategoryCommand.with(
            input.name(),
            input.description(),
            input.isActive()
        );

        final Function<Notification, ResponseEntity<?>> onError = notification ->
            ResponseEntity.unprocessableEntity().body(notification);

        final Function<CreateCategoryOutput, ResponseEntity<?>> onSuccess = output ->
            ResponseEntity.created(URI.create("/categories/" + output.id())).body(output);

        return this.createCategoryUseCase.execute(aCommand).fold(
            onError,
            onSuccess
        );
    }

    /**
     * @param search
     * @param page
     * @param perPage
     * @param sortField
     * @param sortOrder
     * @return
     */
    @Override
    public Pagination<?> listCategories(
        String search,
        int page,
        int perPage,
        int sortField,
        int sortOrder
    ) {
        return null;
    }

    /**
     * @param id
     * @return
     */
    @Override
    public CategoryApiOutput getById(final String id) {
        final CategoryOutput categoryOutput = getCategoryByIdUseCase.execute(id);

        return CategoryApiPresenter.present(categoryOutput);
    }
}
