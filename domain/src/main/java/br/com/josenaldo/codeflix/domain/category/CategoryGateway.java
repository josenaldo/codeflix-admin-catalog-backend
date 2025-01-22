package br.com.josenaldo.codeflix.domain.category;

import java.util.Optional;

public interface CategoryGateway {

  Category create(Category category);

  void deleteById(CategoryID id);

  Optional<Category> findById(CategoryID id);

  Category update(Category category);

  Pagination<Category> list(Search pagination);
}
