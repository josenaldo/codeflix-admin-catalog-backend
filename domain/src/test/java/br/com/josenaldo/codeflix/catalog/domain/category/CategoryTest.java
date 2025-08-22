package br.com.josenaldo.codeflix.catalog.domain.category;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.catchException;

import br.com.josenaldo.codeflix.catalog.domain.exceptions.DomainException;
import br.com.josenaldo.codeflix.catalog.domain.validation.Error;
import br.com.josenaldo.codeflix.catalog.domain.validation.handler.ThrowsValidationHandler;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;

class CategoryTest {

    @Test
    void givenAValidParams_whenCallNewCategory_thenNewInstanceIsCreated() {
        // Arrange - Given
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        // Act - When
        final var actualCategory = Category.newCategory(
            expectedName,
            expectedDescription,
            expectedIsActive
        );

        // Assert - Then
        assertThat(actualCategory).isNotNull();
        assertThat(actualCategory.getId()).isNotNull();
        assertThat(actualCategory.getName()).isEqualTo(expectedName);
        assertThat(actualCategory.getDescription()).isEqualTo(expectedDescription);
        assertThat(actualCategory.isActive()).isEqualTo(expectedIsActive);
        assertThat(actualCategory.getCreatedAt()).isNotNull();
        assertThat(actualCategory.getUpdatedAt()).isNotNull();
        assertThat(actualCategory.getDeletedAt()).isNull();
    }

    @Test
    void givenAnInvalidNullName_whenCallsValidate_thenThrowDomainException() {
        // Arrange - Given
        var category = Category.newCategory(null, "A categoria mais assistida", true);

        // Act - When
        final var actualException = catchException(() -> category.validate(new ThrowsValidationHandler()));

        // Assert - Then
        String expectedMessage = "'name' should not be null";

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

    @Test
    void givenAanInvalidEmptyName_whenCallsValidate_thenThrowDomainException() {
        // Arrange - Given
        var category = Category.newCategory("    ", "A categoria mais assistida", true);

        // When - Then
        final var actualException = catchException(() -> category.validate(new ThrowsValidationHandler()));

        // Assert - Then
        String expectedMessage = "'name' should not be empty";

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

    @Test
    void givenAnInvalidNameLengthLessThan3_whenCallsValidate_thenThrowDomainException() {
        // Arrange - Given
        var category = Category.newCategory("Fi    ", "A categoria mais assistida", true);

        // When - Then
        final var actualException = catchException(() -> category.validate(new ThrowsValidationHandler()));

        // Assert - Then
        String expectedMessage = "'name' length must be between 3 and 255 characters";

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

    @Test
    void givenAnInvalidNameLengthMoreThan255_whenCallsValidate_thenThrowDomainException() {
        // Arrange - Given
        var category = Category.newCategory("F".repeat(256), "A categoria mais assistida", true);

        // When - Then
        final var actualException = catchException(() -> category.validate(new ThrowsValidationHandler()));

        // Assert - Then
        String expectedMessage = "'name' length must be between 3 and 255 characters";

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

    @Test
    void givenAValidEmptyDescription_whenCallsValidate_thenNotThrowDomainException() {
        // Arrange - Given
        var category = Category.newCategory("Filmes", "", true);

        // When - Then
        final var actualException = catchException(() -> category.validate(new ThrowsValidationHandler()));

        // Assert - Then
        assertThat(actualException).isNull();
    }

    @Test
    void givenAValidFalseIsActive_whenCallsValidate_thenNotThrowDomainException() {
        // Arrange - Given
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        var category = Category.newCategory(expectedName, expectedDescription, false);

        // When - Then
        assertThatNoException().isThrownBy(() -> category.validate(new ThrowsValidationHandler()));

        assertThat(category).isNotNull();
        assertThat(category.getId()).isNotNull();
        assertThat(category.getName()).isEqualTo(expectedName);
        assertThat(category.getDescription()).isEqualTo(expectedDescription);
        assertThat(category.isActive()).isFalse();
        assertThat(category.getCreatedAt()).isNotNull();
        assertThat(category.getUpdatedAt()).isNotNull();
        assertThat(category.getDeletedAt()).isNotNull();

    }

    @Test
    void givenAValidActiveCategory_whenCallsDeactivate_thenReturnCategoryInactive() {
        // Arrange - Given
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var category = Category.newCategory(expectedName, expectedDescription, true);
        final Instant updatedAt = category.getUpdatedAt();

        assertThatNoException().isThrownBy(() -> category.validate(new ThrowsValidationHandler()));
        assertThat(category.isActive()).isTrue();
        assertThat(category.getDeletedAt()).isNull();

        // When - Then
        final Category actualCategory = category.deactivate();

        // Assert - Then
        assertThat(actualCategory).isNotNull();
        assertThat(actualCategory.getId()).isEqualTo(category.getId());
        assertThat(actualCategory.getName()).isEqualTo(expectedName);
        assertThat(actualCategory.getDescription()).isEqualTo(expectedDescription);

        assertThat(actualCategory.isActive()).isFalse();
        assertThat(actualCategory.getCreatedAt()).isEqualTo(category.getCreatedAt());
        assertThat(actualCategory.getUpdatedAt()).isAfterOrEqualTo(updatedAt);
        assertThat(actualCategory.getDeletedAt()).isNotNull();
    }

    @Test
    void givenAValidDeactivatedCategory_whenCallsActivate_thenReturnCategoryActive() {
        // Arrange - Given
        final var category = Category.newCategory("Filmes", "A categoria mais assistida", false);
        final Instant updatedAt = category.getUpdatedAt();

        assertThatNoException().isThrownBy(() -> category.validate(new ThrowsValidationHandler()));
        assertThat(category.isActive()).isFalse();
        assertThat(category.getDeletedAt()).isNotNull();

        // When - Then
        final Category actualCategory = category.activate();

        // Assert - Then
        assertThat(actualCategory).isNotNull();
        assertThat(actualCategory.getId()).isEqualTo(category.getId());
        assertThat(actualCategory.getName()).isEqualTo(category.getName());
        assertThat(actualCategory.getDescription()).isEqualTo(category.getDescription());

        assertThat(actualCategory.isActive()).isTrue();
        assertThat(actualCategory.getCreatedAt()).isEqualTo(category.getCreatedAt());
        assertThat(actualCategory.getUpdatedAt()).isAfterOrEqualTo(updatedAt);
        assertThat(actualCategory.getDeletedAt()).isNull();
    }

    @Test
    void givenAValidCategory_whenCallsUpdate_thenReturnCategoryUpdated() {
        // Arrange - Given
        final var category = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final Instant createdAt = category.getCreatedAt();
        final Instant updatedAt = category.getUpdatedAt();
        final Instant deletedAt = category.getDeletedAt();

        assertThatNoException().isThrownBy(() -> category.validate(new ThrowsValidationHandler()));
        assertThat(category.isActive()).isTrue();
        assertThat(createdAt).isNotNull();
        assertThat(updatedAt).isNotNull();
        assertThat(deletedAt).isNull();

        // When - Then
        final Category actualCategory = category.update(
            "Séries",
            "A categoria menos assistida",
            true
        );

        // Assert - Then
        assertThat(actualCategory).isNotNull();
        assertThat(actualCategory.getId()).isEqualTo(category.getId());
        assertThat(actualCategory.getName()).isEqualTo("Séries");
        assertThat(actualCategory.getDescription()).isEqualTo("A categoria menos assistida");

        assertThat(actualCategory.isActive()).isTrue();
        assertThat(actualCategory.getCreatedAt()).isEqualTo(createdAt);
        assertThat(actualCategory.getUpdatedAt()).isAfterOrEqualTo(updatedAt);
        assertThat(actualCategory.getDeletedAt()).isNull();
    }

    @Test
    void givenAValidCategory_whenCallsUpdateWithInactive_thenReturnCategoryUpdatedAndDeactivates() {
        // Arrange - Given
        final var category = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final Instant createdAt = category.getCreatedAt();
        final Instant updatedAt = category.getUpdatedAt();
        final Instant deletedAt = category.getDeletedAt();

        assertThatNoException().isThrownBy(() -> category.validate(new ThrowsValidationHandler()));
        assertThat(category.isActive()).isTrue();
        assertThat(createdAt).isNotNull();
        assertThat(updatedAt).isNotNull();
        assertThat(deletedAt).isNull();

        // When - Then
        final Category actualCategory = category.update(
            "Séries",
            "A categoria menos assistida",
            false
        );

        // Assert - Then
        assertThat(actualCategory).isNotNull();
        assertThat(actualCategory.getId()).isEqualTo(category.getId());
        assertThat(actualCategory.getName()).isEqualTo("Séries");
        assertThat(actualCategory.getDescription()).isEqualTo("A categoria menos assistida");
        assertThat(actualCategory.isActive()).isFalse();

        assertThat(actualCategory.getCreatedAt()).isEqualTo(createdAt);
        assertThat(actualCategory.getUpdatedAt()).isAfterOrEqualTo(updatedAt);
        assertThat(actualCategory.getDeletedAt()).isNotNull();
    }

    @Test
    void givenAValidCategory_whenCallsUpdateWithInvalidName_thenItShouldThrowDomainExceptionOnValidate() {
        // Arrange - Given
        final var category = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final Instant updatedAt = category.getUpdatedAt();

        assertThatNoException().isThrownBy(() -> category.validate(new ThrowsValidationHandler()));
        assertThat(category.isActive()).isTrue();

        // When - Then
        final Category actualCategory = category.update(null, "A categoria menos assistida", true);

        final var actualException = catchException(() -> actualCategory.validate(new ThrowsValidationHandler()));

        // Assert - Then
        String expectedMessage = "'name' should not be null";

        assertThat(actualCategory.getName()).isNull();
        assertThat(actualCategory.getDescription()).isEqualTo("A categoria menos assistida");
        assertThat(actualCategory.isActive()).isTrue();
        assertThat(actualCategory.getCreatedAt()).isNotNull();
        assertThat(actualCategory.getUpdatedAt()).isAfter(updatedAt);
        assertThat(actualCategory.getDeletedAt()).isNull();

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
