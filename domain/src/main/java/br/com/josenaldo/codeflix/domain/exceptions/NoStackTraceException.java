package br.com.josenaldo.codeflix.domain.exceptions;

/**
 * A custom runtime exception that does not generate a stack trace.
 * <p>
 * This exception is useful in scenarios where the stack trace is unnecessary, thereby reducing
 * performance overhead. Instances of {@code NoStackTraceException} are created with the writable
 * stack trace disabled.
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public class NoStackTraceException extends RuntimeException {

    /**
     * Constructs a new {@code NoStackTraceException} with the specified detail message. The cause
     * is not initialized, and the stack trace is disabled.
     *
     * @param message The detail message explaining the reason for the exception.
     */
    public NoStackTraceException(String message) {
        this(message, null);
    }

    /**
     * Constructs a new {@code NoStackTraceException} with the specified detail message and cause.
     * The stack trace is disabled for this exception.
     *
     * @param message The detail message explaining the reason for the exception.
     * @param cause   The cause of the exception, or {@code null} if none.
     */
    public NoStackTraceException(String message, Throwable cause) {
        super(message, cause, true, false);
    }
}
