package br.com.josenaldo.codeflix.application.category.create;

import br.com.josenaldo.codeflix.application.UseCase;
import br.com.josenaldo.codeflix.domain.validation.handler.Notification;
import io.vavr.control.Either;

/**
 * Represents the abstract use case for creating a new category.
 * <p>
 * This abstract class extends {@link UseCase} to define the contract for executing a category
 * creation operation. The input is a {@link CreateCategoryCommand} and the output is an
 * {@link Either} that contains either a {@link Notification} with validation errors or a
 * {@link CreateCategoryOutput} representing the successfully created category.
 * <p>
 * Implementations of this use case must provide the specific business logic for creating a
 * category.
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public abstract class CreateCategoryUseCase extends
    UseCase<CreateCategoryCommand, Either<Notification, CreateCategoryOutput>> {

}
