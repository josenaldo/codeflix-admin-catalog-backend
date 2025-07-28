package br.com.josenaldo.codeflix.catalog.application.category;

import br.com.josenaldo.codeflix.catalog.domain.category.Category;
import br.com.josenaldo.codeflix.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import br.com.josenaldo.codeflix.catalog.infrastructure.category.persistence.CategoryRepository;
import jakarta.transaction.Transactional;
import java.util.Arrays;

/**
 * Utility class for Category tests.
 * <p>
 * This class provides helper methods for saving one or more {@link Category} instances in tests. It
 * converts the domain {@link Category} objects to their corresponding JPA entities using
 * {@link CategoryJpaEntity#from(Category)} and persists them via the given
 * {@link CategoryRepository}.
 * <p>
 * This utility is designed to simplify the setup of test data in integration tests and ensure
 * consistency when saving categories.
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public class CategoryTestUtils {

    /**
     * Saves the given categories to the repository.
     * <p>
     * This method converts each provided {@link Category} into a {@link CategoryJpaEntity} and then
     * persists them using the {@code saveAllAndFlush} method of the provided
     * {@link CategoryRepository}.
     *
     * @param repository the repository used to persist the categories.
     * @param categories one or more categories to be saved.
     */
    public static void save(CategoryRepository repository, Category... categories) {
        Arrays.stream(categories)
              .map(CategoryJpaEntity::from)
              .forEach(entity -> {
                  repository.save(entity);
                  repository.flush(); // força o flush após cada save
              });

    }

}
