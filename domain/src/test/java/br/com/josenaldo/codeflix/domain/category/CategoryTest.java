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
    final var expectedName = "Filmes";
    final var expectedDescription = "A categoria mais assistida";
    final var expectedIsActive = true;

    final var actualCategory = Category.newCategory(
        expectedName,
        expectedDescription,
        expectedIsActive
    );

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
  public void givenAnInvalidNullName_whenCallNewCategoryAndValidate_thenThrowIllegalArgumentException() {
    // Given
    final var expectedDescription = "A categoria mais assistida";
    final var expectedIsActive = true;

    // When
    var category = Category.newCategory(
        null,
        expectedDescription,
        expectedIsActive
    );

    final var actualException = catchException(() -> category.validate(new ThrowsValidationHandler()));

    // Then
    assertThat(actualException).isNotNull();
    assertThat(actualException).isInstanceOf(DomainException.class);
    assertThat(actualException).hasMessage("");
    assertThat(actualException).hasNoCause();

    final DomainException actualDomainException = (DomainException) actualException;
    final List<Error> errors = actualDomainException.getErrors();

    assertThat(errors).hasSize(1);
    assertThat(errors.getFirst().message()).isEqualTo("'name' should not be empty");
  }
}
