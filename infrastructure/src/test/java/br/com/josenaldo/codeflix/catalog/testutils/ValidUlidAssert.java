package br.com.josenaldo.codeflix.catalog.testutils;

import com.github.f4b6a3.ulid.Ulid;
import java.util.function.Supplier;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.description.Description;

/**
 * Custom assertion class for validating that a given string is a valid ULID.
 * <p>
 * This class extends {@link AbstractAssert} from AssertJ to provide a fluent API for asserting that
 * a string represents a valid ULID. It allows chaining custom assertions with a clear and readable
 * syntax.
 * <p>
 * Example usage:
 * <pre>
 *   ValidUlidAssert.assertThat(actualId).isValidUlid(actualId);
 * </pre>
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public class ValidUlidAssert extends AbstractAssert<ValidUlidAssert, String> {

    /**
     * Constructs a new {@code ValidUlidAssert} for the given actual ID.
     *
     * @param actualId the ULID string to assert on.
     */
    public ValidUlidAssert(String actualId) {
        super(actualId, ValidUlidAssert.class);
    }

    /**
     * Entry point for validating a ULID.
     * <p>
     * This static method creates a new instance of {@code ValidUlidAssert} for the given ULID
     * string.
     *
     * @param actualId the ULID string to validate.
     * @return a new {@code ValidUlidAssert} instance.
     */
    public static ValidUlidAssert assertThat(String actualId) {
        return new ValidUlidAssert(actualId);
    }

    /**
     * Asserts that the given ULID string is valid.
     * <p>
     * This method checks if the actual ULID string is not null and is a valid ULID as per the ULID
     * library. If the ULID is not valid, the assertion fails with an appropriate error message.
     *
     * @param actualId the ULID string to validate.
     * @return this {@code ValidUlidAssert} instance for method chaining.
     * @throws AssertionError if the ULID is null or invalid.
     */
    public ValidUlidAssert isValidUlid(String actualId) {
        isNotNull();

        if (!Ulid.isValid(actualId)) {
            failWithMessage("%s is not a valid ULID".formatted(actualId));
        }

        return this;
    }

    /**
     * Overrides the {@code as} method to set a custom description for this assertion.
     *
     * @param description the custom description.
     * @return this {@code ValidUlidAssert} instance.
     */
    @Override
    public ValidUlidAssert as(Description description) {
        return super.as(description);
    }

    /**
     * Overrides the {@code describedAs} method to set a custom description for this assertion.
     *
     * @param description the custom description.
     * @param args        optional arguments for the description.
     * @return this {@code ValidUlidAssert} instance.
     */
    @Override
    public ValidUlidAssert describedAs(String description, Object... args) {
        return super.describedAs(description, args);
    }

    /**
     * Overrides the {@code describedAs} method to set a custom description using a supplier.
     *
     * @param descriptionSupplier the supplier providing the description.
     * @return this {@code ValidUlidAssert} instance.
     */
    @Override
    public ValidUlidAssert describedAs(Supplier<String> descriptionSupplier) {
        return super.describedAs(descriptionSupplier);
    }
}
