package br.com.josenaldo.codeflix.domain.category;

/**
 * Represents a search query for filtering and sorting categories.
 * <p>
 * This record encapsulates the parameters needed to perform a search operation on categories,
 * including pagination details (page and perPage), filtering terms, and sorting options.
 * <p>
 * It is typically used to transfer search criteria from the presentation layer to the service
 * layer.
 *
 * @param page      The current page number for the search result.
 * @param perPage   The number of categories to display per page.
 * @param terms     The search terms used to filter categories.
 * @param sort      The attribute by which the categories should be sorted.
 * @param direction The direction of the sort (e.g., ascending or descending).
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public record CategorySearchQuery(
    int page,
    int perPage,
    String terms,
    String sort,
    String direction) {

}
