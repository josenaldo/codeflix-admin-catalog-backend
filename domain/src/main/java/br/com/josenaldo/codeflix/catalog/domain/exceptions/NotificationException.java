package br.com.josenaldo.codeflix.catalog.domain.exceptions;

import br.com.josenaldo.codeflix.catalog.domain.validation.handler.Notification;

/**
 * Represents a specific type of {@link DomainException} that is used to encapsulate validation
 * errors recorded in a {@link Notification}.
 * <p>
 * This exception is typically thrown when a set of validation rules fail and their corresponding
 * errors are captured in a {@code Notification}. It consolidates the notification's errors and
 * associates them with a descriptive message to provide contextual information about what caused
 * the exception.
 * <p>
 * By inheriting from {@code DomainException}, this class ensures that the errors collected during
 * validation can be accessed, and appropriate handling can be implemented when this exception is
 * thrown.
 *
 * <p>
 * This class is useful for scenarios where business domain constraints are verified, and a formal
 * mechanism for handling multiple validation errors is required.
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public class NotificationException extends DomainException {

    /**
     * Constructs a new {@code NotificationException} with the specified detail message and a
     * {@link Notification} containing validation errors.
     * <p>
     * This exception is designed to encapsulate errors collected in the provided
     * {@link Notification}. The detail message offers contextual information for debugging or error
     * handling purposes.
     *
     * @param message      The detail message that describes the context or reason for this
     *                     exception.
     * @param notification The {@link Notification} object containing the validation errors
     *                     associated with this exception.
     */
    public NotificationException(final String message, final Notification notification) {
        super(message, notification.getErrors());
    }
}
