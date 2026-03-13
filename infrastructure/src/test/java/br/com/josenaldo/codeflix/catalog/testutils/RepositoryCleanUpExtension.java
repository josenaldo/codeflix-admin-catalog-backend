package br.com.josenaldo.codeflix.catalog.testutils;

import br.com.josenaldo.codeflix.catalog.infrastructure.category.persistence.CategoryRepository;
import br.com.josenaldo.codeflix.catalog.infrastructure.genre.persistence.GenreRepository;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * A JUnit 5 extension that cleans up all Spring Data repositories before each test execution.
 * <p>
 * This extension retrieves all beans of type {@link CrudRepository} from the Spring application
 * context and deletes all data from them, ensuring that each test starts with a clean slate.
 * <p>
 * It implements the {@link BeforeEachCallback} interface, allowing the cleanup to occur
 * automatically before each test method runs.
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public class RepositoryCleanUpExtension implements BeforeEachCallback {

    /**
     * Executes cleanup operations before each test method in the JUnit 5 lifecycle.
     * <p>
     * This method is invoked automatically before each test method due to its implementation of the
     * {@link BeforeEachCallback} interface. It retrieves the active Spring application context from
     * the provided {@link ExtensionContext}, clears the data in the specified repositories, and
     * resets the state of the {@link TestEntityManager}.
     * <p>
     * The purpose of this method is to ensure a clean state for the repositories and entity manager
     * before each test, thereby avoiding data contamination between tests and ensuring reliable
     * test results.
     *
     * @param context the extension context supplied by JUnit 5, providing the contextual
     *                information for the ongoing test execution; must not be null.
     * @throws IllegalStateException if the Spring application context cannot be retrieved from the
     *                               given {@link ExtensionContext}.
     */
    @Override
    public void beforeEach(final ExtensionContext context) {
        var appContext = SpringExtension.getApplicationContext(context);

        cleanUp(List.of(
            appContext.getBean(GenreRepository.class),
            appContext.getBean(CategoryRepository.class)
        ));
    }

    /**
     * Deletes all data from the specified collection of repositories.
     * <p>
     * This method iterates through the given collection of {@link CrudRepository} instances and
     * clears all data from each repository, ensuring that they are in a clean state. It is
     * typically used to prepare the repositories for subsequent operations, such as testing, where
     * a clean environment is required.
     *
     * @param repositories a collection of {@link CrudRepository} instances whose data should be
     *                     deleted; must not be null. The collection may contain repositories of
     *                     different entity types.
     */
    @SuppressWarnings("rawtypes")
    private void cleanUp(final Collection<CrudRepository> repositories) {
        repositories.forEach(CrudRepository::deleteAll);
    }
}
