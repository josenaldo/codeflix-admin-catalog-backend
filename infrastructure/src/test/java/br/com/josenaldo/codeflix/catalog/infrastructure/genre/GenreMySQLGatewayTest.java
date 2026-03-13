package br.com.josenaldo.codeflix.catalog.infrastructure.genre;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.josenaldo.codeflix.catalog.annotations.MySQLGatewayTest;
import br.com.josenaldo.codeflix.catalog.domain.category.Category;
import br.com.josenaldo.codeflix.catalog.domain.category.CategoryID;
import br.com.josenaldo.codeflix.catalog.domain.genre.Genre;
import br.com.josenaldo.codeflix.catalog.infrastructure.category.CategoryMySQLGateway;
import br.com.josenaldo.codeflix.catalog.infrastructure.genre.persistence.GenreJpaEntity;
import br.com.josenaldo.codeflix.catalog.infrastructure.genre.persistence.GenreRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@MySQLGatewayTest
@Import(GenreMySQLGateway.class)
class GenreMySQLGatewayTest {

    @Autowired
    private CategoryMySQLGateway categoryGateway;

    @Autowired
    private GenreMySQLGateway genreGateway;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    void testDependenciesInjected() {
        assertThat(categoryGateway).isNotNull();
        assertThat(genreGateway).isNotNull();
        assertThat(genreRepository).isNotNull();
    }

    @Test
    void givenAValidGenreWithCategory_whenCallsCreateGenre_shouldPersistGenre() {

        // Arrange - Given
        final var filmes = categoryGateway.create(Category.newCategory("Filmes", null, true));
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(filmes.getId());

        final var aGenre = Genre.newGenre(expectedName, expectedIsActive);
        aGenre.addCategories(expectedCategories);
        final var expectedId = aGenre.getId();

        assertThat(genreRepository.count()).isZero();

        // Act - When
        final var actualGenre = genreGateway.create(aGenre);

        // Assert - Then
        assertThat(genreRepository.count()).isEqualTo(1);

        assertThat(actualGenre.getId()).isNotNull();
        assertThat(actualGenre.getId()).isEqualTo(expectedId);
        assertThat(actualGenre.getCreatedAt()).isNotNull();
        assertThat(actualGenre.getUpdatedAt()).isNotNull();
        assertThat(actualGenre.getDeletedAt()).isNull();
        assertThat(actualGenre.getName()).isEqualTo(expectedName);
        assertThat(actualGenre.isActive()).isEqualTo(expectedIsActive);
        assertThat(actualGenre.getCategories()).hasSize(1);
        assertThat(actualGenre.getCategories().getFirst()).isEqualTo(filmes.getId());

        var persistedGenre = genreRepository.findById(expectedId.getValue()).get();
        assertThat(persistedGenre.getId()).isEqualTo(expectedId.getValue());
        assertThat(persistedGenre.getCreatedAt()).isNotNull();
        assertThat(persistedGenre.getUpdatedAt()).isNotNull();
        assertThat(persistedGenre.getDeletedAt()).isNull();
        assertThat(persistedGenre.getName()).isEqualTo(expectedName);
        assertThat(persistedGenre.isActive()).isEqualTo(expectedIsActive);
        var categoryIDS = persistedGenre.getCategoryIDS();
        assertThat(categoryIDS).hasSize(1).isEqualTo(expectedCategories);

    }

    @Test
    void givenAValidGenreWithoutCategories_whenCallsCreateGenre_shouldPersistGenre() {

        // Arrange - Given
        final var expectedName = "Ação";
        final var expectedIsActive = true;

        final var aGenre = Genre.newGenre(expectedName, expectedIsActive);
        final var expectedId = aGenre.getId();

        assertThat(genreRepository.count()).isZero();

        // Act - When
        final var actualGenre = genreGateway.create(aGenre);

        // Assert - Then
        assertThat(genreRepository.count()).isEqualTo(1);

        assertThat(actualGenre.getId()).isNotNull();
        assertThat(actualGenre.getId()).isEqualTo(expectedId);
        assertThat(actualGenre.getCreatedAt()).isNotNull();
        assertThat(actualGenre.getUpdatedAt()).isNotNull();
        assertThat(actualGenre.getDeletedAt()).isNull();
        assertThat(actualGenre.getName()).isEqualTo(expectedName);
        assertThat(actualGenre.isActive()).isEqualTo(expectedIsActive);
        assertThat(actualGenre.getCategories()).isEmpty();

        var persistedGenre = genreRepository.findById(expectedId.getValue()).get();
        assertThat(persistedGenre.getId()).isEqualTo(expectedId.getValue());
        assertThat(persistedGenre.getCreatedAt()).isNotNull();
        assertThat(persistedGenre.getUpdatedAt()).isNotNull();
        assertThat(persistedGenre.getDeletedAt()).isNull();
        assertThat(persistedGenre.getName()).isEqualTo(expectedName);
        assertThat(persistedGenre.isActive()).isEqualTo(expectedIsActive);
        var categoryIDS = persistedGenre.getCategoryIDS();
        assertThat(categoryIDS).isEmpty();

    }

    @Test
    void givenAValidGenreWithoutCategories_whenCallsUpdateGenreWithCategories_shouldPersistGenre() {

        // Arrange - Given
        final var filmes = categoryGateway.create(Category.newCategory("Filmes", null, true));
        final var series = categoryGateway.create(Category.newCategory("Séries", null, true));

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of(filmes.getId(), series.getId());

        final var aGenre = Genre.newGenre("ac", expectedIsActive);
        final var expectedId = aGenre.getId();

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));
        assertThat(genreRepository.count()).isEqualTo(1);
        assertThat(aGenre.getName()).isEqualTo("ac");
        assertThat(aGenre.getCategories()).isEmpty();

        // Act - When
        final var actualGenre = genreGateway.update(
            Genre.with(aGenre).update(
                expectedName,
                expectedIsActive,
                expectedCategories
            )
        );

        // Assert - Then
        assertThat(genreRepository.count()).isEqualTo(1);

        assertThat(actualGenre.getId()).isNotNull();
        assertThat(actualGenre.getId()).isEqualTo(expectedId);
        assertThat(actualGenre.getCreatedAt()).isNotNull();
        assertThat(actualGenre.getUpdatedAt()).isNotNull();
        assertThat(actualGenre.getUpdatedAt()).isAfterOrEqualTo(aGenre.getUpdatedAt());
        assertThat(actualGenre.getDeletedAt()).isNull();
        assertThat(actualGenre.getName()).isEqualTo(expectedName);
        assertThat(actualGenre.isActive()).isEqualTo(expectedIsActive);
        assertThat(actualGenre.getCategories()).hasSize(2);
        assertThat(actualGenre.getCategories()).containsExactlyInAnyOrderElementsOf(
            expectedCategories);

        var persistedGenre = genreRepository.findById(expectedId.getValue()).get();
        assertThat(persistedGenre.getId()).isEqualTo(expectedId.getValue());
        assertThat(persistedGenre.getCreatedAt()).isNotNull();
        assertThat(persistedGenre.getUpdatedAt()).isAfter(aGenre.getUpdatedAt());
        assertThat(persistedGenre.getDeletedAt()).isNull();
        assertThat(persistedGenre.getName()).isEqualTo(expectedName);
        assertThat(persistedGenre.isActive()).isEqualTo(expectedIsActive);
        var categoryIDS = persistedGenre.getCategoryIDS();
        assertThat(categoryIDS).hasSize(2).containsExactlyInAnyOrderElementsOf(expectedCategories);
    }

    @Test
    void givenAValidGenreWithCategories_whenCallsUpdateGenreCleaningCategories_shouldPersistGenre() {

        // Arrange - Given
        final var filmes = categoryGateway.create(Category.newCategory("Filmes", null, true));
        final var series = categoryGateway.create(Category.newCategory("Séries", null, true));

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aGenre = Genre.newGenre("ac", expectedIsActive);
        aGenre.addCategories(List.of(filmes.getId(), series.getId()));
        final var expectedId = aGenre.getId();

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));
        assertThat(genreRepository.count()).isEqualTo(1);
        assertThat(aGenre.getName()).isEqualTo("ac");
        assertThat(aGenre.getCategories()).hasSize(2);

        // Act - When
        final var actualGenre = genreGateway.update(
            Genre.with(aGenre).update(
                expectedName,
                expectedIsActive,
                expectedCategories
            )
        );

        // Assert - Then
        assertThat(genreRepository.count()).isEqualTo(1);

        assertThat(actualGenre.getId()).isNotNull();
        assertThat(actualGenre.getId()).isEqualTo(expectedId);
        assertThat(actualGenre.getCreatedAt()).isNotNull();
        assertThat(actualGenre.getUpdatedAt()).isNotNull();
        assertThat(actualGenre.getUpdatedAt()).isAfterOrEqualTo(aGenre.getUpdatedAt());
        assertThat(actualGenre.getDeletedAt()).isNull();
        assertThat(actualGenre.getName()).isEqualTo(expectedName);
        assertThat(actualGenre.isActive()).isEqualTo(expectedIsActive);
        assertThat(actualGenre.getCategories()).isEmpty();
        assertThat(actualGenre.getCategories()).isEqualTo(expectedCategories);

        var persistedGenre = genreRepository.findById(expectedId.getValue()).get();
        assertThat(persistedGenre.getId()).isEqualTo(expectedId.getValue());
        assertThat(persistedGenre.getCreatedAt()).isNotNull();
        assertThat(persistedGenre.getUpdatedAt()).isAfter(aGenre.getUpdatedAt());
        assertThat(persistedGenre.getDeletedAt()).isNull();
        assertThat(persistedGenre.getName()).isEqualTo(expectedName);
        assertThat(persistedGenre.isActive()).isEqualTo(expectedIsActive);
        var categoryIDS = persistedGenre.getCategoryIDS();
        assertThat(categoryIDS).isEmpty();
    }

    @Test
    void givenAValidGenreInactive_whenCallsUpdateGenreActivating_shouldPersistGenre() {

        // Arrange - Given
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aGenre = Genre.newGenre(expectedName, false);
        final var expectedId = aGenre.getId();

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));
        assertThat(genreRepository.count()).isEqualTo(1);
        assertThat(aGenre.getName()).isEqualTo(expectedName);
        assertThat(aGenre.isActive()).isFalse();
        assertThat(aGenre.getDeletedAt()).isNotNull();

        // Act - When
        final var actualGenre = genreGateway.update(
            Genre.with(aGenre).update(
                expectedName,
                expectedIsActive,
                expectedCategories
            )
        );

        // Assert - Then
        assertThat(genreRepository.count()).isEqualTo(1);

        assertThat(actualGenre.getId()).isNotNull();
        assertThat(actualGenre.getId()).isEqualTo(expectedId);
        assertThat(actualGenre.getCreatedAt()).isNotNull();
        assertThat(actualGenre.getUpdatedAt()).isNotNull();
        assertThat(actualGenre.getUpdatedAt()).isAfterOrEqualTo(aGenre.getUpdatedAt());
        assertThat(actualGenre.getDeletedAt()).isNull();
        assertThat(actualGenre.getName()).isEqualTo(expectedName);
        assertThat(actualGenre.isActive()).isTrue();
        assertThat(actualGenre.getCategories()).isEmpty();
        assertThat(actualGenre.getCategories()).isEqualTo(expectedCategories);

        var persistedGenre = genreRepository.findById(expectedId.getValue()).get();
        assertThat(persistedGenre.getId()).isEqualTo(expectedId.getValue());
        assertThat(persistedGenre.getCreatedAt()).isNotNull();
        assertThat(persistedGenre.getUpdatedAt()).isAfter(aGenre.getUpdatedAt());
        assertThat(persistedGenre.getDeletedAt()).isNull();
        assertThat(persistedGenre.getName()).isEqualTo(expectedName);
        assertThat(persistedGenre.isActive()).isTrue();
        var categoryIDS = persistedGenre.getCategoryIDS();
        assertThat(categoryIDS).isEmpty();
    }

    @Test
    void givenAValidGenreActive_whenCallsUpdateGenreInactivating_shouldPersistGenre() {

        // Arrange - Given
        final var expectedName = "Ação";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryID>of();

        final var aGenre = Genre.newGenre(expectedName, true);
        final var expectedId = aGenre.getId();

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));
        assertThat(genreRepository.count()).isEqualTo(1);
        assertThat(aGenre.getName()).isEqualTo(expectedName);
        assertThat(aGenre.isActive()).isTrue();
        assertThat(aGenre.getDeletedAt()).isNull();

        // Act - When
        final var actualGenre = genreGateway.update(
            Genre.with(aGenre).update(
                expectedName,
                expectedIsActive,
                expectedCategories
            )
        );

        // Assert - Then
        assertThat(genreRepository.count()).isEqualTo(1);

        assertThat(actualGenre.getId()).isNotNull();
        assertThat(actualGenre.getId()).isEqualTo(expectedId);
        assertThat(actualGenre.getCreatedAt()).isNotNull();
        assertThat(actualGenre.getUpdatedAt()).isNotNull();
        assertThat(actualGenre.getUpdatedAt()).isAfterOrEqualTo(aGenre.getUpdatedAt());
        assertThat(actualGenre.getDeletedAt()).isNotNull().isAfter(aGenre.getCreatedAt());

        assertThat(actualGenre.getName()).isEqualTo(expectedName);
        assertThat(actualGenre.isActive()).isFalse();
        assertThat(actualGenre.getCategories()).isEmpty();
        assertThat(actualGenre.getCategories()).isEqualTo(expectedCategories);

        var persistedGenre = genreRepository.findById(expectedId.getValue()).get();
        assertThat(persistedGenre.getId()).isEqualTo(expectedId.getValue());
        assertThat(persistedGenre.getCreatedAt()).isNotNull();
        assertThat(persistedGenre.getUpdatedAt()).isAfter(aGenre.getUpdatedAt());
        assertThat(persistedGenre.getDeletedAt()).isNotNull().isAfter(aGenre.getCreatedAt());
        assertThat(persistedGenre.getName()).isEqualTo(expectedName);
        assertThat(persistedGenre.isActive()).isFalse();
        var categoryIDS = persistedGenre.getCategoryIDS();
        assertThat(categoryIDS).isEmpty();
    }
}
