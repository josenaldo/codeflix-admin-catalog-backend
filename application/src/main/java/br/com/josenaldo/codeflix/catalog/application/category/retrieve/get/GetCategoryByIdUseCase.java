package br.com.josenaldo.codeflix.catalog.application.category.retrieve.get;

import br.com.josenaldo.codeflix.catalog.application.UseCase;

/**
 * Represents an abstract use case for retrieving a category by its identifier.
 * <p>
 * This class extends {@link UseCase} with a {@code String} input that represents the unique
 * identifier of the category and a {@link CategoryOutput} as the output containing the details of
 * the retrieved category.
 * <p>
 * Concrete implementations should provide the specific business logic to fetch the category from
 * the data source.
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public abstract class GetCategoryByIdUseCase extends UseCase<String, CategoryOutput> {

}
