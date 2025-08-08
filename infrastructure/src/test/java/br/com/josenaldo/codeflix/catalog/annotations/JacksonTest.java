package br.com.josenaldo.codeflix.catalog.annotations;

import br.com.josenaldo.codeflix.catalog.infrastructure.configuration.json.ObjectMapperConfig;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;

/**
 * Custom annotation for testing JSON serialization and deserialization.
 * <p>
 * This annotation configures the test environment for testing JSON-related components in isolation.
 * It includes necessary filters to load specific beans such as {@link ObjectMapperConfig}.
 * <p>
 * Use this annotation on test classes to:
 * <ul>
 *   <li>Activate the "test" profile.</li>
 *   <li>Configure the {@code ObjectMapper} bean for JSON handling.</li>
 *   <li>Ensure tests are focused on JSON serialization and deserialization.</li>
 * </ul>
 * <p>
 * This annotation is commonly used for unit tests involving Jackson's {@code ObjectMapper}.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test-integration")
@JsonTest(includeFilters = {
    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = ObjectMapperConfig.class)
})
public @interface JacksonTest {

}
