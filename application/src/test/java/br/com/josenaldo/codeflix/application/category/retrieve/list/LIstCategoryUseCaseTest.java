package br.com.josenaldo.codeflix.application.category.retrieve.list;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import br.com.josenaldo.codeflix.domain.category.Category;
import br.com.josenaldo.codeflix.domain.category.CategoryGateway;
import br.com.josenaldo.codeflix.domain.category.CategorySearchQuery;
import br.com.josenaldo.codeflix.domain.exceptions.DomainException;
import br.com.josenaldo.codeflix.domain.pagination.Pagination;
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
public class LIstCategoryUseCaseTest {

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
    public void givenAValidQuery_whenCallsListCategories_thenReturnCategories() {
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

        final CategorySearchQuery query = CategorySearchQuery.of(
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

        when(categoryGateway.findAll(eq(query))).thenReturn(expectedPagination);

        // Act - When
        final var actualResult = useCase.execute(query);

        // Assert - Then
        assertThat(actualResult).isNotNull();
        assertThat(actualResult).isEqualTo(expectedResult);
        assertThat(actualResult.page()).isEqualTo(expectedPage);
        assertThat(actualResult.perPage()).isEqualTo(expectedPerPage);
        assertThat(actualResult.total()).isEqualTo(expectedItemCount);

        assertThat(actualResult.data()).isEqualTo(expectedResult.data());
        assertThat(actualResult.data()).hasSize(expectedItemCount);
    }

    /**
     * Given a valid query with no results, when calls list categories, then return empty list
     */
    @Test
    public void givenAValidQueryWithNoResults_whenCallsListCategories_thenReturnEmptyList() {
        // Arrange - Given
        final var expectedPage = 1;
        final var expectedPerPage = 10;
        final var expectedTerm = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "ASC";
        int expectedItemCount = 0;

        final CategorySearchQuery query = CategorySearchQuery.of(
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

        when(categoryGateway.findAll(eq(query))).thenReturn(expectedPagination);

        // Act - When
        final var actualResult = useCase.execute(query);

        // Assert - Then
        assertThat(actualResult).isNotNull();
        assertThat(actualResult).isEqualTo(expectedResult);
        assertThat(actualResult.page()).isEqualTo(expectedPage);
        assertThat(actualResult.perPage()).isEqualTo(expectedPerPage);
        assertThat(actualResult.total()).isEqualTo(expectedItemCount);

        assertThat(actualResult.data()).isEqualTo(expectedResult.data());
        assertThat(actualResult.data()).hasSize(expectedItemCount);
    }

    /**
     * Given a valid query, when gateway throws exception, then throw exception
     */
    @Test
    public void givenAValidQuery_whenGatewayThrowsException_thenThrowException() {
        // Arrange - Given
        final var expectedPage = 1;
        final var expectedPerPage = 10;
        final var expectedTerm = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "ASC";

        final CategorySearchQuery query = CategorySearchQuery.of(
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
        assertThat(actualException).isNotNull();
        assertThat(actualException).isInstanceOf(DomainException.class);
        assertThat(actualException).hasMessage(expectedErrorMessage);
        assertThat(actualException).hasNoCause();

        final var actualDomainException = (DomainException) actualException;
        assertThat(actualDomainException.getMessage()).isEqualTo(expectedErrorMessage);
        assertThat(actualDomainException.getErrors().getFirst().message()).isEqualTo(
            expectedErrorMessage);

        assertThat(actualDomainException.getErrors()).hasSize(expectedArrorCount);
    }

}
