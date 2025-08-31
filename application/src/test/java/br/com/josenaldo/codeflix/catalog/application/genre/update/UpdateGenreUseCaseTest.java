package br.com.josenaldo.codeflix.catalog.application.genre.update;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.josenaldo.codeflix.catalog.domain.category.CategoryGateway;
import br.com.josenaldo.codeflix.catalog.domain.category.CategoryID;
import br.com.josenaldo.codeflix.catalog.domain.exceptions.NotificationException;
import br.com.josenaldo.codeflix.catalog.domain.genre.Genre;
import br.com.josenaldo.codeflix.catalog.domain.genre.GenreGateway;
import br.com.josenaldo.codeflix.catalog.domain.genre.GenreValidator;
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
            anUpdatedGenre -> Objects.equals(anUpdatedGenre.getId(), expectedId)
                && Objects.equals(anUpdatedGenre.getCreatedAt(), aGenre.getCreatedAt())
                && anUpdatedGenre.getUpdatedAt().isAfter(aGenre.getUpdatedAt())
                && Objects.isNull(anUpdatedGenre.getDeletedAt())
                && Objects.equals(anUpdatedGenre.getName(), expectedName)
                && Objects.equals(anUpdatedGenre.isActive(), expectedIsActive)
                && Objects.equals(anUpdatedGenre.getCategories(), expectedCategories)
        ));
    }

    @Test
    void givenAValidCommandWithCategories_whenCallsUpdateGenre_thenShouldReturnGenreID() {
        // Arrange - Given
        final var aGenre = Genre.newGenre("acao", true);

        final var expectedId = aGenre.getId();
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(
            CategoryID.unique(),
            CategoryID.unique()
        );

        final var aCommand = UpdateGenreCommand.with(
            expectedId.getValue(),
            expectedName,
            expectedIsActive,
            asString(expectedCategories)
        );

        when(genreGateway.findById(any())).thenReturn(Optional.of(aGenre.clone()));

        when(categoryGateway.existsByIds(any())).thenReturn(expectedCategories);

        when(genreGateway.update(any())).thenAnswer(returnsFirstArg());

        // Act - When
        final var actualOutput = usecase.execute(aCommand);

        // Assert - Then
        assertThat(actualOutput).isNotNull();
        assertThat(actualOutput.id()).isEqualTo(expectedId.getValue());

        verify(genreGateway, Mockito.times(1)).findById(eq(expectedId));

        verify(categoryGateway, Mockito.times(1)).existsByIds(eq(expectedCategories));

        verify(genreGateway, Mockito.times(1)).update(argThat(
            anUpdatedGenre -> Objects.equals(anUpdatedGenre.getId(), expectedId)
                && Objects.equals(anUpdatedGenre.getCreatedAt(), aGenre.getCreatedAt())
                && anUpdatedGenre.getUpdatedAt().isAfter(aGenre.getUpdatedAt())
                && Objects.isNull(anUpdatedGenre.getDeletedAt())
                && Objects.equals(anUpdatedGenre.getName(), expectedName)
                && Objects.equals(anUpdatedGenre.isActive(), expectedIsActive)
                && Objects.equals(anUpdatedGenre.getCategories(), expectedCategories)
        ));
    }

    @Test
    void givenAValidCommandWithInactive_whenCallsUpdateGenre_thenShouldReturnGenreID() {
        // Arrange - Given
        final var aGenre = Genre.newGenre("acao", true);

        final var expectedId = aGenre.getId();
        final var expectedName = "Ação";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryID>of();

        final var aCommand = UpdateGenreCommand.with(
            expectedId.getValue(),
            expectedName,
            expectedIsActive,
            asString(expectedCategories)
        );

        when(genreGateway.findById(any())).thenReturn(Optional.of(aGenre.clone()));

        when(genreGateway.update(any())).thenAnswer(returnsFirstArg());

        assertThat(aGenre.isActive()).isTrue();
        assertThat(aGenre.getDeletedAt()).isNull();

        // Act - When
        final var actualOutput = usecase.execute(aCommand);

        // Assert - Then
        assertThat(actualOutput).isNotNull();
        assertThat(actualOutput.id()).isEqualTo(expectedId.getValue());

        verify(genreGateway, Mockito.times(1)).findById(eq(expectedId));

        verify(genreGateway, Mockito.times(1)).update(argThat(
            anUpdatedGenre -> Objects.equals(anUpdatedGenre.getId(), expectedId)
                && Objects.equals(anUpdatedGenre.getCreatedAt(), aGenre.getCreatedAt())
                && anUpdatedGenre.getUpdatedAt().isAfter(aGenre.getUpdatedAt())
                && Objects.nonNull(anUpdatedGenre.getDeletedAt())
                && anUpdatedGenre.getDeletedAt().isAfter(aGenre.getCreatedAt())
                && Objects.equals(anUpdatedGenre.getName(), expectedName)
                && Objects.equals(anUpdatedGenre.isActive(), expectedIsActive)
                && Objects.equals(anUpdatedGenre.getCategories(), expectedCategories)
        ));
    }

    @Test
    void givenAnInvalidNullName_whenCallsUpdateGenre_thenShouldThrowsNotificationException() {
        // Arrange - Given
        final var aGenre = Genre.newGenre("acao", true);

        final var expectedId = aGenre.getId();

        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var expectedErrorMessage = UpdateGenreUseCase.UPDATE_GENRE_VALIDATION_ERROR_MESSAGE_TEMPLATE.formatted(
            expectedId.getValue());

        final var expectedDetailErrorMessage = GenreValidator.NULL_NAME_ERROR;
        final var expectedErrorCount = 1;

        final var aCommand = UpdateGenreCommand.with(
            expectedId.getValue(),
            null,
            expectedIsActive,
            asString(expectedCategories)
        );

        when(genreGateway.findById(any())).thenReturn(Optional.of(aGenre.clone()));

        // Act - When
        final var actualException = catchException(() -> usecase.execute(aCommand));

        // Assert - Then
        assertThat(actualException).isNotNull()
                                   .isExactlyInstanceOf(NotificationException.class)
                                   .hasMessage(expectedErrorMessage)
                                   .hasNoCause();

        final NotificationException notificationException = (NotificationException) actualException;
        assertThat(notificationException.getErrors()).hasSize(expectedErrorCount);
        assertThat(notificationException.getErrors().getFirst().message()).isEqualTo(
            expectedDetailErrorMessage);

        verify(genreGateway, Mockito.times(1)).findById(eq(expectedId));

        verify(categoryGateway, Mockito.never()).existsByIds(any());

        verify(genreGateway, Mockito.never()).update(any());
    }

    @Test
    void givenAnInvalidNullNameAndInexistentCategoryIds_whenCallsUpdateGenre_thenShouldThrowsNotificationException() {
        // Arrange - Given
        final var aGenre = Genre.newGenre("acao", true);

        final var expectedId = aGenre.getId();

        final var expectedIsActive = true;
        var categoryID1 = CategoryID.unique();
        var categoryID2 = CategoryID.unique();
        final var expectedCategories = List.of(categoryID1, categoryID2);

        final var expectedErrorMessage = UpdateGenreUseCase.UPDATE_GENRE_VALIDATION_ERROR_MESSAGE_TEMPLATE.formatted(
            expectedId.getValue());

        final var expectedDetailErrorMessage1 = UpdateGenreUseCase.GENRE_CATEGORIES_NOT_FOUND_ERROR_TEMPLATE.formatted(
            categoryID1.getValue()
        );
        final var expectedDetailErrorMessage2 = GenreValidator.NULL_NAME_ERROR;
        final var expectedErrorCount = 2;

        when(categoryGateway.existsByIds(any())).thenReturn(List.of(categoryID2));

        final var aCommand = UpdateGenreCommand.with(
            expectedId.getValue(),
            null,
            expectedIsActive,
            asString(expectedCategories)
        );

        when(genreGateway.findById(any())).thenReturn(Optional.of(aGenre.clone()));

        // Act - When
        final var actualException = catchException(() -> usecase.execute(aCommand));

        // Assert - Then
        assertThat(actualException).isNotNull()
                                   .isExactlyInstanceOf(NotificationException.class)
                                   .hasMessage(expectedErrorMessage)
                                   .hasNoCause();

        final NotificationException notificationException = (NotificationException) actualException;
        assertThat(notificationException.getErrors()).hasSize(expectedErrorCount);
        assertThat(notificationException.getErrors().getFirst().message()).isEqualTo(
            expectedDetailErrorMessage1);
        assertThat(notificationException.getErrors().get(1).message()).isEqualTo(
            expectedDetailErrorMessage2);

        verify(genreGateway, Mockito.times(1)).findById(eq(expectedId));

        verify(categoryGateway, Mockito.times(1)).existsByIds(eq(expectedCategories));

        verify(genreGateway, Mockito.never()).update(any());
    }

    private List<String> asString(List<CategoryID> categories) {
        return categories.stream().map(CategoryID::getValue).toList();
    }
}
