package br.com.josenaldo.codeflix.application.category.exceptions;

import br.com.josenaldo.codeflix.domain.category.CategoryID;
import br.com.josenaldo.codeflix.domain.exceptions.DomainException;
import br.com.josenaldo.codeflix.domain.validation.Error;
import java.util.function.Supplier;

public class CategoryExceptions {

    public static Supplier<DomainException> categoryNotFoundException(CategoryID id) {
        return () -> {
            Error error = new Error("Category with ID %s was not found".formatted(id.getValue()));
            return DomainException.with(error);
        };
    }
}
