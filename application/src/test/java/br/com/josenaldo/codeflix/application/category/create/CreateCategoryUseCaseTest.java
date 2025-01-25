package br.com.josenaldo.codeflix.application.category.create;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.josenaldo.codeflix.domain.category.CategoryGateway;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CreateCategoryUseCaseTest {

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

        final var categoryGateway = mock(CategoryGateway.class);
        when(categoryGateway.create(any())).thenAnswer(returnsFirstArg());

        final var useCase = new DefaultCreateCategoryUseCase(categoryGateway);

        // Act - When
        var output = useCase.execute(command);

        // Assert - Then
        assertThat(output).isNotNull();
        assertThat(output.id()).isNotNull();

        verify(categoryGateway).create(argThat(category ->
                                                   Objects.equals(expectedName, category.getName())
                                                       && Objects.equals(
                                                       expectedDescription,
                                                       category.getDescription()
                                                   )
                                                       && Objects.equals(
                                                       expectedIsActive,
                                                       category.isActive()
                                                   )
                                                       && Objects.nonNull(category.getId())
                                                       && Objects.nonNull(category.getCreatedAt())
                                                       && Objects.nonNull(category.getUpdatedAt())
                                                       && Objects.isNull(category.getDeletedAt())));
    }

    // Tentar criar uma categoria com dados inválidos
    // Tentar criar uma categoria inativa com dados válidos
    // Tentar criar uma categoria e ocorrer um erro inesperado no gateway
}
