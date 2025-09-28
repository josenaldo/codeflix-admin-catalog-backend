package br.com.josenaldo.codeflix.catalog.infrastructure.genre.persistence;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;

import br.com.josenaldo.codeflix.catalog.domain.category.CategoryID;
import br.com.josenaldo.codeflix.catalog.domain.genre.Genre;
import br.com.josenaldo.codeflix.catalog.domain.genre.GenreID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "genres")
public class GenreJpaEntity {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant updatedAt;

    @Column(name = "deleted_at", columnDefinition = "DATETIME(6)")
    private Instant deletedAt;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "active", nullable = false)
    private boolean active;

    @OneToMany(mappedBy = "genre", cascade = ALL, fetch = LAZY, orphanRemoval = true)
    private Set<GenreCategoryJpaEntity> categories;

    public GenreJpaEntity() {
    }

    private GenreJpaEntity(
        final String id,
        final Instant createdAt,
        final Instant updatedAt,
        final Instant deletedAt,
        final String name,
        final boolean active
    ) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.name = name;
        this.active = active;
        this.categories = new HashSet<>();
    }

    public static GenreJpaEntity from(final Genre aGenre) {
        final var anEntity = new GenreJpaEntity(
            aGenre.getId().getValue(),
            aGenre.getCreatedAt(),
            aGenre.getUpdatedAt(),
            aGenre.getDeletedAt(),
            aGenre.getName(),
            aGenre.isActive()
        );

        aGenre.getCategories().forEach(anEntity::addCategory);

        return anEntity;
    }

    public static Genre toAggregate(GenreJpaEntity anEntity) {

        return Genre.with(
            new GenreID(anEntity.getId()),
            anEntity.getCreatedAt(),
            anEntity.getUpdatedAt(),
            anEntity.getDeletedAt(),
            anEntity.getName(),
            anEntity.isActive(),
            anEntity.getCategories().stream()
                    .map(it -> CategoryID.fromString(it.getId().getCategoryId()))
                    .toList()
        );

    }


    private void addCategory(CategoryID anId) {
        this.categories.add(GenreCategoryJpaEntity.from(this, anId));
    }

    private void removeCategory(CategoryID anId) {
        this.categories.remove(GenreCategoryJpaEntity.from(this, anId));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<GenreCategoryJpaEntity> getCategories() {
        return categories;
    }

    public void setCategories(Set<GenreCategoryJpaEntity> categories) {
        this.categories = categories;
    }
}
