package br.com.josenaldo.codeflix.application;

import br.com.josenaldo.codeflix.domain.category.Category;

public class UseCase {

  public Category execute() {
    return Category.newCategory("Filmes", "Filmes de todos os tipos", true);
  }
}
