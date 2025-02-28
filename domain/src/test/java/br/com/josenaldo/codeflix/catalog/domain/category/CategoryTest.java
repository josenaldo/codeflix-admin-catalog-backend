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
    void givenAnInvalidNullName_whenCallNewCategoryAndValidate_thenShouldThrowDomainException() {
        // Given
        var category = Category.newCategory(null, "A categoria mais assistida", true);

        // When
        final var actualException = catchException(() -> category.validate(new ThrowsValidationHandler()));

        // Then
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
    void givenAnInvalidEmptyName_whenCallNewCategoryAndValidate_thenShouldThrowDomainException() {
        // Given
        var category = Category.newCategory("    ", "A categoria mais assistida", true);

        // When
        final var actualException = catchException(() -> category.validate(new ThrowsValidationHandler()));

        // Then
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
    void givenAnInvalidNameLengthLessThan3_whenCallNewCategoryAndValidate_thenShouldThrowDomainException() {
        // Given
        var category = Category.newCategory("Fi    ", "A categoria mais assistida", true);

        // When
        final var actualException = catchException(() -> category.validate(new ThrowsValidationHandler()));

        // Then
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
    void givenAnInvalidNameLengthMoreThan255_whenCallNewCategoryAndValidate_thenShouldThrowDomainException() {
        // Given
        var category = Category.newCategory("F".repeat(256), "A categoria mais assistida", true);

        // When
        final var actualException = catchException(() -> category.validate(new ThrowsValidationHandler()));

        // Then
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
    void givenAValidEmptyDescription_whenCallNewCategoryAndValidade_thenShouldNotThrowDomainException() {
        // Given
        var category = Category.newCategory("Filmes", "", true);

        // When
        final var actualException = catchException(() -> category.validate(new ThrowsValidationHandler()));

        // Then
        assertThat(actualException).isNull();
    }

    @Test
    void givenAValidFalseIsActive_whenCallNewCategoryAndValidade_thenShouldNotThrowDomainException() {
        // Given
        var category = Category.newCategory("Filmes", "A categoria mais assistida", false);

        // When - Then
        assertThatNoException().isThrownBy(() -> category.validate(new ThrowsValidationHandler()));

        assertThat(category).isNotNull();
        assertThat(category.getId()).isNotNull();
        assertThat(category.getName()).isEqualTo("Filmes");
        assertThat(category.getDescription()).isEqualTo("A categoria mais assistida");
        assertThat(category.isActive()).isFalse();
        assertThat(category.getCreatedAt()).isNotNull();
        assertThat(category.getUpdatedAt()).isNotNull();
        assertThat(category.getDeletedAt()).isNotNull();

    }

    @Test
    void givenAValidActiveCategory_whenCallDeactivate_thenReturnCategoryInactive() {
        // Given
        final var category = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final Instant updatedAt = category.getUpdatedAt();

        assertThatNoException().isThrownBy(() -> category.validate(new ThrowsValidationHandler()));
        assertThat(category.isActive()).isTrue();
        assertThat(category.getDeletedAt()).isNull();

        // When
        final Category actualCategory = category.deactivate();

        // Then
        assertThat(actualCategory).isNotNull();
        assertThat(actualCategory.getId()).isEqualTo(category.getId());
        assertThat(actualCategory.getName()).isEqualTo(category.getName());
        assertThat(actualCategory.getDescription()).isEqualTo(category.getDescription());

        assertThat(actualCategory.isActive()).isFalse();
        assertThat(actualCategory.getCreatedAt()).isEqualTo(category.getCreatedAt());
        assertThat(actualCategory.getUpdatedAt()).isAfter(updatedAt);
        assertThat(actualCategory.getDeletedAt()).isNotNull();
    }

    @Test
    void givenAValidDeactivatedCategory_whenCallActivate_thenReturnCategoryActive() {
        // Given
        final var category = Category.newCategory("Filmes", "A categoria mais assistida", false);
        final Instant updatedAt = category.getUpdatedAt();

        assertThatNoException().isThrownBy(() -> category.validate(new ThrowsValidationHandler()));
        assertThat(category.isActive()).isFalse();
        assertThat(category.getDeletedAt()).isNotNull();

        // When
        final Category actualCategory = category.activate();

        // Then
        assertThat(actualCategory).isNotNull();
        assertThat(actualCategory.getId()).isEqualTo(category.getId());
        assertThat(actualCategory.getName()).isEqualTo(category.getName());
        assertThat(actualCategory.getDescription()).isEqualTo(category.getDescription());

        assertThat(actualCategory.isActive()).isTrue();
        assertThat(actualCategory.getCreatedAt()).isEqualTo(category.getCreatedAt());
        assertThat(actualCategory.getUpdatedAt()).isAfter(updatedAt);
        assertThat(actualCategory.getDeletedAt()).isNull();
    }

    @Test
    void givenAValidCategory_whenCallUpdate_thenReturnCategoryUpdated() {
        // Given
        final var category = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final Instant createdAt = category.getCreatedAt();
        final Instant updatedAt = category.getUpdatedAt();
        final Instant deletedAt = category.getDeletedAt();

        assertThatNoException().isThrownBy(() -> category.validate(new ThrowsValidationHandler()));
        assertThat(category.isActive()).isTrue();
        assertThat(createdAt).isNotNull();
        assertThat(updatedAt).isNotNull();
        assertThat(deletedAt).isNull();

        // When
        final Category actualCategory = category.update(
            "Séries",
            "A categoria menos assistida",
            true
        );

        // Then
        assertThat(actualCategory).isNotNull();
        assertThat(actualCategory.getId()).isEqualTo(category.getId());
        assertThat(actualCategory.getName()).isEqualTo("Séries");
        assertThat(actualCategory.getDescription()).isEqualTo("A categoria menos assistida");

        assertThat(actualCategory.isActive()).isTrue();
        assertThat(actualCategory.getCreatedAt()).isEqualTo(createdAt);
        assertThat(actualCategory.getUpdatedAt()).isAfter(updatedAt);
        assertThat(actualCategory.getDeletedAt()).isNull();
    }

    @Test
    void givenAValidCategory_whenCallUpdateWithInactive_thenReturnCategoryUpdatedAndDeactivates() {
        // Given
        final var category = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final Instant createdAt = category.getCreatedAt();
        final Instant updatedAt = category.getUpdatedAt();
        final Instant deletedAt = category.getDeletedAt();

        assertThatNoException().isThrownBy(() -> category.validate(new ThrowsValidationHandler()));
        assertThat(category.isActive()).isTrue();
        assertThat(createdAt).isNotNull();
        assertThat(updatedAt).isNotNull();
        assertThat(deletedAt).isNull();

        // When
        final Category actualCategory = category.update(
            "Séries",
            "A categoria menos assistida",
            false
        );

        // Then
        assertThat(actualCategory).isNotNull();
        assertThat(actualCategory.getId()).isEqualTo(category.getId());
        assertThat(actualCategory.getName()).isEqualTo("Séries");
        assertThat(actualCategory.getDescription()).isEqualTo("A categoria menos assistida");
        assertThat(actualCategory.isActive()).isFalse();

        assertThat(actualCategory.getCreatedAt()).isEqualTo(createdAt);
        assertThat(actualCategory.getUpdatedAt()).isAfter(updatedAt);
        assertThat(actualCategory.getDeletedAt()).isNotNull();
    }

    @Test
    void givenAValidCategory_whenCallUpdateWithInvalidName_thenItShouldThrowDomainExceptionOnValidate() {
        // Given
        final var category = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final Instant updatedAt = category.getUpdatedAt();

        assertThatNoException().isThrownBy(() -> category.validate(new ThrowsValidationHandler()));
        assertThat(category.isActive()).isTrue();

        // When
        final Category actualCategory = category.update(null, "A categoria menos assistida", true);

        final var actualException = catchException(() -> actualCategory.validate(new ThrowsValidationHandler()));

        // Then
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
