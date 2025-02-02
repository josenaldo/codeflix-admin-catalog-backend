package br.com.josenaldo.codeflix.application.category.update;

/**
 * Represents a command to update an existing category.
 * <p>
 * This record encapsulates all the necessary data to update a category, including its identifier,
 * name, description, and active status. It is typically used to transfer update data from the
 * presentation layer to the application or domain layer.
 *
 * @param id          the identifier of the category to update.
 * @param name        the new name of the category.
 * @param description the new description of the category.
 * @param isActive    the new active status of the category.
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public record UpdateCategoryCommand(String id, String name, String description, boolean isActive) {

    /**
     * Creates a new instance of {@code UpdateCategoryCommand} with the specified parameters.
     * <p>
     * This static factory method provides a convenient way to instantiate an update command.
     *
     * @param id          the identifier of the category to update.
     * @param name        the new name of the category.
     * @param description the new description of the category.
     * @param isActive    the new active status of the category.
     * @return a new {@code UpdateCategoryCommand} instance with the provided data.
     */
    public static UpdateCategoryCommand with(
        final String id,
        final String name,
        final String description,
        final boolean isActive
    ) {
        return new UpdateCategoryCommand(id, name, description, isActive);
    }
}
