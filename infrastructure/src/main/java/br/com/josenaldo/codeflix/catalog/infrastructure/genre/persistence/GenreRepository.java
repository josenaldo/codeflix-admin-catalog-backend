package br.com.josenaldo.codeflix.catalog.infrastructure.genre.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<GenreJpaEntity, String> {

}
