package br.com.josenaldo.codeflix.catalog.infrastructure.category.models;

import br.com.josenaldo.codeflix.catalog.annotations.JacksonTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

@JacksonTest
public class UpdateCategoryRequestTest {

    @Autowired
    private JacksonTester<UpdateCategoryRequest> json;

    @Test
    void testUnmarshall() throws Exception {
        // Arrange - Given
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var json = """
            {
                "name": "%s",
                "description": "%s",
                "is_active": %s
            }
            """.formatted(expectedName, expectedDescription, expectedIsActive);

        // Act - When
        final var parsed = this.json.parse(json);

        // Assert - Then
        Assertions.assertThat(parsed)
                  .hasFieldOrPropertyWithValue("name", expectedName)
                  .hasFieldOrPropertyWithValue("description", expectedDescription)
                  .hasFieldOrPropertyWithValue("active", expectedIsActive);

    }
}
