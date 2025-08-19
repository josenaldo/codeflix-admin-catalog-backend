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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test-e2e")
@SpringBootTest(classes = WebServerConfig.class)
@ExtendWith(RepositoryCleanUpExtension.class)
@EnableAutoConfiguration
@AutoConfigureMockMvc
public @interface E2ETest {

}
