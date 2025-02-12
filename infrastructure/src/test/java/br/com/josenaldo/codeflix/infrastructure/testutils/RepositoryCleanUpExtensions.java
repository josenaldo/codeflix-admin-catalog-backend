package br.com.josenaldo.codeflix.infrastructure.testutils;

import java.util.Collection;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
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
public class RepositoryCleanUpExtensions implements BeforeEachCallback {

    /**
     * Invoked before each test execution.
     * <p>
     * This method retrieves all {@link CrudRepository} beans from the Spring application context
     * and triggers the cleanup process by deleting all entries in each repository.
     *
     * @param context the current extension context; never null.
     * @throws Exception if an error occurs during the cleanup process.
     */
    @Override
    public void beforeEach(final ExtensionContext context) throws Exception {
        final var repositories = SpringExtension.getApplicationContext(context)
                                                .getBeansOfType(CrudRepository.class).values();
        cleanUp(repositories);
    }

    /**
     * Cleans up the given collection of {@link CrudRepository} instances by deleting all records.
     * <p>
     * This method iterates over each repository and invokes its {@code deleteAll()} method to
     * remove any residual data.
     *
     * @param repositories a collection of repositories to be cleaned; must not be null.
     */
    private void cleanUp(final Collection<CrudRepository> repositories) {
        repositories.forEach(CrudRepository::deleteAll);
    }
}
