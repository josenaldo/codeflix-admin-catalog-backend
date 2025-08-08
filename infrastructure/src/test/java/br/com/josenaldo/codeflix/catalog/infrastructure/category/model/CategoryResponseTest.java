package br.com.josenaldo.codeflix.catalog.infrastructure.category.model;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.josenaldo.codeflix.catalog.annotations.JacksonTest;
import br.com.josenaldo.codeflix.catalog.domain.category.CategoryID;
import br.com.josenaldo.codeflix.catalog.infrastructure.category.models.CategoryResponse;
import java.time.Instant;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

@JacksonTest
public class CategoryResponseTest {

    @Autowired
    private JacksonTester<CategoryResponse> json;

    @Test
    void testMarshall() throws Exception {
        // Arrange - Given
        final var expectedId = CategoryID.unique().getValue();
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var expectedCreatedAt = Instant.now();
        final var expectedUpdatedAt = Instant.now();
        final var expectedDeletedAt = Instant.now();

        // Act - When
        final var response = new CategoryResponse(
            expectedId,
            expectedCreatedAt,
            expectedUpdatedAt,
            expectedDeletedAt,
            expectedName,
            expectedDescription,
            expectedIsActive
        );

        final var actualJson = json.write(response);

        // Assert - Then
        assertThat(actualJson)
            .hasJsonPathValue("$.id", expectedId)
            .hasJsonPathValue("$.created_at", expectedCreatedAt)
            .hasJsonPathValue("$.updated_at", expectedUpdatedAt)
            .hasJsonPathValue("$.deleted_at", expectedDeletedAt)
            .hasJsonPathValue("$.name", expectedName)
            .hasJsonPathValue("$.description", expectedDescription)
            .hasJsonPathValue("$.is_active", expectedIsActive);

    }

    @Test
    void testUnmarshall() throws Exception {
        // Arrange - Given
        final var expectedId = CategoryID.unique().getValue();
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var expectedCreatedAt = Instant.now();
        final var expectedUpdatedAt = Instant.now();
        final var expectedDeletedAt = Instant.now();

        final var json = """
            {
                "id": "%s",
                "created_at": "%s",
                "updated_at": "%s",
                "deleted_at": "%s",
                "name": "%s",
                "description": "%s",
                "is_active": %s
            }
            """.formatted(
            expectedId,
            expectedCreatedAt,
            expectedUpdatedAt,
            expectedDeletedAt,
            expectedName,
            expectedDescription,
            expectedIsActive
        );

        // Act - When
        final var parsed = this.json.parse(json);

        // Assert - Then
        Assertions.assertThat(parsed)
                  .hasFieldOrPropertyWithValue("id", expectedId)
                  .hasFieldOrPropertyWithValue("name", expectedName)
                  .hasFieldOrPropertyWithValue("description", expectedDescription)
                  .hasFieldOrPropertyWithValue("active", expectedIsActive)
                  .hasFieldOrPropertyWithValue("createdAt", expectedCreatedAt)
                  .hasFieldOrPropertyWithValue("updatedAt", expectedUpdatedAt)
                  .hasFieldOrPropertyWithValue("deletedAt", expectedDeletedAt);
    }

}
