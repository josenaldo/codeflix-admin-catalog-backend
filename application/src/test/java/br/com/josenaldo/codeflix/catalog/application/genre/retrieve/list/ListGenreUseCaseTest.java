package br.com.josenaldo.codeflix.catalog.application.genre.retrieve.list;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.josenaldo.codeflix.catalog.application.UseCaseTest;
import br.com.josenaldo.codeflix.catalog.domain.genre.Genre;
import br.com.josenaldo.codeflix.catalog.domain.genre.GenreGateway;
import br.com.josenaldo.codeflix.catalog.domain.pagination.Pagination;
import br.com.josenaldo.codeflix.catalog.domain.pagination.SearchQuery;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class ListGenreUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultListGenreUseCase useCase;

    @Mock
    private GenreGateway genreGateway;

    @Override
    public List<Object> getMocks() {
        return List.of(genreGateway);
    }

    @Test
    void givenAValidQuery_whenCallsListGenre_thenReturnGenres() {
        // Arrange - Given
        final var genres = List.of(
            Genre.newGenre("Ação", true),
            Genre.newGenre("Aventura", true)
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;
        final var expectedItems = genres.stream().map(GenreListOutput::from).toList();

        final var expectedPagination =
            new Pagination<>(expectedPage, expectedPerPage, expectedTotal, genres);

        final var aQuery =
            new SearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
            );

        when(genreGateway.findAll(eq(aQuery))).thenReturn(expectedPagination);

        // Act - When
        final var actualOutput = useCase.execute(aQuery);

        // Assert - Then
        assertThat(actualOutput.page()).isEqualTo(expectedPage);
        assertThat(actualOutput.perPage()).isEqualTo(expectedPerPage);
        assertThat(actualOutput.total()).isEqualTo(expectedTotal);
        assertThat(actualOutput.data().size()).isEqualTo(expectedTotal);
        assertThat(actualOutput.data()).isEqualTo(expectedItems);

        verify(genreGateway, times(1)).findAll(eq(aQuery));
    }


    @Test
    void givenAValidQuery_whenCallsListGenreAndResultIsEmpty_thenReturnGenres() {
        // Arrange - Given
        final var genres = List.<Genre>of();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;
        final var expectedItems = List.<GenreListOutput>of();

        final var expectedPagination =
            new Pagination<>(expectedPage, expectedPerPage, expectedTotal, genres);

        final var aQuery =
            new SearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
            );

        when(genreGateway.findAll(eq(aQuery))).thenReturn(expectedPagination);

        // Act - When
        final var actualOutput = useCase.execute(aQuery);

        // Assert - Then
        assertThat(actualOutput.page()).isEqualTo(expectedPage);
        assertThat(actualOutput.perPage()).isEqualTo(expectedPerPage);
        assertThat(actualOutput.total()).isEqualTo(expectedTotal);
        assertThat(actualOutput.data().size()).isEqualTo(expectedTotal);
        assertThat(actualOutput.data()).isEqualTo(expectedItems);

        verify(genreGateway, times(1)).findAll(eq(aQuery));
    }

    @Test
    void givenAValidQuery_whenCallsListGenreAmdGatewayThrowsRandomError_thenThrowsUnexpectedError() {
        // Arrange - Given
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var aQuery =
            new SearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
            );

        final var expectedErrorMessage = "Gateway error";

        when(genreGateway.findAll(eq(aQuery))).thenThrow(new IllegalStateException("Gateway error"));

        // Act - When
        final var actualException = catchException(() -> useCase.execute(aQuery));

        // Assert - Then
        assertThat(actualException)
            .isNotNull()
            .isExactlyInstanceOf(IllegalStateException.class)
            .hasMessage(expectedErrorMessage);

        verify(genreGateway, times(1)).findAll(eq(aQuery));
    }
}
