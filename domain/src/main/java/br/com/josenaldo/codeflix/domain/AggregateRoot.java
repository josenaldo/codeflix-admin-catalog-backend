package br.com.josenaldo.codeflix.domain;

import java.time.Instant;

public abstract class AggregateRoot<ID extends Identifier> extends Entity<ID> {

  protected AggregateRoot(final ID id) {
    super(id);
  }

  protected AggregateRoot(final ID id, final Instant createdAt, final Instant updatedAt, final Instant deletedAt) {
    super(id, createdAt, updatedAt, deletedAt);
  }
}
