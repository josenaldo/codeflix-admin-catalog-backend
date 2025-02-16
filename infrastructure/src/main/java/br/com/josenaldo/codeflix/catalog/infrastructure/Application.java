package br.com.josenaldo.codeflix.catalog.infrastructure;


import br.com.josenaldo.codeflix.catalog.infrastructure.config.WebServerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.AbstractEnvironment;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        System.setProperty(AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME, "dev");
        SpringApplication.run(WebServerConfig.class, args);
    }
}
