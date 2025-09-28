CREATE TABLE genres
(
    id         VARCHAR(26)  NOT NULL PRIMARY KEY,
    created_at DATETIME(6)  NOT NULL,
    updated_at DATETIME(6)  NOT NULL,
    deleted_at DATETIME(6),
    name       VARCHAR(255) NOT NULL,
    active     BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE genres_categories
(
    genre_id    VARCHAR(26) NOT NULL,
    category_id VARCHAR(26) NOT NULL,
    CONSTRAINT idx_genre_category UNIQUE (genre_id, category_id),
    CONSTRAINT fk_genre_id FOREIGN KEY (genre_id) REFERENCES genres (id) ON DELETE CASCADE,
    CONSTRAINT fk_category_id FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE CASCADE
);
