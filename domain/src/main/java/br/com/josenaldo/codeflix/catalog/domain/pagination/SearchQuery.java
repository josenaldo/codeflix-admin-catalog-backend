package br.com.josenaldo.codeflix.catalog.domain.pagination;

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
public record SearchQuery(
    int page,
    int perPage,
    String terms,
    String sort,
    String direction) {

    /**
     * Constructs a new SearchQuery with validation and normalization of input parameters.
     * <p>
     * This constructor performs the following validations and transformations:
     * <ul>
     *   <li>Ensures page number is positive, defaulting to {@link Pagination#FIRST_PAGE} if not</li>
     *   <li>Ensures perPage is positive, defaulting to {@link Pagination#DEFAULT_PER_PAGE} if not</li>
     *   <li>Trims terms or sets to null if empty</li>
     *   <li>Validates and trims sort field, defaulting to {@link #DEFAULT_SORT} if invalid</li>
     *   <li>Validates and trims direction, defaulting to {@link #DEFAULT_DIRECTION} if invalid</li>
     * </ul>
     *
     * @param page      the page number to request (will be set to {@link Pagination#FIRST_PAGE} if < 0)
     * @param perPage   number of items per page (will be set to {@link Pagination#DEFAULT_PER_PAGE} if <= 0)
     * @param terms     search terms to filter by (will be trimmed if not null)
     * @param sort      field name to sort by (will default to {@link #DEFAULT_SORT} if invalid)
     * @param direction sort direction (will default to {@link #DEFAULT_DIRECTION} if invalid)
     */
    public SearchQuery {
        page = Math.max(page, Pagination.FIRST_PAGE);
        perPage = perPage > 0 ? perPage : Pagination.DEFAULT_PER_PAGE;
        terms = terms != null ? terms.trim() : null;
        sort = sort != null && !sort.trim().isEmpty() ? sort.trim() : DEFAULT_SORT;
        direction = direction != null && !direction.trim().isEmpty()
            ? direction.trim()
            : DEFAULT_DIRECTION;
    }

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
     * Creates an empty {@code SearchQuery} with default values.
     * <p>
     * This method returns a {@code SearchQuery} initialized with the following defaults:
     * <ul>
     *   <li>page: {@link Pagination#FIRST_PAGE}</li>
     *   <li>perPage: {@link Pagination#DEFAULT_PER_PAGE}</li>
     *   <li>terms: {@link #EMPTY_TERM}</li>
     *   <li>sort: {@link #DEFAULT_SORT}</li>
     *   <li>direction: {@link #DEFAULT_DIRECTION}</li>
     * </ul>
     *
     * @return a new {@code SearchQuery} instance with default search criteria.
     */
    public static SearchQuery empty() {
        return new SearchQuery(
            Pagination.FIRST_PAGE,
            Pagination.DEFAULT_PER_PAGE,
            EMPTY_TERM,
            DEFAULT_SORT,
            DEFAULT_DIRECTION
        );
    }

    /**
     * Creates a new {@code SearchQuery} with the specified parameters.
     * <p>
     * This method allows creating a customized search query by providing the page number, the
     * number of items per page, search terms, sorting attribute, and sorting direction.
     *
     * @param page      the current page number for the search result.
     * @param perPage   the number of categories to display per page.
     * @param terms     the search terms used to filter categories.
     * @param sort      the attribute by which the categories should be sorted.
     * @param direction the direction of the sort (e.g., ascending or descending).
     * @return a new {@code SearchQuery} instance with the specified parameters.
     */
    public static SearchQuery of(
        int page,
        int perPage,
        String terms,
        String sort,
        String direction
    ) {
        return new SearchQuery(page, perPage, terms, sort, direction);
    }
}
