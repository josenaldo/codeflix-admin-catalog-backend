package br.com.josenaldo.codeflix.catalog.domain.pagination;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * Represents a paginated result set within the application, containing:
 * <ul>
 *   <li>The current page index.</li>
 *   <li>The number of items per page.</li>
 *   <li>The total number of items available.</li>
 *   <li>The items for the current page.</li>
 * </ul>
 * <p>
 * This record also provides utility methods to calculate additional pagination
 * details, such as the total number of pages, the start and end item indices,
 * and the ability to navigate between pages.
 *
 * @param <T>     The type of items contained in the paginated list.
 * @param page    The current page number being viewed.
 * @param perPage The number of items displayed on each page.
 * @param total   The total number of items across all pages.
 * @param data    The list of items for the current page.
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public record Pagination<T>(
    int page,
    int perPage,
    long total,
    List<T> data
) {

    /**
     * Represents the first page number.
     */
    public static final int FIRST_PAGE = 0;
    /**
     * Represents the default number of items per page.
     */
    public static final int DEFAULT_PER_PAGE = 10;

    /**
     * Creates a new {@code Pagination} object by specifying the current page number, the number of
     * items per page, the total count of items, and the list of items for the current page.
     *
     * @param page    The current page number. Must be equal or greater than 0, but cannot exceed
     *                the total number of pages.
     * @param perPage The number of items to display per page. Must be greater than 0.
     * @param total   The total count of items across all pages. Must be greater than or equal to
     *                0.
     * @param data    The list of items for the given page. Must not be null.
     */
    public Pagination {

        if (page < FIRST_PAGE) {
            throw new IllegalArgumentException("Page number must be equal or greater than 0.");
        }

        if (perPage < 1) {
            throw new IllegalArgumentException("Items per page must be greater than 0.");
        }

        if (total < 0) {
            throw new IllegalArgumentException("Total items must be greater than or equal to 0.");
        }

        final int totalPages = (total > 0) ? (int) Math.ceil((double) total / perPage) : 1;

        if (page > totalPages) {
            throw new IllegalArgumentException(String.format(
                "Page number [%d] cannot exceed total pages [%d].", page, totalPages
            ));
        }

        Objects.requireNonNull(data, "Data cannot be null.");
    }

    /**
     * Creates a new {@code Pagination} object by specifying the current page number, the number of
     * items per page, and the total count of items.
     *
     * @param <T>     The type of items in the resulting {@code Pagination}.
     * @param page    The current page number.
     * @param perPage The number of items to display per page.
     * @param total   The total count of items across all pages.
     * @param data    The list of items for the given page.
     * @return A new {@code Pagination} instance based on the provided parameters.
     */
    public static <T> Pagination<T> fromPage(int page, int perPage, long total, List<T> data) {
        return new Pagination<>(page, perPage, total, data);
    }

    /**
     * Creates a new {@code Pagination} object by interpreting a {@link Range} object and the total
     * count of items.
     *
     * @param <T>   The type of items in the resulting {@code Pagination}.
     * @param range The range used to determine the page boundaries (start and end).
     * @param total The total count of items across all pages.
     * @param data  The list of items for the computed page.
     * @return A new {@code Pagination} instance based on the given range.
     */
    public static <T> Pagination<T> fromRange(Range range, int total, List<T> data) {
        final var perPage = range.end() - range.start() + 1;

        final var page = perPage > 0 ? (range.start() / perPage) + 1 : 1;

        return new Pagination<>(page, perPage, total, data);
    }

    /**
     * Creates a new {@code Pagination} object by specifying the start and end indices for the
     * current page, the total count of items, and the list of items for the current page.
     *
     * @param <T>   The type of items in the resulting {@code Pagination}.
     * @param start The zero-based index at which the current page starts.
     * @param end   The zero-based index at which the current page ends.
     * @param total The total count of items across all pages.
     * @param data  The list of items for the given page.
     * @return A new {@code Pagination} instance based on the provided parameters.
     */
    public static <T> Pagination<T> fromRange(int start, int end, int total, List<T> data) {
        return fromRange(new Range(start, end), total, data);
    }

    /**
     * Calculates and returns the total number of pages based on the total item count and the number
     * of items per page.
     *
     * @return The total number of pages.
     */
    public int totalPages() {
        return (int) Math.ceil((double) total / perPage);
    }


    /**
     * Calculates and returns the number of items in the current page.
     *
     * @return The number of items
     */
    public int itemsCount() {
        return data.size();
    }

    /**
     * Determines the zero-based index at which the items for this page start.
     *
     * @return The zero-based start index for the current page.
     */
    public int start() {
        return (page - 1) * perPage;
    }

    /**
     * Determines the zero-based index at which the items for this page end. This value will be the
     * minimum of the current page's maximum index and the total item count.
     *
     * @return The zero-based end index for the current page.
     */
    public int end() {
        return Math.max(Math.min(page * perPage, (int) total) - 1, 0);
    }

    /**
     * Returns the offset for database queries or other data-fetching operations, which is the same
     * as {@link #start()}. This method is provided for consistency with the {@link #limit()}
     * method.
     *
     * @return The offset for data fetching.
     */
    public int offset() {
        return this.start();
    }

    /**
     * Returns the limit for database queries or other data-fetching operations, which is the same
     * as {@link #perPage()}. This method is provided for consistency with the {@link #offset()}
     * method.
     *
     * @return The number of items per page.
     */
    public int limit() {
        return perPage;
    }

    /**
     * Creates and returns a new {@link Range} object representing the start and end indices for the
     * current page.
     *
     * @return A {@code Range} reflecting the start and end indices of this page.
     */
    public Range range() {
        return new Range(start(), end());
    }

    /**
     * Calculates the page number immediately preceding the current page.
     *
     * @return The previous page number, or 1 if the current page is the first page.
     */
    public int previousPage() {
        return Math.max(page - 1, 1);
    }

    /**
     * Calculates the page number immediately following the current page.
     *
     * @return The next page number, or the current page if it's the last one.
     */
    public int nextPage() {
        return Math.min(page + 1, totalPages());
    }

    /**
     * Checks if there is a page before the current page (i.e., if the current page is greater than
     * 1).
     *
     * @return {@code true} if there is a previous page; {@code false} otherwise.
     */
    public boolean hasPrevious() {
        return page > 1;
    }

    /**
     * Checks if there is a page after the current page (i.e., if the current page is less than the
     * total pages).
     *
     * @return {@code true} if there is a next page; {@code false} otherwise.
     */
    public boolean hasNextPage() {
        return page < totalPages();
    }

    /**
     * Determines if the current page is the first page.
     *
     * @return {@code true} if the current page is 1; {@code false} otherwise.
     */
    public boolean isFirstPage() {
        return page == 1;
    }

    /**
     * Determines if the current page is the last page.
     *
     * @return {@code true} if the current page matches the total number of pages; {@code false}
     * otherwise.
     */
    public boolean isLastPage() {
        return page == totalPages();
    }

    /**
     * Determines if the pagination has no items.
     *
     * @return {@code true} if the total number of items is 0; {@code false} otherwise.
     */
    public boolean isEmpty() {
        return data.isEmpty();
    }

    /**
     * Determines if the pagination has at least one item.
     *
     * @return {@code true} if the total number of items is greater than 0; {@code false} otherwise.
     */
    public boolean isNotEmpty() {
        return !data.isEmpty();
    }

    /**
     * Transforms the elements of the current pagination data to a different type.
     * <p>
     * This method applies the provided mapping function to each element in the data list, producing
     * a new list of mapped results. It then creates a new {@code Pagination} instance with the same
     * pagination details (page, perPage, and total) but with the new mapped data.
     * <p>
     *
     * @param mapper the function used to transform each element of type {@code T} into type
     *               {@code R}.
     * @param <R>    the type of elements in the resulting {@code Pagination}.
     * @return a new {@code Pagination} instance containing the mapped data.
     */
    public <R> Pagination<R> map(final Function<T, R> mapper) {
        final List<R> mappedList = this.data().stream().map(mapper).toList();
        return new Pagination<>(this.page(), this.perPage(), this.total(), mappedList);
    }
}
