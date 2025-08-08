package br.com.josenaldo.codeflix.catalog.annotations;

import br.com.josenaldo.codeflix.catalog.infrastructure.configuration.WebServerConfig;
import br.com.josenaldo.codeflix.catalog.testutils.RepositoryCleanUpExtension;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Custom annotation for integration tests.
 * <p>
 * This annotation configures the Spring Boot test context for integration testing by:
 * <ul>
 *   <li>Activating the "test" profile.</li>
 *   <li>Loading the configuration from {@link WebServerConfig}.</li>
 *   <li>Extending the test context with {@link RepositoryCleanUpExtension} to clean up repositories after each test.</li>
 * </ul>
 * <p>
 * Use this annotation on test classes that require a full application context for integration testing.
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test-integration")
@SpringBootTest(classes = WebServerConfig.class)
@ExtendWith(RepositoryCleanUpExtension.class)
@EnableAutoConfiguration
public @interface IntegrationTest {

}
