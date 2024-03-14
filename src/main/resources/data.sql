MERGE INTO rating(id, name)
VALUES (1, 'G'),
       (2, 'PG'),
       (3, 'PG-13'),
       (4, 'R'),
       (5, 'NC-17');

MERGE INTO genre(id, name)
VALUES (1, 'Комедия'),
       (2, 'Драма'),
       (3, 'Мультфильм'),
       (4, 'Триллер'),
       (5, 'Документальный'),
       (6, 'Боевик');

INSERT INTO event_type(event_type_id, event_name)
    VALUES (1, 'LIKE'),
           (2, 'REVIEW'),
           (3, 'FRIEND');

INSERT INTO event_operation(event_operation_id, operation_name)
    VALUES (1, 'ADD'),
           (2, 'UPDATE'),
           (3, 'REMOVE');