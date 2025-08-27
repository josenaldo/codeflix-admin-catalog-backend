package br.com.josenaldo.codeflix.catalog.application.category.create;

/**
 * Represents a command for creating a new category.
 * <p>
 * This record encapsulates all the necessary information to create a category, including its name,
 * description, and activation status. It is used to transfer data from the presentation layer to
 * the application or domain layer.
 * <p>
 *
 * @param name        the name of the category.
 * @param description the description of the category.
 * @param isActive    indicates whether the category is active.
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public record CreateCategoryCommand(
    String name,
    String description,
    boolean isActive) {

    /**
     * Creates a new instance of {@code CreateCategoryCommand} with the specified parameters.
     * <p>
     * This static factory method provides a convenient way to instantiate a new command.
     *
     * @param name        the name of the category.
     * @param description the description of the category.
     * @param isActive    indicates whether the category is active.
     * @return a new {@code CreateCategoryCommand} instance.
     */
    public static CreateCategoryCommand with(
        final String name,
        final String description,
        final boolean isActive
    ) {
        return new CreateCategoryCommand(name, description, isActive);
    }

    /**
     * Creates a new {@code CreateCategoryCommand} instance with the specified parameters.
     * <p>
     * This static factory method provides a convenient way to instantiate a command while ensuring
     * null safety for the {@code isActive} parameter. If {@code isActive} is {@code null}, it
     * defaults to {@code true}.
     *
     * @param name        the name of the category.
     * @param description the description of the category.
     * @param isActive    indicates whether the category is active; if {@code null}, it defaults to
     *                    {@code true}.
     * @return a new {@code CreateCategoryCommand} instance.
     */
    public static CreateCategoryCommand with(
        final String name,
        final String description,
        final Boolean isActive
    ) {
        return new CreateCategoryCommand(name, description, isActive == null || isActive);
    }
}
