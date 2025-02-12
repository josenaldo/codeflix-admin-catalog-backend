package br.com.josenaldo.codeflix.infrastructure.category;

import br.com.josenaldo.codeflix.infrastructure.category.persistence.CategoryRepository;
import br.com.josenaldo.codeflix.infrastructure.testutils.MySQLGatewayTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Test class for verifying dependency injection of MySQL gateway components.
 * <p>
 * This class uses the {@link MySQLGatewayTest} annotation to configure the test environment for
 * MySQL gateway tests. It checks that both {@link CategoryMySQLGateway} and
 * {@link CategoryRepository} are correctly injected by Spring.
 * <p>
 * Proper injection of these components is essential for the correct functioning of the persistence
 * layer.
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
@MySQLGatewayTest
class CategoryMySQLGatewayTest {

    /**
     * The repository used for accessing category data from the database.
     */
    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * The MySQL gateway implementation for category operations.
     */
    @Autowired
    private CategoryMySQLGateway categoryGateway;

    /**
     * Tests that the dependencies are correctly injected.
     * <p>
     * This test verifies that both {@code categoryGateway} and {@code categoryRepository} are not
     * null, ensuring that the Spring dependency injection configuration is working as expected.
     */
    @Test
    public void testInjectedDependencies() {
        Assertions.assertThat(categoryGateway).isNotNull();
        Assertions.assertThat(categoryRepository).isNotNull();
    }
}
