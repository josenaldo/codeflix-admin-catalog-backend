package br.com.josenaldo.codeflix.domain.validation;

/**
 * Represents an error encountered during validation by encapsulating a descriptive message.
 * <p>
 * This record is used to convey information about validation failures in a clear and structured
 * manner. It can be utilized to aggregate multiple errors or to provide a single, descriptive error
 * message for a validation issue.
 *
 * @param message the descriptive error message.
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public record Error(String message) {

}
