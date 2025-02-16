package br.com.josenaldo.codeflix.catalog.infrastructure.category;

import br.com.josenaldo.codeflix.catalog.domain.category.Category;
import br.com.josenaldo.codeflix.catalog.domain.category.CategoryGateway;
import br.com.josenaldo.codeflix.catalog.domain.category.CategoryID;
import br.com.josenaldo.codeflix.catalog.domain.category.CategorySearchQuery;
import br.com.josenaldo.codeflix.catalog.domain.pagination.Pagination;
import br.com.josenaldo.codeflix.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import br.com.josenaldo.codeflix.catalog.infrastructure.category.persistence.CategoryRepository;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * Implements the {@link CategoryGateway} interface using MySQL as the persistence layer.
 * <p>
 * This class serves as a gateway for performing CRUD operations on {@link Category} objects,
 * delegating the database interactions to a {@link CategoryRepository}.
 * <p>
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
@Service
public class CategoryMySQLGateway implements CategoryGateway {

    /**
     * The repository used to access category data from the MySQL database.
     */
    private final CategoryRepository categoryRepository;

    /**
     * Constructs a new {@code CategoryMySQLGateway} with the specified {@link CategoryRepository}.
     * <p>
     * The provided repository is used to perform all persistence operations for categories.
     *
     * @param categoryRepository the repository responsible for category persistence.
     */
    public CategoryMySQLGateway(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Persists a new {@link Category} in the database.
     * <p>
     * This method should save the provided category and return the persisted instance.
     *
     * @param category the category to be created.
     * @return the persisted category, or null if not implemented.
     */
    @Override
    public Category create(final Category category) {
        return save(category);
    }

    /**
     * Updates an existing {@link Category} in the database.
     * <p>
     * This method should update the category details and return the updated instance.
     *
     * @param category the category with updated information.
     * @return the updated category, or null if not implemented.
     */
    @Override
    public Category update(Category category) {
        return save(category);
    }

    /**
     * Persists a {@link Category} in the database.
     * <p>
     * This method should save the provided category and return the persisted instance.
     *
     * @param category the category to be saved.
     * @return the persisted category.
     */
    private Category save(Category category) {
        return this.categoryRepository.save(CategoryJpaEntity.from(category)).to();
    }

    /**
     * Deletes a {@link Category} from the database by its unique identifier.
     * <p>
     * This method should remove the category corresponding to the given ID from the database.
     *
     * @param id the unique identifier of the category to be deleted.
     */
    @Override
    public void deleteById(CategoryID id) {
        final String idValue = id.getValue();
        if (categoryRepository.existsById(idValue)) {
            this.categoryRepository.deleteById(idValue);
        }
    }

    /**
     * Finds a {@link Category} in the database by its unique identifier.
     * <p>
     * This method should return an {@link Optional} containing the category if found, or an empty
     * {@link Optional} if no category exists with the given ID.
     *
     * @param id the unique identifier of the category to be retrieved.
     * @return an {@link Optional} containing the found category, or empty if not found.
     */
    @Override
    public Optional<Category> findById(CategoryID id) {
        return this.categoryRepository.findById(id.getValue()).map(CategoryJpaEntity::to);
    }

    /**
     * Retrieves a paginated list of {@link Category} objects based on the search searchQuery.
     * <p>
     * This method should execute the search using the criteria provided in
     * {@link CategorySearchQuery} and return a {@link Pagination} object containing the list of
     * categories.
     *
     * @param searchQuery the search searchQuery containing filtering and pagination parameters.
     * @return a {@link Pagination} containing the list of categories, or null if not implemented.
     */
    @Override
    public Pagination<Category> findAll(CategorySearchQuery searchQuery) {

        Pageable pageable = PageRequest.of(
            searchQuery.page(),
            searchQuery.perPage(),
            Sort.by(
                Direction.fromString(searchQuery.direction()),
                searchQuery.sort()
            )
        );

        Specification<CategoryJpaEntity> termLikeSpecification = CategoryRepository.getTermLikeSpecification(
            searchQuery);

        final var pageResult = this.categoryRepository.findAll(
            Specification.where(termLikeSpecification), pageable);

        return Pagination.fromPage(
            pageResult.getNumber(),
            pageResult.getSize(),
            pageResult.getTotalElements(),
            pageResult.map(CategoryJpaEntity::to).toList()
        );
    }
}
