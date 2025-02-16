package br.com.josenaldo.codeflix.catalog.domain.category;

/**
 * Represents a search query for filtering and sorting categories.
 * <p>
 * This record encapsulates the parameters needed to perform a search operation on categories,
 * including pagination details (page and perPage), filtering terms, and sorting options.
 * <p>
 * It is typically used to transfer search criteria from the presentation layer to the service
 * layer.
 *
 * @param page      the current page number for the search result.
 * @param perPage   the number of categories to display per page.
 * @param terms     the search terms used to filter categories.
 * @param sort      the attribute by which the categories should be sorted.
 * @param direction the direction of the sort (e.g., ascending or descending).
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public record CategorySearchQuery(
    int page,
    int perPage,
    String terms,
    String sort,
    String direction) {

    /**
     * Represents the first page number.
     */
    public static final int FIRST_PAGE = 1;

    /**
     * Represents the default number of items per page.
     */
    public static final int DEFAULT_PER_PAGE = 10;

    /**
     * Represents the default attribute to sort the categories by.
     */
    public static final String DEFAULT_SORT = "createdAt";

    /**
     * Represents the default direction for sorting categories.
     */
    public static final String DEFAULT_DIRECTION = "asc";

    /**
     * Represents an empty search term.
     */
    public static final String EMPTY_TERM = null;

    /**
     * Creates an empty {@code CategorySearchQuery} with default values.
     * <p>
     * This method returns a {@code CategorySearchQuery} initialized with the following defaults:
     * <ul>
     *   <li>page: {@link #FIRST_PAGE}</li>
     *   <li>perPage: {@link #DEFAULT_PER_PAGE}</li>
     *   <li>terms: {@link #EMPTY_TERM}</li>
     *   <li>sort: {@link #DEFAULT_SORT}</li>
     *   <li>direction: {@link #DEFAULT_DIRECTION}</li>
     * </ul>
     *
     * @return a new {@code CategorySearchQuery} instance with default search criteria.
     */
    public static CategorySearchQuery empty() {
        return new CategorySearchQuery(
            FIRST_PAGE,
            DEFAULT_PER_PAGE,
            EMPTY_TERM,
            DEFAULT_SORT,
            DEFAULT_DIRECTION
        );
    }

    /**
     * Creates a new {@code CategorySearchQuery} with the specified parameters.
     * <p>
     * This method allows creating a customized search query by providing the page number, the
     * number of items per page, search terms, sorting attribute, and sorting direction.
     *
     * @param page      the current page number for the search result.
     * @param perPage   the number of categories to display per page.
     * @param terms     the search terms used to filter categories.
     * @param sort      the attribute by which the categories should be sorted.
     * @param direction the direction of the sort (e.g., ascending or descending).
     * @return a new {@code CategorySearchQuery} instance with the specified parameters.
     */
    public static CategorySearchQuery of(
        int page,
        int perPage,
        String terms,
        String sort,
        String direction
    ) {
        return new CategorySearchQuery(page, perPage, terms, sort, direction);
    }
}
