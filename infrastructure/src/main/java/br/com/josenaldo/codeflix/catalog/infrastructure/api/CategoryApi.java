package br.com.josenaldo.codeflix.catalog.infrastructure.api;

import br.com.josenaldo.codeflix.catalog.domain.pagination.Pagination;
import br.com.josenaldo.codeflix.catalog.infrastructure.category.models.CategoryApiOutput;
import br.com.josenaldo.codeflix.catalog.infrastructure.category.models.CreateCategoryApiInput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping(value = "categories")
@Tag(name = "Categories")
public interface CategoryApi {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Category created successfully"),
        @ApiResponse(responseCode = "422", description = "A validation error occurred"),
        @ApiResponse(responseCode = "500", description = "An unexpected server error occurred")
    })
    ResponseEntity<?> createCategory(@RequestBody CreateCategoryApiInput input);

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "List all categories with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categories listed successfully"),
        @ApiResponse(responseCode = "422", description = "A invalid parameter was sent"),
        @ApiResponse(responseCode = "500", description = "An unexpected server error occurred")
    })
    Pagination<?> listCategories(
        @RequestParam(name = "search", required = false, defaultValue = "") final String search,
        @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
        @RequestParam(name = "perPage", required = false, defaultValue = "10") final int perPage,
        @RequestParam(name = "sortField", required = false, defaultValue = "name") final int sortField,
        @RequestParam(name = "sortOrder", required = false, defaultValue = "ASC") final int sortOrder
    );

    @GetMapping(
        value = "{id}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Get a category by its identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Category retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Category not found"),
        @ApiResponse(responseCode = "500", description = "An unexpected server error occurred")
    })
    CategoryApiOutput getById(@PathVariable(name = "id") String id);
}
