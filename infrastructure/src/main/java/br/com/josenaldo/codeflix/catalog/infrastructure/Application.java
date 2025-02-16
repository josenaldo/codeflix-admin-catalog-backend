package br.com.josenaldo.codeflix.catalog.infrastructure;

import br.com.josenaldo.codeflix.catalog.infrastructure.configuration.WebServerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.AbstractEnvironment;

/**
 * Main entry point of the application.
 * <p>
 * This class bootstraps the Spring Boot application by setting the default Spring profile to "dev"
 * and launching the application using the configuration provided by {@link WebServerConfig}.
 * <p>
 * The default profile is set programmatically to ensure that the application runs with the "dev"
 * environment settings during development.
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
@SpringBootApplication
public class Application {

    /**
     * The main method which starts the Spring Boot application.
     * <p>
     * It sets the active Spring profile to "dev" and then runs the application using the specified
     * configuration class.
     *
     * @param args command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        System.setProperty(AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME, "dev");
        SpringApplication.run(WebServerConfig.class, args);
    }
}
