package br.com.josenaldo.codeflix.catalog.application;

/**
 * Represents an abstract use case in the application layer.
 * <p>
 * This abstract class serves as the base for all use cases, defining a standard contract for
 * executing specific business processes or actions. Concrete implementations must provide the logic
 * in the {@link #execute(Object)} method to process the input and produce an output.
 * <p>
 *
 * @param <IN>  the type of input required by the use case.
 * @param <OUT> the type of output produced by the use case.
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public abstract class UseCase<IN, OUT> {

    /**
     * Executes the use case with the provided input.
     * <p>
     * Implementations of this method should encapsulate the business logic necessary to process the
     * input and generate a corresponding output.
     *
     * @param input the input data required to execute the use case.
     * @return the result produced by executing the use case.
     */
    public abstract OUT execute(IN input);
}
