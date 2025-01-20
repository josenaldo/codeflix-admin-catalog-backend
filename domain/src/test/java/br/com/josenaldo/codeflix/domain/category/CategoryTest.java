package br.com.josenaldo.codeflix.domain.category;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import br.com.josenaldo.codeflix.domain.exceptions.DomainException;
import br.com.josenaldo.codeflix.domain.validation.Error;
import br.com.josenaldo.codeflix.domain.validation.handler.ThrowsValidationHandler;
import java.util.List;
import org.junit.jupiter.api.Test;

public class CategoryTest {

  @Test
  public void givenAValidParams_whenCallNewCategory_thenNewInstanceIsCreated() {
    // Given
    final var expectedName = "Filmes";
    final var expectedDescription = "A categoria mais assistida";
    final var expectedIsActive = true;

    // When
    final var actualCategory = Category.newCategory(
        expectedName,
        expectedDescription,
        expectedIsActive
    );

    // Then
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
  public void givenAnInvalidNullName_whenCallNewCategoryAndValidate_thenShouldThrowDomainException() {
    // Given
    var category = Category.newCategory(null, "A categoria mais assistida", true);

    // When
    final var actualException = catchException(() -> category.validate(new ThrowsValidationHandler()));

    // Then
    String expectedMessage = "'name' should not be null";

    assertThat(actualException).isNotNull();
    assertThat(actualException).isInstanceOf(DomainException.class);
    assertThat(actualException).hasMessage(expectedMessage);
    assertThat(actualException).hasNoCause();

    final DomainException actualDomainException = (DomainException) actualException;
    final List<Error> errors = actualDomainException.getErrors();

    assertThat(errors).hasSize(1);
    assertThat(errors.getFirst().message()).isEqualTo(expectedMessage);
  }

  @Test
  public void givenAnInvalidEmptyName_whenCallNewCategoryAndValidate_thenShouldThrowDomainException() {
    // Given
    var category = Category.newCategory("", "A categoria mais assistida", true);

    // When
    final var actualException = catchException(() -> category.validate(new ThrowsValidationHandler()));

    // Then
    String expectedMessage = "'name' should not be empty";

    assertThat(actualException).isNotNull();
    assertThat(actualException).isInstanceOf(DomainException.class);
    assertThat(actualException).hasMessage(expectedMessage);
    assertThat(actualException).hasNoCause();

    final DomainException actualDomainException = (DomainException) actualException;
    final List<Error> errors = actualDomainException.getErrors();
    assertThat(errors).hasSize(1);
    assertThat(errors.getFirst().message()).isEqualTo(expectedMessage);
  }

  @Test
  public void givenAnInvalidNameLengthLessThan3_whenCallNewCategoryAndValidate_thenShouldThrowDomainException() {
    // Given
    var category = Category.newCategory("Fi", "A categoria mais assistida", true);

    // When
    final var actualException = catchException(() -> category.validate(new ThrowsValidationHandler()));

    // Then
    String expectedMessage = "'name' length must be between 3 and 255 characters";

    assertThat(actualException).isNotNull();
    assertThat(actualException).isInstanceOf(DomainException.class);
    assertThat(actualException).hasMessage(expectedMessage);
    assertThat(actualException).hasNoCause();

    final DomainException actualDomainException = (DomainException) actualException;
    final List<Error> errors = actualDomainException.getErrors();
    assertThat(errors).hasSize(1);
    assertThat(errors.getFirst().message()).isEqualTo(expectedMessage);
  }

  @Test
  public void givenAnInvalidNameLengthMoreThan255_whenCallNewCategoryAndValidate_thenShouldThrowDomainException() {
    // Given
    var category = Category.newCategory("F".repeat(256), "A categoria mais assistida", true);

    // When
    final var actualException = catchException(() -> category.validate(new ThrowsValidationHandler()));

    // Then
    String expectedMessage = "'name' length must be between 3 and 255 characters";

    assertThat(actualException).isNotNull();
    assertThat(actualException).isInstanceOf(DomainException.class);
    assertThat(actualException).hasMessage(expectedMessage);
    assertThat(actualException).hasNoCause();

    final DomainException actualDomainException = (DomainException) actualException;
    final List<Error> errors = actualDomainException.getErrors();
    assertThat(errors).hasSize(1);
    assertThat(errors.getFirst().message()).isEqualTo(expectedMessage);
  }
}
