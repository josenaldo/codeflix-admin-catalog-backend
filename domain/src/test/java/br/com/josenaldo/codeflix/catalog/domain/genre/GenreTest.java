package br.com.josenaldo.codeflix.catalog.domain.genre;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.catchException;

import br.com.josenaldo.codeflix.catalog.domain.category.CategoryID;
import br.com.josenaldo.codeflix.catalog.domain.exceptions.NotificationException;
import br.com.josenaldo.codeflix.catalog.domain.validation.Error;
import br.com.josenaldo.codeflix.catalog.domain.validation.handler.ThrowsValidationHandler;
import java.util.List;
import org.junit.jupiter.api.Test;

public class GenreTest {

    @Test
    void givenValidParams_whenCallNewGenre_thenNewInstanceIsCreated() {
        // Arrange - Given
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategoriesCount = 0;

        // Act - When
        final Genre actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        // Assert - Then
        assertThat(actualGenre).isNotNull();
        assertThat(actualGenre.getId()).isNotNull();
        assertThat(actualGenre.getCreatedAt()).isNotNull();
        assertThat(actualGenre.getUpdatedAt()).isNotNull();
        assertThat(actualGenre.getDeletedAt()).isNull();
        assertThat(expectedName).isEqualTo(actualGenre.getName());
        assertThat(expectedIsActive).isEqualTo(actualGenre.isActive());
        assertThat(actualGenre.getCategories()).hasSize(expectedCategoriesCount);
    }

    @Test
    void givenAnInvalidNullName_whenCallsSelfValidate_thenThrowsNotificationException() {
        // Arrange - Given
        final var expectedMessage = Genre.GENRE_VALIDATION_ERROR_MESSAGE;
        final var expectedDetailedMessage = GenreValidator.NULL_NAME_ERROR;
        final var expectedErrorCount = 1;

        // Act - When
        final var actualException = catchException(() -> Genre.newGenre(null, true));

        // Assert - Then
        assertThat(actualException)
            .isNotNull()
            .isInstanceOf(NotificationException.class)
            .hasMessage(expectedMessage)
            .hasNoCause();

        final NotificationException actualNotificationException = (NotificationException) actualException;
        final var errors = actualNotificationException.getErrors();
        assertThat(errors).hasSize(expectedErrorCount);
        assertThat(errors.getFirst().message()).isEqualTo(expectedDetailedMessage);
    }

    @Test
    void givenAnInvalidEmptyName_whenCallsNewGender_thenThrowsNotificationException() {
        // Arrange - Given
        final var expectedMessage = Genre.GENRE_VALIDATION_ERROR_MESSAGE;
        final var expectedDetailedMessage = GenreValidator.EMPTY_NAME_ERROR;
        final var expectedErrorCount = 1;

        // Act - When
        final var actualException = catchException(() -> Genre.newGenre("", true));

        // Assert - Then
        assertThat(actualException)
            .isNotNull()
            .isInstanceOf(NotificationException.class)
            .hasMessage(expectedMessage)
            .hasNoCause();

        final var actualNotificationException = (NotificationException) actualException;
        final var errors = actualNotificationException.getErrors();
        assertThat(errors).hasSize(expectedErrorCount);
        assertThat(errors.getFirst().message()).isEqualTo(expectedDetailedMessage);
    }

    @Test
    void givenAnInvalidNameWithMoreThan255Characters_whenCreateAGenre_thenThrowsNotificationException() {
        // Arrange - Given
        final var expectedMessage = Genre.GENRE_VALIDATION_ERROR_MESSAGE;
        final var expectedDetailedMessage = GenreValidator.NAME_LENGTH_OUT_OF_RANGE_ERROR;
        final var expectedErrorCount = 1;
        final var expectedName = "A".repeat(256);

        // Act - When
        final var actualException = catchException(() -> Genre.newGenre(expectedName, true));

        // Assert - Then
        assertThat(actualException)
            .isNotNull()
            .isInstanceOf(NotificationException.class)
            .hasMessage(expectedMessage)
            .hasNoCause();

        final var actualNotificationException = (NotificationException) actualException;
        final var errors = actualNotificationException.getErrors();
        assertThat(errors).hasSize(expectedErrorCount);
        assertThat(errors.getFirst().message()).isEqualTo(expectedDetailedMessage);
    }

    @Test
    void givenAValidFalseIsActive_whenCallsSelfValidate_thenNotThrowNotificationException() {
        // Arrange - Given
        final var expectedName = "Ação";
        final var expectedCategoriesCount = 0;

        var actualGenre = Genre.newGenre(expectedName, false);

        // Act - When
        assertThatNoException().isThrownBy(() -> actualGenre.validate(new ThrowsValidationHandler()));

        // Assert - Then
        assertThat(actualGenre).isNotNull();
        assertThat(actualGenre.getId()).isNotNull();
        assertThat(actualGenre.getCreatedAt()).isNotNull();
        assertThat(actualGenre.getUpdatedAt()).isNotNull();
        assertThat(actualGenre.getDeletedAt()).isNotNull();
        assertThat(actualGenre.getName()).isEqualTo(expectedName);
        assertThat(actualGenre.isActive()).isFalse();
        assertThat(actualGenre.getCategories()).hasSize(expectedCategoriesCount);
    }

    @Test
    void givenAValidActiveGenre_whenCallsDeactivate_thenReturnGenreInactive() {
        // Arrange - Given
        final var expectedName = "Filmes";
        final var expectedCategoriesCount = 0;

        final var genre = Genre.newGenre(expectedName, true);
        final var expectedUpdatedAt = genre.getUpdatedAt();

        assertThatNoException().isThrownBy(() -> genre.validate(new ThrowsValidationHandler()));
        assertThat(genre.isActive()).isTrue();
        assertThat(genre.getDeletedAt()).isNull();

        // When - Then
        final var actualGenre = genre.deactivate();

        // Assert - Then
        assertThat(actualGenre)
            .isNotNull()
            .isEqualTo(genre);

        assertThat(actualGenre.getId()).isEqualTo(genre.getId());
        assertThat(actualGenre.getCreatedAt()).isEqualTo(genre.getCreatedAt());
        assertThat(actualGenre.getUpdatedAt()).isAfterOrEqualTo(expectedUpdatedAt);
        assertThat(actualGenre.getDeletedAt()).isNotNull();
        assertThat(actualGenre.getName()).isEqualTo(genre.getName());
        assertThat(actualGenre.isActive()).isFalse();
        assertThat(actualGenre.getCategories()).hasSize(expectedCategoriesCount);
    }

    @Test
    void givenAValidDeactivatedGenre_whenCallsActivate_thenReturnGenreActive() {
        // Arrange - Given
        final var expectedName = "Filmes";
        final var expectedCategoriesCount = 0;

        final var genre = Genre.newGenre(expectedName, false);
        final var updatedAt = genre.getUpdatedAt();

        assertThatNoException().isThrownBy(() -> genre.validate(new ThrowsValidationHandler()));
        assertThat(genre.isActive()).isFalse();
        assertThat(genre.getDeletedAt()).isNotNull();

        // When - Then
        final var actualGenre = genre.activate();

        // Assert - Then
        assertThat(actualGenre)
            .isNotNull()
            .isEqualTo(genre);

        assertThat(actualGenre.getId()).isEqualTo(genre.getId());
        assertThat(actualGenre.getCreatedAt()).isEqualTo(genre.getCreatedAt());
        assertThat(actualGenre.getUpdatedAt()).isAfterOrEqualTo(updatedAt);
        assertThat(actualGenre.getDeletedAt()).isNull();
        assertThat(actualGenre.getName()).isEqualTo(expectedName);
        assertThat(actualGenre.isActive()).isTrue();
        assertThat(actualGenre.getCategories()).hasSize(expectedCategoriesCount);
    }

    @Test
    void givenAValidGenre_whenCallsUpdate_thenReturnGenreUpdated() {
        // Arrange - Given
        final var genre = Genre.newGenre("serie", false);
        final var expectedId = genre.getId();
        final var expectedCreatedAt = genre.getCreatedAt();
        final var expectedUpdatedAt = genre.getUpdatedAt();
        final var expectedDeletedAt = genre.getDeletedAt();
        final var expectedName = "Séries";
        final var expectedIsActive = true;
        final var expectedCategoriesCount = 1;
        final var expectedCategories = List.of(CategoryID.unique());

        assertThatNoException().isThrownBy(() -> genre.validate(new ThrowsValidationHandler()));
        assertThat(genre.isActive()).isFalse();
        assertThat(expectedCreatedAt).isNotNull();
        assertThat(expectedUpdatedAt).isNotNull();
        assertThat(expectedDeletedAt).isNotNull();
        assertThat(genre.getCategories()).hasSize(0);

        // Act - When
        final var actualGenre = genre.update(expectedName, expectedIsActive, expectedCategories);

        // Assert - Then
        assertThat(actualGenre)
            .isNotNull()
            .isEqualTo(genre);

        assertThat(actualGenre.getId()).isEqualTo(expectedId);
        assertThat(actualGenre.getCreatedAt()).isEqualTo(expectedCreatedAt);
        assertThat(actualGenre.getUpdatedAt()).isAfter(expectedUpdatedAt);
        assertThat(actualGenre.getDeletedAt()).isNull();
        assertThat(actualGenre.getName()).isEqualTo(expectedName);
        assertThat(actualGenre.isActive()).isTrue();
        assertThat(actualGenre.getCategories())
            .hasSize(expectedCategoriesCount)
            .containsExactlyElementsOf(expectedCategories);
    }

    @Test
    void givenAValidActiveGenre_whenCallsUpdateWithDeactivate_thenReturnUpdatedAndInactiveGenre() {
        // Arrange - Given
        final var genre = Genre.newGenre("series", true);

        final var expectedId = genre.getId();
        final var expectedCreatedAt = genre.getCreatedAt();
        final var expectedUpdatedAt = genre.getUpdatedAt();
        final var expectedDeletedAt = genre.getDeletedAt();
        final var expectedName = "Séries";
        final var expectedIsActive = false;
        final var expectedCategoriesCount = 1;
        final var expectedCategories = List.of(CategoryID.unique());

        assertThatNoException().isThrownBy(() -> genre.validate(new ThrowsValidationHandler()));
        assertThat(genre.isActive()).isTrue();
        assertThat(expectedCreatedAt).isNotNull();
        assertThat(expectedUpdatedAt).isNotNull();
        assertThat(expectedDeletedAt).isNull();
        assertThat(genre.getCategories()).hasSize(0);

        // When - Then
        final var actualGenre = genre.update(expectedName, expectedIsActive, expectedCategories);

        // Assert - Then
        assertThat(actualGenre)
            .isNotNull()
            .isEqualTo(genre);

        assertThat(actualGenre.getId()).isEqualTo(expectedId);
        assertThat(actualGenre.getCreatedAt()).isEqualTo(expectedCreatedAt);
        assertThat(actualGenre.getUpdatedAt()).isAfterOrEqualTo(expectedUpdatedAt);
        assertThat(actualGenre.getDeletedAt()).isNotNull().isAfter(genre.getCreatedAt());
        assertThat(actualGenre.getName()).isEqualTo(expectedName);
        assertThat(actualGenre.isActive()).isFalse();
        assertThat(actualGenre.getCategories())
            .hasSize(expectedCategoriesCount)
            .containsExactlyElementsOf(expectedCategories);
    }

    @Test
    void givenAValidInactiveGenre_whenCallsUpdateWithActivate_thenReturnUpdatedAndActiveGenre() {
        // Arrange - Given
        final var genre = Genre.newGenre("series", false);

        final var expectedId = genre.getId();
        final var expectedCreatedAt = genre.getCreatedAt();
        final var expectedUpdatedAt = genre.getUpdatedAt();
        final var expectedDeletedAt = genre.getDeletedAt();
        final var expectedName = "Séries";
        final var expectedIsActive = true;
        final var expectedCategoriesCount = 1;
        final var expectedCategories = List.of(CategoryID.unique());

        assertThatNoException().isThrownBy(() -> genre.validate(new ThrowsValidationHandler()));
        assertThat(genre.isActive()).isFalse();
        assertThat(expectedCreatedAt).isNotNull();
        assertThat(expectedUpdatedAt).isNotNull();
        assertThat(expectedDeletedAt).isNotNull();
        assertThat(genre.getCategories()).hasSize(0);

        // When - Then
        final var actualGenre = genre.update(expectedName, expectedIsActive, expectedCategories);

        // Assert - Then
        assertThat(actualGenre)
            .isNotNull()
            .isEqualTo(genre);

        assertThat(actualGenre.getId()).isEqualTo(expectedId);
        assertThat(actualGenre.getCreatedAt()).isEqualTo(expectedCreatedAt);
        assertThat(actualGenre.getUpdatedAt()).isAfter(expectedUpdatedAt);
        assertThat(actualGenre.getDeletedAt()).isNull();
        assertThat(actualGenre.getName()).isEqualTo(expectedName);
        assertThat(actualGenre.isActive()).isTrue();
        assertThat(actualGenre.getCategories())
            .hasSize(expectedCategoriesCount)
            .containsExactlyElementsOf(expectedCategories);
    }

    @Test
    void givenAValidGenre_whenCallsUpdateWithNullName_thenThrowsNotificationException() {
        // Arrange - Given
        final var genre = Genre.newGenre("Filmes", true);
        final var expectedCategories = List.of(CategoryID.unique());

        final var expectedMessage = Genre.GENRE_VALIDATION_ERROR_MESSAGE;
        final var expectedDetailedMessage = GenreValidator.NULL_NAME_ERROR;

        assertThatNoException().isThrownBy(() -> genre.validate(new ThrowsValidationHandler()));
        assertThat(genre.isActive()).isTrue();

        // When - Then
        final var actualException = catchException(() -> genre.update(
            null,
            true,
            expectedCategories
        ));

        // Assert - Then
        assertThat(actualException)
            .isNotNull()
            .isInstanceOf(NotificationException.class)
            .hasMessage(expectedMessage)
            .hasNoCause();

        final NotificationException actualNotificationException = (NotificationException) actualException;
        final List<Error> errors = actualNotificationException.getErrors();
        assertThat(errors).hasSize(1);
        assertThat(errors.getFirst().message()).isEqualTo(expectedDetailedMessage);
    }

    @Test
    void givenAValidGenre_whenCallsUpdateWithEmptyName_thenThrowsNotificationException() {
        // Arrange - Given
        final var genre = Genre.newGenre("Filmes", true);
        final var expectedCategories = List.of(CategoryID.unique());

        final var expectedMessage = Genre.GENRE_VALIDATION_ERROR_MESSAGE;
        final var expectedDetailedMessage = GenreValidator.EMPTY_NAME_ERROR;

        assertThatNoException().isThrownBy(() -> genre.validate(new ThrowsValidationHandler()));
        assertThat(genre.isActive()).isTrue();

        // When - Then
        final var actualException = catchException(() -> genre.update(
            "",
            true,
            expectedCategories
        ));

        // Assert - Then
        assertThat(actualException)
            .isNotNull()
            .isInstanceOf(NotificationException.class)
            .hasMessage(expectedMessage)
            .hasNoCause();

        final NotificationException actualNotificationException = (NotificationException) actualException;
        final List<Error> errors = actualNotificationException.getErrors();
        assertThat(errors).hasSize(1);
        assertThat(errors.getFirst().message()).isEqualTo(expectedDetailedMessage);
    }
}


