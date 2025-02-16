package br.com.josenaldo.codeflix.catalog.application.category.delete;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import br.com.josenaldo.codeflix.catalog.domain.category.Category;
import br.com.josenaldo.codeflix.catalog.domain.category.CategoryGateway;
import br.com.josenaldo.codeflix.catalog.domain.category.CategoryID;
import br.com.josenaldo.codeflix.catalog.domain.exceptions.DomainException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DeleteCategoryUseCaseTest {

    @InjectMocks
    private DefaultDeleteCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @AfterEach
    public void tearDown() {
        reset(categoryGateway);
    }

    /**
     * Tests when a valid id is used to delete a category.
     */
    @Test
    public void givenAValidId_whenCallDelete_thenShouldBeOk() {
        // Arrange - Given
        final var category = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final var expectedId = category.getId();

        doNothing()
            .when(categoryGateway).deleteById(eq(expectedId));

        // Act - When
        final var actualException = catchException(() -> useCase.execute(expectedId.getValue()));

        // Assert - Then
        assertThat(actualException).isNull();

        verify(categoryGateway, times(1)).deleteById(eq(expectedId));
    }

    /**
     * Tests when a non-existent id is used to delete a category.
     */
    @Test
    public void givenANoExistentId_whenCallDelete_thenShouldBeOk() {
        // Arrange - Given
        final var expectedId = CategoryID.unique();
        doNothing()
            .when(categoryGateway).deleteById(eq(expectedId));

        // Act - When
        final var actualException = catchException(() -> useCase.execute(expectedId.getValue()));

        // Assert - Then
        assertThat(actualException).isNull();
        verify(categoryGateway, times(1)).deleteById(eq(expectedId));
    }

    /**
     * Tests when an error occurs when deleting a category.
     */
    @Test
    public void givenAValidId_whenCallDeleteByIdAndAnErrorOccur_thenShouldReturnAnException() {
        // Arrange - Given
        final var category = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final var expectedId = category.getId();

        doThrow(new IllegalStateException("Gateway error"))
            .when(categoryGateway).deleteById(eq(expectedId));

        // Act - When
        final var actualException = catchException(() -> useCase.execute(expectedId.getValue()));

        // Assert - Then
        assertThat(actualException).isNotNull();
        assertThat(actualException).isInstanceOf(IllegalStateException.class);
        assertThat(actualException).hasMessage("Gateway error");

        verify(categoryGateway, times(1)).deleteById(eq(expectedId));
    }

    /**
     * Tests when an invalid id is used to delete a category, then a exception should be thrown.
     */
    @Test
    public void givenAnInvalidId_whenCallDelete_thenShouldReturnAnException() {
        // Arrange - Given
        final var invalidId = "invalid-id";

        // Act - When
        final var actualException = catchException(() -> useCase.execute(invalidId));

        // Assert - Then
        assertThat(actualException).isNotNull();
        assertThat(actualException).isInstanceOf(DomainException.class);
        assertThat(actualException).hasMessage("the Id invalid-id is invalid");

        verify(categoryGateway, times(0)).deleteById(any());
    }

}
