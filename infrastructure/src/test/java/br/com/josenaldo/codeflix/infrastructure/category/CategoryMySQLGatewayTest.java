package br.com.josenaldo.codeflix.infrastructure.category;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.josenaldo.codeflix.domain.category.Category;
import br.com.josenaldo.codeflix.domain.category.CategoryID;
import br.com.josenaldo.codeflix.domain.category.CategorySearchQuery;
import br.com.josenaldo.codeflix.infrastructure.category.persistence.CategoryJpaEntity;
import br.com.josenaldo.codeflix.infrastructure.category.persistence.CategoryRepository;
import br.com.josenaldo.codeflix.infrastructure.testutils.MySQLGatewayTest;
import java.util.List;
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
    public void givenAValidCategory_whenCallsCreate_thenShouldReturnANewCategory() {

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
    public void givenAValidCategory_whenCallsUpdate_thenShouldReturnAUpdatedCategory() {
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
    public void givenAPrePersistedCategoryAndAValidCategoryId_whenCallsFindById_thenShouldReturnACategory() {
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
    public void givenNotStoredCategoryId_whenCallsFindById_thenShouldReturnEmpty() {
        // Arrange - Given
        final var id = CategoryID.unique();

        // Act - When
        final var actualCategory = categoryGateway.findById(id);

        // Assert - Then
        assertThat(actualCategory.isEmpty()).isTrue();
    }

    @Test
    public void givenAPrePersistedCategories_whenCallsFindAll_thenShouldReturnAPaginated() {
        // Arrange - Given
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var filmes = Category.newCategory("Filmes", null, true);
        final var series = Category.newCategory("Series", null, true);
        final var documentarios = Category.newCategory("Documentários", null, true);

        assertThat(categoryRepository.count()).isEqualTo(0);

        categoryRepository.saveAllAndFlush(List.of(
            CategoryJpaEntity.from(filmes),
            CategoryJpaEntity.from(series),
            CategoryJpaEntity.from(documentarios)
        ));
        assertThat(categoryRepository.count()).isEqualTo(3);

        CategorySearchQuery query = CategorySearchQuery.of(0, 1, null, "name", "ASC");

        // Act - When
        final var actualResult = categoryGateway.findAll(query);

        // Assert - Then
        assertThat(actualResult).isNotNull();
        assertThat(actualResult.page()).isEqualTo(expectedPage);
        assertThat(actualResult.perPage()).isEqualTo(expectedPerPage);
        assertThat(actualResult.total()).isEqualTo(expectedTotal);
        assertThat(actualResult.data()).hasSize(expectedPerPage);
        assertThat(actualResult.data()).isNotNull();
        assertThat(actualResult.data()).isNotEmpty();
        assertThat(actualResult.data()).extracting(Category::getName)
                                       .containsExactly(documentarios.getName());
        assertThat(actualResult.data()).extracting(Category::getId)
                                       .containsExactly(documentarios.getId());
        assertThat(actualResult.data()).extracting(Category::getDescription)
                                       .containsExactly(documentarios.getDescription());
        assertThat(actualResult.data()).extracting(Category::isActive)
                                       .containsExactly(documentarios.isActive());
        assertThat(actualResult.data()).extracting(Category::getCreatedAt)
                                       .containsExactly(documentarios.getCreatedAt());
        assertThat(actualResult.data()).extracting(Category::getUpdatedAt)
                                       .containsExactly(documentarios.getUpdatedAt());
        assertThat(actualResult.data()).extracting(Category::getDeletedAt)
                                       .containsExactly(documentarios.getDeletedAt());
    }

    @Test
    public void givenEmptyCategoriesTable_whenCallsFindAll_thenShouldReturnEmptyPaginated() {
        // Arrange - Given
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 0;

        assertThat(categoryRepository.count()).isEqualTo(0);

        CategorySearchQuery query = CategorySearchQuery.of(0, 1, null, "name", "ASC");

        // Act - When
        final var actualResult = categoryGateway.findAll(query);

        // Assert - Then
        assertThat(actualResult).isNotNull();
        assertThat(actualResult.page()).isEqualTo(expectedPage);
        assertThat(actualResult.perPage()).isEqualTo(expectedPerPage);
        assertThat(actualResult.total()).isEqualTo(expectedTotal);
        assertThat(actualResult.data()).hasSize(expectedTotal);
        assertThat(actualResult.data()).isEmpty();
    }

    @Test
    public void givenFollowPagination_whenCallsFindAll_thenShouldReturnAPaginated() {
        // Arrange - Given
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var filmes = Category.newCategory("Filmes", null, true);
        final var series = Category.newCategory("Series", null, true);
        final var documentarios = Category.newCategory("Documentários", null, true);

        assertThat(categoryRepository.count()).isEqualTo(0);

        categoryRepository.saveAllAndFlush(List.of(
            CategoryJpaEntity.from(filmes),
            CategoryJpaEntity.from(series),
            CategoryJpaEntity.from(documentarios)
        ));
        assertThat(categoryRepository.count()).isEqualTo(3);

        CategorySearchQuery query = CategorySearchQuery.of(0, 1, null, "name", "ASC");

        // Act - When
        var actualResult = categoryGateway.findAll(query);

        // Assert - Then
        assertThat(actualResult).isNotNull();
        assertThat(actualResult.page()).isEqualTo(expectedPage);
        assertThat(actualResult.perPage()).isEqualTo(expectedPerPage);
        assertThat(actualResult.total()).isEqualTo(expectedTotal);
        assertThat(actualResult.data()).hasSize(expectedPerPage);
        assertThat(actualResult.data()).isNotNull();
        assertThat(actualResult.data()).isNotEmpty();
        assertThat(actualResult.data()).extracting(Category::getName)
                                       .containsExactly(documentarios.getName());
        assertThat(actualResult.data()).extracting(Category::getId)
                                       .containsExactly(documentarios.getId());
        assertThat(actualResult.data()).extracting(Category::getDescription)
                                       .containsExactly(documentarios.getDescription());
        assertThat(actualResult.data()).extracting(Category::isActive)
                                       .containsExactly(documentarios.isActive());
        assertThat(actualResult.data()).extracting(Category::getCreatedAt)
                                       .containsExactly(documentarios.getCreatedAt());
        assertThat(actualResult.data()).extracting(Category::getUpdatedAt)
                                       .containsExactly(documentarios.getUpdatedAt());
        assertThat(actualResult.data()).extracting(Category::getDeletedAt)
                                       .containsExactly(documentarios.getDeletedAt());

        final var newExpectedPage = 2;
        query = CategorySearchQuery.of(2, 1, null, "name", "ASC");

        // Act - When
        actualResult = categoryGateway.findAll(query);

        // Assert - Then
        assertThat(actualResult).isNotNull();
        assertThat(actualResult.page()).isEqualTo(newExpectedPage);
        assertThat(actualResult.perPage()).isEqualTo(expectedPerPage);
        assertThat(actualResult.total()).isEqualTo(expectedTotal);
        assertThat(actualResult.data()).hasSize(expectedPerPage);
        assertThat(actualResult.data()).isNotNull();
        assertThat(actualResult.data()).isNotEmpty();
        assertThat(actualResult.data()).extracting(Category::getName)
                                       .containsExactly(series.getName());
        assertThat(actualResult.data()).extracting(Category::getId)
                                       .containsExactly(series.getId());
        assertThat(actualResult.data()).extracting(Category::getDescription)
                                       .containsExactly(series.getDescription());
        assertThat(actualResult.data()).extracting(Category::isActive)
                                       .containsExactly(series.isActive());
        assertThat(actualResult.data()).extracting(Category::getCreatedAt)
                                       .containsExactly(series.getCreatedAt());
        assertThat(actualResult.data()).extracting(Category::getUpdatedAt)
                                       .containsExactly(series.getUpdatedAt());
        assertThat(actualResult.data()).extracting(Category::getDeletedAt)
                                       .containsExactly(series.getDeletedAt());
    }

    @Test
    public void givenAPrePersistedCategoriesAndDocAsTerm_whenCallsFindAllAndTermsMatchesCategoryName_thenShouldReturnAPaginated() {
        // Arrange - Given
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var filmes = Category.newCategory("Filmes", null, true);
        final var series = Category.newCategory("Series", null, true);
        final var documentarios = Category.newCategory("Documentários", null, true);

        assertThat(categoryRepository.count()).isEqualTo(0);

        categoryRepository.saveAllAndFlush(List.of(
            CategoryJpaEntity.from(filmes),
            CategoryJpaEntity.from(series),
            CategoryJpaEntity.from(documentarios)
        ));
        assertThat(categoryRepository.count()).isEqualTo(3);

        CategorySearchQuery query = CategorySearchQuery.of(0, 1, "doc", "name", "ASC");

        // Act - When
        final var actualResult = categoryGateway.findAll(query);

        // Assert - Then
        assertThat(actualResult).isNotNull();
        assertThat(actualResult.page()).isEqualTo(expectedPage);
        assertThat(actualResult.perPage()).isEqualTo(expectedPerPage);
        assertThat(actualResult.total()).isEqualTo(expectedTotal);
        assertThat(actualResult.data()).hasSize(expectedPerPage);
        assertThat(actualResult.data()).isNotNull();
        assertThat(actualResult.data()).isNotEmpty();
        assertThat(actualResult.data()).extracting(Category::getName)
                                       .containsExactly(documentarios.getName());
        assertThat(actualResult.data()).extracting(Category::getId)
                                       .containsExactly(documentarios.getId());
        assertThat(actualResult.data()).extracting(Category::getDescription)
                                       .containsExactly(documentarios.getDescription());
        assertThat(actualResult.data()).extracting(Category::isActive)
                                       .containsExactly(documentarios.isActive());
        assertThat(actualResult.data()).extracting(Category::getCreatedAt)
                                       .containsExactly(documentarios.getCreatedAt());
        assertThat(actualResult.data()).extracting(Category::getUpdatedAt)
                                       .containsExactly(documentarios.getUpdatedAt());
        assertThat(actualResult.data()).extracting(Category::getDeletedAt)
                                       .containsExactly(documentarios.getDeletedAt());
    }

    @Test
    public void givenAPrePersistedCategoriesAndMaisAssistidaAsTerm_whenCallsFindAllAndTermsMatchesCategoryDescription_thenShouldReturnAPaginated() {
        // Arrange - Given
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var filmes = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final var series = Category.newCategory("Series", "Uma categoria assistida", true);
        final var documentarios = Category.newCategory(
            "Documentários",
            "A categoria menos assistida",
            true
        );

        assertThat(categoryRepository.count()).isEqualTo(0);

        categoryRepository.saveAllAndFlush(List.of(
            CategoryJpaEntity.from(filmes),
            CategoryJpaEntity.from(series),
            CategoryJpaEntity.from(documentarios)
        ));
        assertThat(categoryRepository.count()).isEqualTo(3);

        CategorySearchQuery query = CategorySearchQuery.of(0, 1, "MAIS ASSISTIDA", "name", "ASC");

        // Act - When
        final var actualResult = categoryGateway.findAll(query);

        // Assert - Then
        assertThat(actualResult).isNotNull();
        assertThat(actualResult.page()).isEqualTo(expectedPage);
        assertThat(actualResult.perPage()).isEqualTo(expectedPerPage);
        assertThat(actualResult.total()).isEqualTo(expectedTotal);
        assertThat(actualResult.data()).hasSize(expectedPerPage);
        assertThat(actualResult.data()).isNotNull();
        assertThat(actualResult.data()).isNotEmpty();
        assertThat(actualResult.data()).extracting(Category::getName)
                                       .containsExactly(filmes.getName());
        assertThat(actualResult.data()).extracting(Category::getId)
                                       .containsExactly(filmes.getId());
        assertThat(actualResult.data()).extracting(Category::getDescription)
                                       .containsExactly(filmes.getDescription());
        assertThat(actualResult.data()).extracting(Category::isActive)
                                       .containsExactly(filmes.isActive());
        assertThat(actualResult.data()).extracting(Category::getCreatedAt)
                                       .containsExactly(filmes.getCreatedAt());
        assertThat(actualResult.data()).extracting(Category::getUpdatedAt)
                                       .containsExactly(filmes.getUpdatedAt());
        assertThat(actualResult.data()).extracting(Category::getDeletedAt)
                                       .containsExactly(filmes.getDeletedAt());
    }

    @Test
    public void givenAPrePersistedCategoriesAndDescAsOrders_whenCallsFindAll_thenShouldReturnAPaginatedInDescOrder() {
        // Arrange - Given
        final var expectedPage = 0;
        final var expectedPerPage = 3;
        final var expectedTotal = 3;

        final var filmes = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final var series = Category.newCategory("Series", "Uma categoria assistida", true);
        final var documentarios = Category.newCategory(
            "Documentários",
            "A categoria menos assistida",
            true
        );

        assertThat(categoryRepository.count()).isEqualTo(0);
        categoryRepository.saveAllAndFlush(List.of(
            CategoryJpaEntity.from(filmes),
            CategoryJpaEntity.from(series),
            CategoryJpaEntity.from(documentarios)
        ));
        assertThat(categoryRepository.count()).isEqualTo(3);

        // Act - When
        CategorySearchQuery query = CategorySearchQuery.of(0, 3, null, "name", "DESC");

        // Assert - Then
        var actualResult = categoryGateway.findAll(query);
        assertThat(actualResult).isNotNull();
        assertThat(actualResult.page()).isEqualTo(expectedPage);
        assertThat(actualResult.perPage()).isEqualTo(expectedPerPage);
        assertThat(actualResult.total()).isEqualTo(expectedTotal);
        assertThat(actualResult.data()).hasSize(expectedPerPage);
        assertThat(actualResult.data()).isNotNull();
        assertThat(actualResult.data()).isNotEmpty();
        assertThat(actualResult.data()).extracting(Category::getId)
                                       .containsExactly(
                                           series.getId(),
                                           filmes.getId(),
                                           documentarios.getId()
                                       );

    }
}
