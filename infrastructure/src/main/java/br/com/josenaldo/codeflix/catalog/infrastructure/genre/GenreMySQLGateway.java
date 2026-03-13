package br.com.josenaldo.codeflix.catalog.infrastructure.genre;

import br.com.josenaldo.codeflix.catalog.domain.genre.Genre;
import br.com.josenaldo.codeflix.catalog.domain.genre.GenreGateway;
import br.com.josenaldo.codeflix.catalog.domain.genre.GenreID;
import br.com.josenaldo.codeflix.catalog.domain.pagination.Pagination;
import br.com.josenaldo.codeflix.catalog.domain.pagination.SearchQuery;
import br.com.josenaldo.codeflix.catalog.infrastructure.genre.persistence.GenreJpaEntity;
import br.com.josenaldo.codeflix.catalog.infrastructure.genre.persistence.GenreRepository;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class GenreMySQLGateway implements GenreGateway {

    private final GenreRepository genreRepository;

    public GenreMySQLGateway(GenreRepository genreRepository) {
        this.genreRepository = Objects.requireNonNull(genreRepository);
    }

    @Override
    public Genre create(Genre aGenre) {
        return save(aGenre);
    }

    @Override
    public Genre update(Genre aGenre) {
        return save(aGenre);
    }


    @Override
    public void deleteById(GenreID id) {

    }

    @Override
    public Optional<Genre> findById(GenreID id) {
        return Optional.empty();
    }


    @Override
    public Pagination<Genre> findAll(SearchQuery aSearchQuery) {
        return null;
    }

    private Genre save(final Genre aGenre) {
        return this.genreRepository.save(GenreJpaEntity.from(aGenre)).toAggregate();
    }
}
