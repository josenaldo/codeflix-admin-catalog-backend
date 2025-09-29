package br.com.josenaldo.codeflix.catalog.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.josenaldo.codeflix.catalog.infrastructure.configuration.WebServerConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = WebServerConfig.class)
@ActiveProfiles("test-integration")
public class ApplicationTest {

    @Value("${server.port}")
    private String serverPort;

    @Test
    public void testMain() {
        assertThat(serverPort).isEqualTo("9090");
    }
}
