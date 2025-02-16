package br.com.josenaldo.codeflix.catalog.application.category.create;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import br.com.josenaldo.codeflix.catalog.annotations.IntegrationTest;
import br.com.josenaldo.codeflix.catalog.domain.category.CategoryGateway;
import br.com.josenaldo.codeflix.catalog.domain.validation.handler.Notification;
import br.com.josenaldo.codeflix.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import br.com.josenaldo.codeflix.catalog.infrastructure.category.persistence.CategoryRepository;
import io.vavr.control.Either;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

/**
 * Integration test class for the CreateCategoryUseCase.
 * <p>
 * This class verifies the end-to-end behavior of the CreateCategoryUseCase, including its
 * interaction with the persistence layer through the CategoryGateway and CategoryRepository. It
 * tests scenarios for creating active and inactive categories, handling invalid input, and
 * propagating exceptions thrown by the gateway.
 * <p>
 * The tests ensure that the use case returns the expected outputs or error notifications and that
 * the database state reflects the performed operations.
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
@IntegrationTest
public class CreateCategoryUseCaseIntegrationTest {

    @Autowired
    private CreateCategoryUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @MockitoSpyBean
    private CategoryGateway categoryGateway;

    /**
     * Resets the mock objects before each test.
     * <p>
     * This ensures that previous interactions with the CategoryGateway do not affect subsequent
     * tests.
     */
    @BeforeEach
    void tearDown() {
        Mockito.reset(categoryGateway);
    }

    /**
     * Tests that when a valid command is provided for creating an active category, the use case
     * returns a valid category ID and persists the category with the expected properties.
     * <p>
     * The test verifies that the repository count increases, the returned category output is not
     * null, and that the persisted entity matches the expected name, description, active status,
     * and deletion state.
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
        assertThat(categoryRepository.count()).isEqualTo(0);

        // Act - When
        Either<Notification, CreateCategoryOutput> executeOutput = useCase.execute(command);

        // Assert - Then
        assertThat(categoryRepository.count()).isEqualTo(1);
        assertThat(executeOutput.isRight()).isTrue();
        CreateCategoryOutput createCategoryOutput = executeOutput.get();
        assertThat(createCategoryOutput).isNotNull();
        assertThat(createCategoryOutput.id()).isNotNull();

        String id = createCategoryOutput.id().getValue();
        CategoryJpaEntity entity = categoryRepository.findById(id).orElse(null);
        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getName()).isEqualTo(expectedName);
        assertThat(entity.getDescription()).isEqualTo(expectedDescription);
        assertThat(entity.isActive()).isEqualTo(expectedIsActive);
        assertThat(entity.getDeletedAt()).isNull();
    }

    /**
     * Tests that when a valid command is provided for creating an inactive category, the use case
     * returns a valid category ID and persists the category with a non-null deletion date.
     * <p>
     * The test verifies that the repository count increases, the returned category output is valid,
     * and that the persisted entity has its deletion date set, indicating an inactive category.
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
        assertThat(categoryRepository.count()).isEqualTo(0);

        // Act - When
        Either<Notification, CreateCategoryOutput> executeOutput = useCase.execute(command);

        // Assert - Then
        assertThat(categoryRepository.count()).isEqualTo(1);
        assertThat(executeOutput.isRight()).isTrue();
        CreateCategoryOutput createCategoryOutput = executeOutput.get();
        String id = createCategoryOutput.id().getValue();
        assertThat(createCategoryOutput).isNotNull();
        assertThat(id).isNotNull();

        CategoryJpaEntity entity = categoryRepository.findById(id).orElse(null);
        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getName()).isEqualTo(expectedName);
        assertThat(entity.getDescription()).isEqualTo(expectedDescription);
        assertThat(entity.isActive()).isEqualTo(expectedIsActive);
        assertThat(entity.getDeletedAt()).isNotNull();
    }

    /**
     * Tests that when an invalid command (with a null name) is provided, the use case returns a
     * notification with an error and does not invoke the category gateway.
     * <p>
     * The test verifies that the repository remains unchanged, the output is a left containing the
     * expected error message, and that the create method of the category gateway is not called.
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
        assertThat(categoryRepository.count()).isEqualTo(0);

        // Act - When
        Either<Notification, CreateCategoryOutput> executeOutput = useCase.execute(command);

        // Assert - Then
        assertThat(categoryRepository.count()).isEqualTo(0);
        assertThat(executeOutput.isLeft()).isTrue();
        Notification notification = executeOutput.getLeft();
        assertThat(notification).isNotNull();
        assertThat(notification.hasErrors()).isTrue();
        assertThat(notification.getErrors()).hasSize(expectedErrorCount);
        assertThat(notification.fisrtError().message()).isEqualTo(expectedMessage);
        verify(categoryGateway, times(0)).create(any());
    }

    /**
     * Tests that when a valid command is provided but the category gateway throws an unexpected
     * exception, the use case returns a notification with the gateway's error message.
     * <p>
     * The test verifies that the repository remains unchanged, and the output is a left containing
     * the expected error message, indicating that the exception was handled appropriately.
     */
    @Test
    public void givenAValidCommand_whenGatewayThrowsRandomException_thenShouldThrowDomainException() {
        // Arrange - Given
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final int expectedErrorCount = 1;
        final var expectedMessage = "Gateway error";
        final var command = CreateCategoryCommand.with(
            expectedName,
            expectedDescription,
            expectedIsActive
        );

        doThrow(new IllegalStateException("Gateway error")).when(categoryGateway).create(any());
        assertThat(categoryRepository.count()).isEqualTo(0);

        // Act - When
        Either<Notification, CreateCategoryOutput> executeOutput = useCase.execute(command);

        // Assert - Then
        assertThat(categoryRepository.count()).isEqualTo(0);
        assertThat(executeOutput.isLeft()).isTrue();
        Notification notification = executeOutput.getLeft();
        assertThat(notification).isNotNull();
        assertThat(notification.hasErrors()).isTrue();
        assertThat(notification.getErrors()).hasSize(expectedErrorCount);
        assertThat(notification.fisrtError().message()).isEqualTo(expectedMessage);
    }
}
