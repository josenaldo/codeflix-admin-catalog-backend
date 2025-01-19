package br.com.josenaldo.codeflix.domain.category;

import static org.assertj.core.api.Assertions.assertThat;

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
}
