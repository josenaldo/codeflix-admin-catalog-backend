package br.com.josenaldo.codeflix.catalog.infrastructure.category.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO for
 * {@link br.com.josenaldo.codeflix.catalog.infrastructure.category.persistence.CategoryJpaEntity}
 */
public record CategoryListResponse(
    @JsonProperty("id") String id,
    @JsonProperty("created_at") String createdAt,
    @JsonProperty("updated_at") String updatedAt,
    @JsonProperty("deleted_at") String deletedAt,
    @JsonProperty("name") String name,
    @JsonProperty("description") String description,
    @JsonProperty("is_active") Boolean isActive
) {

}
