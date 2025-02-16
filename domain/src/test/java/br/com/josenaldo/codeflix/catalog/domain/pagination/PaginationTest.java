package br.com.josenaldo.codeflix.catalog.domain.pagination;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import java.util.List;
import org.junit.jupiter.api.Test;

class PaginationTest {

    /**
     * Tests the creation of a valid Pagination object from the static method
     * {@link Pagination#fromPage(int, int, long, List)}, verifying that all fields and computed
     * values (start, end, range, etc.) match the expected results.
     */
    @Test
    public void givenAValidPageAndPerPage_whenCallsFromPage_thenShouldCreateAValidPagination() {
        // Arrange - Given
        final int page = 2;
        final int perPage = 10;
        final int total = 100;
        final List<String> data = List.of(
            "10",
            "11",
            "12",
            "13",
            "14",
            "15",
            "16",
            "17",
            "18",
            "19"
        );

        // Act - When
        final var pagination = Pagination.fromPage(page, perPage, total, data);

        // Assert - Then
        assertThat(pagination).isNotNull();
        assertThat(pagination.page()).isEqualTo(2);
        assertThat(pagination.perPage()).isEqualTo(10);
        assertThat(pagination.total()).isEqualTo(total);
        assertThat(pagination.data()).isEqualTo(data);

        assertThat(pagination.start()).isEqualTo(10);
        assertThat(pagination.end()).isEqualTo(19);
        assertThat(pagination.range()).isEqualTo(Range.of(10, 19));

        assertThat(pagination.offset()).isEqualTo(10);
        assertThat(pagination.limit()).isEqualTo(10);

        assertThat(pagination.previousPage()).isEqualTo(1);
        assertThat(pagination.nextPage()).isEqualTo(3);

        assertThat(pagination.hasPrevious()).isTrue();
        assertThat(pagination.hasNextPage()).isTrue();
        assertThat(pagination.isFirstPage()).isFalse();
        assertThat(pagination.isLastPage()).isFalse();

        assertThat(pagination.isEmpty()).isFalse();
        assertThat(pagination.isNotEmpty()).isTrue();
    }

    /**
     * Tests creating a Pagination object from a given start and end index, ensuring the page
     * calculation and related properties (offset, limit, range) are correct.
     */
    @Test
    public void givenAValidStartAndEnd_whenCallsFromRange_thenShouldCreateAValidPagination() {
        // Arrange - Given
        final int start = 0;
        final int end = 9;
        final int total = 100;
        final List<String> data = List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");

        // Act - When
        final var pagination = Pagination.fromRange(start, end, total, data);

        // Assert - Then
        assertThat(pagination).isNotNull();
        assertThat(pagination.page()).isEqualTo(1);
        assertThat(pagination.perPage()).isEqualTo(10);
        assertThat(pagination.total()).isEqualTo(total);
        assertThat(pagination.data()).isEqualTo(data);

        assertThat(pagination.start()).isEqualTo(start);
        assertThat(pagination.end()).isEqualTo(end);
        assertThat(pagination.range()).isEqualTo(Range.of(start, end));

        assertThat(pagination.offset()).isEqualTo(0);
        assertThat(pagination.limit()).isEqualTo(10);

        assertThat(pagination.previousPage()).isEqualTo(1);
        assertThat(pagination.nextPage()).isEqualTo(2);

        assertThat(pagination.hasPrevious()).isFalse();
        assertThat(pagination.hasNextPage()).isTrue();
        assertThat(pagination.isFirstPage()).isTrue();
        assertThat(pagination.isLastPage()).isFalse();

        assertThat(pagination.isEmpty()).isFalse();
        assertThat(pagination.isNotEmpty()).isTrue();

    }

    /**
     * Tests creating a Pagination object from a {@link Range}, ensuring correct calculations of
     * page, perPage, and other pagination details.
     */
    @Test
    public void givenAValidRange_whenCallsFromRange_thenShouldCreateAValidPagination() {
        // Arrange - Given
        Range range = Range.of(40, 49);
        final int total = 100;
        final List<String> data = List.of(
            "40",
            "41",
            "42",
            "43",
            "44",
            "45",
            "46",
            "47",
            "48",
            "49"
        );

        // Act - When
        final var pagination = Pagination.fromRange(range, total, data);

        // Assert - Then
        assertThat(pagination).isNotNull();
        assertThat(pagination.page()).isEqualTo(5);
        assertThat(pagination.perPage()).isEqualTo(10);
        assertThat(pagination.total()).isEqualTo(total);
        assertThat(pagination.data()).isEqualTo(data);

        assertThat(pagination.start()).isEqualTo(40);
        assertThat(pagination.end()).isEqualTo(49);
        assertThat(pagination.range()).isEqualTo(Range.of(40, 49));

        assertThat(pagination.offset()).isEqualTo(40);
        assertThat(pagination.limit()).isEqualTo(10);

        assertThat(pagination.previousPage()).isEqualTo(4);
        assertThat(pagination.nextPage()).isEqualTo(6);

        assertThat(pagination.hasPrevious()).isTrue();
        assertThat(pagination.hasNextPage()).isTrue();
        assertThat(pagination.isFirstPage()).isFalse();
        assertThat(pagination.isLastPage()).isFalse();

        assertThat(pagination.isEmpty()).isFalse();
        assertThat(pagination.isNotEmpty()).isTrue();
    }

    /**
     * Tests the {@link Pagination#totalPages()} method to ensure it calculates the correct number
     * of pages based on total items and items per page.
     */
    @Test
    public void givenAValidPagination_whenCallTotalPages_thenShouldReturnCorrectTotalPages() {
        // Arrange - Given
        final var pagination = Pagination.fromPage(1, 10, 95, List.of());

        // Act - When
        final int totalPages = pagination.totalPages();

        // Assert - Then
        assertThat(totalPages).isEqualTo(10);
    }

    /**
     * Tests the {@link Pagination#range()} method to ensure it returns a {@link Range} consistent
     * with the current page and perPage values.
     */
    @Test
    public void givenAValidPagination_whenCallGetRange_thenShouldReturnCorrectRange() {
        // Arrange - Given
        final var pagination = Pagination.fromPage(2, 10, 100, List.of());

        // Act - When
        final var range = pagination.range();

        // Assert - Then
        assertThat(range).isEqualTo(Range.of(10, 19));
    }

    /**
     * Tests the {@link Pagination#hasPrevious()} method to ensure it correctly indicates if a
     * previous page is available.
     */
    @Test
    public void givenAValidPagination_whenCallHasPrevious_thenShouldReturnCorrectValue() {
        // Arrange - Given
        final var pagination = Pagination.fromPage(2, 10, 100, List.of());

        // Act - When
        final boolean hasPrevious = pagination.hasPrevious();

        // Assert - Then
        assertThat(hasPrevious).isTrue();
    }

    /**
     * Tests the {@link Pagination#hasNextPage()} method to ensure it correctly indicates if a next
     * page is available.
     */
    @Test
    public void givenAValidPagination_whenCallHasNextPage_thenShouldReturnCorrectValue() {
        // Arrange - Given
        final var pagination = Pagination.fromPage(1, 10, 100, List.of());

        // Act - When
        final boolean hasNextPage = pagination.hasNextPage();

        // Assert - Then
        assertThat(hasNextPage).isTrue();
    }

    /**
     * Tests the {@link Pagination#isFirstPage()} method to ensure it returns true when the current
     * page is page 1.
     */
    @Test
    public void givenAValidPagination_whenCallIsFirstPage_thenShouldReturnCorrectValue() {
        // Arrange - Given
        final var pagination = Pagination.fromPage(1, 10, 100, List.of());

        // Act - When
        final boolean isFirstPage = pagination.isFirstPage();

        // Assert - Then
        assertThat(isFirstPage).isTrue();
    }

    /**
     * Tests the {@link Pagination#isLastPage()} method to ensure it returns true when the current
     * page is the final page.
     */
    @Test
    public void givenAValidPagination_whenCallIsLastPage_thenShouldReturnCorrectValue() {
        // Arrange - Given
        final var pagination = Pagination.fromPage(10, 10, 100, List.of());

        // Act - When
        final boolean isLastPage = pagination.isLastPage();

        // Assert - Then
        assertThat(isLastPage).isTrue();
    }

    /**
     * Tests the {@link Pagination#isEmpty()} method to ensure it correctly identifies a scenario
     * where there are zero total items.
     */
    @Test
    public void givenAValidPagination_whenCallIsEmpty_thenShouldReturnCorrectValue() {
        // Arrange - Given
        final var pagination = Pagination.fromPage(1, 10, 0, List.of());

        // Act - When
        final boolean isEmpty = pagination.isEmpty();

        // Assert - Then
        assertThat(isEmpty).isTrue();
    }

    /**
     * Tests the {@link Pagination#isNotEmpty()} method to ensure it correctly identifies when there
     * is at least one item in the list.
     */
    @Test
    public void givenAValidPagination_whenCallIsNotEmpty_thenShouldReturnCorrectValue() {
        // Arrange - Given
        final var pagination = Pagination.fromPage(1, 10, 100, List.of("item1"));

        // Act - When
        final boolean hasData = pagination.isNotEmpty();

        // Assert - Then
        assertThat(hasData).isTrue();
    }

    /**
     * Tests the creation of a {@link Pagination} object when the page number is invalid (i.e., less
     * than 1). Expects an {@link IllegalArgumentException}.
     */
    @Test
    public void givenInvalidPage_whenCreatePagination_thenShouldThrowException() {
        // Arrange
        final int page = -1;
        final int perPage = 10;
        final long total = 100;
        final List<String> data = List.of();

        // Act & Assert

        final var exception = catchException(() -> Pagination.fromPage(page, perPage, total, data));

        // Assert - Then
        assertThat(exception).isNotNull();
        assertThat(exception).isInstanceOf(IllegalArgumentException.class);
        assertThat(exception).hasMessageContaining("Page number must be equal or greater than 0.");
    }

    /**
     * Tests the creation of a {@link Pagination} object when the perPage value is invalid (i.e.,
     * less than 1). Expects an {@link IllegalArgumentException}.
     */
    @Test
    public void givenInvalidPerPage_whenCreatePagination_thenShouldThrowException() {
        // Arrange
        final int page = 1;
        final int perPage = 0;
        final long total = 100;
        final List<String> data = List.of();

        // Act & Assert

        final var exception = catchException(() -> Pagination.fromPage(page, perPage, total, data));

        // Assert - Then
        assertThat(exception).isNotNull();
        assertThat(exception).isInstanceOf(IllegalArgumentException.class);
        assertThat(exception).hasMessageContaining("Items per page must be greater than 0.");
    }

    /**
     * Tests the creation of a {@link Pagination} object when the total number of items is invalid
     * (i.e., negative). Expects an {@link IllegalArgumentException}.
     */
    @Test
    public void givenInvalidTotal_whenCreatePagination_thenShouldThrowException() {
        // Arrange
        final int page = 1;
        final int perPage = 10;
        final long total = -1;
        final List<String> data = List.of();

        // Act & Assert

        final var exception = catchException(() -> Pagination.fromPage(page, perPage, total, data));

        // Assert - Then
        assertThat(exception).isNotNull();
        assertThat(exception).isInstanceOf(IllegalArgumentException.class);
        assertThat(exception).hasMessageContaining("Total items must be greater than or equal to 0.");
    }

    /**
     * Tests a scenario where total equals perPage, meaning there should only be a single page.
     * Verifies that it's recognized as both first and last.
     */
    @Test
    public void givenSinglePageScenario_whenCreatePagination_thenShouldIdentifyAsOnlyPage() {
        // Arrange - Given
        final int page = 1;
        final int perPage = 10;
        final long total = 10;
        final List<String> data = List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");

        // Act - When
        final var pagination = Pagination.fromPage(page, perPage, total, data);

        // Assert - Then
        assertThat(pagination).isNotNull();
        assertThat(pagination.page()).isEqualTo(1);
        assertThat(pagination.perPage()).isEqualTo(10);
        assertThat(pagination.total()).isEqualTo(10);
        assertThat(pagination.data()).hasSize(10);

        // Deve haver apenas uma página
        assertThat(pagination.totalPages()).isEqualTo(1);
        assertThat(pagination.isFirstPage()).isTrue();
        assertThat(pagination.isLastPage()).isTrue();
        assertThat(pagination.hasPrevious()).isFalse();
        assertThat(pagination.hasNextPage()).isFalse();
    }

    /**
     * Tests the creation of a {@link Pagination} object when the requested page exceeds the total
     * number of pages. Expects an {@link IllegalArgumentException}.
     */
    @Test
    public void givenPageBeyondTotalPages_whenCreatePagination_thenShouldThrowException() {
        // Arrange - Given
        final int page = 20;   // maior do que totalPages = 5
        final int perPage = 10;
        final long total = 50;
        final List<String> data = List.of();

        // Act
        final var exception = catchException(() -> Pagination.fromPage(page, perPage, total, data));

        // Assert
        assertThat(exception)
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("cannot exceed total pages");
    }

    /**
     * Tests the creation of a {@link Pagination} object from a {@link Range} that has start > end,
     * resulting in an invalid calculation of perPage. Expects an {@link IllegalArgumentException}.
     */
    @Test
    public void givenInvalidRange_whenCallsFromRange_thenShouldThrowException() {
        // Arrange - Givens
        final var range = Range.of(10, 5); // start > end
        final int total = 100;
        final List<String> data = List.of();

        // Act - When
        final var exception = catchException(() -> Pagination.fromRange(range, total, data));

        // Assert - Then
        assertThat(exception).isNotNull();
        assertThat(exception).isInstanceOf(IllegalArgumentException.class);
        assertThat(exception).hasMessageContaining("Items per page must be greater than 0.");
    }

    /**
     * Tests the creation of a Pagination object using
     * {@link Pagination#fromPage(int, int, long, List)} when the provided data list is null. It
     * should throw a {@link NullPointerException}.
     */
    @Test
    public void givenNullData_whenCallsFromPage_thenShouldThrowException() {
        // Arrange - Given
        final int page = 1;
        final int perPage = 10;
        final long total = 100;
        final List<String> data = null;

        // Act - When
        final var exception = catchException(() -> Pagination.fromPage(page, perPage, total, data));

        // Assert - Then
        assertThat(exception).isNotNull();
        assertThat(exception).isInstanceOf(NullPointerException.class);
        assertThat(exception).hasMessageContaining("Data cannot be null.");
    }

    /**
     * Tests the creation of a Pagination object using
     * {@link Pagination#fromRange(Range, int, List)} when the provided data list is null. It should
     * throw a {@link NullPointerException}.
     */
    @Test
    public void givenNullData_whenCallsFromRange_thenShouldThrowException() {
        // Arrange - Given
        final var range = Range.of(0, 9);
        final int total = 100;
        final List<String> data = null;

        // Act - When
        final var exception = catchException(() -> Pagination.fromRange(range, total, data));

        // Assert - Then
        assertThat(exception).isNotNull();
        assertThat(exception).isInstanceOf(NullPointerException.class);
        assertThat(exception).hasMessageContaining("Data cannot be null.");
    }

    /**
     * Tests the creation of a Pagination object using
     * {@link Pagination#fromRange(Range, int, List)} when the range exceeds the total number of
     * items. It should throw an {@link IllegalArgumentException}.
     */
    @Test
    public void givenRangeExceedsTotal_whenCallsFromRange_thenShouldThrowException() {
        // Arrange - Given
        final var range = Range.of(90, 99);
        final int total = 50;
        final List<String> data = List.of();

        // Act - When
        final var exception = catchException(() -> Pagination.fromRange(range, total, data));

        // Assert - Then
        assertThat(exception).isNotNull();
        assertThat(exception).isInstanceOf(IllegalArgumentException.class);
        assertThat(exception).hasMessageContaining("cannot exceed total pages");
    }

    /**
     * Tests a scenario where the requested page is valid, but the data list returned is empty,
     * simulating a search with no results. The Pagination object should still be valid but report
     * an empty state.
     */
    @Test
    public void givenIntermediatePageWithEmptyData_whenCreatePagination_thenShouldBeValidButEmpty() {
        // Arrange - Given
        final int page = 3;
        final int perPage = 10;
        final long total = 100;
        final List<String> data = List.of();

        // Act - When
        final var pagination = Pagination.fromPage(page, perPage, total, data);

        // Assert - Then
        assertThat(pagination).isNotNull();
        assertThat(pagination.page()).isEqualTo(3);
        assertThat(pagination.totalPages()).isEqualTo(10);

        assertThat(pagination.data()).isEmpty();
        assertThat(pagination.isEmpty()).isTrue();
        assertThat(pagination.isNotEmpty()).isFalse();

        assertThat(pagination.hasPrevious()).isTrue();
        assertThat(pagination.hasNextPage()).isTrue();
        assertThat(pagination.isFirstPage()).isFalse();
        assertThat(pagination.isLastPage()).isFalse();
    }

    /**
     * Tests the creation of a Pagination object with very large values for perPage and total,
     * ensuring the class can handle them without overflow or incorrect calculations.
     */
    @Test
    public void givenLargeValues_whenCreatePagination_thenShouldHandleWithoutOverflow() {
        // Arrange - Given
        final int page = 1;
        final int perPage = 2_000_000_000;
        final long total = 3_000_000_000L;
        final List<String> data = List.of();

        // Act - When
        final var exception = catchException(() -> Pagination.fromPage(page, perPage, total, data));

        // Assert - Then
        assertThat(exception).isNull();

        final var pagination = Pagination.fromPage(page, perPage, total, data);
        assertThat(pagination.totalPages()).isEqualTo(2);
        assertThat(pagination.hasNextPage()).isTrue();
        assertThat(pagination.isFirstPage()).isTrue();
    }
}
