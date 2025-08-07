package br.com.josenaldo.codeflix.catalog.infrastructure.category.presenters;

import br.com.josenaldo.codeflix.catalog.application.category.retrieve.get.CategoryOutput;
import br.com.josenaldo.codeflix.catalog.application.category.retrieve.list.CategoryListOutput;
import br.com.josenaldo.codeflix.catalog.infrastructure.category.models.CategoryListResponse;
import br.com.josenaldo.codeflix.catalog.infrastructure.category.models.CategoryResponse;

public interface CategoryApiPresenter {


    static CategoryResponse present(CategoryOutput categoryOutput) {
        return new CategoryResponse(
            categoryOutput.id().getValue(),
            categoryOutput.name(),
            categoryOutput.description(),
            categoryOutput.isActive(),
            categoryOutput.createdAt().toString(),
            categoryOutput.updatedAt().toString(),
            categoryOutput.deletedAt() != null ? categoryOutput.deletedAt().toString() : null
        );
    }

    static CategoryListResponse present(CategoryListOutput categoryListOutput) {

        return new CategoryListResponse(
            categoryListOutput.id().getValue(),
            categoryListOutput.createdAt().toString(),
            categoryListOutput.updatedAt() != null ? categoryListOutput.updatedAt().toString()
                : null,
            categoryListOutput.deletedAt() != null ? categoryListOutput.deletedAt().toString()
                : null,
            categoryListOutput.name(),
            categoryListOutput.description(),
            categoryListOutput.isActive()
        );
    }
}
