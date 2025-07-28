package br.com.josenaldo.codeflix.catalog.application.category.update;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.josenaldo.codeflix.catalog.domain.category.Category;
import br.com.josenaldo.codeflix.catalog.domain.category.CategoryGateway;
import br.com.josenaldo.codeflix.catalog.domain.category.CategoryID;
import br.com.josenaldo.codeflix.catalog.domain.category.CategoryValidator;
import br.com.josenaldo.codeflix.catalog.domain.exceptions.DomainException;
import br.com.josenaldo.codeflix.catalog.domain.exceptions.NotFoundException;
import br.com.josenaldo.codeflix.catalog.domain.validation.Error;
import br.com.josenaldo.codeflix.catalog.domain.validation.handler.Notification;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for the UpdateCategoryUseCase.
 */
@ExtendWith(MockitoExtension.class)
class UpdateCategoryUseCaseTest {

    @InjectMocks
    private DefaultUpdateCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    /**
     * Resets the mock objects before each test.
     */
    @BeforeEach
    void tearDown() {
        reset(categoryGateway);
    }

    /**
     * Tests the happy path for updating a category. It verifies that when a valid command is
     * provided, the category is updated correctly and the updated category output contains a valid
     * id.
     */
    @Test
    void givenAValidCommand_whenUpdateCategory_thenReturnCategory() {
        // Arrange - Given
        final var category = Category.newCategory("Movie", null, true);
        final var expectedId = category.getId();

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedActive = true;

        final var command = UpdateCategoryCommand.with(
            expectedId.getValue(),
            expectedName,
            expectedDescription,
            expectedActive
        );

        when(categoryGateway.findById(expectedId)).thenReturn(Optional.of(category.clone()));
        when(categoryGateway.update(any())).thenAnswer(returnsFirstArg());

        // Act - When
        final var actualOutput = useCase.execute(command).get();

        // Assert - Then
        assertThat(actualOutput).isNotNull();
        assertThat(actualOutput.id()).isNotNull();

        verify(categoryGateway, times(1)).findById(expectedId);
        verify(categoryGateway, times(1)).update(argThat(
            aCategory ->
                Objects.deepEquals(aCategory.getId(), expectedId)
                    && Objects.deepEquals(aCategory.getName(), expectedName)
                    && Objects.deepEquals(aCategory.getDescription(), expectedDescription)
                    && Objects.deepEquals(aCategory.isActive(), expectedActive)
                    && Objects.nonNull(aCategory.getCreatedAt())
                    && Objects.nonNull(aCategory.getUpdatedAt())
                    && aCategory.getUpdatedAt().isAfter(category.getUpdatedAt())
                    && Objects.isNull(aCategory.getDeletedAt())
        ));
    }

    /**
     * Tests that updating a category with an invalid name (empty string) results in a
     * DomainException containing the expected error message. Also verifies that the update method
     * on the gateway is not invoked.
     */
    @Test
    void givenAInvalidName_whenCallsUpdateCategory_thenReturnDomainExpception() {
        // Arrange - Given
        final var category = Category.newCategory("Movie", null, true);
        final var expectedId = category.getId();

        final var expectedName = "";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedActive = true;

        final var expectedErrorMessage = CategoryValidator.EMPTY_NAME_ERROR;
        final var expectedErrorCount = 1;

        final var command = UpdateCategoryCommand.with(
            expectedId.getValue(),
            expectedName,
            expectedDescription,
            expectedActive
        );

        when(categoryGateway.findById(expectedId)).thenReturn(Optional.of(category.clone()));

        // Act - When
        final Notification notification = useCase.execute(command).getLeft();

        // Assert - Then
        assertThat(notification).isNotNull();
        assertThat(notification.getErrors()).isNotEmpty();
        assertThat(notification.fisrtError().message()).isEqualTo(expectedErrorMessage);
        assertThat(notification.getErrors()).hasSize(expectedErrorCount);

        verify(categoryGateway, times(0)).update(any());
    }

    /**
     * Tests that updating a category to inactive status updates the deletedAt field accordingly.
     * Verifies that the category is updated correctly and that the updated category output contains
     * a valid id.
     */
    @Test
    void givenAnInactiveCategory_whenUpdateCategory_thenReturnDomainException() {
        // Arrange - Given
        final var category = Category.newCategory("Movie", null, true);
        final var expectedId = category.getId();

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedActive = false;

        final var command = UpdateCategoryCommand.with(
            expectedId.getValue(),
            expectedName,
            expectedDescription,
            expectedActive
        );

        when(categoryGateway.findById(expectedId)).thenReturn(Optional.of(category.clone()));
        when(categoryGateway.update(any())).thenAnswer(returnsFirstArg());

        assertThat(category.isActive()).isTrue();
        assertThat(category.getDeletedAt()).isNull();

        // Act - When
        final UpdateCategoryOutput actualOutput = useCase.execute(command).get();

        // Assert - Then
        assertThat(actualOutput).isNotNull();
        assertThat(actualOutput.id()).isNotNull();

        verify(categoryGateway, times(1)).findById(expectedId);
        verify(categoryGateway, times(1)).update(argThat(
            aCategory ->
                Objects.deepEquals(aCategory.getId(), expectedId)
                    && Objects.deepEquals(aCategory.getName(), expectedName)
                    && Objects.deepEquals(aCategory.getDescription(), expectedDescription)
                    && Objects.deepEquals(aCategory.isActive(), expectedActive)
                    && Objects.nonNull(aCategory.getCreatedAt())
                    && Objects.nonNull(aCategory.getUpdatedAt())
                    && aCategory.getUpdatedAt().isAfter(category.getUpdatedAt())
                    && Objects.nonNull(aCategory.getDeletedAt())
        ));
    }

    /**
     * Tests that when the gateway throws an unexpected exception during update, the use case
     * returns a notification with the gateway's error message. Also verifies that the gateway's
     * update method is called with the correct parameters.
     */
    @Test
    void givenAValidCommand_whenGatewayThrowsRandomException_thenShouldReturnDomainException() {
        // Arrange - Given
        final var category = Category.newCategory("Movie", null, true);

        final var expectedId = category.getId();
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedActive = true;

        final var expectedErrorCount = 1;
        final var expectedMessage = "Gateway error";

        final var command = UpdateCategoryCommand.with(
            expectedId.getValue(),
            expectedName,
            expectedDescription,
            expectedActive
        );

        when(categoryGateway.findById(expectedId)).thenReturn(Optional.of(category.clone()));
        when(categoryGateway.update(any())).thenThrow(new IllegalStateException("Gateway error"));

        // Act - When
        Notification notification = useCase.execute(command).getLeft();

        // Assert - Then
        assertThat(notification).isNotNull();
        assertThat(notification.hasErrors()).isTrue();
        assertThat(notification.getErrors()).hasSize(expectedErrorCount);
        assertThat(notification.fisrtError().message()).isEqualTo(expectedMessage);

        verify(categoryGateway, times(1)).findById(expectedId);
        verify(categoryGateway, times(1)).update(argThat(
            aCategory ->
                Objects.deepEquals(aCategory.getId(), expectedId)
                    && Objects.deepEquals(aCategory.getName(), expectedName)
                    && Objects.deepEquals(aCategory.getDescription(), expectedDescription)
                    && Objects.deepEquals(aCategory.isActive(), expectedActive)
                    && Objects.nonNull(aCategory.getCreatedAt())
                    && Objects.nonNull(aCategory.getUpdatedAt())
                    && aCategory.getUpdatedAt().isAfter(category.getUpdatedAt())
                    && Objects.isNull(aCategory.getDeletedAt())
        ));
    }

    /**
     * Tests that when an invalid category id is provided for update, the use case throws a
     * DomainException indicating that the category was not found. Also verifies that the gateway's
     * update method is not invoked.
     */
    @Test
    void givenAnInvalidId_whenCallsUpdateCategory_thenReturnDomainException() {
        // Arrange - Given
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedActive = true;

        final var invalidId = CategoryID.unique();

        final var expectedErrorCount = 0;
        final var expectedMessage = "Category with ID %s was not found".formatted(invalidId.getValue());

        final var command = UpdateCategoryCommand.with(
            invalidId.getValue(),
            expectedName,
            expectedDescription,
            expectedActive
        );

        when(categoryGateway.findById(invalidId)).thenReturn(Optional.empty());

        // Act - When
        final var actualException = catchException(() -> useCase.execute(command));

        // Assert - Then
        verify(categoryGateway, times(0)).update(any());
        assertThat(actualException)
            .isNotNull()
            .isInstanceOf(NotFoundException.class);

        final NotFoundException notFoundException = (NotFoundException) actualException;
        assertThat(notFoundException)
            .hasMessage(expectedMessage)
            .hasNoCause();

        final List<Error> errors = notFoundException.getErrors();
        assertThat(errors).hasSize(expectedErrorCount);
    }
}
