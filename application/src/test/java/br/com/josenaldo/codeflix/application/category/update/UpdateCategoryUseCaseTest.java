package br.com.josenaldo.codeflix.application.category.update;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.josenaldo.codeflix.domain.category.Category;
import br.com.josenaldo.codeflix.domain.category.CategoryGateway;
import br.com.josenaldo.codeflix.domain.category.CategoryID;
import br.com.josenaldo.codeflix.domain.category.CategoryValidator;
import br.com.josenaldo.codeflix.domain.exceptions.DomainException;
import br.com.josenaldo.codeflix.domain.validation.Error;
import br.com.josenaldo.codeflix.domain.validation.handler.Notification;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UpdateCategoryUseCaseTest {

    @InjectMocks
    private DefaultUpdateCategoyUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    // 1. Teste do caminho feliz
    @Test
    public void givenAValidCommand_whenUpdateCategory_thenReturnCategory() {
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

        when(categoryGateway.findById(eq(expectedId))).thenReturn(Optional.of(category.clone()));
        when(categoryGateway.update(any())).thenAnswer(returnsFirstArg());

        // Act - When
        final var actualOutput = useCase.execute(command).get();

        // Assert - Then
        assertThat(actualOutput).isNotNull();
        assertThat(actualOutput.id()).isNotNull();

        verify(categoryGateway, times(1)).findById(eq(expectedId));
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

    // 2. Teste passando uma propriedade inválida (nome)
    @Test
    public void givenAInvalidName_whenCallsUpdateCategory_thenReturnDomainExpception() {
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

        when(categoryGateway.findById(eq(expectedId))).thenReturn(Optional.of(category.clone()));

        // Act - When
        final Notification notification = useCase.execute(command).getLeft();

        // Assert - Then
        assertThat(notification).isNotNull();
        assertThat(notification.getErrors()).isNotEmpty();
        assertThat(notification.fisrtError().message()).isEqualTo(expectedErrorMessage);
        assertThat(notification.getErrors().size()).isEqualTo(expectedErrorCount);

        verify(categoryGateway, times(0)).update(any());

    }

    // 3. Teste atualizando uma categoria para inativa
    @Test
    public void givenAnInactiveCategory_whenUpdateCategory_thenReturnDomainException() {
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

        when(categoryGateway.findById(eq(expectedId))).thenReturn(Optional.of(category.clone()));
        when(categoryGateway.update(any())).thenAnswer(returnsFirstArg());

        assertThat(category.isActive()).isTrue();
        assertThat(category.getDeletedAt()).isNull();

        // Act - When
        final UpdateCategoryOutput actualOutput = useCase.execute(command).get();

        // Assert - Then
        assertThat(actualOutput).isNotNull();
        assertThat(actualOutput.id()).isNotNull();

        verify(categoryGateway, times(1)).findById(eq(expectedId));
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

    // 4. Teste simulando erro ao atualizar a categoria
    @Test
    public void givenAValidCommand_whenGatewayThrowsRandomException_thenShouldReturnDomainException() {
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

        when(categoryGateway.findById(eq(expectedId))).thenReturn(Optional.of(category.clone()));
        when(categoryGateway.update(any())).thenThrow(new IllegalStateException("Gateway error"));

        // Act - When
        Notification notification = useCase.execute(command).getLeft();

        // Assert - Then
        assertThat(notification).isNotNull();
        assertThat(notification.hasErrors()).isTrue();
        assertThat(notification.getErrors()).hasSize(expectedErrorCount);
        assertThat(notification.fisrtError().message()).isEqualTo(expectedMessage);

        verify(categoryGateway, times(1)).findById(eq(expectedId));

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

    // 5. Teste atualizar categoria passando ID inválido
    @Test
    public void givenAnInvalidId_whenCallsUpdateCategory_thenReturnDomainException() {
        // Arrange - Given
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedActive = true;

        final var invalidId = CategoryID.unique();

        final var expectedErrorCount = 1;
        final var expectedMessage = "Category with ID %s was not found".formatted(invalidId.getValue());

        final var command = UpdateCategoryCommand.with(
            invalidId.getValue(),
            expectedName,
            expectedDescription,
            expectedActive
        );

        when(categoryGateway.findById(eq(invalidId))).thenReturn(Optional.empty());

        // Act - When
        final var actualException = catchException(() -> useCase.execute(command));

        // Assert - Then
        verify(categoryGateway, times(0)).update(any());

        assertThat(actualException).isNotNull();
        assertThat(actualException).isInstanceOf(DomainException.class);

        final DomainException actualDomainException = (DomainException) actualException;
        assertThat(actualDomainException).hasMessage(expectedMessage);
        assertThat(actualDomainException).hasNoCause();

        final List<Error> errors = actualDomainException.getErrors();
        assertThat(errors).hasSize(expectedErrorCount);

        Error firstError = errors.getFirst();
        assertThat(firstError.message()).isEqualTo(expectedMessage);

    }
}
