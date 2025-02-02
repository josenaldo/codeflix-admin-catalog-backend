package br.com.josenaldo.codeflix.domain;

/**
 * Represents an abstract concept of a unique identifier within the domain. Extending this class
 * allows for customized identifier implementations, ensuring each entity or value object can be
 * distinctly identified.
 * <p>
 * Since this class is abstract, it does not provide a concrete representation of the identifier's
 * internal structure, but it mandates that all subclasses implement the retrieval of the identifier
 * value.
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public abstract class Identifier extends ValueObject {

    /**
     * Returns the internal value of this identifier as a {@code String}. Subclasses should provide
     * the appropriate logic for generating or storing this value.
     *
     * @return A {@code String} representing the identifier's value.
     */
    public abstract String getValue();
}
