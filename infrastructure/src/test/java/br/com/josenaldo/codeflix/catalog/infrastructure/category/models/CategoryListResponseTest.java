package br.com.josenaldo.codeflix.catalog.infrastructure.category.models;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.josenaldo.codeflix.catalog.annotations.JacksonTest;
import br.com.josenaldo.codeflix.catalog.domain.category.CategoryID;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

@JacksonTest
public class CategoryListResponseTest {

    @Autowired
    private JacksonTester<CategoryListResponse> json;

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
        final var response = new CategoryListResponse(
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

}
