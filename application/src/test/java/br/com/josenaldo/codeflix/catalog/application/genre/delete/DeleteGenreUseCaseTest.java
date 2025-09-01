package br.com.josenaldo.codeflix.catalog.application.genre.delete;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import br.com.josenaldo.codeflix.catalog.application.UseCaseTest;
import br.com.josenaldo.codeflix.catalog.domain.genre.Genre;
import br.com.josenaldo.codeflix.catalog.domain.genre.GenreGateway;
import br.com.josenaldo.codeflix.catalog.domain.genre.GenreID;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

public class DeleteGenreUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultDeleteGenreUseCase useCase;

    @Mock
    private GenreGateway genreGateway;

    @Override
    public List<Object> getMocks() {
        return List.of(genreGateway);
    }

    @Test
    void givenAValidGenreId_whenCallsDeleteGenre_thenShouldDeleteGenre() {
        // Arrange - Given
        final var aGenre = Genre.newGenre("Ação", true);
        final var expectedId = aGenre.getId();

        doNothing().when(genreGateway).deleteById(expectedId);

        // Act - When
        assertThatNoException().isThrownBy(() -> useCase.execute(expectedId.getValue()));

        // Assert - Then
        verify(genreGateway, Mockito.times(1)).deleteById(expectedId);
    }

    @Test
    void givenAnInvalidGenreId_whenCallsDeleteGenre_thenShouldBeOk() {
        // Arrange - Given
        final var expectedId = GenreID.unique();

        doNothing().when(genreGateway).deleteById(expectedId);

        // Act - When
        assertThatNoException().isThrownBy(() -> useCase.execute(expectedId.getValue()));

        // Assert - Then
        verify(genreGateway, Mockito.times(1)).deleteById(expectedId);
    }

    @Test
    void givenAnInvalidNullGenreId_whenCallsDeleteGenre_thenShouldThrowNullPointerException() {
        // Arrange - Given

        // Act - When
        final var actualException = catchException(() -> useCase.execute(null));

        // Assert - Then
        assertThat(actualException)
            .isNotNull()
            .isInstanceOf(NullPointerException.class)
            .hasMessage(DeleteGenreUseCase.GENRE_ID_NULL_ERROR);

        verify(genreGateway, Mockito.never()).deleteById(any());
    }

    @Test
    void givenAValidGenreId_whenCallsDeleteGenreAndGatewayThrowsUnexpectedError_thenShouldThrowUnexpectedError() {
        // Arrange - Given
        final var expectedId = GenreID.unique();

        doThrow(new IllegalStateException("Gateway Error"))
            .when(genreGateway).deleteById(expectedId);

        // Act - When
        final var actualException = catchException(() -> useCase.execute(expectedId.getValue()));

        // Assert - Then
        assertThat(actualException)
            .isNotNull()
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Gateway Error");

        verify(genreGateway, Mockito.times(1)).deleteById(expectedId);
    }

}
