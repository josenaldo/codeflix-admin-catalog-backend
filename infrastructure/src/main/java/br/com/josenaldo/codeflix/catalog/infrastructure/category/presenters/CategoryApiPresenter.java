package br.com.josenaldo.codeflix.catalog.infrastructure.category.presenters;

import br.com.josenaldo.codeflix.catalog.application.category.retrieve.get.CategoryOutput;
import br.com.josenaldo.codeflix.catalog.application.category.retrieve.list.CategoryListOutput;
import br.com.josenaldo.codeflix.catalog.infrastructure.category.models.CategoryListResponse;
import br.com.josenaldo.codeflix.catalog.infrastructure.category.models.CategoryResponse;

public interface CategoryApiPresenter {


    static CategoryResponse present(CategoryOutput categoryOutput) {
        return new CategoryResponse(
            categoryOutput.id().getValue(),
            categoryOutput.createdAt(),
            categoryOutput.updatedAt(),
            categoryOutput.deletedAt() != null ? categoryOutput.deletedAt() : null,
            categoryOutput.name(),
            categoryOutput.description(),
            categoryOutput.isActive()
        );
    }

    static CategoryListResponse present(CategoryListOutput categoryListOutput) {

        return new CategoryListResponse(
            categoryListOutput.id().getValue(),
            categoryListOutput.createdAt(),
            categoryListOutput.updatedAt() != null ? categoryListOutput.updatedAt() : null,
            categoryListOutput.deletedAt() != null ? categoryListOutput.deletedAt() : null,
            categoryListOutput.name(),
            categoryListOutput.description(),
            categoryListOutput.isActive()
        );
    }
}
