package br.com.josenaldo.codeflix.application.category.create;

import br.com.josenaldo.codeflix.application.UseCase;
import br.com.josenaldo.codeflix.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class CreateCategoryUseCase extends
    UseCase<CreateCategoryCommand, Either<Notification, CreateCategoryOutput>> {

}
