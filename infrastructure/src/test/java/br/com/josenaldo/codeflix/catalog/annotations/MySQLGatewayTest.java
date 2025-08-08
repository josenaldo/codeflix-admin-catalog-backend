package br.com.josenaldo.codeflix.catalog.annotations;

import br.com.josenaldo.codeflix.catalog.testutils.RepositoryCleanUpExtension;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;

/**
 * Custom annotation for MySQL Gateway tests.
 * <p>
 * This annotation is a composed annotation that configures the test environment for testing MySQL
 * Gateway components. It activates the "test" profile, sets up JPA tests with {@code @DataJpaTest},
 * and restricts component scanning to include only beans that match the regex pattern
 * ".*[MySQLGateway]".
 * <p>
 * Additionally, it extends the test context with {@code RepositoryCleanUpExtensions} to ensure
 * proper cleanup of repositories after each test execution.
 * <p>
 * Use this annotation on test classes that require a dedicated MySQL Gateway context to ensure
 * consistent and isolated testing of database interactions.
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test-integration")
@DataJpaTest
@ComponentScan(includeFilters = {
    @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".[MySQLGateway]")
})
@ExtendWith(RepositoryCleanUpExtension.class)
public @interface MySQLGatewayTest {

}
