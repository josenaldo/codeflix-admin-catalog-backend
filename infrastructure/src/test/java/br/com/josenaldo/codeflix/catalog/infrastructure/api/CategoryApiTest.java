package br.com.josenaldo.codeflix.catalog.infrastructure.api;

import static io.vavr.API.Left;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.josenaldo.codeflix.catalog.annotations.ControllerTest;
import br.com.josenaldo.codeflix.catalog.application.category.create.CreateCategoryOutput;
import br.com.josenaldo.codeflix.catalog.application.category.create.CreateCategoryUseCase;
import br.com.josenaldo.codeflix.catalog.application.category.retrieve.get.CategoryOutput;
import br.com.josenaldo.codeflix.catalog.application.category.retrieve.get.GetCategoryByIdUseCase;
import br.com.josenaldo.codeflix.catalog.domain.category.Category;
import br.com.josenaldo.codeflix.catalog.domain.category.CategoryID;
import br.com.josenaldo.codeflix.catalog.domain.category.CategoryValidator;
import br.com.josenaldo.codeflix.catalog.domain.exceptions.DomainException;
import br.com.josenaldo.codeflix.catalog.domain.validation.Error;
import br.com.josenaldo.codeflix.catalog.domain.validation.handler.Notification;
import br.com.josenaldo.codeflix.catalog.infrastructure.category.models.CreateCategoryApiInput;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.API;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

/**
 * Unit tests for the {@link CategoryApi} controller.
 * <p>
 * This test class ensures the proper behavior of the {@code CategoryApi} endpoints by simulating
 * HTTP requests and verifying the interactions with application service layers. It uses
 * {@code MockMvc} to simulate HTTP requests and JSON responses, enhancing the test coverage of the
 * API.
 * <p>
 * The primary goal is to validate the integration of the controller, serialization/deserialization
 * of request/response, and interactions with the service layer while ensuring the API adheres to
 * the expected behavior and contracts.
 * <p>
 * This class verifies:
 * <ul>
 *   <li>HTTP response statuses are as expected for valid/invalid inputs.</li>
 *   <li>Serialization of input and output objects.</li>
 *   <li>Correct delegation of tasks to the service layer.</li>
 *   <li>Structured and correct HTTP headers in responses.</li>
 * </ul>
 * <p>
 * Dependencies are mocked to focus on API and controller behavior without invoking actual service logic.
 *
 * @author Josenaldo de Oliveira Matos Filho
 */
@ControllerTest(controllers = CategoryApi.class)
class CategoryApiTest {

    /**
     * Provides the {@link MockMvc} instance for testing the application's controllers.
     * <p>
     * This variable is used to simulate HTTP requests and perform end-to-end tests of the
     * controllers in the Spring MVC application. It facilitates the validation of controller
     * behavior, including request handling, response processing, and integration with service
     * layers.
     * <p>
     * The {@link Autowired} annotation ensures that this instance is automatically injected by the
     * Spring framework during test initialization.
     */
    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private CreateCategoryUseCase createCategoryUseCase;

    @MockitoBean
    private GetCategoryByIdUseCase getCategoryByIdUseCase;

    @Autowired
    private ObjectMapper mapper;


    /**
     * Tests the behavior of the category creation API when a valid command is provided.
     * <p>
     * This method verifies that the API correctly processes a valid {@code CreateCategoryApiInput},
     * delegates to the use case, and returns the expected category ID in the response. It ensures
     * the proper integration between the controller, service layer, and response behavior.
     * <p>
     * Specifically, it performs the following verifications:
     * <ul>
     *     <li>Checks that the HTTP response status is 201 Created.</li>
     *     <li>Validates that the {@code Location} header in the response is correct.</li>
     *     <li>Ensures that the use case is executed exactly once with the expected command inputs.</li>
     * </ul>
     *
     * @throws Exception if any unexpected error occurs during the test execution, including errors
     *                   related to JSON serialization or MockMvc simulation.
     */
    @Test
    void givenAValidCommand_whenCallsCreateCategory_thenShouldReturnCategoryId() throws Exception {
        // Arrange - Given
        final var expectedId = CategoryID.unique();
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aInput = new CreateCategoryApiInput(
            expectedName,
            expectedDescription,
            expectedIsActive
        );

        when(createCategoryUseCase.execute(any()))
            .thenReturn(API.Right(CreateCategoryOutput.from(expectedId)));

        // Act - When
        final var request = post("/categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(aInput));

        ResultActions response = this.mvc.perform(request).andDo(print());

        // Assert - Then
        response.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/categories/" + expectedId))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId.getValue())));

        verify(createCategoryUseCase, times(1)).execute(
            argThat(cmd ->
                        Objects.equals(expectedName, cmd.name())
                            && Objects.equals(expectedDescription, cmd.description())
                            && Objects.equals(expectedIsActive, cmd.isActive())
            )
        );
    }

    @Test
    void givenAInvalidName_whenCallsCreateCategory_thenShouldReturnNotification() throws Exception {
        // Arrange - Given
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = CategoryValidator.NULL_NAME_ERROR;

        final var aInput = new CreateCategoryApiInput(
            null,
            expectedDescription,
            expectedIsActive
        );

        when(createCategoryUseCase.execute(any()))
            .thenReturn(Left(Notification.create(new Error(expectedErrorMessage))));

        // Act - When
        final var request = post("/categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(aInput));

        ResultActions response = this.mvc.perform(request).andDo(print());

        // Assert - Then
        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors.[0].message", equalTo(expectedErrorMessage)));

        verify(createCategoryUseCase, times(1)).execute(
            argThat(cmd ->
                        Objects.equals(null, cmd.name())
                            && Objects.equals(expectedDescription, cmd.description())
                            && Objects.equals(expectedIsActive, cmd.isActive())
            )
        );
    }

    @Test
    void givenAInvalidCommand_whenCallsCreateCategory_thenShouldReturnDomainException()
        throws Exception {
        // Arrange - Given
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = CategoryValidator.NULL_NAME_ERROR;

        final var aInput = new CreateCategoryApiInput(
            null,
            expectedDescription,
            expectedIsActive
        );

        when(createCategoryUseCase.execute(any()))
            .thenThrow(DomainException.with(expectedErrorMessage));

        // Act - When
        final var request = post("/categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(aInput));

        ResultActions response = this.mvc.perform(request).andDo(print());

        // Assert - Then
        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors.[0].message", equalTo(expectedErrorMessage)));

        verify(createCategoryUseCase, times(1)).execute(
            argThat(cmd ->
                        Objects.equals(null, cmd.name())
                            && Objects.equals(expectedDescription, cmd.description())
                            && Objects.equals(expectedIsActive, cmd.isActive())
            )
        );
    }

    @Test
    void givenAValidId_whenCallsGetCategory_thenShouldReturnCategory() throws Exception {
        // Arrange - Given
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(
            expectedName,
            expectedDescription,
            expectedIsActive
        );

        final var expectedId = aCategory.getId().getValue();

        when(getCategoryByIdUseCase.execute(any()))
            .thenReturn(CategoryOutput.from(aCategory));

        // Act - When
        final var request = get("/categories/{id}", expectedId).accept(MediaType.APPLICATION_JSON);

        final ResultActions response = this.mvc.perform(request).andDo(print());

        // Assert - Then
        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId)))
                .andExpect(jsonPath("$.name", equalTo(expectedName)))
                .andExpect(jsonPath("$.description", equalTo(expectedDescription)))
                .andExpect(jsonPath("$.is_active", equalTo(expectedIsActive)))
                .andExpect(jsonPath("$.created_at", equalTo(aCategory.getCreatedAt().toString())))
                .andExpect(jsonPath("$.updated_at", equalTo(aCategory.getUpdatedAt().toString())))
                .andExpect(jsonPath("$.deleted_at", equalTo(null)));
        verify(getCategoryByIdUseCase, times(1)).execute(expectedId);

    }

    @Test
    void givenInvalidId_whenCallsGetCategory_thenShouldReturnNotFoundException() throws Exception {
        // Arrange - Given
        final var expectedId = "invalid-id";
        final var expectedErrorMessage = "Category with ID %s was not found".formatted(expectedId);

        when(getCategoryByIdUseCase.execute(any()))
            .thenThrow(DomainException.with(expectedErrorMessage));

        // Act - When
        final var request = get(
            "/categories/{id}",
            expectedId
        ).contentType(MediaType.APPLICATION_JSON);

        final ResultActions response = this.mvc.perform(request).andDo(print());

        // Assert - Then
        response
            .andExpect(status().isNotFound())
            .andExpect(header().string("Location", nullValue()))
            .andExpect(jsonPath("$.errors", hasSize(1)))
            .andExpect(jsonPath("$.errors.[0].message", equalTo(expectedErrorMessage)));
    }
}
