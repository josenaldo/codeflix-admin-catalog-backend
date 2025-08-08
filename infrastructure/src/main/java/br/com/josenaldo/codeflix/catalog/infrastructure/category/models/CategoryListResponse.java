package br.com.josenaldo.codeflix.catalog.infrastructure.category.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

/**
 * DTO for
 * {@link br.com.josenaldo.codeflix.catalog.infrastructure.category.persistence.CategoryJpaEntity}
 */
public record CategoryListResponse(
    @JsonProperty("id") String id,
    @JsonProperty("created_at") Instant createdAt,
    @JsonProperty("updated_at") Instant updatedAt,
    @JsonProperty("deleted_at") Instant deletedAt,
    @JsonProperty("name") String name,
    @JsonProperty("description") String description,
    @JsonProperty("is_active") Boolean isActive
) {

}
