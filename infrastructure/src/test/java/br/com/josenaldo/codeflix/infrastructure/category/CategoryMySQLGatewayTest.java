package br.com.josenaldo.codeflix.infrastructure.category;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.josenaldo.codeflix.domain.category.Category;
import br.com.josenaldo.codeflix.domain.category.CategoryID;
import br.com.josenaldo.codeflix.infrastructure.category.persistence.CategoryJpaEntity;
import br.com.josenaldo.codeflix.infrastructure.category.persistence.CategoryRepository;
import br.com.josenaldo.codeflix.infrastructure.testutils.MySQLGatewayTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Test class for verifying dependency injection of MySQL gateway components.
 * <p>
 * This class uses the {@link MySQLGatewayTest} annotation to configure the test environment for
 * MySQL gateway tests. It checks that both {@link CategoryMySQLGateway} and
 * {@link CategoryRepository} are correctly injected by Spring.
 * <p>
 * Proper injection of these components is essential for the correct functioning of the persistence
 * layer.
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
@MySQLGatewayTest
class CategoryMySQLGatewayTest {

    /**
     * The repository used for accessing category data from the database.
     */
    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * The MySQL gateway implementation for category operations.
     */
    @Autowired
    private CategoryMySQLGateway categoryGateway;

    @Test
    public void givenAValidCategory_whenCallsCreate_shouldReturnANewCategory() {

        // Arrange - Given
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var category = Category.newCategory(
            expectedName,
            expectedDescription,
            expectedIsActive
        );

        assertThat(categoryRepository.count()).isEqualTo(0);

        // Act - When
        Category actualCategory = categoryGateway.create(category);

        // Assert - Then
        assertThat(categoryRepository.count()).isEqualTo(1);

        assertThat(actualCategory).isNotNull();
        assertThat(actualCategory.getId()).isEqualTo(category.getId());
        assertThat(actualCategory.getName()).isEqualTo(expectedName);
        assertThat(actualCategory.getDescription()).isEqualTo(expectedDescription);
        assertThat(actualCategory.isActive()).isEqualTo(expectedIsActive);
        assertThat(actualCategory.getCreatedAt()).isEqualTo(category.getUpdatedAt());
        assertThat(actualCategory.getUpdatedAt()).isEqualTo(category.getUpdatedAt());
        assertThat(actualCategory.getDeletedAt()).isNull();

        String id = actualCategory.getId().getValue();
        final var createdCategoryEntity = categoryRepository.findById(id).orElse(null);

        assertThat(createdCategoryEntity).isNotNull();
        assertThat(createdCategoryEntity.getId()).isEqualTo(category.getId().getValue());
        assertThat(createdCategoryEntity.getName()).isEqualTo(expectedName);
        assertThat(createdCategoryEntity.getDescription()).isEqualTo(expectedDescription);
        assertThat(createdCategoryEntity.isActive()).isEqualTo(expectedIsActive);
        assertThat(createdCategoryEntity.getCreatedAt()).isEqualTo(category.getCreatedAt());
        assertThat(createdCategoryEntity.getUpdatedAt()).isEqualTo(category.getUpdatedAt());
        assertThat(createdCategoryEntity.getDeletedAt()).isNull();
    }

    @Test
    public void givenAValidCategory_whenCallsUpdate_shouldReturnAUpdatedCategory() {
        // Arrange - Given
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var category = Category.newCategory("Film", null, expectedIsActive);

        assertThat(categoryRepository.count()).isEqualTo(0);
        categoryRepository.saveAndFlush(CategoryJpaEntity.from(category));
        assertThat(categoryRepository.count()).isEqualTo(1);

        final var currentInvalidCategory = categoryRepository.findById(category.getId().getValue())
                                                             .orElse(null);
        assertThat(currentInvalidCategory).isNotNull();
        assertThat(currentInvalidCategory.getName()).isEqualTo("Film");
        assertThat(currentInvalidCategory.getDescription()).isNull();
        assertThat(currentInvalidCategory.isActive()).isTrue();

        // Act - When
        final var updatedCategory = category.clone()
                                            .update(
                                                expectedName,
                                                expectedDescription,
                                                expectedIsActive
                                            );
        Category actualCategory = categoryGateway.update(updatedCategory);

        // Assert - Then
        assertThat(categoryRepository.count()).isEqualTo(1);

        assertThat(actualCategory).isNotNull();
        assertThat(actualCategory.getId()).isEqualTo(updatedCategory.getId());
        assertThat(actualCategory.getName()).isEqualTo(expectedName);
        assertThat(actualCategory.getDescription()).isEqualTo(expectedDescription);
        assertThat(actualCategory.isActive()).isEqualTo(expectedIsActive);
        assertThat(actualCategory.getCreatedAt()).isEqualTo(category.getCreatedAt());
        assertThat(actualCategory.getUpdatedAt()).isAfter(category.getUpdatedAt());
        assertThat(actualCategory.getDeletedAt()).isNull();

        String id = actualCategory.getId().getValue();
        final var updatedCategoryEntity = categoryRepository.findById(id).orElse(null);

        assertThat(updatedCategoryEntity).isNotNull();
        assertThat(updatedCategoryEntity.getId()).isEqualTo(category.getId().getValue());
        assertThat(updatedCategoryEntity.getName()).isEqualTo(expectedName);
        assertThat(updatedCategoryEntity.getDescription()).isEqualTo(expectedDescription);
        assertThat(updatedCategoryEntity.isActive()).isEqualTo(expectedIsActive);
        assertThat(updatedCategoryEntity.getCreatedAt()).isEqualTo(category.getUpdatedAt());
        assertThat(updatedCategoryEntity.getUpdatedAt()).isAfter(category.getUpdatedAt());
        assertThat(updatedCategoryEntity.getDeletedAt()).isNull();
    }

    @Test
    public void givenAPrePersistedCategory_whenTryToDelete_thenShouldDeleteCategory() {
        // Arrange - Given
        final var category = Category.newCategory("Filmes", "A categoria mais assistida", true);

        assertThat(categoryRepository.count()).isEqualTo(0);
        categoryRepository.saveAndFlush(CategoryJpaEntity.from(category));
        assertThat(categoryRepository.count()).isEqualTo(1);

        // Act - When
        categoryGateway.deleteById(category.getId());

        // Assert - Then
        assertThat(categoryRepository.count()).isEqualTo(0);

        final var deletedCategoryEntity = categoryRepository.findById(category.getId().getValue())
                                                            .orElse(null);

        assertThat(deletedCategoryEntity).isNull();

    }

    @Test
    public void givenAnInvalidCategoryId_whenTryToDelete_thenShouldDeleteCategory() {
        // Arrange - Given
        final var id = CategoryID.unique();

        // Act - When
        categoryGateway.deleteById(id);

        // Assert - Then
        final var deletedCategoryEntity = categoryRepository.findById(id.getValue()).orElse(null);
        assertThat(deletedCategoryEntity).isNull();
    }

    @Test
    public void givenAPrePersistedCategoryAndAValidCategoryId_whenCallsFindById_shouldReturnACategory() {
        // Arrange - Given
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var category = Category.newCategory(
            expectedName,
            expectedDescription,
            expectedIsActive
        );

        assertThat(categoryRepository.count()).isEqualTo(0);
        categoryRepository.saveAndFlush(CategoryJpaEntity.from(category));
        assertThat(categoryRepository.count()).isEqualTo(1);

        // Act - When
        Category actualCategory = categoryGateway.findById(category.getId()).orElse(null);

        // Assert - Then
        assertThat(categoryRepository.count()).isEqualTo(1);

        assertThat(actualCategory).isNotNull();
        assertThat(actualCategory.getId()).isEqualTo(category.getId());
        assertThat(actualCategory.getName()).isEqualTo(category.getName());
        assertThat(actualCategory.getDescription()).isEqualTo(category.getDescription());
        assertThat(actualCategory.isActive()).isEqualTo(category.isActive());
        assertThat(actualCategory.getCreatedAt()).isEqualTo(category.getCreatedAt());
        assertThat(actualCategory.getUpdatedAt()).isEqualTo(category.getUpdatedAt());
        assertThat(actualCategory.getDeletedAt()).isNull();
    }

    @Test
    public void givenNotStoredCategoryId_whenCallsFindById_shouldReturnEmpty() {
        // Arrange - Given
        final var id = CategoryID.unique();

        // Act - When
        final var actualCategory = categoryGateway.findById(id);

        // Assert - Then
        assertThat(actualCategory.isEmpty()).isTrue();
    }
}
