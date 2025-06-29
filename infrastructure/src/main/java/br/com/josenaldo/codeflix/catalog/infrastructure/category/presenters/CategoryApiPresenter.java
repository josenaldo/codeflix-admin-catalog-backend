package br.com.josenaldo.codeflix.catalog.infrastructure.category.presenters;

import br.com.josenaldo.codeflix.catalog.application.category.retrieve.get.CategoryOutput;
import br.com.josenaldo.codeflix.catalog.infrastructure.category.models.CategoryApiOutput;

public interface CategoryApiPresenter {


    static CategoryApiOutput present(CategoryOutput categoryOutput) {
        return new CategoryApiOutput(
            categoryOutput.id().getValue(),
            categoryOutput.name(),
            categoryOutput.description(),
            categoryOutput.isActive(),
            categoryOutput.createdAt().toString(),
            categoryOutput.updatedAt().toString(),
            categoryOutput.deletedAt() != null ? categoryOutput.deletedAt().toString() : null
        );
    }
}
