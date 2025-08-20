package br.com.josenaldo.codeflix.catalog.domain.exceptions;

import br.com.josenaldo.codeflix.catalog.domain.AggregateRoot;
import br.com.josenaldo.codeflix.catalog.domain.Identifier;
import br.com.josenaldo.codeflix.catalog.domain.validation.Error;
import java.util.List;

public class NotFoundException extends DomainException{

    /**
     * Private constructor to create a new {@code DomainException} with a message and a list of errors.
     *
     * @param message The detail message for this exception.
     * @param errors  The list of {@link Error} objects associated with this exception.
     */
    protected NotFoundException(final String message, final List<Error> errors) {
        super(message, errors);
    }

    public static NotFoundException with(
        final Class<? extends AggregateRoot<?>> anAggregate,
        final Identifier id
    ) {
        final var anError = createMessage(anAggregate.getSimpleName(), id.getValue());

            return new NotFoundException(anError, List.of());
        }

    public static NotFoundException with(
        final Class<? extends AggregateRoot<?>> anAggregate,
        final String id
    ) {
        final var anError = createMessage(anAggregate.getSimpleName(), id);

        return new NotFoundException(anError, List.of());
    }

    public static String createMessage(String anAggregate, String id) {
        return "%s with ID %s was not found".formatted(anAggregate, id);
    }

    public static String createMessage(String anAggregate, Identifier id) {
        return createMessage(anAggregate, id.getValue());
    }
}
