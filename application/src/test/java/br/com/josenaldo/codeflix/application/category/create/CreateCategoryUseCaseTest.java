package br.com.josenaldo.codeflix.application.category.create;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.josenaldo.codeflix.domain.category.CategoryGateway;
import br.com.josenaldo.codeflix.domain.exceptions.DomainException;
import br.com.josenaldo.codeflix.domain.validation.Error;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CreateCategoryUseCaseTest {

    @InjectMocks
    private DefaultCreateCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    // Tentar criar uma categoria ativa com dados válidos
    @Test
    public void givenAValidCommand_whenCreateCategory_thenShouldReturnCategoryId() {
        // Arrange - Given
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var command = CreateCategoryCommand.with(
            expectedName,
            expectedDescription,
            expectedIsActive
        );

        when(categoryGateway.create(any())).thenAnswer(returnsFirstArg());

        // Act - When
        var output = useCase.execute(command);

        // Assert - Then
        assertThat(output).isNotNull();
        assertThat(output.id()).isNotNull();

        verify(categoryGateway).create(
            argThat(category ->
                        Objects.equals(expectedName, category.getName())
                            && Objects.equals(expectedDescription, category.getDescription())
                            && Objects.equals(expectedIsActive, category.isActive())
                            && Objects.nonNull(category.getId())
                            && Objects.nonNull(category.getCreatedAt())
                            && Objects.nonNull(category.getUpdatedAt())
                            && Objects.isNull(category.getDeletedAt())
            )
        );
    }

    // Tentar criar uma categoria com dados inválidos
    @Test
    public void givenAnInvalidName_whenCallsCreateCategory_thenShouldThrowDomainException() {
        // Arrange - Given
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var command = CreateCategoryCommand.with(
            expectedName,
            expectedDescription,
            expectedIsActive
        );

        // Act
        final var actualException = catchException(() -> useCase.execute(command));

        // Then
        String expectedMessage = "'name' should not be null";

        assertThat(actualException).isNotNull();
        assertThat(actualException).isInstanceOf(DomainException.class);
        assertThat(actualException).hasMessage(expectedMessage);
        assertThat(actualException).hasNoCause();

        final DomainException actualDomainException = (DomainException) actualException;
        final List<Error> errors = actualDomainException.getErrors();

        assertThat(errors).hasSize(1);
        assertThat(errors.getFirst().message()).isEqualTo(expectedMessage);

        verify(categoryGateway, times(0)).create(any());
    }

    // Tentar criar uma categoria inativa com dados válidos
    @Test
    public void givenAValidCommandWithInactiveCategory_whenCreateCategory_thenShouldReturnInactiveCategoryId() {
        // Arrange - Given
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;

        final var command = CreateCategoryCommand.with(
            expectedName,
            expectedDescription,
            expectedIsActive
        );

        when(categoryGateway.create(any())).thenAnswer(returnsFirstArg());

        // Act - When
        var output = useCase.execute(command);

        // Assert - Then
        assertThat(output).isNotNull();
        assertThat(output.id()).isNotNull();

        verify(categoryGateway).create(
            argThat(category ->
                        Objects.equals(expectedName, category.getName())
                            && Objects.equals(expectedDescription, category.getDescription())
                            && Objects.equals(expectedIsActive, category.isActive())
                            && Objects.nonNull(category.getId())
                            && Objects.nonNull(category.getCreatedAt())
                            && Objects.nonNull(category.getUpdatedAt())
                            && Objects.nonNull(category.getDeletedAt())
            )
        );
    }

    // Tentar criar uma categoria e ocorrer um erro inesperado no gateway
    @Test
    public void givenAValidCommand_whenGatewayThrowsRandomException_thenShouldThrowDomainException() {
        // Arrange - Given
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var command = CreateCategoryCommand.with(
            expectedName,
            expectedDescription,
            expectedIsActive
        );

        when(categoryGateway.create(any())).thenThrow(new IllegalStateException("Gateway error"));

        // Act - When
        final var actualException = catchException(() -> useCase.execute(command));

        // Assert - Then
        assertThat(actualException).isNotNull();
        assertThat(actualException).hasMessage("Gateway error");
        assertThat(actualException).hasNoCause();

        verify(categoryGateway, times(1)).create(
            argThat(category ->
                        Objects.equals(expectedName, category.getName())
                            && Objects.equals(expectedDescription, category.getDescription())
                            && Objects.equals(expectedIsActive, category.isActive())
                            && Objects.nonNull(category.getId())
                            && Objects.nonNull(category.getCreatedAt())
                            && Objects.nonNull(category.getUpdatedAt())
                            && Objects.isNull(category.getDeletedAt())
            )
        );
    }
}
