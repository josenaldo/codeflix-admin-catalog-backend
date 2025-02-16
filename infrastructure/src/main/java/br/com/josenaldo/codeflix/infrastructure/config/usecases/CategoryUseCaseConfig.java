package br.com.josenaldo.codeflix.infrastructure.config.usecases;

import br.com.josenaldo.codeflix.application.category.create.CreateCategoryUseCase;
import br.com.josenaldo.codeflix.application.category.create.DefaultCreateCategoryUseCase;
import br.com.josenaldo.codeflix.application.category.delete.DefaultDeleteCategoryUseCase;
import br.com.josenaldo.codeflix.application.category.delete.DeleteCategoryUseCase;
import br.com.josenaldo.codeflix.application.category.retrieve.get.DefaultGetCategoryByIdUseCase;
import br.com.josenaldo.codeflix.application.category.retrieve.get.GetCategoryByIdUseCase;
import br.com.josenaldo.codeflix.application.category.retrieve.list.DefaultListCategoryUseCase;
import br.com.josenaldo.codeflix.application.category.retrieve.list.ListCategoryUseCase;
import br.com.josenaldo.codeflix.application.category.update.DefaultUpdateCategoryUseCase;
import br.com.josenaldo.codeflix.application.category.update.UpdateCategoryUseCase;
import br.com.josenaldo.codeflix.domain.category.CategoryGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for Category Use Cases.
 * <p>
 * This class defines the Spring beans for all use cases related to category operations, such as
 * creating, updating, deleting, and retrieving categories. It wires the use cases with the
 * {@link CategoryGateway} implementation, ensuring that all business logic related to categories is
 * properly configured and available for injection.
 * <p>
 * Each use case is exposed as a Spring bean and can be used in the application service layer.
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
@Configuration
public class CategoryUseCaseConfig {

    /**
     * The gateway used to perform category operations.
     */
    private final CategoryGateway categoryGateway;

    /**
     * Constructs a new {@code CategoryUseCaseConfig} with the specified {@link CategoryGateway}.
     * <p>
     * The provided gateway is injected by Spring and is used to configure the use cases.
     *
     * @param categoryGateway the gateway responsible for category operations.
     */
    public CategoryUseCaseConfig(CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    /**
     * Creates the use case for creating a new category.
     * <p>
     * This bean instantiates the {@link DefaultCreateCategoryUseCase} using the configured
     * {@link CategoryGateway}.
     *
     * @return an instance of {@link CreateCategoryUseCase} for category creation.
     */
    @Bean
    public CreateCategoryUseCase createCategoryUseCase() {
        return new DefaultCreateCategoryUseCase(categoryGateway);
    }

    /**
     * Creates the use case for updating an existing category.
     * <p>
     * This bean instantiates the {@link DefaultUpdateCategoryUseCase} using the configured
     * {@link CategoryGateway}.
     *
     * @return an instance of {@link UpdateCategoryUseCase} for category updates.
     */
    @Bean
    public UpdateCategoryUseCase updateCategoryUseCase() {
        return new DefaultUpdateCategoryUseCase(categoryGateway);
    }

    /**
     * Creates the use case for deleting a category.
     * <p>
     * This bean instantiates the {@link DefaultDeleteCategoryUseCase} using the configured
     * {@link CategoryGateway}.
     *
     * @return an instance of {@link DeleteCategoryUseCase} for category deletion.
     */
    @Bean
    public DeleteCategoryUseCase deleteCategoryUseCase() {
        return new DefaultDeleteCategoryUseCase(categoryGateway);
    }

    /**
     * Creates the use case for retrieving a category by its identifier.
     * <p>
     * This bean instantiates the {@link DefaultGetCategoryByIdUseCase} using the configured
     * {@link CategoryGateway}.
     *
     * @return an instance of {@link GetCategoryByIdUseCase} for retrieving a category by ID.
     */
    @Bean
    public GetCategoryByIdUseCase getCategoryByIdUseCase() {
        return new DefaultGetCategoryByIdUseCase(categoryGateway);
    }

    /**
     * Creates the use case for listing categories based on search criteria.
     * <p>
     * This bean instantiates the {@link DefaultListCategoryUseCase} using the configured
     * {@link CategoryGateway}.
     *
     * @return an instance of {@link ListCategoryUseCase} for listing categories.
     */
    @Bean
    public ListCategoryUseCase listCategoryUseCase() {
        return new DefaultListCategoryUseCase(categoryGateway);
    }
}
