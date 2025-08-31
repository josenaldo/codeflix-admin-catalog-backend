package br.com.josenaldo.codeflix.catalog.application;


import java.util.List;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * The {@code UseCaseTest} class provides a foundation for testing use case implementations by
 * setting up a common structure to reset mock dependencies before each test execution.
 * <p>
 * This abstract class is designed to be extended by specific use case test classes. It ensures that
 * the mocks defined in the test class are reset before each test method is executed, to provide a
 * clean state and isolate tests from one another.
 * <p>
 * Subclasses must implement the {@link #getMocks()} method to specify the list of mock dependencies
 * to be reset.
 * <p>
 * This class uses the JUnit 5 {@code MockitoExtension} and the {@code BeforeEachCallback} interface
 * for lifecycle management of test dependencies.
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
public abstract class UseCaseTest implements BeforeEachCallback {

    /**
     * Resets all mock objects returned by the {@link #getMocks()} method to their initial state
     * before the execution of each test method. This ensures that the mocks are cleared of any
     * recorded interactions or stubbing, providing isolation between test cases.
     *
     * <p>It is invoked automatically as part of the JUnit 5 test lifecycle by the
     * {@code BeforeEachCallback} mechanism. Subclasses must implement the {@link #getMocks()}
     * method to specify the list of mock dependencies that should be reset.
     *
     * @param context the context for the current test execution, providing access to test metadata
     *                and configuration information.
     * @throws Exception if an error occurs while resetting the mocks. Actual test scenarios should
     *                   ensure that the mocks return appropriate values and are configured
     *                   correctly to avoid issues.
     */
    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Mockito.reset(getMocks().toArray());
    }

    /**
     * Specifies a list of mock objects that will be reset before the execution of each test
     * method.
     * <p>
     * Subclasses must implement this method to define the mock dependencies involved in the testing
     * process. Returning these mock objects allows the framework to reset them to their initial
     * state, ensuring test isolation and preventing side effects across test executions.
     *
     * @return a list of mock objects to be reset. If no mocks are required, an empty list should be
     * returned. The implementation must never return {@code null}.
     */
    public abstract List<Object> getMocks();
}
