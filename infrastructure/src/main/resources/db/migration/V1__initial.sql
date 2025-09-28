CREATE TABLE categories
(
    id         VARCHAR(26) NOT NULL PRIMARY KEY,
    created_at DATETIME(6)  NOT NULL,
    updated_at DATETIME(6)  NOT NULL,
    deleted_at DATETIME(6),
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(4000),
    active     BOOLEAN     NOT NULL DEFAULT TRUE
)
