package br.com.josenaldo.codeflix.catalog.application.category.retrieve.get;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.josenaldo.codeflix.catalog.domain.category.Category;
import br.com.josenaldo.codeflix.catalog.domain.category.CategoryGateway;
import br.com.josenaldo.codeflix.catalog.domain.category.CategoryID;
import br.com.josenaldo.codeflix.catalog.domain.exceptions.NotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Get Category By Id Use Case Test
 */
@ExtendWith(MockitoExtension.class)
class GetCategoryByIdUseCaseTest {

    @InjectMocks
    private DefaultGetCategoryByIdUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    public void setup() {
        reset(categoryGateway);
    }

    /**
     * Given a existent category id, when find category by id, then return category
     */
    @Test
    void givenExistentCategoryId_whenFindCategoryById_thenShouldReturnCategory() {
        // Arrange - Given
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var category = Category.newCategory(
            expectedName,
            expectedDescription,
            expectedIsActive
        );
        final var expectedId = category.getId();

        when(categoryGateway.findById(expectedId)).thenReturn(Optional.of(category.clone()));

        // Act - When
        final CategoryOutput actualCategory = useCase.execute(expectedId.getValue());

        // Assert - Then
        assertThat(actualCategory).isNotNull();
        assertThat(actualCategory.id()).isEqualTo(expectedId);
        assertThat(actualCategory.createdAt()).isEqualTo(category.getCreatedAt());
        assertThat(actualCategory.updatedAt()).isEqualTo(category.getUpdatedAt());
        assertThat(actualCategory.deletedAt()).isEqualTo(category.getDeletedAt());
        assertThat(actualCategory.name()).isEqualTo(expectedName);
        assertThat(actualCategory.description()).isEqualTo(expectedDescription);
        assertThat(actualCategory.isActive()).isEqualTo(expectedIsActive);
    }

    /**
     * Given a non-existent category id, when find category by id, then return not found
     */
    @Test
    void givenNonExistentCategoryId_whenGetCategoryById_thenShouldThrowNotFoundException() {
        // Arrange - Given
        final var expectedId = CategoryID.unique();
        final var expectedErrorMessage = "Category with ID %s was not found".formatted(expectedId.getValue());

        when(categoryGateway.findById(expectedId)).thenReturn(Optional.empty());

        // Act - When
        final var actualException = catchException(() -> useCase.execute(expectedId.getValue()));

        // Assert - Then
        assertThat(actualException)
            .isNotNull()
            .isInstanceOf(NotFoundException.class)
            .hasMessage(expectedErrorMessage);
    }

    /**
     * Given an empty category id, when find category, then return not found exception
     */
    @Test
    void givenEmptyCategoryId_whenGetCategoryById_thenShouldThrowNotFoundException() {
        // Arrange - Given
        final var expectedId = "";
        final var expectedErrorMessage = "Category with ID %s was not found".formatted(expectedId);

        // Act - When
        final var actualException = catchException(() -> useCase.execute(expectedId));

        // Assert - Then
        assertThat(actualException)
            .isNotNull()
            .isInstanceOf(NotFoundException.class)
            .hasMessage(expectedErrorMessage);

        verify(categoryGateway, times(0)).findById(any());
    }

    /**
     * Given a blank category id, when find category, then return not found exception
     */
    @Test
    void givenBlankCategoryId_whenGetCategoryById_thenShouldThrowNotFoundException() {
        // Arrange - Given
        final var expectedId = "   ";
        final var expectedErrorMessage = "Category with ID %s was not found".formatted(expectedId);

        // Act - When
        final var actualException = catchException(() -> useCase.execute(expectedId));

        // Assert - Then
        assertThat(actualException)
            .isNotNull()
            .isInstanceOf(NotFoundException.class)
            .hasMessage(expectedErrorMessage);

        verify(categoryGateway, times(0)).findById(any());
    }

    /**
     * Given a invalid category id, when find category, then return not found exception
     */
    @Test
    void givenInvalidCategoryId_whenGetCategoryById_thenShouldThrowNotFoundException() {
        // Arrange - Given
        final var expectedId = "invalid-id";
        final var expectedErrorMessage = "Category with ID %s was not found".formatted(expectedId);

        // Act - When
        final var actualException = catchException(() -> useCase.execute(expectedId));

        // Assert - Then
        assertThat(actualException)
            .isNotNull()
            .isInstanceOf(NotFoundException.class)
            .hasMessage(expectedErrorMessage);

        verify(categoryGateway, times(0)).findById(any());
    }

    /**
     * Given a null category id, when find category, then return required ID exception
     */
    @Test
    void givenNullCategoryId_whenGetCategoryById_thenShouldThrowNotFoundException() {
        // Arrange - Given
        final String expectedId = null;
        final var expectedErrorMessage = "Category with ID %s was not found".formatted(expectedId);

        // Act - When
        final var actualException = catchException(() -> useCase.execute(expectedId));

        // Assert - Then
        assertThat(actualException)
            .isNotNull()
            .isInstanceOf(NotFoundException.class)
            .hasMessage(expectedErrorMessage);

        verify(categoryGateway, times(0)).findById(any());


    }

    /**
     * Given a error on call a gateway method, when find category by id, then throw exception
     */
    @Test
    void givenAnyCategoryId_whenCategoryGatewayThrowsAnException_thenReturnException() {
        // Arrange - Given
        final var expectedId = CategoryID.unique();
        final var expectedErrorMessage = "Gateway error";

        when(categoryGateway.findById(expectedId)).thenThrow(new IllegalStateException(
            expectedErrorMessage));

        // Act - When
        final var actualException = catchException(() -> useCase.execute(expectedId.getValue()));

        // Assert - Then
        assertThat(actualException)
            .isNotNull()
            .isInstanceOf(IllegalStateException.class)
            .hasMessage(expectedErrorMessage);
    }
}
