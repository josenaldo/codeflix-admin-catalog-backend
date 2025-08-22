package br.com.josenaldo.codeflix.catalog.domain.genre;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.catchException;

import br.com.josenaldo.codeflix.catalog.domain.exceptions.DomainException;
import br.com.josenaldo.codeflix.catalog.domain.exceptions.NotificationException;
import br.com.josenaldo.codeflix.catalog.domain.validation.Error;
import br.com.josenaldo.codeflix.catalog.domain.validation.handler.ThrowsValidationHandler;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;

public class GenreTest {

    @Test
    void givenValidParams_whenCallNewGenre_thenNewInstanceIsCreated() {
        // Arrange - Given
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = 0;

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
        assertThat(actualGenre.getCategories()).hasSize(expectedCategories);
    }

    @Test
    void givenAnInvalidNullName_whenCallsValidate_thenThrowNotificationException() {
        // Arrange - Given
        final var expectedMessage = Genre.GENRE_VALIDATION_ERROR_MESSAGE;
        final var expectedDetailedMessage = "'name' should not be null";
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
    void givenAnInvalidEmptyName_whenCallsNewGender_thenThrowNotificationException() {
        // Arrange - Given
        final var expectedMessage = Genre.GENRE_VALIDATION_ERROR_MESSAGE;
        final var expectedDetailedMessage = "'name' should not be empty";
        final var expectedErrorCount = 1;

        // Act - When
        final var actualException = catchException(() -> Genre.newGenre("", true));

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
    void givenAnInvalidNameWithMoreThan255Characters_whenCreateAGenre_thenThrowDomainException() {
        // Arrange - Given
        final var expectedMessage = Genre.GENRE_VALIDATION_ERROR_MESSAGE;
        final var expectedDetailedMessage = "'name' length must be between 1 and 255 characters";
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

        final NotificationException actualNotificationException = (NotificationException) actualException;
        final var errors = actualNotificationException.getErrors();
        assertThat(errors).hasSize(expectedErrorCount);
        assertThat(errors.getFirst().message()).isEqualTo(expectedDetailedMessage);
    }

    @Test
    void givenAValidFalseIsActive_whenCallsValidate_thenNotThrowDomainException() {
        // Arrange - Given
        final var expectedName = "Ação";
        var genre = Genre.newGenre(expectedName, false);

        // When - Then
        assertThatNoException().isThrownBy(() -> genre.validate(new ThrowsValidationHandler()));

        assertThat(genre).isNotNull();
        assertThat(genre.getId()).isNotNull();
        assertThat(genre.getName()).isEqualTo(expectedName);
        assertThat(genre.isActive()).isFalse();
        assertThat(genre.getCreatedAt()).isNotNull();
        assertThat(genre.getUpdatedAt()).isNotNull();
        assertThat(genre.getDeletedAt()).isNotNull();
    }

    @Test
    void givenAValidActiveGenre_whenCallsDeactivate_thenReturnGenreInactive() {
        // Arrange - Given
        String expectedName = "Filmes";
        final var genre = Genre.newGenre(expectedName, true);
        final Instant updatedAt = genre.getUpdatedAt();

        assertThatNoException().isThrownBy(() -> genre.validate(new ThrowsValidationHandler()));
        assertThat(genre.isActive()).isTrue();
        assertThat(genre.getDeletedAt()).isNull();

        // When - Then
        final Genre actualGenre = genre.deactivate();

        // Assert - Then
        assertThat(actualGenre).isNotNull();
        assertThat(actualGenre.getId()).isEqualTo(genre.getId());
        assertThat(actualGenre.getName()).isEqualTo(genre.getName());

        assertThat(actualGenre.isActive()).isFalse();
        assertThat(actualGenre.getCreatedAt()).isEqualTo(genre.getCreatedAt());
        assertThat(actualGenre.getUpdatedAt()).isAfterOrEqualTo(updatedAt);
        assertThat(actualGenre.getDeletedAt()).isNotNull();
    }

    @Test
    void givenAValidDeactivatedGenre_whenCallsActivate_thenReturnGenreActive() {
        // Arrange - Given
        String expectedName = "Filmes";
        final var genre = Genre.newGenre(expectedName, false);
        final Instant updatedAt = genre.getUpdatedAt();

        assertThatNoException().isThrownBy(() -> genre.validate(new ThrowsValidationHandler()));
        assertThat(genre.isActive()).isFalse();
        assertThat(genre.getDeletedAt()).isNotNull();

        // When - Then
        final Genre actualGenre = genre.activate();

        // Assert - Then
        assertThat(actualGenre).isNotNull();
        assertThat(actualGenre.getId()).isEqualTo(genre.getId());
        assertThat(actualGenre.getName()).isEqualTo(expectedName);

        assertThat(actualGenre.isActive()).isTrue();
        assertThat(actualGenre.getCreatedAt()).isEqualTo(genre.getCreatedAt());
        assertThat(actualGenre.getUpdatedAt()).isAfterOrEqualTo(updatedAt);
        assertThat(actualGenre.getDeletedAt()).isNull();
    }

    @Test
    void givenAValidGenre_whenCallsUpdate_thenReturnGenreUpdated() {
        // Arrange - Given
        final var genre = Genre.newGenre("Filmes", true);
        final Instant createdAt = genre.getCreatedAt();
        final Instant updatedAt = genre.getUpdatedAt();
        final Instant deletedAt = genre.getDeletedAt();

        assertThatNoException().isThrownBy(() -> genre.validate(new ThrowsValidationHandler()));
        assertThat(genre.isActive()).isTrue();
        assertThat(createdAt).isNotNull();
        assertThat(updatedAt).isNotNull();
        assertThat(deletedAt).isNull();

        // When - Then
        final Genre actualGenre = genre.update("Séries", true);

        // Assert - Then
        assertThat(actualGenre).isNotNull();
        assertThat(actualGenre.getId()).isEqualTo(genre.getId());
        assertThat(actualGenre.getName()).isEqualTo("Séries");

        assertThat(actualGenre.isActive()).isTrue();
        assertThat(actualGenre.getCreatedAt()).isEqualTo(createdAt);
        assertThat(actualGenre.getUpdatedAt()).isAfterOrEqualTo(updatedAt);
        assertThat(actualGenre.getDeletedAt()).isNull();
    }

    @Test
    void givenAValidGenre_whenCallsUpdateWithInactive_thenReturnGenreUpdatedAndDeactivates() {
        // Arrange - Given
        final var genre = Genre.newGenre("Filmes", true);
        final Instant createdAt = genre.getCreatedAt();
        final Instant updatedAt = genre.getUpdatedAt();
        final Instant deletedAt = genre.getDeletedAt();

        assertThatNoException().isThrownBy(() -> genre.validate(new ThrowsValidationHandler()));
        assertThat(genre.isActive()).isTrue();
        assertThat(createdAt).isNotNull();
        assertThat(updatedAt).isNotNull();
        assertThat(deletedAt).isNull();

        // When - Then
        final Genre actualGenre = genre.update("Séries", false);

        // Assert - Then
        assertThat(actualGenre).isNotNull();
        assertThat(actualGenre.getId()).isEqualTo(genre.getId());
        assertThat(actualGenre.getName()).isEqualTo("Séries");

        assertThat(actualGenre.isActive()).isFalse();

        assertThat(actualGenre.getCreatedAt()).isEqualTo(createdAt);
        assertThat(actualGenre.getUpdatedAt()).isAfterOrEqualTo(updatedAt);
        assertThat(actualGenre.getDeletedAt()).isNotNull();
    }

    @Test
    void givenAValidGenre_whenCallsUpdateWithInvalidName_thenItShouldThrowDomainExceptionOnValidate() {
        // Arrange - Given
        final var genre = Genre.newGenre("Filmes", true);
        final Instant updatedAt = genre.getUpdatedAt();

        assertThatNoException().isThrownBy(() -> genre.validate(new ThrowsValidationHandler()));
        assertThat(genre.isActive()).isTrue();

        // When - Then
        final Genre actualGenre = genre.update(null, true);

        final var actualException = catchException(() -> actualGenre.validate(new ThrowsValidationHandler()));

        // Assert - Then
        String expectedMessage = "'name' should not be null";

        assertThat(actualGenre.getName()).isNull();
        assertThat(actualGenre.isActive()).isTrue();
        assertThat(actualGenre.getCreatedAt()).isNotNull();
        assertThat(actualGenre.getUpdatedAt()).isAfter(updatedAt);
        assertThat(actualGenre.getDeletedAt()).isNull();

        assertThat(actualException)
            .isNotNull()
            .isInstanceOf(DomainException.class)
            .hasMessage(expectedMessage)
            .hasNoCause();

        final DomainException actualDomainException = (DomainException) actualException;
        final List<Error> errors = actualDomainException.getErrors();
        assertThat(errors).hasSize(1);
        assertThat(errors.getFirst().message()).isEqualTo(expectedMessage);
    }
}


