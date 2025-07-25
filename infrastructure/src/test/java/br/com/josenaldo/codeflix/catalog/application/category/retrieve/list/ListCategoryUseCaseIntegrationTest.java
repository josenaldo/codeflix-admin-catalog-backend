package br.com.josenaldo.codeflix.catalog.application.category.retrieve.list;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.josenaldo.codeflix.catalog.annotations.IntegrationTest;
import br.com.josenaldo.codeflix.catalog.application.category.CategoryTestUtils;
import br.com.josenaldo.codeflix.catalog.domain.category.Category;
import br.com.josenaldo.codeflix.catalog.domain.category.CategorySearchQuery;
import br.com.josenaldo.codeflix.catalog.domain.pagination.Pagination;
import br.com.josenaldo.codeflix.catalog.infrastructure.category.persistence.CategoryRepository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
class ListCategoryUseCaseIntegrationTest {

    @Autowired
    private ListCategoryUseCase useCase;

    @Autowired
    private CategoryRepository repository;

    @BeforeEach
    void setup() {
        final List<Category> categories = List.of(
            Category.newCategory("Filmes", "A categoria mais assistida ", true),
            Category.newCategory("Netflix Originals", "Títulos de autoria da Netflix", true),
            Category.newCategory("Netflix Recentes", "Títulos recentes da Netflix", true),
            Category.newCategory("Amazon Originals", "Títulos de autoria da Amazon", true),
            Category.newCategory("Documentários", "Documentários do Discovery Channel", true),
            Category.newCategory("Kids", "Para crianças", true),
            Category.newCategory("Séries", "Séries de sucesso", true)
        );

        CategoryTestUtils.save(repository, categories.toArray(new Category[0]));
        assertThat(repository.count()).isEqualTo(categories.size());
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    void givenAValidTerms_whenTermDoesNotMatchAnyCategory_thenShouldReturnEmptyPage() {
        // Arrange - Given
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "Jamanta";
        final var expectedSortField = "name";
        final var expectedSortDirection = "ASC";
        final var expectedItemsCount = 0;
        final var expectedTotal = 0;

        CategorySearchQuery searchQuery = CategorySearchQuery.of(
            expectedPage,
            expectedPerPage,
            expectedTerms,
            expectedSortField,
            expectedSortDirection
        );

        // Act - When
        Pagination<CategoryListOutput> actualResult = useCase.execute(searchQuery);

        // Assert - Then
        assertThat(actualResult).isNotNull();
        assertThat(actualResult.itemsCount()).isEqualTo(expectedItemsCount);
        assertThat(actualResult.perPage()).isEqualTo(expectedPerPage);
        assertThat(actualResult.page()).isEqualTo(expectedPage);
        assertThat(actualResult.total()).isEqualTo(expectedTotal);
        assertThat(actualResult.isEmpty()).isTrue();
    }

    @ParameterizedTest
    @CsvSource({
        "fil, 0, 10, 1, 1, Filmes",
        "netflix, 0, 10, 2, 2, Netflix Originals",
        "zon, 0, 10, 1, 1, Amazon Originals",
        "ki, 0, 10, 1, 1, Kids",
        "ries, 0, 10, 1, 1, Séries",
        "crianças, 0, 10, 1, 1, Kids"
    })
    void givenAValidTerms_whenTermMatchesSomeCategories_thenShouldReturnPageWithCategories(
        final String expectedTerms,
        final int expectedPage,
        final int expectedPerPage,
        final int expectedItemsCount,
        final int expectedTotal,
        final String expectedCategoryName
    ) {
        // Arrange - Given
        final var expectedSortField = "name";
        final var expectedSortDirection = "ASC";

        CategorySearchQuery searchQuery = CategorySearchQuery.of(
            expectedPage,
            expectedPerPage,
            expectedTerms,
            expectedSortField,
            expectedSortDirection
        );

        // Act - When
        Pagination<CategoryListOutput> actualResult = useCase.execute(searchQuery);

        // Assert - Then
        assertThat(actualResult)
            .isNotNull()
            .extracting(
                Pagination::page,
                Pagination::perPage,
                Pagination::total,
                Pagination::itemsCount,
                Pagination::isEmpty
            )
            .containsExactly(
                expectedPage,
                expectedPerPage,
                (long) expectedTotal,
                expectedItemsCount,
                false
            );

        assertThat(actualResult.data())
            .isNotNull()
            .hasSize(expectedItemsCount);

        assertThat(actualResult.data().getFirst().name()).isEqualTo(expectedCategoryName);

    }

    @ParameterizedTest
    @CsvSource({
        "name, ASC, 0, 10, 7, 7, Amazon Originals",
        "name, DESC, 0, 10, 7, 7, Séries",
        "createdAt, DESC, 0, 10, 7, 7, Filmes",
        "description, DESC, 0, 10, 7, 7, Netflix Recentes"
    })
    void givenAValidSortAndDirection_whenCallsList_thenShouldReturnPageWithCategoriesSorted(
        final String expectedSortField,
        final String expectedSortDirection,
        final int expectedPage,
        final int expectedPerPage,
        final int expectedItemsCount,
        final int expectedTotal,
        final String expectedCategoryName
    ) {
        // Arrange - Given
        final var expectedTerms = "";

        CategorySearchQuery searchQuery = CategorySearchQuery.of(
            expectedPage,
            expectedPerPage,
            expectedTerms,
            expectedSortField,
            expectedSortDirection
        );

        // Act - When
        Pagination<CategoryListOutput> actualResult = useCase.execute(searchQuery);

        // Assert - Then
        assertThat(actualResult)
            .isNotNull()
            .extracting(
                Pagination::page,
                Pagination::perPage,
                Pagination::total,
                Pagination::itemsCount,
                Pagination::isEmpty
            )
            .containsExactly(
                expectedPage,
                expectedPerPage,
                (long) expectedTotal,
                expectedItemsCount,
                false
            );

        assertThat(actualResult.data())
            .isNotNull()
            .hasSize(expectedItemsCount);

        assertThat(actualResult.data().getFirst().name()).isEqualTo(expectedCategoryName);
    }

    @ParameterizedTest
    @CsvSource({
        "0, 2, 2, 7, Amazon Originals;Documentários",
        "1, 2, 2, 7, Filmes;Kids",
        "2, 2, 2, 7, Netflix Originals;Netflix Recentes",
        "3, 2, 1, 7, Séries"
    })
    void givenAValidPage_whenCallsList_thenShouldReturnCategoriesPaginated(
        final int expectedPage,
        final int expectedPerPage,
        final int expectedItemsCount,
        final int expectedTotal,
        final String expectedCategoriesName
    ) {
        // Arrange - Given
        final var expectedTerms = "";
        final var expectedSortField = "name";
        final var expectedSortDirection = "ASC";

        CategorySearchQuery searchQuery = CategorySearchQuery.of(
            expectedPage,
            expectedPerPage,
            expectedTerms,
            expectedSortField,
            expectedSortDirection
        );

        // Act - When
        Pagination<CategoryListOutput> actualResult = useCase.execute(searchQuery);

        // Assert - Then
        assertThat(actualResult)
            .isNotNull()
            .extracting(
                Pagination::page,
                Pagination::perPage,
                Pagination::total,
                Pagination::itemsCount,
                Pagination::isEmpty
            )
            .containsExactly(
                expectedPage,
                expectedPerPage,
                (long) expectedTotal,
                expectedItemsCount,
                false
            );

        assertThat(actualResult.data())
            .isNotNull()
            .hasSize(expectedItemsCount);

        int index = 0;
        String[] names = expectedCategoriesName.split(";");

        for (String actualName : names) {
            assertThat(actualResult.data().get(index).name()).isEqualTo(actualName);
            index++;
        }

    }
}
