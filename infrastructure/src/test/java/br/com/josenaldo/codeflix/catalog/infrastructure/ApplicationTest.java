package br.com.josenaldo.codeflix.catalog.infrastructure;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = Application.class)
@ActiveProfiles("test-integration")
public class ApplicationTest {

    @Value("${server.port}")
    private String serverPort;

    @Test
    public void testMain() {
        Assertions.assertThat(serverPort).isEqualTo("9090");
    }
}
