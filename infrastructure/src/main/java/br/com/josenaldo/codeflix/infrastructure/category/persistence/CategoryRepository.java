package br.com.josenaldo.codeflix.infrastructure.category.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

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

}
