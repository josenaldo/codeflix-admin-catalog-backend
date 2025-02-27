package br.com.josenaldo.codeflix.catalog.application.category.update;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import br.com.josenaldo.codeflix.catalog.annotations.IntegrationTest;
import br.com.josenaldo.codeflix.catalog.application.category.CategoryTestUtils;
import br.com.josenaldo.codeflix.catalog.domain.category.Category;
import br.com.josenaldo.codeflix.catalog.domain.category.CategoryGateway;
import br.com.josenaldo.codeflix.catalog.domain.category.CategoryID;
import br.com.josenaldo.codeflix.catalog.domain.category.CategoryValidator;
import br.com.josenaldo.codeflix.catalog.domain.exceptions.DomainException;
import br.com.josenaldo.codeflix.catalog.domain.validation.Error;
import br.com.josenaldo.codeflix.catalog.domain.validation.handler.Notification;
import br.com.josenaldo.codeflix.catalog.infrastructure.category.persistence.CategoryRepository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

/**
 * Integration test class for the category update use case.
 * <p>
 * This class contains integration tests to verify the behavior of the {@link UpdateCategoryUseCase}
 * in an integrated environment, interacting with the database and the category gateway.
 * <p>
 * It is responsible for verifying the entire flow of updating a category, including data
 * persistence and interactions with external components.
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
@IntegrationTest
public class UpdateCategoryUseCaseIntegrationTest {

    @Autowired
    private UpdateCategoryUseCase useCase;

    @Autowired
    private CategoryRepository repository;

    @MockitoSpyBean
    private CategoryGateway categoryGateway;

    private CategoryID categoryId;

    /**
     * Sets up the environment before each test.
     * <p>
     * This method is executed before each test and has the following responsibilities:
     * <ul>
     *     <li>Creates a new category to be used in the tests.</li>
     * </ul>
     */
    @BeforeEach
    public void setup() {
        Category category = Category.newCategory("Movie", null, true);
        CategoryTestUtils.save(repository, category);
        assertThat(repository.count()).isEqualTo(1);

        categoryId = category.getId();
    }

    /**
     * Cleans up the environment after each test.
     * <p>
     * This method is executed after each test and has the following responsibilities:
     * <ul>
     *     <li>Deletes all categories from the repository.</li>
     *     <li>Resets the category gateway spy.</li>
     * </ul>
     */
    @AfterEach
    public void tearDown() {
        repository.deleteAll();
        reset(categoryGateway);
    }

    /**
     * Tests the happy path for updating a category.
     * <p>
     * This test verifies that when a valid command is provided, the category is updated correctly
     * and that the updated category output contains a valid ID.
     */
    @Test
    public void givenAValidCommand_whenUpdateCategory_thenReturnCategory() {
        // Arrange - Given
        final var expectedId = categoryId;
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var command = UpdateCategoryCommand.with(
            expectedId.getValue(),
            expectedName,
            expectedDescription,
            expectedIsActive
        );

        assertThat(repository.count()).isEqualTo(1);
        final var existingCategory = repository.findById(expectedId.getValue()).orElse(null);
        assertThat(existingCategory).isNotNull();

        // Act - When
        final var actualOutput = useCase.execute(command).get();

        // Assert - Then
        assertThat(actualOutput).isNotNull();
        assertThat(actualOutput.id()).isNotNull();
        assertThat(repository.count()).isEqualTo(1);

        String id = actualOutput.id().getValue();
        var savedCategory = repository.findById(id).orElse(null);
        assertThat(savedCategory).isNotNull();
        assertThat(savedCategory.getId()).isEqualTo(existingCategory.getId());

        assertThat(savedCategory.getName()).isEqualTo(expectedName);
        assertThat(savedCategory.getName()).isNotEqualTo(existingCategory.getName());

        assertThat(savedCategory.getDescription()).isEqualTo(expectedDescription);
        assertThat(savedCategory.getDescription()).isNotEqualTo(existingCategory.getDescription());

        assertThat(savedCategory.isActive()).isEqualTo(expectedIsActive);
        assertThat(savedCategory.isActive()).isEqualTo(existingCategory.isActive());

        assertThat(savedCategory.getCreatedAt()).isEqualTo(existingCategory.getCreatedAt());
        assertThat(savedCategory.getUpdatedAt()).isAfter(existingCategory.getUpdatedAt());
        assertThat(savedCategory.getDeletedAt()).isNull();
    }

    /**
     * Tests updating a category with an invalid name (empty string).
     * <p>
     * This test verifies if a {@link DomainException} is thrown when the name is invalid and if the
     * expected error message is present. It also checks if the update method on the gateway is not
     * invoked.
     */
    @Test
    public void givenAInvalidName_whenCallsUpdateCategory_thenReturnDomainException() {
        // Arrange - Given
        final var expectedId = categoryId;
        final var expectedName = "";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var expectedErrorMessage = CategoryValidator.EMPTY_NAME_ERROR;
        final var expectedErrorCount = 1;

        final var command = UpdateCategoryCommand.with(
            expectedId.getValue(),
            expectedName,
            expectedDescription,
            expectedIsActive
        );

        assertThat(repository.count()).isEqualTo(1);
        final var existingCategory = repository.findById(expectedId.getValue()).orElse(null);
        assertThat(existingCategory).isNotNull();

        // Act - When
        final Notification notification = useCase.execute(command).getLeft();

        // Assert - Then
        assertThat(notification).isNotNull();
        assertThat(notification.getErrors()).isNotEmpty();
        assertThat(notification.fisrtError().message()).isEqualTo(expectedErrorMessage);
        assertThat(notification.getErrors().size()).isEqualTo(expectedErrorCount);

        assertThat(repository.count()).isEqualTo(1);

        var savedCategory = repository.findById(categoryId.getValue()).orElse(null);
        assertThat(savedCategory).isNotNull();
        assertThat(savedCategory.getId()).isEqualTo(existingCategory.getId());

        assertThat(savedCategory.getName()).isNotEqualTo(expectedName);
        assertThat(savedCategory.getName()).isEqualTo(existingCategory.getName());

        assertThat(savedCategory.getDescription()).isNotEqualTo(expectedDescription);
        assertThat(savedCategory.getDescription()).isEqualTo(existingCategory.getDescription());

        assertThat(savedCategory.isActive()).isEqualTo(expectedIsActive);
        assertThat(savedCategory.isActive()).isEqualTo(existingCategory.isActive());

        assertThat(savedCategory.getCreatedAt()).isEqualTo(existingCategory.getCreatedAt());
        assertThat(savedCategory.getUpdatedAt()).isEqualTo(existingCategory.getUpdatedAt());
        assertThat(savedCategory.getDeletedAt()).isNull();
    }

    /**
     * Tests updating a category to inactive status.
     * <p>
     * This test verifies if the `deletedAt` field is updated correctly when the category is
     * deactivated. It also checks if the category is updated correctly and if the updated category
     * output contains a valid ID.
     */
    @Test
    public void givenAnInactivateCategory_whenUpdateCategory_thenShouldReturnAnUpdatedCategory() {
        // Arrange - Given
        final var expectedId = categoryId;
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;

        final var command = UpdateCategoryCommand.with(
            expectedId.getValue(),
            expectedName,
            expectedDescription,
            expectedIsActive
        );

        assertThat(repository.count()).isEqualTo(1);

        final var existingCategory = repository.findById(expectedId.getValue()).orElse(null);
        assertThat(existingCategory).isNotNull();
        assertThat(existingCategory.isActive()).isTrue();
        assertThat(existingCategory.getDeletedAt()).isNull();

        // Act - When
        final UpdateCategoryOutput actualOutput = useCase.execute(command).get();

        // Assert - Then
        // Assert - Then
        assertThat(actualOutput).isNotNull();
        assertThat(actualOutput.id()).isNotNull();
        assertThat(repository.count()).isEqualTo(1);

        String id = actualOutput.id().getValue();
        var savedCategory = repository.findById(id).orElse(null);
        assertThat(savedCategory).isNotNull();
        assertThat(savedCategory.getId()).isEqualTo(existingCategory.getId());

        assertThat(savedCategory.getName()).isEqualTo(expectedName);
        assertThat(savedCategory.getName()).isNotEqualTo(existingCategory.getName());

        assertThat(savedCategory.getDescription()).isEqualTo(expectedDescription);
        assertThat(savedCategory.getDescription()).isNotEqualTo(existingCategory.getDescription());

        assertThat(savedCategory.isActive()).isEqualTo(expectedIsActive);
        assertThat(savedCategory.isActive()).isNotEqualTo(existingCategory.isActive());

        assertThat(savedCategory.getCreatedAt()).isEqualTo(existingCategory.getCreatedAt());
        assertThat(savedCategory.getUpdatedAt()).isAfter(existingCategory.getUpdatedAt());
        assertThat(savedCategory.getDeletedAt()).isAfter(existingCategory.getCreatedAt());
    }

    /**
     * Tests if, when the gateway throws an unexpected exception during the update, the use case
     * returns a notification with the gateway's error message.
     * <p>
     * It also verifies if the gateway's update method is called with the correct parameters.
     */
    @Test
    public void givenAValidCommand_whenGatewayThrowsRandomException_thenShouldReturnDomainException() {
        // Arrange - Given
        final var expectedId = categoryId;
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var expectedErrorCount = 1;
        final var expectedMessage = "Gateway error";

        assertThat(repository.count()).isEqualTo(1);
        final var existingCategory = repository.findById(expectedId.getValue()).orElse(null);
        assertThat(existingCategory).isNotNull();

        final var command = UpdateCategoryCommand.with(
            expectedId.getValue(),
            expectedName,
            expectedDescription,
            expectedIsActive
        );

        doThrow(new IllegalStateException("Gateway error")).when(categoryGateway).update(any());

        // Act - When
        Notification notification = useCase.execute(command).getLeft();

        // Assert - Then
        assertThat(notification).isNotNull();
        assertThat(notification.hasErrors()).isTrue();
        assertThat(notification.getErrors()).hasSize(expectedErrorCount);
        assertThat(notification.fisrtError().message()).isEqualTo(expectedMessage);

        assertThat(repository.count()).isEqualTo(1);

        String id = categoryId.getValue();
        var savedCategory = repository.findById(id).orElse(null);
        assertThat(savedCategory).isNotNull();
        assertThat(savedCategory.getId()).isEqualTo(existingCategory.getId());

        assertThat(savedCategory.getName()).isNotEqualTo(expectedName);
        assertThat(savedCategory.getName()).isEqualTo(existingCategory.getName());

        assertThat(savedCategory.getDescription()).isNotEqualTo(expectedDescription);
        assertThat(savedCategory.getDescription()).isEqualTo(existingCategory.getDescription());

        assertThat(savedCategory.isActive()).isEqualTo(expectedIsActive);
        assertThat(savedCategory.isActive()).isEqualTo(existingCategory.isActive());

        assertThat(savedCategory.getCreatedAt()).isEqualTo(existingCategory.getCreatedAt());
        assertThat(savedCategory.getUpdatedAt()).isEqualTo(existingCategory.getUpdatedAt());
        assertThat(savedCategory.getDeletedAt()).isNull();

    }

    /**
     * Tests if, when an invalid category ID is provided for update, the use case throws a
     * {@link DomainException} indicating that the category was not found.
     * <p>
     * It also verifies that the gateway's update method is not invoked.
     */
    @Test
    public void givenAnInvalidId_whenCallsUpdateCategory_thenReturnDomainException() {
        // Arrange - Given
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var invalidId = CategoryID.unique();

        final var expectedErrorCount = 1;
        final var expectedMessage = "Category with ID %s was not found".formatted(invalidId.getValue());

        final var command = UpdateCategoryCommand.with(
            invalidId.getValue(),
            expectedName,
            expectedDescription,
            expectedIsActive
        );

        // Act - When
        final var actualException = catchException(() -> useCase.execute(command));

        // Assert - Then
        verify(categoryGateway, times(0)).update(any());
        assertThat(actualException).isNotNull();
        assertThat(actualException).isInstanceOf(DomainException.class);

        final DomainException actualDomainException = (DomainException) actualException;
        assertThat(actualDomainException).hasMessage(expectedMessage);
        assertThat(actualDomainException).hasNoCause();

        final List<br.com.josenaldo.codeflix.catalog.domain.validation.Error> errors = actualDomainException.getErrors();
        assertThat(errors).hasSize(expectedErrorCount);

        Error firstError = errors.getFirst();
        assertThat(firstError.message()).isEqualTo(expectedMessage);
    }
}
