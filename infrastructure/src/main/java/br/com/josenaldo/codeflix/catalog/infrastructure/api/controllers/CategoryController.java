package br.com.josenaldo.codeflix.catalog.infrastructure.api.controllers;

import br.com.josenaldo.codeflix.catalog.domain.pagination.Pagination;
import br.com.josenaldo.codeflix.catalog.infrastructure.api.CategoryApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryController implements CategoryApi {

    /**
     * @return
     */
    @Override
    public ResponseEntity<?> createCategory() {
        return null;
    }

    /**
     * @param search
     * @param page
     * @param perPage
     * @param sortField
     * @param sortOrder
     * @return
     */
    @Override
    public Pagination<?> listCategories(
        String search,
        int page,
        int perPage,
        int sortField,
        int sortOrder
    ) {
        return null;
    }
}
