package br.com.josenaldo.codeflix.catalog.application.genre.create;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.josenaldo.codeflix.catalog.domain.category.CategoryGateway;
import br.com.josenaldo.codeflix.catalog.domain.category.CategoryID;
import br.com.josenaldo.codeflix.catalog.domain.genre.GenreGateway;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CreateGenreUseCaseTest {

    @InjectMocks
    private DefaultCreateGenreUseCase useCase;

    @Mock
    private GenreGateway genreGateway;

    @Mock
    private CategoryGateway categoryGateway;

    @Test
    void givenValidCommand_whenCreateGenre_thenShouldReturnGenre() {
        // Arrange - Given
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        CreateGenreCommand aCommand = CreateGenreCommand.with(
            expectedName,
            expectedIsActive,
            asString(expectedCategories)
        );
        when(genreGateway.create(any()))
            .thenAnswer(returnsFirstArg());

        // Act - When
        final CreateGenreOutput actualOutput = useCase.execute(aCommand);

        // Assert - Then
        assertThat(actualOutput).isNotNull();
        assertThat(actualOutput.id()).isNotNull();

        verify(genreGateway, times(1)).create(
            argThat(
                aGenre ->
                    Objects.nonNull(aGenre)
                        && Objects.nonNull(aGenre.getId())
                        && Objects.nonNull(aGenre.getCreatedAt())
                        && Objects.nonNull(aGenre.getUpdatedAt())
                        && Objects.isNull(aGenre.getDeletedAt())
                        && Objects.equals(expectedName, aGenre.getName())
                        && Objects.equals(expectedIsActive, aGenre.isActive())
            )
        );
    }

    private List<String> asString(List<CategoryID> categories) {
        return categories.stream()
                         .map(CategoryID::getValue)
                         .toList();
    }
}
