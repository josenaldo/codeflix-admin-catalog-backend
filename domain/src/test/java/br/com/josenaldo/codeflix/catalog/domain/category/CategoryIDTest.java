package br.com.josenaldo.codeflix.catalog.domain.category;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import br.com.josenaldo.codeflix.catalog.domain.exceptions.DomainException;
import com.github.f4b6a3.ulid.Ulid;
import org.junit.jupiter.api.Test;

class CategoryIDTest {

    /**
     * Tests that the unique() method returns a CategoryID with a valid ULID string in lowercase.
     */
    @Test
    public void givenUniqueMethod_whenCalled_thenReturnsValidCategoryID() {
        // Arrange - Given

        // Act - When
        CategoryID categoryID = CategoryID.unique();

        // Assert - Then
        assertThat(categoryID).isNotNull();

        // Validate that the returned string is a valid ULID by trying to create a ULID instance from it.
        Ulid ulid = Ulid.from(categoryID.getValue());

        // Verify that the value is in lowercase.
        assertThat(categoryID.getValue()).isEqualTo(categoryID.getValue().toLowerCase());
        assertThat(ulid).isNotNull();
    }

    /**
     * Tests that the fromString() method returns a CategoryID for a valid ULID string (case
     * insensitive).
     */
    @Test
    public void givenValidULIDString_whenFromString_thenReturnsCategoryID() {
        // Arrange - Given
        // Generate a valid ULID and convert to uppercase.
        Ulid ulid = Ulid.from("01ARYZ6S41TSV4RRFFQ69G5FAV");
        String validUlidUpper = ulid.toString().toUpperCase();

        // Act - When
        CategoryID categoryID = CategoryID.fromString(validUlidUpper);

        // Assert - Then
        // The returned value should be the lowercase version of the ULID.
        assertThat(categoryID).isNotNull();
        assertThat(categoryID.getValue()).isEqualTo(validUlidUpper.toLowerCase());
    }

    /**
     * Tests that the fromString() method throws a DomainException when provided with an invalid
     * ULID string.
     */
    @Test
    public void givenInvalidULIDString_whenFromString_thenThrowsDomainException() {
        // Arrange - Given
        String invalidUlid = "invalid-id";

        // Act - When
        final var exception = catchException(() -> CategoryID.fromString(invalidUlid));

        // Assert - Then
        assertThat(exception)
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("the Id " + invalidUlid + " is invalid");
    }

    /**
     * Tests that getValue() and toString() return the same value for a CategoryID.
     */
    @Test
    public void givenCategoryID_whenGetValueAndToString_thenReturnSameValue() {
        // Arrange - Given
        CategoryID categoryID = CategoryID.unique();

        // Act - When
        String valueFromGet = categoryID.getValue();
        String valueFromToString = categoryID.toString();

        // Assert - Then
        assertThat(valueFromGet).isEqualTo(valueFromToString);
    }

    /**
     * Tests that two CategoryID instances created from the same valid ULID string are equal and
     * have the same hash code.
     */
    @Test
    public void givenSameULIDString_whenCreatingTwoCategoryIDs_thenTheyAreEqual() {
        // Arrange - Given
        String ulidString = CategoryID.unique().getValue();
        CategoryID categoryID1 = CategoryID.fromString(ulidString);
        CategoryID categoryID2 = CategoryID.fromString(ulidString);

        // Act - When

        // Assert - Then
        assertThat(categoryID1).isEqualTo(categoryID2);
        assertThat(categoryID1.hashCode()).isEqualTo(categoryID2.hashCode());
    }

    /**
     * Tests that calling fromString() with a null value throws a NullPointerException.
     */
    @Test
    public void givenNullValue_whenFromString_thenThrowsNullPointerException() {
        // Arrange - Given
        String nullValue = null;

        // Act - When
        final var exception = catchException(() -> CategoryID.fromString(nullValue));

        // Assert - Then
        assertThat(exception)
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("must not be null");
    }
}
