package br.com.josenaldo.codeflix.catalog.domain.exceptions;

import br.com.josenaldo.codeflix.catalog.domain.AggregateRoot;
import br.com.josenaldo.codeflix.catalog.domain.Identifier;
import br.com.josenaldo.codeflix.catalog.domain.validation.Error;
import java.util.List;

/**
 * Represents a specialized domain exception indicating that a specific aggregate root was not
 * found.
 * <p>
 * This exception is used to provide detailed feedback when a requested entity, identified by its
 * aggregate root and identifier, cannot be located in the domain context.
 * <p>
 * The {@code NotFoundException} serves as a way to communicate domain-specific errors related to
 * missing aggregates in a consistent and structured manner.
 * <p>
 * It extends {@link DomainException}, inheriting its ability to encapsulate a list of errors for
 * further context.
 * <p>
 * Instances of this exception can be created using the provided static factory methods to specify
 * the aggregate and identifier details.
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public class NotFoundException extends DomainException {

    /**
     * Protected constructor to create a new {@code NotFoundException} with a message and a list of
     * errors.
     *
     * @param message The detail message for this exception.
     * @param errors  The list of {@link Error} objects associated with this exception.
     */
    protected NotFoundException(final String message, final List<Error> errors) {
        super(message, errors);
    }

    /**
     * Creates a {@link NotFoundException} for a specified aggregate root and its identifier.
     * <p>
     * This method generates an exception instance with a standardized error message indicating that
     * the aggregate root with the provided identifier could not be found.
     *
     * @param anAggregate The class of the aggregate root that was not found. Must not be
     *                    {@code null}.
     * @param id          The identifier of the missing aggregate root. Must not be {@code null}.
     * @return A new {@link NotFoundException} instance containing the formatted error message.
     * @throws NullPointerException if either {@code anAggregate} or {@code id} is {@code null}.
     */
    public static NotFoundException with(
        final Class<? extends AggregateRoot<?>> anAggregate,
        final Identifier id
    ) {
        final var anError = createMessage(anAggregate.getSimpleName(), id.getValue());
        return new NotFoundException(anError, List.of());
    }

    /**
     * Creates a {@link NotFoundException} for a specified aggregate root and its identifier.
     * <p>
     * This method generates an exception instance with a standardized error message indicating that
     * the aggregate root with the provided identifier could not be found.
     * <p>
     * It is mainly used in scenarios where the ID is provided as a raw {@code String}, offering
     * flexibility for different identifier representations.
     *
     * @param anAggregate The class of the aggregate root that was not found. Must not be
     *                    {@code null}.
     * @param id          The identifier of the missing aggregate root. Must not be {@code null}.
     * @return A new {@link NotFoundException} instance containing the formatted error message.
     * @throws NullPointerException if either {@code anAggregate} or {@code id} is {@code null}.
     */
    public static NotFoundException with(
        final Class<? extends AggregateRoot<?>> anAggregate,
        final String id
    ) {
        final var anError = createMessage(anAggregate.getSimpleName(), id);

        return new NotFoundException(anError, List.of());
    }

    /**
     * Constructs a standardized error message indicating that an aggregate root with a specified
     * identifier could not be found.
     * <p>
     * The generated message follows the format: {@code {aggregate} with ID {id} was not found}.
     *
     * @param anAggregate The name of the aggregate root that was not found. Must not be
     *                    {@code null}.
     * @param id          The identifier of the missing aggregate root. Must not be {@code null}.
     * @return A formatted {@link String} representing the error message. Never {@code null}.
     * @throws NullPointerException if either {@code anAggregate} or {@code id} is {@code null}.
     */
    public static String createMessage(String anAggregate, String id) {
        return "%s with ID %s was not found".formatted(anAggregate, id);
    }

    /**
     * Constructs a standardized error message indicating that an aggregate root with a specified
     * identifier could not be found.
     * <p>
     * This method serves as a utility to simplify the creation of error messages by automatically
     * extracting the string representation of the identifier from an {@link Identifier} instance.
     * The generated message follows the format: {@code {aggregate} with ID {id} was not found}.
     *
     * @param anAggregate The name of the aggregate root that was not found. Must not be
     *                    {@code null}.
     * @param id          The identifier of the missing aggregate root. Must not be {@code null}.
     * @return A formatted {@link String} representing the error message. Never {@code null}.
     * @throws NullPointerException if either {@code anAggregate} or {@code id} is {@code null}.
     */
    public static String createMessage(String anAggregate, Identifier id) {
        return createMessage(anAggregate, id.getValue());
    }
}
