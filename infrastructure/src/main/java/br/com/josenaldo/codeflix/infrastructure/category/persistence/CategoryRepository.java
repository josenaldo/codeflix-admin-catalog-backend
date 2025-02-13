package br.com.josenaldo.codeflix.infrastructure.category.persistence;

import br.com.josenaldo.codeflix.domain.category.CategorySearchQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import static br.com.josenaldo.codeflix.infrastructure.utils.SpecificationUtils.like;

/**
 * Repository interface for managing {@link CategoryJpaEntity} persistence operations.
 * <p>
 * This interface extends {@link JpaRepository} to provide CRUD operations and query methods for
 * {@code CategoryJpaEntity} objects in the database. The primary key type is {@code String}.
 * <p>
 * Implementations of this interface are automatically provided by Spring Data JPA.
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public interface CategoryRepository extends JpaRepository<CategoryJpaEntity, String> {

    /**
     * Builds a JPA Specification to filter {@link CategoryJpaEntity} objects based on a search term.
     * <p>
     * This method checks if the {@link CategorySearchQuery} contains a non-blank term. If a valid term is provided,
     * it creates a Specification that performs a "like" match on both the "name" and "description" fields.
     * If no valid term is present, the method returns null.
     *
     * @param searchQuery the search query containing the term used for filtering.
     * @return a Specification for filtering by the search term, or null if no valid term is provided.
     */
    static Specification<CategoryJpaEntity> getTermLikeSpecification(CategorySearchQuery searchQuery) {
        return Optional.ofNullable(searchQuery.terms())
                .filter(str -> !str.isBlank())
                .map(str -> {
                    Specification<CategoryJpaEntity> nameLike = like("name", str);
                    Specification<CategoryJpaEntity> descriptionLike = like("description", str);
                    return nameLike.or(descriptionLike);
                })
                .orElse(null);
    }

    /**
     * Retrieves a paginated list of {@link CategoryJpaEntity} objects that match the given Specification.
     * <p>
     * This method accepts a Specification defining the filtering criteria and a Pageable object defining
     * the pagination parameters. It returns a Page containing the matching CategoryJpaEntity objects.
     *
     * @param whereClause the Specification containing the filtering criteria.
     * @param pageable    the pagination information.
     * @return a Page of CategoryJpaEntity objects matching the given criteria.
     */
    Page<CategoryJpaEntity> findAll(Specification<CategoryJpaEntity> whereClause, Pageable pageable);
}
