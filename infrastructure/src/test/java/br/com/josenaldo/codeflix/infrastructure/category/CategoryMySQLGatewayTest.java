package br.com.josenaldo.codeflix.infrastructure.category;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.josenaldo.codeflix.domain.category.Category;
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
        assertThat(createdCategoryEntity.getCreatedAt()).isEqualTo(category.getUpdatedAt());
        assertThat(createdCategoryEntity.getUpdatedAt()).isEqualTo(category.getUpdatedAt());
        assertThat(createdCategoryEntity.getDeletedAt()).isNull();
    }
}
