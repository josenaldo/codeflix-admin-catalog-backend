package br.com.josenaldo.codeflix.application.category.create;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.josenaldo.codeflix.domain.category.CategoryGateway;
import br.com.josenaldo.codeflix.domain.validation.handler.Notification;
import io.vavr.control.Either;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Testes unitários para o caso de uso CreateCategoryUseCase
 */
@ExtendWith(MockitoExtension.class)
public class CreateCategoryUseCaseTest {

    @InjectMocks
    private DefaultCreateCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    /**
     * Tentar criar uma categoria ativa com dados válidos
     */
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
        Either<Notification, CreateCategoryOutput> executeOutput = useCase.execute(command);

        // Assert - Then
        assertThat(executeOutput.isRight()).isTrue();

        CreateCategoryOutput createCategoryOutput = executeOutput.get();
        assertThat(createCategoryOutput).isNotNull();
        assertThat(createCategoryOutput.id()).isNotNull();

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

    /**
     * Tentar criar uma categoria inativa com dados válidos
     */
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
        Either<Notification, CreateCategoryOutput> executeOutput = useCase.execute(command);

        // Assert - Then
        assertThat(executeOutput.isRight()).isTrue();

        CreateCategoryOutput createCategoryOutput = executeOutput.get();
        assertThat(createCategoryOutput).isNotNull();
        assertThat(createCategoryOutput.id()).isNotNull();

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

    /**
     * Tentar criar uma categoria com dados inválidos
     */
    @Test
    public void givenAnInvalidName_whenCallsCreateCategory_thenShouldThrowDomainException() {
        // Arrange - Given
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final String expectedMessage = "'name' should not be null";
        final int expectedErrorCount = 1;

        final var command = CreateCategoryCommand.with(
            expectedName,
            expectedDescription,
            expectedIsActive
        );

        // Act
        Either<Notification, CreateCategoryOutput> executeOutput = useCase.execute(command);

        // Then
        assertThat(executeOutput.isLeft()).isTrue();
        Notification notification = useCase.execute(command).getLeft();
        assertThat(notification).isNotNull();
        assertThat(notification.hasErrors()).isTrue();
        assertThat(notification.getErrors()).hasSize(expectedErrorCount);
        assertThat(notification.fisrtError().message()).isEqualTo(expectedMessage);

        verify(categoryGateway, times(0)).create(any());
    }

    /**
     * Tentar criar uma categoria e ocorrer um erro inesperado no gateway
     */
    @Test
    public void givenAValidCommand_whenGatewayThrowsRandomException_thenShouldThrowDomainException() {
        // Arrange - Given
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var expectedErrorCount = 1;
        final var expectedMessage = "Gateway error";

        final var command = CreateCategoryCommand.with(
            expectedName,
            expectedDescription,
            expectedIsActive
        );

        when(categoryGateway.create(any())).thenThrow(new IllegalStateException("Gateway error"));

        // Act - When
        Either<Notification, CreateCategoryOutput> executeOutput = useCase.execute(command);

        // Assert - Then
        assertThat(executeOutput.isLeft()).isTrue();
        Notification notification = executeOutput.getLeft();
        assertThat(notification).isNotNull();
        assertThat(notification.hasErrors()).isTrue();
        assertThat(notification.getErrors()).hasSize(expectedErrorCount);
        assertThat(notification.fisrtError().message()).isEqualTo(expectedMessage);

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
}
