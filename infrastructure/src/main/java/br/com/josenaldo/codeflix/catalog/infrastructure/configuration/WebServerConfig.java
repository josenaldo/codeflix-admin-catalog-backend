package br.com.josenaldo.codeflix.catalog.infrastructure.configuration;

import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main configuration class for the web server.
 * <p>
 * This class is annotated with {@code @SpringBootApplication} and sets the base package to scan for
 * Spring components to "br.com.josenaldo.codeflix". This ensures that all components within the
 * specified package are automatically detected and registered by Spring Boot.
 * <p>
 * It serves as the entry point for starting the web application and bootstraps the Spring context.
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
@SpringBootApplication(scanBasePackages = "br.com.josenaldo.codeflix")
public class WebServerConfig {

}
