package br.com.josenaldo.codeflix.catalog.infrastructure.category.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import br.com.josenaldo.codeflix.catalog.annotations.MySQLGatewayTest;
import br.com.josenaldo.codeflix.catalog.domain.category.Category;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

@MySQLGatewayTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void givenAnInvalidNullName_whenCallsSave_thenShouldReturnDataIntegrityViolationException() {
        // Arrange - Given
        final var expectedPropertyName = "name";
        final var expectedErrorMessage = "not-null property references a null or transient value: br.com.josenaldo.codeflix.catalog.infrastructure.category.persistence.CategoryJpaEntity.name";
        final var category = Category.newCategory("Filmes", null, true);
        final var entity = CategoryJpaEntity.from(category);
        entity.setName(null);

        // Act - When
        Exception exception = catchException(() -> categoryRepository.save(entity));

        // Assert - Then
        assertThat(exception).isNotNull();
        assertThat(exception).isInstanceOf(DataIntegrityViolationException.class);
        assertThat(exception.getCause()).isInstanceOf(PropertyValueException.class);

        final var cause = (PropertyValueException) exception.getCause();
        assertThat(cause.getPropertyName()).isEqualTo(expectedPropertyName);
        assertThat(cause.getMessage()).isEqualTo(expectedErrorMessage);
    }

    @Test
    public void givenAnInvalidNullCreatedAt_whenCallsSave_thenShouldReturnDataIntegrityViolationException() {
        // Arrange - Given
        final var expectedPropertyName = "createdAt";
        final var expectedErrorMessage = "not-null property references a null or transient value: br.com.josenaldo.codeflix.catalog.infrastructure.category.persistence.CategoryJpaEntity.createdAt";
        final var category = Category.newCategory("Filmes", null, true);
        final var entity = CategoryJpaEntity.from(category);
        entity.setCreatedAt(null);

        // Act - When
        Exception exception = catchException(() -> categoryRepository.save(entity));

        // Assert - Then
        assertThat(exception).isNotNull();
        assertThat(exception).isInstanceOf(DataIntegrityViolationException.class);
        assertThat(exception.getCause()).isInstanceOf(PropertyValueException.class);

        final var cause = (PropertyValueException) exception.getCause();
        assertThat(cause.getPropertyName()).isEqualTo(expectedPropertyName);
        assertThat(cause.getMessage()).isEqualTo(expectedErrorMessage);

    }

    @Test
    public void givenAnInvalidNullUpdatedAt_whenCallsSave_thenShouldReturnDataIntegrityViolationException() {
        // Arrange - Given
        final var expectedPropertyName = "updatedAt";
        final var expectedErrorMessage = "not-null property references a null or transient value: br.com.josenaldo.codeflix.catalog.infrastructure.category.persistence.CategoryJpaEntity.updatedAt";
        final var category = Category.newCategory("Filmes", null, true);
        final var entity = CategoryJpaEntity.from(category);
        entity.setUpdatedAt(null);

        // Act - When
        Exception exception = catchException(() -> categoryRepository.save(entity));

        // Assert - Then
        assertThat(exception).isNotNull();
        assertThat(exception).isInstanceOf(DataIntegrityViolationException.class);
        assertThat(exception.getCause()).isInstanceOf(PropertyValueException.class);

        final var cause = (PropertyValueException) exception.getCause();
        assertThat(cause.getPropertyName()).isEqualTo(expectedPropertyName);
        assertThat(cause.getMessage()).isEqualTo(expectedErrorMessage);

    }
}
