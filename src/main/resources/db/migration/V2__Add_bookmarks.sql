CREATE TABLE IF NOT EXISTS bookmarks
(
    id            INT PRIMARY KEY NOT NULL,
    created_at    TIMESTAMP       NOT NULL,
    modified_at   TIMESTAMP,
    version       INT,
    user_id       VARCHAR(255)    NOT NULL,
    book_id       INT             NOT NULL,
    last_position DECIMAL         NOT NULL,
    FOREIGN KEY (book_id) REFERENCES books (id) ON DELETE CASCADE
);

CREATE UNIQUE INDEX bookmarks_user_book_idx ON bookmarks (user_id, book_id);