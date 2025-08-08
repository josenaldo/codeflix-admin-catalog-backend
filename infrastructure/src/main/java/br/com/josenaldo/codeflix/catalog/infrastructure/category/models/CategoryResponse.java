package br.com.josenaldo.codeflix.catalog.infrastructure.category.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

public record CategoryResponse(
    @JsonProperty("id") String id,
    @JsonProperty("created_at") Instant createdAt,
    @JsonProperty("updated_at") Instant updatedAt,
    @JsonProperty("deleted_at") Instant deletedAt,
    @JsonProperty("name") String name,
    @JsonProperty("description") String description,
    @JsonProperty("is_active") Boolean isActive
) {
}
