package br.com.josenaldo.codeflix.catalog.domain.category;

import br.com.josenaldo.codeflix.catalog.domain.Identifier;
import br.com.josenaldo.codeflix.catalog.domain.exceptions.DomainException;
import br.com.josenaldo.codeflix.catalog.domain.validation.Error;
import com.github.f4b6a3.ulid.Ulid;
import com.github.f4b6a3.ulid.UlidCreator;
import java.util.Objects;

/**
 * Represents a unique identifier for a category.
 * <p>
 * This class extends {@link Identifier} and uses ULID (Universally Unique Lexicographically
 * Sortable Identifier) to generate and validate unique category IDs.
 * <p>
 * All identifiers are stored in lowercase format to ensure consistency.
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public class CategoryID extends Identifier {

    /**
     * The string representation of the unique identifier.
     */
    private final String value;

    /**
     * Private constructor that creates a new {@code CategoryID} instance with the specified value.
     *
     * @param value the unique identifier value, must not be {@code null}.
     * @throws NullPointerException if {@code value} is {@code null}.
     */
    private CategoryID(String value) {
        Objects.requireNonNull(value, "value must not be null");
        this.value = value;
    }

    /**
     * Generates a new unique {@code CategoryID} using ULID.
     *
     * @return a new {@code CategoryID} instance with a generated unique value.
     * <p>
     * The generated ID is returned in lowercase.
     */
    public static CategoryID unique() {
        Ulid ulid = UlidCreator.getUlid();
        return new CategoryID(ulid.toString().toLowerCase());
    }

    /**
     * Creates a {@code CategoryID} from the provided string.
     *
     * @param value the string representation of the identifier.
     * @return a {@code CategoryID} instance representing the given string.
     * <p>
     * If the provided string is not a valid ULID, a {@link DomainException} is thrown. The
     * exception message indicates that the ID is invalid.
     */
    public static CategoryID fromString(String value) {
        try {

            Objects.requireNonNull(value, "value must not be null");
            Ulid ulid = Ulid.from(value.toLowerCase());
            return new CategoryID(ulid.toString().toLowerCase());
        } catch (IllegalArgumentException e) {
            throw DomainException.with(new Error("the Id %s is invalid".formatted(value)));
        }
    }

    /**
     * Retrieves the string value of this identifier.
     *
     * @return the unique identifier as a string.
     */
    @Override
    public String getValue() {
        return value;
    }

    /**
     * Returns the string representation of this {@code CategoryID}.
     *
     * @return the unique identifier as a string.
     */
    @Override
    public String toString() {
        return value;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param o the object to compare with this {@code CategoryID}.
     * @return {@code true} if the given object is equal to this {@code CategoryID}; {@code false}
     * otherwise.
     * <p>
     * Equality is based on the value of the identifier.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CategoryID categoryID = (CategoryID) o;
        return Objects.equals(value, categoryID.value);
    }

    /**
     * Returns a hash code value for this {@code CategoryID}.
     *
     * @return the hash code of this {@code CategoryID}, based on its value.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
