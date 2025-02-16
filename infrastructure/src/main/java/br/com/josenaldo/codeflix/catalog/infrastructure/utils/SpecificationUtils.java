package br.com.josenaldo.codeflix.catalog.infrastructure.utils;

import org.springframework.data.jpa.domain.Specification;

/**
 * Utility class for building JPA Specifications for performing case-insensitive "like" queries.
 * <p>
 * This class provides helper methods to create a Specification that compares a given property using
 * a "like" clause, converting both the property value and search term to uppercase to ensure
 * case-insensitive matching.
 * <p>
 * The class is final and has a private constructor to prevent instantiation.
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public final class SpecificationUtils {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private SpecificationUtils() {
    }

    /**
     * Creates a Specification that performs a case-insensitive "like" query on the specified
     * property.
     * <p>
     * This method builds a JPA Specification that uses the {@code like} clause to match values of
     * the given property. The property value is converted to uppercase, and the search term is
     * transformed to include wildcards and also converted to uppercase, ensuring case-insensitive
     * matching.
     *
     * @param <T>  the type of the entity
     * @param prop the property name to be compared
     * @param term the search term used for matching
     * @return a Specification that represents a case-insensitive "like" query on the given property
     */
    public static <T> Specification<T> like(final String prop, final String term) {
        return (root, query, cb) -> cb.like(cb.upper(root.get(prop)), like(term));
    }

    /**
     * Builds a search pattern for a "like" query by wrapping the given term with wildcard
     * characters and converting it to uppercase.
     * <p>
     * For example, if the term is "test", the resulting pattern will be "%TEST%".
     *
     * @param term the search term to be transformed into a like pattern
     * @return the like pattern string with wildcards
     */
    private static String like(String term) {
        return "%" + term.toUpperCase() + "%";
    }
}
