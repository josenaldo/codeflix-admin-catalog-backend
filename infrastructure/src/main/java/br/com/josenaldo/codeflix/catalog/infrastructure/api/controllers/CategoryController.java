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
import br.com.josenaldo.codeflix.catalog.domain.exceptions.NotFoundException;
import br.com.josenaldo.codeflix.catalog.domain.pagination.Pagination;
import br.com.josenaldo.codeflix.catalog.domain.pagination.SearchQuery;
import br.com.josenaldo.codeflix.catalog.domain.validation.handler.Notification;
import br.com.josenaldo.codeflix.catalog.infrastructure.api.CategoryApi;
import br.com.josenaldo.codeflix.catalog.infrastructure.category.models.CategoryListResponse;
import br.com.josenaldo.codeflix.catalog.infrastructure.category.models.CategoryResponse;
import br.com.josenaldo.codeflix.catalog.infrastructure.category.models.CreateCategoryRequest;
import br.com.josenaldo.codeflix.catalog.infrastructure.category.models.UpdateCategoryRequest;
import br.com.josenaldo.codeflix.catalog.infrastructure.category.presenters.CategoryApiPresenter;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.Objects;
import java.util.function.Function;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller provides endpoints to manage category resources within the application. It
 * implements the {@link CategoryApi} interface, ensuring adherence to a predefined contract for
 * category-related operations.
 *
 * <p>The {@code CategoryController} class integrates category management use cases,
 * allowing clients to:
 * <ul>
 *   <li>Create a category</li>
 *   <li>Retrieve details of a specific category by ID</li>
 *   <li>List categories with support for pagination, searching, and sorting</li>
 *   <li>Update an existing category</li>
 *   <li>Delete a category</li>
 * </ul>
 *
 * <p>Each endpoint processes input requests and returns responses in the expected format
 * while utilizing the appropriate application use case.
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
@RestController
public class CategoryController implements CategoryApi {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final GetCategoryByIdUseCase getCategoryByIdUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;
    private final ListCategoryUseCase listCategoryUseCase;

    /**
     * Initializes a new instance of the {@code CategoryController} class.
     * <p>
     * This constructor sets up the category management use cases required to handle operations such
     * as creating, retrieving, updating, deleting, and listing categories.
     *
     * @param aCreateCategoryUseCase  The use case for creating a new category. Must not be
     *                                {@code null}.
     * @param aGetCategoryByIdUseCase The use case for retrieving a category by its ID. Must not be
     *                                {@code null}.
     * @param anUpdateCategoryUseCase The use case for updating an existing category. Must not be
     *                                {@code null}.
     * @param aDeleteCategoryUseCase  The use case for deleting a category by its ID. Must not be
     *                                {@code null}.
     * @param aListCategoryUseCase    The use case for listing categories with search and pagination
     *                                functionality. Must not be {@code null}.
     * @throws NullPointerException If any of the provided use cases is {@code null}.
     */
    public CategoryController(
        final CreateCategoryUseCase aCreateCategoryUseCase,
        final GetCategoryByIdUseCase aGetCategoryByIdUseCase,
        final UpdateCategoryUseCase anUpdateCategoryUseCase,
        final DeleteCategoryUseCase aDeleteCategoryUseCase,
        final ListCategoryUseCase aListCategoryUseCase
    ) {
        this.createCategoryUseCase = Objects.requireNonNull(aCreateCategoryUseCase);
        this.getCategoryByIdUseCase = Objects.requireNonNull(aGetCategoryByIdUseCase);
        this.updateCategoryUseCase = Objects.requireNonNull(anUpdateCategoryUseCase);
        this.deleteCategoryUseCase = Objects.requireNonNull(aDeleteCategoryUseCase);
        this.listCategoryUseCase = Objects.requireNonNull(aListCategoryUseCase);
    }

    /**
     * Creates a new category based on the provided input data.
     * <p>
     * This method processes the given category creation request, delegating the creation logic to
     * the appropriate use case. If the creation is successful, a {@code ResponseEntity} with the
     * created category's details and a location header is returned. If validation fails or an error
     * occurs, an appropriate error response is returned.
     *
     * @param input The data required to create a new category, including name, description, and
     *              activation status. Must not be {@code null}.
     * @return A {@link ResponseEntity} containing one of the following:
     * <ul>
     *   <li>A created response with the new category details if the creation succeeds.</li>
     *   <li>An unprocessable entity response containing validation notifications
     *       if the creation fails due to input validation issues.</li>
     * </ul>
     * @throws NullPointerException If {@code input} is {@code null}.
     */
    @Override
    public ResponseEntity<?> createCategory(final CreateCategoryRequest input) {
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
     * Retrieves the details of a specific category using its unique identifier.
     * <p>
     * This method delegates the retrieval logic to the corresponding use case and adapts the result
     * into an appropriate response object.
     *
     * @param id The unique identifier of the category to retrieve. Must not be {@code null} or
     *           empty.
     * @return A {@link CategoryResponse} containing all the details of the requested category.
     * @throws NotFoundException If {@code id} is invalid or not found.
     */
    @Override
    public CategoryResponse getById(final String id) {
        final CategoryOutput categoryOutput = getCategoryByIdUseCase.execute(id);

        return CategoryApiPresenter.present(categoryOutput);
    }

    /**
     * Lists categories based on search criteria, pagination options, and sorting preferences.
     * <p>
     * This method allows clients to retrieve a paginated list of categories. The results can be
     * filtered using a search term, sorted by specific fields, and ordered in ascending or
     * descending order. Pagination is controlled through the {@code page} and {@code perPage}
     * parameters, while sorting is determined by {@code sortField} and {@code sortOrder}.
     *
     * @param search    A search term used to filter categories by name, description, or other
     *                  searchable attributes. Can be {@code null} or empty to list all categories.
     * @param page      The page number to retrieve, starting from 1. Must be greater than or equal
     *                  to 1.
     * @param perPage   The number of categories to include per page. Must be greater than 0.
     * @param sortField The field by which to sort the categories (e.g., name, createdAt). Must not
     *                  be {@code null}.
     * @param sortOrder The sort order, either {@code ASC} for ascending or {@code DESC} for
     *                  descending. Must not be {@code null}.
     * @return A {@link Pagination} object containing a list of categories and pagination details,
     * such as total count, current page, and total pages.
     */
    @Override
    public Pagination<CategoryListResponse> listCategories(
        final String search,
        final int page,
        final int perPage,
        final String sortField,
        final String sortOrder
    ) {
        Pagination<CategoryListOutput> pagination = listCategoryUseCase
            .execute(new SearchQuery(page, perPage, search, sortField, sortOrder));
        return pagination.map(CategoryApiPresenter::present);
    }

    /**
     * Updates a category identified by the given ID using the data provided in the input. This
     * method processes the update operation and returns an appropriate HTTP response.
     * <p>
     * It uses a command pattern to encapsulate the update details and handles the response through
     * success and error folding functions.
     *
     * @param id    The unique identifier of the category to be updated. Must not be null or empty.
     * @param input The request object containing update details, including name, description, and
     *              activation status. Must be validated before calling this method.
     * @return A {@link ResponseEntity} containing the result of the update operation:
     * <ul>
     *     <li>If successful, returns the updated category details with an HTTP 200 status.</li>
     *     <li>If the update fails due to validation errors or other issues, returns
     *         an HTTP 422 (unprocessable entity) with a detailed notification object.</li>
     * </ul>
     */
    @Override
    public ResponseEntity<?> updateById(
        final String id,
        @Valid final UpdateCategoryRequest input
    ) {

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

    /**
     * Deletes a category identified by the given ID.
     *
     * <p>
     * This method triggers the execution of the delete operation for the category with the
     * specified identifier.
     *
     * @param id the unique identifier of the category to be deleted; must not be null or empty
     */
    @Override
    public void deleteById(final String id) {
        deleteCategoryUseCase.execute(id);
    }
}
