package br.com.josenaldo.codeflix.application.category.update;

import br.com.josenaldo.codeflix.application.UseCase;
import br.com.josenaldo.codeflix.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class UpdateCategoryUseCase extends
    UseCase<UpdateCategoryCommand, Either<Notification, UpdateCategoryOutput>> {

}
