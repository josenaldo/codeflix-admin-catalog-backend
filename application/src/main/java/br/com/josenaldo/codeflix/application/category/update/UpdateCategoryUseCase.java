package br.com.josenaldo.codeflix.application.category.update;

import br.com.josenaldo.codeflix.application.UseCase;
import br.com.josenaldo.codeflix.domain.validation.handler.Notification;
import io.vavr.control.Either;

/**
 * Represents the abstract use case for updating an existing category.
 * <p>
 * This abstract class extends {@link UseCase} with an input of {@link UpdateCategoryCommand} and an
 * output of {@link Either} that contains either a {@link Notification} with validation errors or an
 * {@link UpdateCategoryOutput} representing the updated category.
 * <p>
 * Implementations of this use case must provide the business logic to update a category in the
 * domain layer.
 *
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public abstract class UpdateCategoryUseCase extends
    UseCase<UpdateCategoryCommand, Either<Notification, UpdateCategoryOutput>> {

}
