package br.com.josenaldo.codeflix.catalog.application;

/**
 * Represents an abstract nullary use case that performs an operation without requiring any input.
 * <p>
 * This abstract class defines a contract for executing a business process that does not take any
 * parameters and returns a result. Concrete implementations must override the {@link #execute()}
 * method to encapsulate the necessary business logic for the use case.
 * <p>
 *
 * @param <OUT> the type of output produced by the use case.
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public abstract class NullaryUseCase<OUT> {

    /**
     * Executes the use case without any input.
     * <p>
     * Implementations of this method should encapsulate the business logic needed to produce a
     * result.
     *
     * @return the result produced by executing the use case.
     */
    public abstract OUT execute();
}
