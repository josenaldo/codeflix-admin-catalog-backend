package br.com.josenaldo.codeflix.catalog.e2e.category;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.josenaldo.codeflix.catalog.annotations.E2ETest;
import br.com.josenaldo.codeflix.catalog.domain.category.CategoryID;
import br.com.josenaldo.codeflix.catalog.infrastructure.category.models.CategoryResponse;
import br.com.josenaldo.codeflix.catalog.infrastructure.category.models.CreateCategoryRequest;
import br.com.josenaldo.codeflix.catalog.infrastructure.category.persistence.CategoryRepository;
import br.com.josenaldo.codeflix.catalog.infrastructure.configuration.json.Json;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@E2ETest
@Testcontainers
public class CategoryE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @Container
    private static final MySQLContainer MY_SQL_CONTAINER = new MySQLContainer("mysql:8").withDatabaseName(
        "codeflix_adm_videos").withUsername("root").withPassword("123456");

    @DynamicPropertySource
    public static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
        final var port = MY_SQL_CONTAINER.getMappedPort(3306);
        final var url = MY_SQL_CONTAINER.getJdbcUrl();
        final var username = MY_SQL_CONTAINER.getUsername();
        final var password = MY_SQL_CONTAINER.getPassword();
        final var driverClassName = MY_SQL_CONTAINER.getDriverClassName();

        System.out.printf("- Container is running on url: %s\n", url);
        System.out.printf("- Container is running on port: %s\n", port);
        System.out.printf("- Container is running on username: %s\n", username);
        System.out.printf("- Container is running on password: %s\n", password);
        System.out.printf("- Container is running on driverClassName: %s\n", driverClassName);

        registry.add("mysql.port", port::toString);
    }

    @Test
    void givenIAmACatalogAdmin_whenIAddACategory_thenItShouldBePersisted() throws Exception {
        // Arrange - Given
        assertThat(MY_SQL_CONTAINER.isRunning()).isTrue();
        assertEquals(0, categoryRepository.count());

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        // Act - When
        final var categoryId = this.givenACategory(
            expectedName,
            expectedDescription,
            expectedIsActive
        );

        final var actualCategory = retrieveCategory(categoryId.getValue());

        // Assert - Then
        assertThat(actualCategory).isNotNull();
        assertNotNull(actualCategory.id());
        assertNotNull(actualCategory.createdAt());
        assertNotNull(actualCategory.updatedAt());
        assertNull(actualCategory.deletedAt());
        assertThat(expectedName).isEqualTo(actualCategory.name());
        assertThat(expectedDescription).isEqualTo(actualCategory.description());
        assertThat(expectedIsActive).isEqualTo(actualCategory.isActive());

    }

    @Test
    void givenIAmCatalogAdmin_whenIListCategories_thenIShouldNavigateToAllCategories()
        throws Exception {

        // Arrange - Given
        assertThat(MY_SQL_CONTAINER.isRunning()).isTrue();
        assertEquals(0, categoryRepository.count());

        // Act - When
        final var categoryId1 = this.givenACategory("Filmes", null, true);
        final var categoryId2 = this.givenACategory("Documentários", null, true);
        final var categoryId3 = this.givenACategory("Séries", null, true);

        assertEquals(3, categoryRepository.count());

        // Assert - Then
        this.listCategories(0, 1)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.page", equalTo(0)))
            .andExpect(jsonPath("$.per_page", equalTo(1)))
            .andExpect(jsonPath("$.total", equalTo(3)))
            .andExpect(jsonPath("$.data", hasSize(1)))
            .andExpect(jsonPath("$.data[0].id", equalTo(categoryId2.getValue())))
            .andExpect(jsonPath("$.data[0].name", equalTo("Documentários")));

        this.listCategories(1, 1)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.page", equalTo(1)))
            .andExpect(jsonPath("$.per_page", equalTo(1)))
            .andExpect(jsonPath("$.total", equalTo(3)))
            .andExpect(jsonPath("$.data", hasSize(1)))
            .andExpect(jsonPath("$.data[0].id", equalTo(categoryId1.getValue())))
            .andExpect(jsonPath("$.data[0].name", equalTo("Filmes")));

        this.listCategories(2, 1)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.page", equalTo(2)))
            .andExpect(jsonPath("$.per_page", equalTo(1)))
            .andExpect(jsonPath("$.total", equalTo(3)))
            .andExpect(jsonPath("$.data", hasSize(1)))
            .andExpect(jsonPath("$.data[0].id", equalTo(categoryId3.getValue())))
            .andExpect(jsonPath("$.data[0].name", equalTo("Séries")));

        this.listCategories(3, 1)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.page", equalTo(3)))
            .andExpect(jsonPath("$.per_page", equalTo(1)))
            .andExpect(jsonPath("$.total", equalTo(3)))
            .andExpect(jsonPath("$.data", hasSize(0)));
    }

    @Test
    void givenValidSearchTerm_whenListCategories_thenShouldReturnFilteredResults()
        throws Exception {

        // Arrange - Given
        assertThat(MY_SQL_CONTAINER.isRunning()).isTrue();
        assertEquals(0, categoryRepository.count());

        // Act - When
        final var categoryId1 = this.givenACategory("Filmes", null, true);
        final var categoryId2 = this.givenACategory("Documentários", null, true);
        final var categoryId3 = this.givenACategory("Séries", null, true);

        assertEquals(3, categoryRepository.count());

        // Assert - Then
        this.listCategories(0, 1, "fil")
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.page", equalTo(0)))
            .andExpect(jsonPath("$.per_page", equalTo(1)))
            .andExpect(jsonPath("$.total", equalTo(1)))
            .andExpect(jsonPath("$.data", hasSize(1)))
            .andExpect(jsonPath("$.data[0].id", equalTo(categoryId1.getValue())))
            .andExpect(jsonPath("$.data[0].name", equalTo("Filmes")));

    }

    @Test
    void givenValidSortFieldAndSortOrder_whenListCategories_thenShouldReturnOrderedResults()
        throws Exception {

        // Arrange - Given
        assertThat(MY_SQL_CONTAINER.isRunning()).isTrue();
        assertEquals(0, categoryRepository.count());

        // Act - When
        final var categoryId1 = this.givenACategory("Filmes", "C", true);
        final var categoryId2 = this.givenACategory("Documentários", "Z", true);
        final var categoryId3 = this.givenACategory("Séries", "A", true);

        assertEquals(3, categoryRepository.count());

        // Assert - Then
        this.listCategories(0, 3, "", "description", "desc")
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.page", equalTo(0)))
            .andExpect(jsonPath("$.per_page", equalTo(3)))
            .andExpect(jsonPath("$.total", equalTo(3)))
            .andExpect(jsonPath("$.data", hasSize(3)))
            .andExpect(jsonPath("$.data[0].name", equalTo("Documentários")))
            .andExpect(jsonPath("$.data[1].name", equalTo("Filmes")))
            .andExpect(jsonPath("$.data[2].name", equalTo("Séries")));
    }

    @Test
    void givenAValidCategoryId_whenGetCategoryByItsIdentifier_thenReturnTheCategory()
        throws Exception {
        // Arrange - Given
        assertThat(MY_SQL_CONTAINER.isRunning()).isTrue();
        assertEquals(0, categoryRepository.count());

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var categoryId = this.givenACategory(
            expectedName,
            expectedDescription,
            expectedIsActive
        );

        // Act - When
        final var actualCategory = retrieveCategory(categoryId.getValue());

        // Assert - Then
        assertThat(actualCategory).isNotNull();
        assertNotNull(actualCategory.id());
        assertNotNull(actualCategory.createdAt());
        assertNotNull(actualCategory.updatedAt());
        assertNull(actualCategory.deletedAt());
        assertThat(expectedName).isEqualTo(actualCategory.name());
        assertThat(expectedDescription).isEqualTo(actualCategory.description());
        assertThat(expectedIsActive).isEqualTo(actualCategory.isActive());
    }

    @Test
    void givenAnInexistentCategoryId_whenGetCategoryByItsIdentifier_thenReturnANotFoundError()
        throws Exception {
        // Arrange - Given
        assertThat(MY_SQL_CONTAINER.isRunning()).isTrue();
        assertEquals(0, categoryRepository.count());

        final var categoryId = CategoryID.unique();

        // Act - When
        MockHttpServletRequestBuilder aRequest = get("/categories/" + categoryId.getValue())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

        ResultActions response = this.mockMvc.perform(aRequest);

        // Assert - Then
        response.andExpect(status().isNotFound());
    }

    private ResultActions listCategories(final int page, final int perPage) throws Exception {
        return listCategories(page, perPage, "", "", "");
    }

    private ResultActions listCategories(final int page, final int perPage, final String search)
        throws Exception {
        return listCategories(page, perPage, search, "", "");
    }

    private ResultActions listCategories(
        final int page,
        final int perPage,
        final String search,
        final String sortField,
        final String sortOrder
    ) throws Exception {

        MockHttpServletRequestBuilder aRequest = get("/categories")
            .queryParam("page", String.valueOf(page))
            .queryParam("perPage", String.valueOf(perPage))
            .queryParam("search", search)
            .queryParam("sortField", sortField)
            .queryParam("sortOrder", sortOrder)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

        return this.mockMvc.perform(aRequest);
    }

    private CategoryResponse retrieveCategory(final String aCategoryId) throws Exception {
        MockHttpServletRequestBuilder aRequest = get("/categories/" + aCategoryId).contentType(
            MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);

        final var json = this.mockMvc.perform(aRequest)
                                     .andExpect(status().isOk())
                                     .andReturn()
                                     .getResponse()
                                     .getContentAsString();

        return Json.readValue(json, CategoryResponse.class);
    }

    private CategoryID givenACategory(
        final String expectedName,
        final String expectedDescription,
        final boolean expectedIsActive
    ) throws Exception {
        final var request = new CreateCategoryRequest(
            expectedName,
            expectedDescription,
            expectedIsActive
        );

        final var aRequest = post("/categories").contentType(MediaType.APPLICATION_JSON)
                                                .content(Json.writeValueAsString(request));

        final var actualId = this.mockMvc.perform(aRequest)
                                         .andExpect(status().isCreated())
                                         .andReturn()
                                         .getResponse()
                                         .getHeader("Location")
                                         .replace("/categories/", "");
        ;

        return CategoryID.fromString(actualId);
    }


}
