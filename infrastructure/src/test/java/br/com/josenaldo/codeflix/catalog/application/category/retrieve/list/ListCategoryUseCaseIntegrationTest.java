package br.com.josenaldo.codeflix.catalog.application.category.retrieve.list;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.josenaldo.codeflix.catalog.annotations.IntegrationTest;
import br.com.josenaldo.codeflix.catalog.application.category.CategoryTestUtils;
import br.com.josenaldo.codeflix.catalog.domain.category.Category;
import br.com.josenaldo.codeflix.catalog.domain.category.CategorySearchQuery;
import br.com.josenaldo.codeflix.catalog.domain.pagination.Pagination;
import br.com.josenaldo.codeflix.catalog.infrastructure.category.persistence.CategoryRepository;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
class ListCategoryUseCaseIntegrationTest {

    @Autowired
    private ListCategoryUseCase useCase;

    @Autowired
    private CategoryRepository repository;

    /**
     * Static mock of {@link Instant#now()} used for controlling and manipulating the current time
     * during tests.
     * <p>
     * This mock allows the test cases to substitute the system's actual current time with a
     * predetermined or dynamically adjusted time, enabling deterministic and repeatable testing of
     * time-dependent logic.
     */
    private MockedStatic<Instant> instantMock;


    /**
     * Represents the base timestamp used for integration test operations.
     * <p>
     * This field holds a fixed {@link Instant} value that serves as a reference point for
     * operations requiring a consistent and predictable starting time.
     * <p>
     * The value is immutable and is always set to "2024-01-01T00:00:00Z". It ensures consistent
     * temporal references within test scenarios.
     */
    private final Instant baseTime = Instant.parse("2024-01-01T00:00:00Z");


    /**
     * Represents the interval in milliseconds for incrementing or stepping through a time-related
     * operation.
     * <p>
     * This value is used to define a consistent amount of time between each step in tests or
     * processes that involve time-based logic.
     * <p>
     * In the context of integration tests, {@code stepMillis} ensures that time-related events,
     * such as timestamps or durations, advance uniformly by the specified amount.
     */
    private final long stepMillis = 1L;


    /**
     * Tracks the number of calls or invocations performed during the test execution.
     * <p>
     * This field is primarily used within the context of integration testing to record how many
     * times a specific operation or functionality is executed. It helps in asserting and verifying
     * expected behavior, such as counting calls to a particular method or interaction.
     * <p>
     * The field is thread-safe due to its use of {@link AtomicLong}, allowing it to be used
     * reliably in concurrent test scenarios.
     */
    private final AtomicLong callCounter = new AtomicLong(0L);

    @BeforeEach
    void setup() {
        // Inicia o mock de método estático
        instantMock = Mockito.mockStatic(Instant.class, Mockito.CALLS_REAL_METHODS);

        // Cada chamada a Instant.now() retorna um instante crescente e único
        instantMock.when(Instant::now)
                   .thenAnswer(inv -> baseTime.plusMillis(callCounter.getAndIncrement() * stepMillis));

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

        // Finaliza o mock para não vazar para outros testes
        if (instantMock != null) {
            instantMock.close();
        }
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
        final var expectedSortField = "name";
        final var expectedSortDirection = "ASC";

        CategorySearchQuery searchQuery = CategorySearchQuery.of(
            expectedPage,
            expectedPerPage,
            expectedTerms,
            expectedSortField,
            expectedSortDirection
        );

        Pagination<CategoryListOutput> actualResult = useCase.execute(searchQuery);

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
        String[] names = expectedCategoryName.split(";");

        for (String actualName : names) {
            assertThat(actualResult.data().get(index).name()).isEqualTo(actualName);
            index++;
        }
    }
}
