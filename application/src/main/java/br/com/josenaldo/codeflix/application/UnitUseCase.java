package br.com.josenaldo.codeflix.application;

/**
 * Represents an abstract unit use case that performs an operation without returning an output.
 * <p>
 * Concrete implementations of this class should override the {@link #execute(Object)} method to
 * define the business logic required to process the provided input.
 * <p>
 * This class is typically used for actions or commands that do not produce a result.
 *
 * @param <IN> the type of input required by the use case.
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public abstract class UnitUseCase<IN> {

    /**
     * Executes the use case with the provided input.
     * <p>
     * Implementations of this method should encapsulate the necessary business logic to process the
     * input.
     *
     * @param input the input data to be processed.
     */
    public abstract void execute(IN input);
}
