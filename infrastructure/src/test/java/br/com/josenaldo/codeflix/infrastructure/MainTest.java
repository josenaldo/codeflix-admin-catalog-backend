package br.com.josenaldo.codeflix.infrastructure;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MainTest {

    @Test
    public void testMain() {
        Main main = new Main();
        Assertions.assertNotNull(main);
        main.main(null);
    }
}
