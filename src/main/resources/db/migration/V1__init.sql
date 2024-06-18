CREATE SEQUENCE IF NOT EXISTS global_sequence START WITH 100000;

CREATE TABLE IF NOT EXISTS publishers
(
    id              INT PRIMARY KEY NOT NULL,
    created_at      TIMESTAMP       NOT NULL,
    modified_at     TIMESTAMP,
    version         INT,
    name            VARCHAR(255)    NOT NULL,
    foundation_year INT             NOT NULL,
    city            VARCHAR(255)    NOT NULL
);

CREATE UNIQUE INDEX publisher_name_idx ON publishers (name);

CREATE TYPE file_type AS ENUM ('AUDIO', 'TEXT', 'IMAGE');

CREATE TABLE IF NOT EXISTS books
(
    id               INT PRIMARY KEY NOT NULL,
    created_at       TIMESTAMP       NOT NULL,
    modified_at      TIMESTAMP,
    version          INT,
    name             VARCHAR(255)    NOT NULL,
    author           VARCHAR(255)    NOT NULL,
    description      VARCHAR(2047)   NOT NULL,
    publication_year INT             NOT NULL,
    file_name        VARCHAR(255)    NOT NULL,
    file_extension   VARCHAR(17)     NOT NULL,
    file_type        file_type       NOT NULL,
    source_url       VARCHAR(255)    NOT NULL,
    image_url        VARCHAR(255),
    language         VARCHAR(33)     NOT NULL,
    narrator         VARCHAR(255),
    translator       VARCHAR(255),
    genre            VARCHAR(31)     NOT NULL,
    publisher_id     INT             NOT NULL,
    foreign key (publisher_id) REFERENCES publishers (id) ON DELETE CASCADE
);

CREATE UNIQUE INDEX books_nap_idx ON books (name, author, publication_year);