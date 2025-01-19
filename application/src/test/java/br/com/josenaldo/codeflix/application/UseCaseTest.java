package br.com.josenaldo.codeflix.application;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UseCaseTest {

    @Test
    public void testCreateUseCase() {
        UseCase useCase = new UseCase();

        Assertions.assertNotNull(useCase);
        Assertions.assertNotNull(useCase.execute());
    }
}
