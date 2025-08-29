package br.com.josenaldo.codeflix.catalog.application.genre.update;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.josenaldo.codeflix.catalog.domain.category.CategoryGateway;
import br.com.josenaldo.codeflix.catalog.domain.category.CategoryID;
import br.com.josenaldo.codeflix.catalog.domain.genre.Genre;
import br.com.josenaldo.codeflix.catalog.domain.genre.GenreGateway;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UpdateGenreUseCaseTest {

    @InjectMocks
    private DefaultUpdateGenreUseCase usecase;

    @Mock
    private CategoryGateway categoryGateway;

    @Mock
    private GenreGateway genreGateway;

    @Test
    void givenAValidCommand_whenCallsUpdateGenre_thenShouldReturnGenreID() {
        // Arrange - Given
        final var aGenre = Genre.newGenre("acao", true);

        final var expectedId = aGenre.getId();
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aCommand = UpdateGenreCommand.with(
            expectedId.getValue(),
            expectedName,
            expectedIsActive,
            asString(expectedCategories)
        );

        when(genreGateway.findById(any())).thenReturn(Optional.of(aGenre.clone()));

        when(genreGateway.update(any())).thenAnswer(returnsFirstArg());

        // Act - When
        final var actualOutput = usecase.execute(aCommand);

        // Assert - Then
        assertThat(actualOutput).isNotNull();
        assertThat(actualOutput.id()).isEqualTo(expectedId.getValue());

        verify(genreGateway, Mockito.times(1)).findById(eq(expectedId));

        verify(genreGateway, Mockito.times(1)).update(argThat(
            anUpdatedGenre ->
                Objects.equals(anUpdatedGenre.getId(), expectedId)
                    && Objects.equals(anUpdatedGenre.getCreatedAt(), aGenre.getCreatedAt())
                    && anUpdatedGenre.getUpdatedAt().isAfter(aGenre.getUpdatedAt())
                    && Objects.isNull(anUpdatedGenre.getDeletedAt())
                    && Objects.equals(anUpdatedGenre.getName(), expectedName)
                    && Objects.equals(anUpdatedGenre.isActive(), expectedIsActive)
                    && Objects.equals(anUpdatedGenre.getCategories(), expectedCategories)
        ));
    }

    private List<String> asString(List<CategoryID> categories) {
        return categories.stream()
                         .map(CategoryID::getValue)
                         .toList();
    }
}
