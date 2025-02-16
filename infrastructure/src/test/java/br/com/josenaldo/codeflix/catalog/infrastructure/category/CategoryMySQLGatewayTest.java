package br.com.josenaldo.codeflix.catalog.infrastructure.category;

import br.com.josenaldo.codeflix.catalog.domain.category.Category;
import br.com.josenaldo.codeflix.catalog.domain.category.CategoryID;
import br.com.josenaldo.codeflix.catalog.domain.category.CategorySearchQuery;
import br.com.josenaldo.codeflix.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import br.com.josenaldo.codeflix.catalog.infrastructure.category.persistence.CategoryRepository;
import br.com.josenaldo.codeflix.catalog.infrastructure.testutils.MySQLGatewayTest;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Test class for the MySQL gateway implementation for categories.
 * <p>
 * This class verifies the proper functioning of the {@link CategoryMySQLGateway} and its
 * integration with the underlying {@link CategoryRepository}. It covers operations such as
 * creating, updating, deleting, and retrieving categories with various search criteria and
 * pagination.
 * <p>
 * All tests assume that the test profile is active and the database is cleaned between tests.
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

    /**
     * Tests that the Spring dependency injection successfully injects both the
     * {@link CategoryMySQLGateway} and the {@link CategoryRepository}.
     */
    @Test
    public void testInjectedDependencies() {
        Assertions.assertThat(categoryGateway).isNotNull();
        Assertions.assertThat(categoryRepository).isNotNull();
    }

    /**
     * Tests that a valid category is created successfully.
     * <p>
     * The test verifies that after creating a category, the repository count increases and the
     * returned category has the expected properties. It also confirms that the persisted entity
     * matches the domain object.
     */
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
        Assertions.assertThat(categoryRepository.count()).isEqualTo(0);

        // Act - When
        Category actualCategory = categoryGateway.create(category);

        // Assert - Then
        Assertions.assertThat(categoryRepository.count()).isEqualTo(1);
        Assertions.assertThat(actualCategory).isNotNull();
        Assertions.assertThat(actualCategory.getId()).isEqualTo(category.getId());
        Assertions.assertThat(actualCategory.getName()).isEqualTo(expectedName);
        Assertions.assertThat(actualCategory.getDescription()).isEqualTo(expectedDescription);
        Assertions.assertThat(actualCategory.isActive()).isEqualTo(expectedIsActive);
        Assertions.assertThat(actualCategory.getCreatedAt()).isEqualTo(category.getUpdatedAt());
        Assertions.assertThat(actualCategory.getUpdatedAt()).isEqualTo(category.getUpdatedAt());
        Assertions.assertThat(actualCategory.getDeletedAt()).isNull();

        String id = actualCategory.getId().getValue();
        final var createdCategoryEntity = categoryRepository.findById(id).orElse(null);
        Assertions.assertThat(createdCategoryEntity).isNotNull();
        Assertions.assertThat(createdCategoryEntity.getId()).isEqualTo(category.getId().getValue());
        Assertions.assertThat(createdCategoryEntity.getName()).isEqualTo(expectedName);
        Assertions.assertThat(createdCategoryEntity.getDescription())
                  .isEqualTo(expectedDescription);
        Assertions.assertThat(createdCategoryEntity.isActive()).isEqualTo(expectedIsActive);
        Assertions.assertThat(createdCategoryEntity.getCreatedAt())
                  .isEqualTo(category.getCreatedAt());
        Assertions.assertThat(createdCategoryEntity.getUpdatedAt())
                  .isEqualTo(category.getUpdatedAt());
        Assertions.assertThat(createdCategoryEntity.getDeletedAt()).isNull();
    }

    /**
     * Tests that a valid category is updated successfully.
     * <p>
     * The test ensures that after updating the category, its properties are updated accordingly,
     * and the persisted entity reflects the new values.
     */
    @Test
    public void givenAValidCategory_whenCallsUpdate_thenShouldReturnAUpdatedCategory() {
        // Arrange - Given
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var category = Category.newCategory("Film", null, expectedIsActive);
        Assertions.assertThat(categoryRepository.count()).isEqualTo(0);
        categoryRepository.saveAndFlush(CategoryJpaEntity.from(category));
        Assertions.assertThat(categoryRepository.count()).isEqualTo(1);

        final var currentInvalidCategory = categoryRepository.findById(category.getId().getValue())
                                                             .orElse(null);
        Assertions.assertThat(currentInvalidCategory).isNotNull();
        Assertions.assertThat(currentInvalidCategory.getName()).isEqualTo("Film");
        Assertions.assertThat(currentInvalidCategory.getDescription()).isNull();
        Assertions.assertThat(currentInvalidCategory.isActive()).isTrue();

        // Act - When
        final var updatedCategory = category.clone().update(
            expectedName,
            expectedDescription,
            expectedIsActive
        );
        Category actualCategory = categoryGateway.update(updatedCategory);

        // Assert - Then
        Assertions.assertThat(categoryRepository.count()).isEqualTo(1);
        Assertions.assertThat(actualCategory).isNotNull();
        Assertions.assertThat(actualCategory.getId()).isEqualTo(updatedCategory.getId());
        Assertions.assertThat(actualCategory.getName()).isEqualTo(expectedName);
        Assertions.assertThat(actualCategory.getDescription()).isEqualTo(expectedDescription);
        Assertions.assertThat(actualCategory.isActive()).isEqualTo(expectedIsActive);
        Assertions.assertThat(actualCategory.getCreatedAt()).isEqualTo(category.getCreatedAt());
        Assertions.assertThat(actualCategory.getUpdatedAt()).isAfter(category.getUpdatedAt());
        Assertions.assertThat(actualCategory.getDeletedAt()).isNull();

        String id = actualCategory.getId().getValue();
        final var updatedCategoryEntity = categoryRepository.findById(id).orElse(null);
        Assertions.assertThat(updatedCategoryEntity).isNotNull();
        Assertions.assertThat(updatedCategoryEntity.getId()).isEqualTo(category.getId().getValue());
        Assertions.assertThat(updatedCategoryEntity.getName()).isEqualTo(expectedName);
        Assertions.assertThat(updatedCategoryEntity.getDescription())
                  .isEqualTo(expectedDescription);
        Assertions.assertThat(updatedCategoryEntity.isActive()).isEqualTo(expectedIsActive);
        Assertions.assertThat(updatedCategoryEntity.getCreatedAt())
                  .isEqualTo(category.getUpdatedAt());
        Assertions.assertThat(updatedCategoryEntity.getUpdatedAt())
                  .isAfter(category.getUpdatedAt());
        Assertions.assertThat(updatedCategoryEntity.getDeletedAt()).isNull();
    }

    /**
     * Tests that a pre-persisted category is deleted successfully.
     * <p>
     * The test verifies that after deleting the category via the gateway, the repository no longer
     * contains the category.
     */
    @Test
    public void givenAPrePersistedCategory_whenTryToDelete_thenShouldDeleteCategory() {
        // Arrange - Given
        final var category = Category.newCategory("Filmes", "A categoria mais assistida", true);
        Assertions.assertThat(categoryRepository.count()).isEqualTo(0);
        categoryRepository.saveAndFlush(CategoryJpaEntity.from(category));
        Assertions.assertThat(categoryRepository.count()).isEqualTo(1);

        // Act - When
        categoryGateway.deleteById(category.getId());

        // Assert - Then
        Assertions.assertThat(categoryRepository.count()).isEqualTo(0);
        final var deletedCategoryEntity = categoryRepository.findById(category.getId().getValue())
                                                            .orElse(null);
        Assertions.assertThat(deletedCategoryEntity).isNull();
    }

    /**
     * Tests that attempting to delete a category with an invalid ID does not affect the
     * repository.
     * <p>
     * The test ensures that the deletion operation does not throw an error and that no entity
     * exists with the given invalid ID.
     */
    @Test
    public void givenAnInvalidCategoryId_whenTryToDelete_thenShouldDeleteCategory() {
        // Arrange - Given
        final var id = CategoryID.unique();

        // Act - When
        categoryGateway.deleteById(id);

        // Assert - Then
        final var deletedCategoryEntity = categoryRepository.findById(id.getValue()).orElse(null);
        Assertions.assertThat(deletedCategoryEntity).isNull();
    }

    /**
     * Tests that a pre-persisted category is successfully retrieved by its ID.
     * <p>
     * The test confirms that the category returned by the gateway matches the persisted entity in
     * the repository.
     */
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
        Assertions.assertThat(categoryRepository.count()).isEqualTo(0);
        categoryRepository.saveAndFlush(CategoryJpaEntity.from(category));
        Assertions.assertThat(categoryRepository.count()).isEqualTo(1);

        // Act - When
        Category actualCategory = categoryGateway.findById(category.getId()).orElse(null);

        // Assert - Then
        Assertions.assertThat(categoryRepository.count()).isEqualTo(1);
        Assertions.assertThat(actualCategory).isNotNull();
        Assertions.assertThat(actualCategory.getId()).isEqualTo(category.getId());
        Assertions.assertThat(actualCategory.getName()).isEqualTo(category.getName());
        Assertions.assertThat(actualCategory.getDescription()).isEqualTo(category.getDescription());
        Assertions.assertThat(actualCategory.isActive()).isEqualTo(category.isActive());
        Assertions.assertThat(actualCategory.getCreatedAt()).isEqualTo(category.getCreatedAt());
        Assertions.assertThat(actualCategory.getUpdatedAt()).isEqualTo(category.getUpdatedAt());
        Assertions.assertThat(actualCategory.getDeletedAt()).isNull();
    }

    /**
     * Tests that searching for a category by an ID that does not exist returns an empty result.
     * <p>
     * The test confirms that the gateway's findById method returns an empty Optional when no
     * category is found.
     */
    @Test
    public void givenNotStoredCategoryId_whenCallsFindById_thenShouldReturnEmpty() {
        // Arrange - Given
        final var id = CategoryID.unique();

        // Act - When
        final var actualCategory = categoryGateway.findById(id);

        // Assert - Then
        Assertions.assertThat(actualCategory.isEmpty()).isTrue();
    }

    /**
     * Tests that the gateway returns a paginated list of categories.
     * <p>
     * This test creates multiple categories, verifies the repository count, and checks that the
     * paginated result contains the expected data for the first page.
     */
    @Test
    public void givenAPrePersistedCategories_whenCallsFindAll_thenShouldReturnAPaginated() {
        // Arrange - Given
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var filmes = Category.newCategory("Filmes", null, true);
        final var series = Category.newCategory("Series", null, true);
        final var documentarios = Category.newCategory("Documentários", null, true);
        Assertions.assertThat(categoryRepository.count()).isEqualTo(0);
        categoryRepository.saveAllAndFlush(List.of(
            CategoryJpaEntity.from(filmes),
            CategoryJpaEntity.from(series),
            CategoryJpaEntity.from(documentarios)
        ));
        Assertions.assertThat(categoryRepository.count()).isEqualTo(3);

        CategorySearchQuery query = CategorySearchQuery.of(0, 1, null, "name", "ASC");

        // Act - When
        final var actualResult = categoryGateway.findAll(query);

        // Assert - Then
        Assertions.assertThat(actualResult).isNotNull();
        Assertions.assertThat(actualResult.page()).isEqualTo(expectedPage);
        Assertions.assertThat(actualResult.perPage()).isEqualTo(expectedPerPage);
        Assertions.assertThat(actualResult.total()).isEqualTo(expectedTotal);
        Assertions.assertThat(actualResult.data()).hasSize(expectedPerPage);
        Assertions.assertThat(actualResult.data()).isNotNull();
        Assertions.assertThat(actualResult.data()).isNotEmpty();
        Assertions.assertThat(actualResult.data()).extracting(Category::getName)
                  .containsExactly(documentarios.getName());
        Assertions.assertThat(actualResult.data()).extracting(Category::getId)
                  .containsExactly(documentarios.getId());
        Assertions.assertThat(actualResult.data()).extracting(Category::getDescription)
                  .containsExactly(documentarios.getDescription());
        Assertions.assertThat(actualResult.data()).extracting(Category::isActive)
                  .containsExactly(documentarios.isActive());
        Assertions.assertThat(actualResult.data()).extracting(Category::getCreatedAt)
                  .containsExactly(documentarios.getCreatedAt());
        Assertions.assertThat(actualResult.data()).extracting(Category::getUpdatedAt)
                  .containsExactly(documentarios.getUpdatedAt());
        Assertions.assertThat(actualResult.data()).extracting(Category::getDeletedAt)
                  .containsExactly(documentarios.getDeletedAt());
    }

    /**
     * Tests that when no categories are persisted, the gateway returns an empty paginated result.
     * <p>
     * This test confirms that the findAll method returns an empty Pagination object when the
     * repository is empty.
     */
    @Test
    public void givenEmptyCategoriesTable_whenCallsFindAll_thenShouldReturnEmptyPaginated() {
        // Arrange - Given
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 0;
        Assertions.assertThat(categoryRepository.count()).isEqualTo(0);

        CategorySearchQuery query = CategorySearchQuery.of(0, 1, null, "name", "ASC");

        // Act - When
        final var actualResult = categoryGateway.findAll(query);

        // Assert - Then
        Assertions.assertThat(actualResult).isNotNull();
        Assertions.assertThat(actualResult.page()).isEqualTo(expectedPage);
        Assertions.assertThat(actualResult.perPage()).isEqualTo(expectedPerPage);
        Assertions.assertThat(actualResult.total()).isEqualTo(expectedTotal);
        Assertions.assertThat(actualResult.data()).hasSize(expectedTotal);
        Assertions.assertThat(actualResult.data()).isEmpty();
    }

    /**
     * Tests that pagination works correctly by retrieving different pages.
     * <p>
     * This test first retrieves the first page and then retrieves another page to verify that
     * pagination correctly returns the expected category for each page.
     */
    @Test
    public void givenFollowPagination_whenCallsFindAll_thenShouldReturnAPaginated() {
        // Arrange - Given
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var filmes = Category.newCategory("Filmes", null, true);
        final var series = Category.newCategory("Series", null, true);
        final var documentarios = Category.newCategory("Documentários", null, true);
        Assertions.assertThat(categoryRepository.count()).isEqualTo(0);
        categoryRepository.saveAllAndFlush(List.of(
            CategoryJpaEntity.from(filmes),
            CategoryJpaEntity.from(series),
            CategoryJpaEntity.from(documentarios)
        ));
        Assertions.assertThat(categoryRepository.count()).isEqualTo(3);

        CategorySearchQuery query = CategorySearchQuery.of(0, 1, null, "name", "ASC");

        // Act - When (Page 0)
        var actualResult = categoryGateway.findAll(query);

        // Assert - Then (Page 0)
        Assertions.assertThat(actualResult).isNotNull();
        Assertions.assertThat(actualResult.page()).isEqualTo(expectedPage);
        Assertions.assertThat(actualResult.perPage()).isEqualTo(expectedPerPage);
        Assertions.assertThat(actualResult.total()).isEqualTo(expectedTotal);
        Assertions.assertThat(actualResult.data()).hasSize(expectedPerPage);
        Assertions.assertThat(actualResult.data()).isNotNull();
        Assertions.assertThat(actualResult.data()).isNotEmpty();
        Assertions.assertThat(actualResult.data()).extracting(Category::getName)
                  .containsExactly(documentarios.getName());
        Assertions.assertThat(actualResult.data()).extracting(Category::getId)
                  .containsExactly(documentarios.getId());
        Assertions.assertThat(actualResult.data()).extracting(Category::getDescription)
                  .containsExactly(documentarios.getDescription());
        Assertions.assertThat(actualResult.data()).extracting(Category::isActive)
                  .containsExactly(documentarios.isActive());
        Assertions.assertThat(actualResult.data()).extracting(Category::getCreatedAt)
                  .containsExactly(documentarios.getCreatedAt());
        Assertions.assertThat(actualResult.data()).extracting(Category::getUpdatedAt)
                  .containsExactly(documentarios.getUpdatedAt());
        Assertions.assertThat(actualResult.data()).extracting(Category::getDeletedAt)
                  .containsExactly(documentarios.getDeletedAt());

        // Arrange - Given new expected page for next test
        final var newExpectedPage = 2;
        query = CategorySearchQuery.of(2, 1, null, "name", "ASC");

        // Act - When (Page 2)
        actualResult = categoryGateway.findAll(query);

        // Assert - Then (Page 2)
        Assertions.assertThat(actualResult).isNotNull();
        Assertions.assertThat(actualResult.page()).isEqualTo(newExpectedPage);
        Assertions.assertThat(actualResult.perPage()).isEqualTo(expectedPerPage);
        Assertions.assertThat(actualResult.total()).isEqualTo(expectedTotal);
        Assertions.assertThat(actualResult.data()).hasSize(expectedPerPage);
        Assertions.assertThat(actualResult.data()).isNotNull();
        Assertions.assertThat(actualResult.data()).isNotEmpty();
        Assertions.assertThat(actualResult.data()).extracting(Category::getName)
                  .containsExactly(series.getName());
        Assertions.assertThat(actualResult.data()).extracting(Category::getId)
                  .containsExactly(series.getId());
        Assertions.assertThat(actualResult.data()).extracting(Category::getDescription)
                  .containsExactly(series.getDescription());
        Assertions.assertThat(actualResult.data()).extracting(Category::isActive)
                  .containsExactly(series.isActive());
        Assertions.assertThat(actualResult.data()).extracting(Category::getCreatedAt)
                  .containsExactly(series.getCreatedAt());
        Assertions.assertThat(actualResult.data()).extracting(Category::getUpdatedAt)
                  .containsExactly(series.getUpdatedAt());
        Assertions.assertThat(actualResult.data()).extracting(Category::getDeletedAt)
                  .containsExactly(series.getDeletedAt());
    }

    /**
     * Tests that when the search term matches a category name, the gateway returns a paginated
     * result.
     * <p>
     * The test verifies that using a search term ("doc") correctly returns the category whose name
     * matches the term.
     */
    @Test
    public void givenAPrePersistedCategoriesAndDocAsTerm_whenCallsFindAllAndTermsMatchesCategoryName_thenShouldReturnAPaginated() {
        // Arrange - Given
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var filmes = Category.newCategory("Filmes", null, true);
        final var series = Category.newCategory("Series", null, true);
        final var documentarios = Category.newCategory("Documentários", null, true);
        Assertions.assertThat(categoryRepository.count()).isEqualTo(0);
        categoryRepository.saveAllAndFlush(List.of(
            CategoryJpaEntity.from(filmes),
            CategoryJpaEntity.from(series),
            CategoryJpaEntity.from(documentarios)
        ));
        Assertions.assertThat(categoryRepository.count()).isEqualTo(3);

        CategorySearchQuery query = CategorySearchQuery.of(0, 1, "doc", "name", "ASC");

        // Act - When
        final var actualResult = categoryGateway.findAll(query);

        // Assert - Then
        Assertions.assertThat(actualResult).isNotNull();
        Assertions.assertThat(actualResult.page()).isEqualTo(expectedPage);
        Assertions.assertThat(actualResult.perPage()).isEqualTo(expectedPerPage);
        Assertions.assertThat(actualResult.total()).isEqualTo(expectedTotal);
        Assertions.assertThat(actualResult.data()).hasSize(expectedPerPage);
        Assertions.assertThat(actualResult.data()).isNotNull();
        Assertions.assertThat(actualResult.data()).isNotEmpty();
        Assertions.assertThat(actualResult.data()).extracting(Category::getName)
                  .containsExactly(documentarios.getName());
        Assertions.assertThat(actualResult.data()).extracting(Category::getId)
                  .containsExactly(documentarios.getId());
        Assertions.assertThat(actualResult.data()).extracting(Category::getDescription)
                  .containsExactly(documentarios.getDescription());
        Assertions.assertThat(actualResult.data()).extracting(Category::isActive)
                  .containsExactly(documentarios.isActive());
        Assertions.assertThat(actualResult.data()).extracting(Category::getCreatedAt)
                  .containsExactly(documentarios.getCreatedAt());
        Assertions.assertThat(actualResult.data()).extracting(Category::getUpdatedAt)
                  .containsExactly(documentarios.getUpdatedAt());
        Assertions.assertThat(actualResult.data()).extracting(Category::getDeletedAt)
                  .containsExactly(documentarios.getDeletedAt());
    }

    /**
     * Tests that when the search term matches a category description, the gateway returns a
     * paginated result.
     * <p>
     * The test verifies that using a search term ("MAIS ASSISTIDA") correctly returns the category
     * whose description matches the term.
     */
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
        Assertions.assertThat(categoryRepository.count()).isEqualTo(0);
        categoryRepository.saveAllAndFlush(List.of(
            CategoryJpaEntity.from(filmes),
            CategoryJpaEntity.from(series),
            CategoryJpaEntity.from(documentarios)
        ));
        Assertions.assertThat(categoryRepository.count()).isEqualTo(3);

        CategorySearchQuery query = CategorySearchQuery.of(0, 1, "MAIS ASSISTIDA", "name", "ASC");

        // Act - When
        final var actualResult = categoryGateway.findAll(query);

        // Assert - Then
        Assertions.assertThat(actualResult).isNotNull();
        Assertions.assertThat(actualResult.page()).isEqualTo(expectedPage);
        Assertions.assertThat(actualResult.perPage()).isEqualTo(expectedPerPage);
        Assertions.assertThat(actualResult.total()).isEqualTo(expectedTotal);
        Assertions.assertThat(actualResult.data()).hasSize(expectedPerPage);
        Assertions.assertThat(actualResult.data()).isNotNull();
        Assertions.assertThat(actualResult.data()).isNotEmpty();
        Assertions.assertThat(actualResult.data()).extracting(Category::getName)
                  .containsExactly(filmes.getName());
        Assertions.assertThat(actualResult.data()).extracting(Category::getId)
                  .containsExactly(filmes.getId());
        Assertions.assertThat(actualResult.data()).extracting(Category::getDescription)
                  .containsExactly(filmes.getDescription());
        Assertions.assertThat(actualResult.data()).extracting(Category::isActive)
                  .containsExactly(filmes.isActive());
        Assertions.assertThat(actualResult.data()).extracting(Category::getCreatedAt)
                  .containsExactly(filmes.getCreatedAt());
        Assertions.assertThat(actualResult.data()).extracting(Category::getUpdatedAt)
                  .containsExactly(filmes.getUpdatedAt());
        Assertions.assertThat(actualResult.data()).extracting(Category::getDeletedAt)
                  .containsExactly(filmes.getDeletedAt());
    }

    /**
     * Tests that the gateway returns a paginated result in descending order when requested.
     * <p>
     * The test verifies that when the sort order is set to DESC on the "name" field, the paginated
     * result is returned in the correct descending order.
     */
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
        Assertions.assertThat(categoryRepository.count()).isEqualTo(0);
        categoryRepository.saveAllAndFlush(List.of(
            CategoryJpaEntity.from(filmes),
            CategoryJpaEntity.from(series),
            CategoryJpaEntity.from(documentarios)
        ));
        Assertions.assertThat(categoryRepository.count()).isEqualTo(3);

        // Act - When
        CategorySearchQuery query = CategorySearchQuery.of(0, 3, null, "name", "DESC");
        var actualResult = categoryGateway.findAll(query);

        // Assert - Then
        Assertions.assertThat(actualResult).isNotNull();
        Assertions.assertThat(actualResult.page()).isEqualTo(expectedPage);
        Assertions.assertThat(actualResult.perPage()).isEqualTo(expectedPerPage);
        Assertions.assertThat(actualResult.total()).isEqualTo(expectedTotal);
        Assertions.assertThat(actualResult.data()).hasSize(expectedPerPage);
        Assertions.assertThat(actualResult.data()).isNotNull();
        Assertions.assertThat(actualResult.data()).isNotEmpty();
        Assertions.assertThat(actualResult.data()).extracting(Category::getId)
                  .containsExactly(
                      series.getId(),
                      filmes.getId(),
                      documentarios.getId()
                  );
    }
}
