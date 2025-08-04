package br.com.josenaldo.codeflix.catalog.infrastructure.api.controllers;

import br.com.josenaldo.codeflix.catalog.application.category.create.CreateCategoryCommand;
import br.com.josenaldo.codeflix.catalog.application.category.create.CreateCategoryOutput;
import br.com.josenaldo.codeflix.catalog.application.category.create.CreateCategoryUseCase;
import br.com.josenaldo.codeflix.catalog.application.category.delete.DeleteCategoryUseCase;
import br.com.josenaldo.codeflix.catalog.application.category.retrieve.get.CategoryOutput;
import br.com.josenaldo.codeflix.catalog.application.category.retrieve.get.GetCategoryByIdUseCase;
import br.com.josenaldo.codeflix.catalog.application.category.retrieve.list.CategoryListOutput;
import br.com.josenaldo.codeflix.catalog.application.category.retrieve.list.ListCategoryUseCase;
import br.com.josenaldo.codeflix.catalog.application.category.update.UpdateCategoryCommand;
import br.com.josenaldo.codeflix.catalog.application.category.update.UpdateCategoryOutput;
import br.com.josenaldo.codeflix.catalog.application.category.update.UpdateCategoryUseCase;
import br.com.josenaldo.codeflix.catalog.domain.category.CategorySearchQuery;
import br.com.josenaldo.codeflix.catalog.domain.pagination.Pagination;
import br.com.josenaldo.codeflix.catalog.domain.validation.handler.Notification;
import br.com.josenaldo.codeflix.catalog.infrastructure.api.CategoryApi;
import br.com.josenaldo.codeflix.catalog.infrastructure.category.models.CategoryApiOutput;
import br.com.josenaldo.codeflix.catalog.infrastructure.category.models.CreateCategoryApiInput;
import br.com.josenaldo.codeflix.catalog.infrastructure.category.models.UpdateCategoryApiInput;
import br.com.josenaldo.codeflix.catalog.infrastructure.category.presenters.CategoryApiPresenter;
import io.vavr.control.Either;
import java.net.URI;
import java.util.Objects;
import java.util.function.Function;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryController implements CategoryApi {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final GetCategoryByIdUseCase getCategoryByIdUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;
    private final ListCategoryUseCase listCategoryUseCase;

    public CategoryController(
        final CreateCategoryUseCase createCategoryUseCase,
        final GetCategoryByIdUseCase getCategoryByIdUseCase,
        final UpdateCategoryUseCase updateCategoryUseCase,
        final DeleteCategoryUseCase deleteCategoryUseCase,
        final ListCategoryUseCase listCategoryUseCase
    ) {
        this.createCategoryUseCase = Objects.requireNonNull(createCategoryUseCase);
        this.getCategoryByIdUseCase = Objects.requireNonNull(getCategoryByIdUseCase);
        this.updateCategoryUseCase = Objects.requireNonNull(updateCategoryUseCase);
        this.deleteCategoryUseCase = Objects.requireNonNull(deleteCategoryUseCase);
        this.listCategoryUseCase = Objects.requireNonNull(listCategoryUseCase);
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
     * @param id
     * @return
     */
    @Override
    public CategoryApiOutput getById(final String id) {
        final CategoryOutput categoryOutput = getCategoryByIdUseCase.execute(id);

        return CategoryApiPresenter.present(categoryOutput);
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
        final String search,
        final int page,
        final int perPage,
        final String sortField,
        final String sortOrder
    ) {
        return listCategoryUseCase
            .execute(new CategorySearchQuery(page, perPage, search, sortField, sortOrder));
    }

    @Override
    public ResponseEntity<?> updateById(final String id, final UpdateCategoryApiInput input) {

        final var aCommand = UpdateCategoryCommand.with(
            id,
            input.name(),
            input.description(),
            input.isActive()
        );

        final Function<Notification, ResponseEntity<?>> onError = notification ->
            ResponseEntity.unprocessableEntity().body(notification);

        final Function<UpdateCategoryOutput, ResponseEntity<?>> onSuccess = ResponseEntity::ok;

        return this.updateCategoryUseCase.execute(aCommand).fold(
            onError,
            onSuccess
        );
    }

    @Override
    public void deleteById(final String id) {
        deleteCategoryUseCase.execute(id);
    }
}
