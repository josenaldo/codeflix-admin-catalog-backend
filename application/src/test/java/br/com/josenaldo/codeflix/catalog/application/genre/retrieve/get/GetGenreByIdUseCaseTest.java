package br.com.josenaldo.codeflix.catalog.application.genre.retrieve.get;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.josenaldo.codeflix.catalog.application.UseCaseTest;
import br.com.josenaldo.codeflix.catalog.domain.category.CategoryID;
import br.com.josenaldo.codeflix.catalog.domain.exceptions.NotFoundException;
import br.com.josenaldo.codeflix.catalog.domain.genre.Genre;
import br.com.josenaldo.codeflix.catalog.domain.genre.GenreGateway;
import br.com.josenaldo.codeflix.catalog.domain.genre.GenreID;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class GetGenreByIdUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultGetGenreByIdUseCase useCase;

    @Mock
    private GenreGateway genreGateway;

    @Override
    public List<Object> getMocks() {
        return List.of(genreGateway);
    }

    @Test
    void givenAValidId_whenCallsGetGenre_thenShouldReturnGenre() {
        // Arrange - Given
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(CategoryID.unique(), CategoryID.unique());
        final var aGenre = Genre.newGenre(expectedName, expectedIsActive)
                                .addCategories(expectedCategories);

        final var expectedId = aGenre.getId();

        when(genreGateway.findById(any())).thenReturn(Optional.of(aGenre));

        // Act - When
        final var actualGenre = useCase.execute(expectedId.getValue());

        // Assert - Then
        assertThat(actualGenre).isNotNull();
        assertThat(actualGenre.id()).isNotNull().isEqualTo(expectedId.getValue());
        assertThat(actualGenre.createdAt()).isEqualTo(aGenre.getCreatedAt());
        assertThat(actualGenre.updatedAt()).isEqualTo(aGenre.getUpdatedAt());
        assertThat(actualGenre.deletedAt()).isNull();
        assertThat(actualGenre.name()).isEqualTo(expectedName);
        assertThat(actualGenre.isActive()).isEqualTo(expectedIsActive);
        assertThat(actualGenre.categories()).isEqualTo(asString(expectedCategories));

        verify(genreGateway, times(1)).findById(eq(expectedId));
    }


    @Test
    void givenAnCalidIdInexistentID_whenCallsGetGenre_thenShouldReturnNotFound() {
        // Arrange - Given
        final var expectedId = GenreID.unique();
        final var expectedErrorMessage = "Genre with ID %s was not found".formatted(expectedId.getValue());

        when(genreGateway.findById(eq(expectedId)))
            .thenReturn(Optional.empty());

        // Act - When
        final var actualException = catchException(() -> useCase.execute(expectedId.getValue()));

        // Assert - Then
        assertThat(actualException)
            .isInstanceOf(NotFoundException.class)
            .hasMessage(expectedErrorMessage);

        verify(genreGateway, times(1)).findById(eq(expectedId));
    }

    private List<String> asString(final List<CategoryID> categories) {
        return categories.stream().map(CategoryID::getValue).toList();
    }
}
