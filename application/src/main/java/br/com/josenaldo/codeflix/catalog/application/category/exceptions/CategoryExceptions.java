package br.com.josenaldo.codeflix.catalog.application.category.exceptions;

import br.com.josenaldo.codeflix.catalog.domain.category.CategoryID;
import br.com.josenaldo.codeflix.catalog.domain.exceptions.DomainException;
import br.com.josenaldo.codeflix.catalog.domain.validation.Error;
import java.util.function.Supplier;

/**
 * Provides utility methods for creating domain exceptions related to category operations.
 * <p>
 * This class encapsulates the creation of a {@link DomainException} when a category is not found.
 * It returns a {@link Supplier} of {@link DomainException} for lazy exception generation.
 * <p>
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public class CategoryExceptions {

    /**
     * Returns a supplier that, when invoked, produces a {@link DomainException} indicating that the
     * category with the specified ID was not found.
     * <p>
     * The exception message will include the provided category ID value.
     *
     * @param id the {@link CategoryID} for which the category was not found.
     * @return a supplier of {@link DomainException} that creates an exception with an appropriate
     * error message.
     */
    public static Supplier<DomainException> categoryNotFoundException(CategoryID id) {
        return () -> {
            Error error = new Error("Category with ID %s was not found".formatted(id.getValue()));
            return DomainException.with(error);
        };
    }

    /**
     * Returns a supplier that, when invoked, produces a {@link DomainException} indicating that the
     * category with the specified ID was not found.
     * <p>
     * The exception message will include the provided category ID value.
     *
     * @param id the {@link String} for which the category was not found.
     * @return a supplier of {@link DomainException} that creates an exception with an appropriate
     * error message.
     */
    public static Supplier<DomainException> categoryNotFoundException(String id) {
        return () -> {
            Error error = new Error("Category with ID %s was not found".formatted(id));
            return DomainException.with(error);
        };
    }

}
