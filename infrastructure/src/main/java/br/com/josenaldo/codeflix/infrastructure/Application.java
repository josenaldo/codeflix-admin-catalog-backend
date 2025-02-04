package br.com.josenaldo.codeflix.infrastructure;


import br.com.josenaldo.codeflix.infrastructure.config.WebServerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(WebServerConfig.class, args);
    }
}
