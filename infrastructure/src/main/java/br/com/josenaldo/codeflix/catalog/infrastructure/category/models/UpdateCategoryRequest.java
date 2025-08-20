package br.com.josenaldo.codeflix.catalog.infrastructure.category.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateCategoryRequest(
    @NotBlank
    @JsonProperty("name")
    String name,

    @NotBlank
    @JsonProperty("description")
    String description,

    @NotNull
    @JsonProperty("is_active")
    Boolean isActive
) {

}
