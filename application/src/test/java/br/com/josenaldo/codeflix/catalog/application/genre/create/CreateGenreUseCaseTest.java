package br.com.josenaldo.codeflix.catalog.application.genre.create;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.josenaldo.codeflix.catalog.domain.category.CategoryGateway;
import br.com.josenaldo.codeflix.catalog.domain.category.CategoryID;
import br.com.josenaldo.codeflix.catalog.domain.exceptions.NotificationException;
import br.com.josenaldo.codeflix.catalog.domain.genre.GenreGateway;
import br.com.josenaldo.codeflix.catalog.domain.genre.GenreValidator;
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
    void givenValidCommandWithEmptyCategories_whenCreateGenre_thenShouldReturnGenreID() {
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
                        && Objects.equals(expectedCategories, aGenre.getCategories())
            )
        );
    }

    @Test
    void givenAValidCommandWithCategories_whenCreateGenre_thenShouldReturnGenreID() {
        // Arrange - Given
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final CategoryID categoryID1 = CategoryID.unique();
        final CategoryID categoryID2 = CategoryID.unique();
        final var expectedCategories = List.of(
            categoryID1, categoryID2
        );

        CreateGenreCommand aCommand = CreateGenreCommand.with(
            expectedName,
            expectedIsActive,
            asString(expectedCategories)
        );
        when(categoryGateway.existsByIds(any())).thenReturn(expectedCategories);

        when(genreGateway.create(any()))
            .thenAnswer(returnsFirstArg());

        // Act - When
        final CreateGenreOutput actualOutput = useCase.execute(aCommand);

        // Assert - Then
        assertThat(actualOutput).isNotNull();
        assertThat(actualOutput.id()).isNotNull();

        verify(categoryGateway, times(1)).existsByIds(expectedCategories);

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
                        && Objects.equals(expectedCategories, aGenre.getCategories())
            )
        );
    }

    @Test
    void givenAValidCommandWithInactive_whenCreateGenre_thenShouldReturnGenreID() {
        // Arrange - Given
        final var expectedName = "Ação";
        final var expectedIsActive = false;
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

        verify(categoryGateway, times(0)).existsByIds(expectedCategories);

        verify(genreGateway, times(1)).create(
            argThat(
                aGenre ->
                    Objects.nonNull(aGenre)
                        && Objects.nonNull(aGenre.getId())
                        && Objects.nonNull(aGenre.getCreatedAt())
                        && Objects.nonNull(aGenre.getUpdatedAt())
                        && Objects.nonNull(aGenre.getDeletedAt())
                        && Objects.equals(expectedName, aGenre.getName())
                        && Objects.equals(expectedIsActive, aGenre.isActive())
                        && Objects.equals(expectedCategories, aGenre.getCategories())
            )
        );
    }

    @Test
    void givenAnInvalidEmptyName_whenCallsCreateGenre_thenThrowsNotificationException() {
        // Arrange - Given
        final var expectedName = " ";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var expectedErrorMessage = CreateGenreUseCase.CREATE_GENRE_VALIDATION_ERROR_MESSAGE_TEMPLATE;
        final var expectedDetailMessage = GenreValidator.EMPTY_NAME_ERROR;
        final var expectedErrorCount = 1;

        CreateGenreCommand aCommand = CreateGenreCommand.with(
            expectedName,
            expectedIsActive,
            asString(expectedCategories)
        );

        // Act - When
        final var actualException = catchException(() -> useCase.execute(aCommand));

        // Assert - Then
        assertThat(actualException)
            .isNotNull()
            .isInstanceOf(NotificationException.class)
            .hasMessage(expectedErrorMessage)
            .hasNoCause();

        final NotificationException notificationException = (NotificationException) actualException;

        assertThat(notificationException.getErrors())
            .hasSize(expectedErrorCount);
        assertThat(notificationException.getErrors().getFirst().message())
            .isEqualTo(expectedDetailMessage);

        verify(categoryGateway, times(0)).existsByIds(any());
        verify(genreGateway, times(0)).create(any());
    }

    @Test
    void givenAnInvalidNullName_whenCallsCreateGenre_thenThrowsNotificationException() {
        // Arrange - Given
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var expectedErrorMessage = CreateGenreUseCase.CREATE_GENRE_VALIDATION_ERROR_MESSAGE_TEMPLATE;
        final var expectedDetailMessage = GenreValidator.NULL_NAME_ERROR;
        final var expectedErrorCount = 1;

        CreateGenreCommand aCommand = CreateGenreCommand.with(
            expectedName,
            expectedIsActive,
            asString(expectedCategories)
        );

        // Act - When
        final var actualException = catchException(() -> useCase.execute(aCommand));

        // Assert - Then
        assertThat(actualException)
            .isNotNull()
            .isInstanceOf(NotificationException.class)
            .hasMessage(expectedErrorMessage)
            .hasNoCause();

        final NotificationException notificationException = (NotificationException) actualException;

        assertThat(notificationException.getErrors())
            .hasSize(expectedErrorCount);
        assertThat(notificationException.getErrors().getFirst().message())
            .isEqualTo(expectedDetailMessage);

        verify(categoryGateway, times(0)).existsByIds(any());
        verify(genreGateway, times(0)).create(any());
    }

    @Test
    void givenAnInvalidCommand_whenCallsCreateGenreAndSomeCategoriesDoesNotExists_thenThrowsNotificationException() {
        // Arrange - Given
        final String expectedName = "Ação";
        final var expectedIsActive = true;

        CategoryID categoryID1 = CategoryID.unique();
        CategoryID categoryID2 = CategoryID.unique();
        final var expectedCategories = List.of(
            categoryID1, categoryID2
        );

        final var expectedErrorMessage = CreateGenreUseCase
            .CREATE_GENRE_VALIDATION_ERROR_MESSAGE_TEMPLATE;
        final var expectedDetailMessage = CreateGenreUseCase
            .GENRE_CATEGORIES_NOT_FOUND_ERROR_TEMPLATE.formatted(categoryID2.getValue());
        final var expectedErrorCount = 1;

        CreateGenreCommand aCommand = CreateGenreCommand.with(
            expectedName,
            expectedIsActive,
            asString(expectedCategories)
        );

        when(categoryGateway.existsByIds(any())).thenReturn(List.of(categoryID1));

        // Act - When
        final var actualException = catchException(() -> useCase.execute(aCommand));

        // Assert - Then
        assertThat(actualException)
            .isNotNull()
            .isInstanceOf(NotificationException.class)
            .hasMessage(expectedErrorMessage)
            .hasNoCause();

        final NotificationException notificationException = (NotificationException) actualException;

        assertThat(notificationException.getErrors())
            .hasSize(expectedErrorCount);
        assertThat(notificationException.getErrors().getFirst().message())
            .isEqualTo(expectedDetailMessage);

        verify(categoryGateway, times(1)).existsByIds(any());
        verify(genreGateway, times(0)).create(any());
    }

    @Test
    void givenAnInvalidNames_whenCallsCreateGenreAndSomeCategoriesDoesNotExists_thenThrowsNotificationException() {
        // Arrange - Given
        final String expectedName = " ";
        final var expectedIsActive = true;

        CategoryID categoryID1 = CategoryID.unique();
        CategoryID categoryID2 = CategoryID.unique();
        final var expectedCategories = List.of(
            categoryID1, categoryID2
        );

        final var expectedErrorMessage = CreateGenreUseCase.CREATE_GENRE_VALIDATION_ERROR_MESSAGE_TEMPLATE;
        final var expectedDetailMessage1 = CreateGenreUseCase
            .GENRE_CATEGORIES_NOT_FOUND_ERROR_TEMPLATE.formatted(categoryID2.getValue());
        final var expectedDetailMessage2 = GenreValidator.EMPTY_NAME_ERROR;
        final var expectedErrorCount = 2;

        CreateGenreCommand aCommand = CreateGenreCommand.with(
            expectedName,
            expectedIsActive,
            asString(expectedCategories)
        );

        when(categoryGateway.existsByIds(any())).thenReturn(List.of(categoryID1));

        // Act - When
        final var actualException = catchException(() -> useCase.execute(aCommand));

        // Assert - Then
        assertThat(actualException)
            .isNotNull()
            .isInstanceOf(NotificationException.class)
            .hasMessage(expectedErrorMessage)
            .hasNoCause();

        final NotificationException notificationException = (NotificationException) actualException;

        assertThat(notificationException.getErrors())
            .hasSize(expectedErrorCount);
        assertThat(notificationException.getErrors().getFirst().message())
            .isEqualTo(expectedDetailMessage1);
        assertThat(notificationException.getErrors().get(1).message())
            .isEqualTo(expectedDetailMessage2);

        verify(categoryGateway, times(1)).existsByIds(any());
        verify(genreGateway, times(0)).create(any());
    }

    private List<String> asString(List<CategoryID> categories) {
        return categories.stream()
                         .map(CategoryID::getValue)
                         .toList();
    }
}
