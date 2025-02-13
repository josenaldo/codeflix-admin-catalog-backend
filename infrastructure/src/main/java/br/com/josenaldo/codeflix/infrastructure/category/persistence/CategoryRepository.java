package br.com.josenaldo.codeflix.infrastructure.category.persistence;

import br.com.josenaldo.codeflix.domain.category.CategorySearchQuery;
import br.com.josenaldo.codeflix.infrastructure.utils.SpecificationUtils;
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

    static Specification<CategoryJpaEntity> getTermLikeSpecification(CategorySearchQuery searchQuery) {
        return Optional.ofNullable(searchQuery.terms())
                .filter(str -> !str.isBlank())
                .map(str -> SpecificationUtils
                        .<CategoryJpaEntity>like("name", str)
                        .or(like("description", str)))
                .orElse(null);
    }

    Page<CategoryJpaEntity> findAll(Specification<CategoryJpaEntity> whereClause, Pageable pageable);
}
