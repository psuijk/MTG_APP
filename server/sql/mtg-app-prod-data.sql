use mtg_app;

INSERT INTO player (player_id, first_name, last_name, username)
    VALUES
    (1, 'Alice', 'Smith', 'alice@domain.com'),
    (2, 'Bob', 'Johnson', 'bob@domain.com'),
    (3, 'Charlie', 'Williams', 'charlie@domain.com'),
    (4, 'Dana', 'Brown', 'dana@domain.com'),
    (5, 'Eli', 'Jones', 'eli@domain.com'),
    (6, 'Test', 'User', 'test@user.com'),
    (7, 'Steve', 'Vai', null),
    (8, 'Joe', 'Bob', null);

insert into app_role (`name`) values
    ('USER'),
    ('ADMIN');

-- passwords are set to "P@ssw0rd!"
INSERT INTO app_user (username, password_hash, disabled)
    VALUES
    
    ('bob@domain.com', '$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa', 0),
    ('charlie@domain.com', '$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa', 0),
    ('dana@domain.com', '$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa', 0),
    ('eli@domain.com', '$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa', 0);

insert into app_user_role
    values
    (1, 2),
    (2, 1);

INSERT INTO deck (deck_id, player_id, name, active, commander_id)
VALUES
    (1, 1, 'Dragons Fury', TRUE, 'Nicol Bolas'),
    (2, 2, 'Elves of the Wood', TRUE, 'Ezuri'),
    (3, 3, 'Mystic Waters', FALSE, 'Thassa'),
    (4, 4, 'Angels and Demons', TRUE, 'Kaalia of the Vast'),
    (5, 5, 'Goblin Horde', TRUE, 'Krenko'),
    (6, 2, 'test', FALSE, 'elfy');

INSERT INTO game (game_id, date_played, winner_deck_id, player_count)
VALUES
    (1, '2024-08-01', 1, 4),
    (2, '2024-08-02', 2, 3),
    (3, '2024-08-03', 3, 2),
    (4, '2024-08-04', 4, 5),
    (5, '2024-08-05', 5, 4),
    (6, '2024-08-05', 5, 4);

INSERT INTO game_deck (game_deck_id, game_id, deck_id, position)
VALUES
    (1, 1, 1, 1),
    (2, 1, 2, 2),
    (3, 1, 3, 3),
    (4, 1, 4, 4),
    (5, 2, 2, 1),
    (6, 2, 3, 2),
    (7, 3, 3, 1),
    (8, 3, 4, 2),
    (9, 4, 4, 1),
    (10, 4, 5, 2);
