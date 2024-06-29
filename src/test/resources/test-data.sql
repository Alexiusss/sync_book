DELETE FROM publishers;

INSERT INTO publishers (id, created_at, modified_at, version, name, foundation_year, city)
VALUES ('100001', now(), null, 0, 'Publisher 1', '2001', 'City 1'),
       ('100002', now(), null, 0, 'Publisher 2', '2002', 'City 2'),
       ('100003', now(), null, 0, 'Publisher 3', '2003', 'City 3')