package br.com.josenaldo.codeflix.catalog.application.category.retrieve.get;

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
import br.com.josenaldo.codeflix.catalog.domain.exceptions.DomainException;
import br.com.josenaldo.codeflix.catalog.infrastructure.category.persistence.CategoryRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

/**
 * Integration test class for the GetCategoryByIdUseCase.
 * <p>
 * This class tests the behavior of the GetCategoryByIdUseCase, ensuring that a category can be
 * retrieved correctly by its ID when it exists, and that appropriate exceptions are thrown for
 * non-existent or invalid IDs.
 * <p>
 * It verifies that the category gateway and repository interact as expected, and that the domain
 * exceptions are thrown with the correct error messages.
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
@IntegrationTest
class GetCategoryByIdUseCaseIntegrationTest {

    /**
     * The use case for retrieving a category by its ID.
     */
    @Autowired
    private GetCategoryByIdUseCase useCase;

    /**
     * The repository used for accessing category data.
     */
    @Autowired
    private CategoryRepository repository;

    /**
     * The category gateway, spied to verify its interactions.
     */
    @MockitoSpyBean
    private CategoryGateway categoryGateway;

    /**
     * The id of the category used in the tests.
     */
    private CategoryID categoryId;

    /**
     * The category used in the tests.
     */
    private Category category;

    /**
     * Resets the category gateway spy before each test.
     * <p>
     * This ensures that previous interactions do not affect subsequent tests. It also resets the
     * repository to a clean state before each test and creates a new category to be used in the
     * tests.
     */
    @BeforeEach
    public void setup() {
        category = Category.newCategory("Filmes", "A categoria mais assistida", true);
        CategoryTestUtils.save(repository, category);
        assertThat(repository.count()).isEqualTo(1);

        categoryId = category.getId();
    }

    /**
     * Deletes all categories from the repository after each test.
     * <p>
     * This ensures that the repository is clean before each test. It also resets the category
     * gateway spy after each test.
     */
    @AfterEach
    public void tearDown() {
        repository.deleteAll();
        reset(categoryGateway);
    }

    /**
     * Tests that when an existing category ID is provided, the use case returns the corresponding
     * category.
     * <p>
     * The test verifies that the retrieved category output matches the expected properties (ID,
     * name, description, active status, and timestamps) and that the persisted entity in the
     * repository reflects these values.
     */
    @Test
    void givenExistentCategoryId_whenFindCategoryById_thenShouldReturnCategory() {
        // Arrange - Given
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var expectedId = categoryId;
        assertThat(repository.count()).isEqualTo(1);

        // Act - When
        final CategoryOutput actualCategory = useCase.execute(expectedId.getValue());

        // Assert - Then
        assertThat(actualCategory).isNotNull();
        assertThat(actualCategory.id()).isEqualTo(expectedId);

        Instant actualCreatedAt = actualCategory.createdAt().truncatedTo(ChronoUnit.SECONDS);
        Instant expectedCreatedAt = category.getCreatedAt().truncatedTo(ChronoUnit.SECONDS);
        assertThat(actualCreatedAt).isEqualTo(expectedCreatedAt);

        Instant actualUpdatedAt = actualCategory.updatedAt().truncatedTo(ChronoUnit.SECONDS);
        Instant expectedUpdatedAt = category.getUpdatedAt().truncatedTo(ChronoUnit.SECONDS);
        assertThat(actualUpdatedAt).isEqualTo(expectedUpdatedAt);

        assertThat(actualCategory.deletedAt()).isNull();
        assertThat(actualCategory.name()).isEqualTo(expectedName);
        assertThat(actualCategory.description()).isEqualTo(expectedDescription);
        assertThat(actualCategory.isActive()).isEqualTo(expectedIsActive);

        final var savedCategory = repository.findById(expectedId.getValue()).orElse(null);
        assertThat(savedCategory).isNotNull();
        assertThat(savedCategory.getId()).isEqualTo(expectedId.getValue());
        assertThat(savedCategory.getName()).isEqualTo(expectedName);
        assertThat(savedCategory.getDescription()).isEqualTo(expectedDescription);
        assertThat(savedCategory.isActive()).isEqualTo(expectedIsActive);

        Instant savedCreatedAt = savedCategory.getCreatedAt().truncatedTo(ChronoUnit.SECONDS);
        assertThat(savedCreatedAt).isEqualTo(expectedCreatedAt);

        Instant savedUpdatedAt = savedCategory.getUpdatedAt().truncatedTo(ChronoUnit.SECONDS);
        assertThat(savedUpdatedAt).isEqualTo(expectedUpdatedAt);

        assertThat(savedCategory.getDeletedAt()).isNull();
    }

    /**
     * Tests that when a non-existent category ID is provided, the use case throws a
     * DomainException.
     * <p>
     * The test verifies that the error message matches the expected format and that the exception
     * is of type DomainException.
     */
    @Test
    void givenNonExistentCategoryId_whenGetCategoryById_thenShouldThrowNotFoundException() {
        // Arrange - Given
        final var expectedId = CategoryID.unique();
        final var expectedErrorMessage = "Category with ID %s was not found".formatted(expectedId.getValue());

        // Act - When
        final var actualException = catchException(() -> useCase.execute(expectedId.getValue()));

        // Assert - Then
        assertThat(actualException)
            .isNotNull()
            .isInstanceOf(DomainException.class)
            .hasMessage(expectedErrorMessage);
    }

    /**
     * Tests that when an empty category ID is provided, the use case throws a DomainException.
     * <p>
     * The test ensures that no gateway methods are invoked and that the exception message indicates
     * the category was not found.
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
            .isInstanceOf(DomainException.class)
            .hasMessage(expectedErrorMessage);

        verify(categoryGateway, times(0)).findById(any());
    }

    /**
     * Tests that when a blank category ID is provided, the use case throws a DomainException.
     * <p>
     * The test verifies that the error message is as expected and that the category gateway is not
     * called.
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
            .isInstanceOf(DomainException.class)
            .hasMessage(expectedErrorMessage);

        verify(categoryGateway, times(0)).findById(any());
    }

    /**
     * Tests that when an invalid category ID is provided, the use case throws a DomainException.
     * <p>
     * The test verifies that the exception message matches the expected message for an invalid ID
     * and that the gateway is not invoked.
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
            .isInstanceOf(DomainException.class)
            .hasMessage(expectedErrorMessage);
        verify(categoryGateway, times(0)).findById(any());
    }

    /**
     * Tests that when a null category ID is provided, the use case throws a DomainException.
     * <p>
     * The test verifies that the exception message is correctly formatted and that no gateway
     * method is invoked.
     */
    @Test
    void givenNullCategoryId_whenGetCategoryById_thenShouldThrowDomainException() {
        // Arrange - Given
        final String expectedId = null;
        final var expectedErrorMessage = "Category with ID %s was not found".formatted(expectedId);

        // Act - When
        final var actualException = catchException(() -> useCase.execute(expectedId));

        // Assert - Then
        assertThat(actualException)
            .isNotNull()
            .isInstanceOf(DomainException.class)
            .hasMessage(expectedErrorMessage);

        verify(categoryGateway, times(0)).findById(any());
    }

    /**
     * Tests that when the category gateway throws an exception, the use case propagates the
     * exception.
     * <p>
     * The test simulates a gateway failure by making the gateway throw an IllegalStateException and
     * verifies that the use case throws the same exception with the correct message.
     */
    @Test
    void givenAnyCategoryId_whenCategoryGatewayThrowsAnException_thenReturnException() {
        // Arrange - Given
        final var expectedId = CategoryID.unique();
        final var expectedErrorMessage = "Gateway error";

        doThrow(new IllegalStateException(expectedErrorMessage))
            .when(categoryGateway).findById(expectedId);

        // Act - When
        final var actualException = catchException(() -> useCase.execute(expectedId.getValue()));

        // Assert - Then
        assertThat(actualException)
            .isNotNull()
            .isInstanceOf(IllegalStateException.class);
        assertThat(actualException.getMessage()).isEqualTo(expectedErrorMessage);
    }
}
