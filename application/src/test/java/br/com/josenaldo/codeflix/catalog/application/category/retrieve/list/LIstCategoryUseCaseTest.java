package br.com.josenaldo.codeflix.catalog.application.category.retrieve.list;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import br.com.josenaldo.codeflix.catalog.domain.category.Category;
import br.com.josenaldo.codeflix.catalog.domain.category.CategoryGateway;
import br.com.josenaldo.codeflix.catalog.domain.exceptions.DomainException;
import br.com.josenaldo.codeflix.catalog.domain.pagination.Pagination;
import br.com.josenaldo.codeflix.catalog.domain.pagination.SearchQuery;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * List Categories UseCase unit tests
 */
@ExtendWith(MockitoExtension.class)
class LIstCategoryUseCaseTest {

    @InjectMocks
    private DefaultListCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    public void setup() {
        reset(categoryGateway);
    }

    /**
     * Given a valid query, when calls list categories, then return all categories
     */
    @Test
    void givenAValidQuery_whenCallsListCategories_thenReturnCategories() {
        // Arrange - Given
        final var categories = List.of(
            Category.newCategory("Filmes", "A categoria mais vista", true),
            Category.newCategory("Séries", "A categoria mais assistida", true),
            Category.newCategory("Documentários", "A categoria mais informativa", true),
            Category.newCategory("Animes", "A categoria mais animada", true),
            Category.newCategory("Desenhos", "A categoria mais infantil", true),
            Category.newCategory("Programas de TV", "A categoria mais diversificada", true)
        );

        final var expectedPage = 1;
        final var expectedPerPage = 10;
        final var expectedTerm = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "ASC";
        int expectedItemCount = categories.size();

        final SearchQuery query = SearchQuery.of(
            expectedPage,
            expectedPerPage,
            expectedTerm,
            expectedSort,
            expectedDirection
        );

        final var expectedPagination = Pagination.fromPage(
            expectedPage,
            expectedPerPage,
            expectedItemCount,
            categories
        );

        final var expectedResult = expectedPagination.map(CategoryListOutput::from);

        when(categoryGateway.findAll(query)).thenReturn(expectedPagination);

        // Act - When
        final var actualResult = useCase.execute(query);

        // Assert - Then
        assertThat(actualResult)
            .isNotNull()
            .isEqualTo(expectedResult)
            .extracting(Pagination::page, Pagination::perPage, Pagination::total)
            .containsExactly(expectedPage, expectedPerPage, (long) expectedItemCount);

        assertThat(actualResult.data())
            .isEqualTo(expectedResult.data())
            .hasSize(expectedItemCount);
    }

    /**
     * Given a valid query with no results, when calls list categories, then return empty list
     */
    @Test
    void givenAValidQueryWithNoResults_whenCallsListCategories_thenReturnEmptyList() {
        // Arrange - Given
        final var expectedPage = 1;
        final var expectedPerPage = 10;
        final var expectedTerm = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "ASC";
        int expectedItemCount = 0;

        final SearchQuery query = SearchQuery.of(
            expectedPage,
            expectedPerPage,
            expectedTerm,
            expectedSort,
            expectedDirection
        );

        final Pagination<Category> expectedPagination = Pagination.fromPage(
            expectedPage,
            expectedPerPage,
            expectedItemCount,
            List.of()
        );

        Pagination<CategoryListOutput> expectedResult = expectedPagination.map(CategoryListOutput::from);

        when(categoryGateway.findAll(query)).thenReturn(expectedPagination);

        // Act - When
        final var actualResult = useCase.execute(query);

        // Assert - Then
        assertThat(actualResult)
            .isNotNull()
            .isEqualTo(expectedResult)
            .extracting(Pagination::page, Pagination::perPage, Pagination::total)
            .containsExactly(expectedPage, expectedPerPage, (long) expectedItemCount);

        assertThat(actualResult.data())
            .isEqualTo(expectedResult.data())
            .hasSize(expectedItemCount);
    }

    /**
     * Given a valid query, when gateway throws exception, then throw exception
     */
    @Test
    void givenAValidQuery_whenGatewayThrowsException_thenThrowException() {
        // Arrange - Given
        final var expectedPage = 1;
        final var expectedPerPage = 10;
        final var expectedTerm = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "ASC";

        final SearchQuery query = SearchQuery.of(
            expectedPage,
            expectedPerPage,
            expectedTerm,
            expectedSort,
            expectedDirection
        );

        final var expectedErrorMessage = "Gateway error";
        final var expectedArrorCount = 1;

        when(categoryGateway.findAll(any())).thenThrow(new IllegalStateException(
            expectedErrorMessage));

        // Act - When
        final var actualException = catchException(() -> useCase.execute(query));

        // Assert - Then
        assertThat(actualException)
            .isNotNull()
            .isInstanceOf(DomainException.class)
            .hasMessage(expectedErrorMessage)
            .hasNoCause();

        final var actualDomainException = (DomainException) actualException;
        assertThat(actualDomainException).hasMessage(expectedErrorMessage);

        assertThat(actualDomainException.getErrors().getFirst().message()).isEqualTo(
            expectedErrorMessage);

        assertThat(actualDomainException.getErrors()).hasSize(expectedArrorCount);
    }

}
