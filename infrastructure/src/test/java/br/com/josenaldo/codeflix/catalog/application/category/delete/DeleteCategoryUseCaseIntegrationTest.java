package br.com.josenaldo.codeflix.catalog.application.category.delete;

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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

/**
 * Integration test class for the DeleteCategoryUseCase.
 * <p>
 * This class tests the behavior of the DeleteCategoryUseCase for deleting categories through
 * various scenarios, such as deleting with a valid id, a non-existent id, and handling errors
 * during deletion.
 * <p>
 * The tests validate that the use case correctly interacts with the CategoryGateway and that the
 * underlying repository is updated as expected.
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
@IntegrationTest
class DeleteCategoryUseCaseIntegrationTest {

    /**
     * The use case for deleting a category.
     */
    @Autowired
    private DeleteCategoryUseCase useCase;

    /**
     * The repository used for persisting category data.
     */
    @Autowired
    private CategoryRepository repository;

    /**
     * The category gateway spy, used to verify interactions during tests.
     */
    @MockitoSpyBean
    private CategoryGateway categoryGateway;

    /**
     * The id of the category used in the tests.
     */
    private CategoryID categoryId;

    /**
     * Resets the category gateway spy before each test.
     * <p>
     * This ensures that previous interactions do not affect subsequent tests. It also resets the
     * repository to a clean state before each test and creates a new category to be used in the
     * tests.
     */
    @BeforeEach
    public void setup() {
        final var category = Category.newCategory("Movie", null, true);
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
     * Tests that when a valid category id is provided, the category is deleted successfully.
     * <p>
     * The test saves a valid category, verifies that the repository contains one record, then
     * deletes the category using its id. It confirms that no exception is thrown and that the
     * repository count becomes zero.
     */
    @Test
    void givenAValidId_whenCallDelete_thenShouldBeOk() {
        // Arrange - Given
        final var expectedId = categoryId;

        // Act - When
        final var actualException = catchException(() -> useCase.execute(expectedId.getValue()));

        // Assert - Then
        assertThat(actualException).isNull();
        assertThat(repository.count()).isZero();
    }

    /**
     * Tests that when a non-existent category id is provided, no deletion occurs.
     * <p>
     * The test saves a valid category and then attempts to delete a category using an id that does
     * not exist. It verifies that no exception is thrown and that the repository count remains
     * unchanged.
     */
    @Test
    void givenANoExistentId_whenCallDelete_thenShouldBeOk() {
        // Arrange - Given
        final var expectedId = CategoryID.unique();

        // Act - When
        final var actualException = catchException(() -> useCase.execute(expectedId.getValue()));

        // Assert - Then
        assertThat(actualException).isNull();
        assertThat(repository.count()).isEqualTo(1);
    }

    /**
     * Tests that when a valid category id is provided but an error occurs during deletion, the use
     * case throws an exception.
     * <p>
     * The test saves a valid category, then configures the category gateway to throw an
     * IllegalStateException when attempting to delete the category. It verifies that the exception
     * is thrown with the expected message, and that the repository remains unchanged.
     */
    @Test
    void givenAValidId_whenCallDeleteByIdAndAnErrorOccur_thenShouldReturnAnException() {
        // Arrange - Given
        final var expectedId = categoryId;

        doThrow(new IllegalStateException("Gateway error"))
            .when(categoryGateway).deleteById(expectedId);

        // Act - When
        final var actualException = catchException(() -> useCase.execute(expectedId.getValue()));

        // Assert - Then
        assertThat(actualException)
            .isNotNull()
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Gateway error");

        verify(categoryGateway, times(1)).deleteById(expectedId);
        assertThat(repository.count()).isEqualTo(1);
    }

    /**
     * Tests that when an invalid category id is provided, the use case throws a DomainException.
     * <p>
     * The test verifies that using an invalid id format results in a DomainException with the
     * expected error message, and that the category gateway's deleteById method is not invoked.
     */
    @Test
    void givenAnInvalidId_whenCallDelete_thenShouldReturnAnException() {
        // Arrange - Given
        final var invalidId = "invalid-id";

        // Act - When
        final var actualException = catchException(() -> useCase.execute(invalidId));

        // Assert - Then
        assertThat(actualException)
            .isNotNull()
            .isInstanceOf(DomainException.class)
            .hasMessage("the Id invalid-id is invalid");

        verify(categoryGateway, times(0)).deleteById(any());
    }
}
