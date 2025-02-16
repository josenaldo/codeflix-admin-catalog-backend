package br.com.josenaldo.codeflix.catalog.application.category.delete;

import br.com.josenaldo.codeflix.catalog.application.UnitUseCase;

/**
 * Represents an abstract use case for deleting a category.
 * <p>
 * This class defines a contract for the deletion of a category by accepting a String input, which
 * is typically the unique identifier of the category. Concrete implementations should override the
 * {@code execute} method from {@link UnitUseCase} to provide the specific business logic for the
 * deletion process.
 * <p>
 * This use case does not return any output.
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public abstract class DeleteCategoryUseCase extends UnitUseCase<String> {

}
