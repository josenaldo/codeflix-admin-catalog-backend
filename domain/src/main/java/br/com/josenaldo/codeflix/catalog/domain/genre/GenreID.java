package br.com.josenaldo.codeflix.catalog.domain.genre;

import br.com.josenaldo.codeflix.catalog.domain.Identifier;
import br.com.josenaldo.codeflix.catalog.domain.exceptions.DomainException;
import com.github.f4b6a3.ulid.Ulid;
import com.github.f4b6a3.ulid.UlidCreator;
import java.util.Objects;

/**
 * Represents a unique identifier for a Genre.
 * <p>
 * This class extends {@link Identifier} and uses ULID (Universally Unique Lexicographically
 * Sortable Identifier) to generate and validate unique Genre IDs.
 * <p>
 * All identifiers are stored in lowercase format to ensure consistency.
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public class GenreID extends Identifier {

    /**
     * The string representation of the unique identifier.
     */
    private final String value;

    /**
     * Private constructor that creates a new {@code GenreID} instance with the specified value.
     *
     * @param value the unique identifier value, must not be {@code null}.
     * @throws NullPointerException if {@code value} is {@code null}.
     */
    public GenreID(String value) {
        Objects.requireNonNull(value, "value must not be null");
        this.value = value;
    }

    /**
     * Generates a new unique {@code GenreID} using ULID.
     *
     * @return a new {@code GenreID} instance with a generated unique value.
     * <p>
     * The generated ID is returned in lowercase.
     */
    public static GenreID unique() {
        Ulid ulid = UlidCreator.getUlid();
        return new GenreID(ulid.toString().toLowerCase());
    }

    /**
     * Creates a {@code GenreID} from the provided string.
     *
     * @param value the string representation of the identifier.
     * @return a {@code GenreID} instance representing the given string.
     * @throws NullPointerException if {@code value} is {@code null}.
     * @throws DomainException      if the provided string is not a valid ULID.
     */
    public static GenreID fromString(String value) {
        return new GenreID(value);
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
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GenreID genreID = (GenreID) o;
        return Objects.equals(value, genreID.value);
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
