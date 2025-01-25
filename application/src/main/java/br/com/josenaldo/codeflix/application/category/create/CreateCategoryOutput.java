package br.com.josenaldo.codeflix.application.category.create;

import br.com.josenaldo.codeflix.domain.category.Category;
import br.com.josenaldo.codeflix.domain.category.CategoryID;

public record CreateCategoryOutput(

    CategoryID id
) {

    public static CreateCategoryOutput from(final Category category) {
        return new CreateCategoryOutput(category.getId());
    }
}
