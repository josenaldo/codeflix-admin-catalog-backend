CREATE TABLE category
(
    id          BINARY(26) PRIMARY KEY,
    created_at  TIMESTAMP    NOT NULL,
    updated_at  TIMESTAMP    NOT NULL,
    deleted_at  TIMESTAMP,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(2000),
    active      BOOLEAN      NOT NULL
)
