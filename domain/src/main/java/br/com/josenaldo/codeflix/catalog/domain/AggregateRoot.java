package br.com.josenaldo.codeflix.catalog.domain;

import java.time.Instant;

/**
 * Represents the root entity of an aggregate in the domain-driven design (DDD) context. An
 * aggregate root is responsible for controlling access and modifications to the entities within its
 * aggregate, ensuring data consistency and integrity.
 * <p>
 * This class extends {@link Entity}, inheriting fields and behaviors that are common to all domain
 * entities, such as identifiers and timestamps.
 *
 * @param <ID> The type of the identifier, which must extend {@link Identifier}.
 * @author Josenaldo de Oliveira Matos Filho
 * @version 1.0
 */
public abstract class AggregateRoot<ID extends Identifier> extends Entity<ID> {

    /**
     * Constructs an aggregate root with the specified unique identifier. Initializes the creation
     * and update timestamps to the current time.
     *
     * @param id The unique identifier of the aggregate root. Must not be {@code null}.
     * @throws NullPointerException if {@code id} is {@code null}.
     */
    protected AggregateRoot(final ID id) {
        super(id);
    }

    /**
     * Constructs an aggregate root with the specified unique identifier and timestamps. This
     * constructor allows complete control over the timing attributes, which can be especially
     * useful in scenarios such as data restoration or synchronization from external sources.
     *
     * @param id        The unique identifier of the aggregate root. Must not be {@code null}.
     * @param createdAt The creation timestamp of the aggregate root.
     * @param updatedAt The last updated timestamp of the aggregate root.
     * @param deletedAt The deletion timestamp of the aggregate root, or {@code null} if it has not
     *                  been logically deleted.
     */
    protected AggregateRoot(
        final ID id,
        final Instant createdAt,
        final Instant updatedAt,
        final Instant deletedAt
    ) {
        super(id, createdAt, updatedAt, deletedAt);
    }
}
