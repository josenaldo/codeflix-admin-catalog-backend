package br.com.josenaldo.codeflix.catalog.infrastructure.category;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.josenaldo.codeflix.catalog.annotations.MySQLGatewayTest;
import br.com.josenaldo.codeflix.catalog.domain.category.Category;
import br.com.josenaldo.codeflix.catalog.domain.category.CategoryID;
import br.com.josenaldo.codeflix.catalog.domain.category.CategorySearchQuery;
import br.com.josenaldo.codeflix.catalog.domain.pagination.Pagination;
import br.com.josenaldo.codeflix.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import br.com.josenaldo.codeflix.catalog.infrastructure.category.persistence.CategoryRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

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
@Import(CategoryMySQLGateway.class)
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
    void testInjectedDependencies() {
        assertThat(categoryGateway).isNotNull();
        assertThat(categoryRepository).isNotNull();
    }

    /**
     * Tests that a valid category is created successfully.
     * <p>
     * The test verifies that after creating a category, the repository count increases and the
     * returned category has the expected properties. It also confirms that the persisted entity
     * matches the domain object.
     */
    @Test
    void givenAValidCategory_whenCallsCreate_thenShouldReturnANewCategory() {
        // Arrange - Given
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var category = Category.newCategory(
            expectedName,
            expectedDescription,
            expectedIsActive
        );
        assertThat(categoryRepository.count()).isZero();

        // Act - When
        Category actualCategory = categoryGateway.create(category);

        // Assert - Then
        assertThat(categoryRepository.count()).isEqualTo(1);
        assertThat(actualCategory).isNotNull();
        assertThat(actualCategory.getId()).isEqualTo(category.getId());
        assertThat(actualCategory.getCreatedAt()).isEqualTo(category.getUpdatedAt());
        assertThat(actualCategory.getUpdatedAt()).isEqualTo(category.getUpdatedAt());
        assertThat(actualCategory.getDeletedAt()).isNull();
        assertThat(actualCategory.getName()).isEqualTo(expectedName);
        assertThat(actualCategory.getDescription()).isEqualTo(expectedDescription);
        assertThat(actualCategory.isActive()).isEqualTo(expectedIsActive);

        String id = actualCategory.getId().getValue();
        final var createdCategoryEntity = categoryRepository.findById(id).orElse(null);
        assertThat(createdCategoryEntity).isNotNull();
        assertThat(createdCategoryEntity.getId()).isEqualTo(category.getId().getValue());
        assertThat(createdCategoryEntity.getCreatedAt())
            .isEqualTo(category.getCreatedAt());
        assertThat(createdCategoryEntity.getUpdatedAt())
            .isEqualTo(category.getUpdatedAt());
        assertThat(createdCategoryEntity.getDeletedAt()).isNull();
        assertThat(createdCategoryEntity.getName()).isEqualTo(expectedName);
        assertThat(createdCategoryEntity.getDescription())
            .isEqualTo(expectedDescription);
        assertThat(createdCategoryEntity.isActive()).isEqualTo(expectedIsActive);

    }

    /**
     * Tests that a valid category is updated successfully.
     * <p>
     * The test ensures that after updating the category, its properties are updated accordingly,
     * and the persisted entity reflects the new values.
     */
    @Test
    void givenAValidCategory_whenCallsUpdate_thenShouldReturnAUpdatedCategory() {
        // Arrange - Given
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var category = Category.newCategory("Film", null, expectedIsActive);

        assertThat(categoryRepository.count()).isZero();

        categoryRepository.saveAndFlush(CategoryJpaEntity.from(category));
        assertThat(categoryRepository.count()).isEqualTo(1);

        final var currentInvalidCategory = categoryRepository.findById(category.getId().getValue())
                                                             .orElse(null);
        assertThat(currentInvalidCategory).isNotNull();
        assertThat(currentInvalidCategory.getName()).isEqualTo("Film");
        assertThat(currentInvalidCategory.getDescription()).isNull();
        assertThat(currentInvalidCategory.isActive()).isTrue();

        // Act - When
        final var updatedCategory = category.clone().update(
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
        assertThat(updatedCategoryEntity.getDescription())
            .isEqualTo(expectedDescription);
        assertThat(updatedCategoryEntity.isActive()).isEqualTo(expectedIsActive);
        assertThat(updatedCategoryEntity.getCreatedAt())
            .isEqualTo(category.getUpdatedAt());
        assertThat(updatedCategoryEntity.getUpdatedAt())
            .isAfter(category.getUpdatedAt());
        assertThat(updatedCategoryEntity.getDeletedAt()).isNull();
    }

    /**
     * Tests that a pre-persisted category is deleted successfully.
     * <p>
     * The test verifies that after deleting the category via the gateway, the repository no longer
     * contains the category.
     */
    @Test
    void givenAPrePersistedCategory_whenTryToDelete_thenShouldDeleteCategory() {
        // Arrange - Given
        final var category = Category.newCategory("Filmes", "A categoria mais assistida", true);
        assertThat(categoryRepository.count()).isZero();
        categoryRepository.saveAndFlush(CategoryJpaEntity.from(category));
        assertThat(categoryRepository.count()).isEqualTo(1);

        // Act - When
        categoryGateway.deleteById(category.getId());

        // Assert - Then
        assertThat(categoryRepository.count()).isZero();
        final var deletedCategoryEntity = categoryRepository.findById(category.getId().getValue())
                                                            .orElse(null);
        assertThat(deletedCategoryEntity).isNull();
    }

    /**
     * Tests that attempting to delete a category with an invalid ID does not affect the
     * repository.
     * <p>
     * The test ensures that the deletion operation does not throw an error and that no entity
     * exists with the given invalid ID.
     */
    @Test
    void givenAnInvalidCategoryId_whenTryToDelete_thenShouldDeleteCategory() {
        // Arrange - Given
        final var id = CategoryID.unique();

        // Act - When
        categoryGateway.deleteById(id);

        // Assert - Then
        final var deletedCategoryEntity = categoryRepository.findById(id.getValue()).orElse(null);
        assertThat(deletedCategoryEntity).isNull();
    }

    /**
     * Tests that a pre-persisted category is successfully retrieved by its ID.
     * <p>
     * The test confirms that the category returned by the gateway matches the persisted entity in
     * the repository.
     */
    @Test
    void givenAPrePersistedCategoryAndAValidCategoryId_whenCallsFindById_thenShouldReturnACategory() {
        // Arrange - Given
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var category = Category.newCategory(
            expectedName,
            expectedDescription,
            expectedIsActive
        );
        assertThat(categoryRepository.count()).isZero();
        categoryRepository.saveAndFlush(CategoryJpaEntity.from(category));
        assertThat(categoryRepository.count()).isOne();

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

    /**
     * Tests that searching for a category by an ID that does not exist returns an empty result.
     * <p>
     * The test confirms that the gateway's findById method returns an empty Optional when no
     * category is found.
     */
    @Test
    void givenNotStoredCategoryId_whenCallsFindById_thenShouldReturnEmpty() {
        // Arrange - Given
        final var id = CategoryID.unique();

        // Act - When
        final var actualCategory = categoryGateway.findById(id);

        // Assert - Then
        assertThat(actualCategory).isEmpty();
    }

    /**
     * Tests that the gateway returns a paginated list of categories.
     * <p>
     * This test creates multiple categories, verifies the repository count, and checks that the
     * paginated result contains the expected data for the first page.
     */
    @Test
    void givenAPrePersistedCategories_whenCallsFindAll_thenShouldReturnAPaginated() {
        // Arrange - Given
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var filmes = Category.newCategory("Filmes", null, true);
        final var series = Category.newCategory("Series", null, true);
        final var documentarios = Category.newCategory("Documentários", null, true);
        assertThat(categoryRepository.count()).isZero();
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

    /**
     * Tests that when no categories are persisted, the gateway returns an empty paginated result.
     * <p>
     * This test confirms that the findAll method returns an empty Pagination object when the
     * repository is empty.
     */
    @Test
    void givenEmptyCategoriesTable_whenCallsFindAll_thenShouldReturnEmptyPaginated() {
        // Arrange - Given
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 0;
        assertThat(categoryRepository.count()).isZero();

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

    /**
     * Tests that pagination works correctly by retrieving different pages.
     * <p>
     * This test first retrieves the first page and then retrieves another page to verify that
     * pagination correctly returns the expected category for each page.
     */
    @Test
    void givenFollowPagination_whenCallsFindAll_thenShouldReturnAPaginated() {
        // Arrange - Given
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3L;

        final var filmes = Category.newCategory("Filmes", null, true);
        final var series = Category.newCategory("Series", null, true);
        final var documentarios = Category.newCategory("Documentários", null, true);

        assertThat(categoryRepository.count()).isZero();

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
        assertThat(actualResult)
            .isNotNull()
            .extracting(Pagination::page, Pagination::perPage, Pagination::total)
            .containsExactly(expectedPage, expectedPerPage, expectedTotal);

        List<Category> data = actualResult.data();
        assertThat(data)
            .isNotNull()
            .isNotEmpty()
            .hasSize(expectedPerPage)
            .extracting(Category::getName)
            .containsExactly(documentarios.getName());

        assertThat(data).extracting(Category::getId)
                        .containsExactly(documentarios.getId());
        assertThat(data).extracting(Category::getDescription)
                        .containsExactly(documentarios.getDescription());
        assertThat(data).extracting(Category::isActive)
                        .containsExactly(documentarios.isActive());
        assertThat(data).extracting(Category::getCreatedAt)
                        .containsExactly(documentarios.getCreatedAt());
        assertThat(data).extracting(Category::getUpdatedAt)
                        .containsExactly(documentarios.getUpdatedAt());
        assertThat(data).extracting(Category::getDeletedAt)
                        .containsExactly(documentarios.getDeletedAt());

        // Arrange - Given new expected page for next test
        final var newExpectedPage = 2;
        query = CategorySearchQuery.of(2, 1, null, "name", "ASC");

        // Act - When (Page 2)
        actualResult = categoryGateway.findAll(query);

        // Assert - Then (Page 2)
        assertThat(actualResult).isNotNull();
        assertThat(actualResult.page()).isEqualTo(newExpectedPage);
        assertThat(actualResult.perPage()).isEqualTo(expectedPerPage);
        assertThat(actualResult.total()).isEqualTo(expectedTotal);

        final var actualData = actualResult.data();

        assertThat(actualData)
            .isNotNull()
            .isNotEmpty()
            .hasSize(expectedPerPage);

        assertThat(actualData).extracting(Category::getName)
                              .containsExactly(series.getName());
        assertThat(actualData).extracting(Category::getId)
                              .containsExactly(series.getId());
        assertThat(actualData).extracting(Category::getDescription)
                              .containsExactly(series.getDescription());
        assertThat(actualData).extracting(Category::isActive)
                              .containsExactly(series.isActive());
        assertThat(actualData).extracting(Category::getCreatedAt)
                              .containsExactly(series.getCreatedAt());
        assertThat(actualData).extracting(Category::getUpdatedAt)
                              .containsExactly(series.getUpdatedAt());
        assertThat(actualData).extracting(Category::getDeletedAt)
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
    void givenAPrePersistedCategoriesAndDocAsTerm_whenCallsFindAllAndTermsMatchesCategoryName_thenShouldReturnAPaginated() {
        // Arrange - Given
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var filmes = Category.newCategory("Filmes", null, true);
        final var series = Category.newCategory("Series", null, true);
        final var documentarios = Category.newCategory("Documentários", null, true);
        assertThat(categoryRepository.count()).isZero();
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

    /**
     * Tests that when the search term matches a category description, the gateway returns a
     * paginated result.
     * <p>
     * The test verifies that using a search term ("MAIS ASSISTIDA") correctly returns the category
     * whose description matches the term.
     */
    @Test
    void givenAPrePersistedCategoriesAndMaisAssistidaAsTerm_whenCallsFindAllAndTermsMatchesCategoryDescription_thenShouldReturnAPaginated() {
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
        assertThat(categoryRepository.count()).isZero();
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

    /**
     * Tests that the gateway returns a paginated result in descending order when requested.
     * <p>
     * The test verifies that when the sort order is set to DESC on the "name" field, the paginated
     * result is returned in the correct descending order.
     */
    @Test
    void givenAPrePersistedCategoriesAndDescAsOrders_whenCallsFindAll_thenShouldReturnAPaginatedInDescOrder() {
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
        assertThat(categoryRepository.count()).isZero();
        categoryRepository.saveAllAndFlush(List.of(
            CategoryJpaEntity.from(filmes),
            CategoryJpaEntity.from(series),
            CategoryJpaEntity.from(documentarios)
        ));
        assertThat(categoryRepository.count()).isEqualTo(3);

        // Act - When
        CategorySearchQuery query = CategorySearchQuery.of(0, 3, null, "name", "DESC");
        var actualResult = categoryGateway.findAll(query);

        // Assert - Then
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
