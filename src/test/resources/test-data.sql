DELETE FROM publishers;

INSERT INTO publishers (id, created_at, modified_at, version, name, foundation_year, city)
VALUES ('100001', now(), null, 0, 'Publisher 1', '2001', 'City 1'),
       ('100002', now(), null, 0, 'Publisher 2', '2002', 'City 2'),
       ('100003', now(), null, 0, 'Publisher 3', '2003', 'City 3');

INSERT INTO books (id, created_at, modified_at, version, name, author, description, publication_year, source_url, image_url, file_name, file_extension, file_type, language, narrator, translator, genre, publisher_id)
VALUES (100004, now(), null, 0, 'Book 1', 'Book 1 author', 'Book 1 description', 2021, 'http://book1source.com/book1.fb2', 'http://book1image.com', 'book1', 'fb2', 'TEXT', 'ru', 'Book 1 narrator', 'Book 1 translator', 'DETECTIVE', 100001),
       (100005, now(), null, 0, 'Book 2', 'Book 2 author', 'Book 2 description', 2022, 'http://book2source.com/book2.mp2', 'http://book2image.com', 'book2', 'mp3', 'AUDIO', 'ru', null, null, 'HISTORY', 100002),
       (100006, now(), null, 0, 'Book 3', 'Book 3 author', 'Book 3 description', 2023, 'http://book3source.com/book3.epub', 'http://book3image.com', 'book3', 'epub','TEXT', 'en', null, null, 'FANTASY', 100003);

INSERT INTO bookmarks (id, created_at, modified_at, version, user_id, book_id, last_position)
VALUES (1000101, now(), null, 0, 'User_1_id', 100004, 11.11),
       (1000102, now(), null, 0, 'User_2_id', 100005, 22.22),
       (1000103, now(), null, 0, 'User_3_id', 100006, 44.44),
       (1000104, now(), null, 0, 'User_1_id', 100005, 55.55),
       (1000105, now(), null, 0, 'User_1_id', 100006, 66.66);