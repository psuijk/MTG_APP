drop database if exists mtg_app_test;
create database mtg_app_test;
use mtg_app_test;

create table app_user (
    app_user_id int primary key auto_increment,
    username varchar(50) not null unique,
    password_hash varchar(2048) not null,
    disabled boolean not null default(0)
);

create table app_role (
    app_role_id int primary key auto_increment,
    `name` varchar(50) not null unique
);

create table app_user_role (
    app_user_id int not null,
    app_role_id int not null,
    constraint pk_app_user_role
        primary key (app_user_id, app_role_id),
    constraint fk_app_user_role_user_id
        foreign key (app_user_id)
        references app_user(app_user_id),
    constraint fk_app_user_role_role_id
        foreign key (app_role_id)
        references app_role(app_role_id)
);

CREATE TABLE player (
    player_id INT AUTO_INCREMENT PRIMARY KEY,
    `first_name` varchar (30) not null,
    `last_name` varchar (30) not null,
    username varchar(50) unique,
    constraint fk_app_user_username
        foreign key(username)
        references app_user(username)
);

CREATE TABLE deck (
    deck_id INT AUTO_INCREMENT PRIMARY KEY,
    player_id INT,
    name VARCHAR(255) NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    commander_id VARCHAR(255),
    FOREIGN KEY (player_id) REFERENCES player(player_id)
);

CREATE TABLE game (
    game_id INT AUTO_INCREMENT PRIMARY KEY,
    date_played DATE NOT NULL,
    winner_deck_id INT,
    player_count INT NOT NULL,
    FOREIGN KEY (winner_deck_id) REFERENCES deck(deck_id)
);

CREATE TABLE game_deck (
    game_deck_id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    game_id INT not null,
    deck_id INT not null,
    `position` INT not null,
    FOREIGN KEY (game_id) REFERENCES game(game_id),
    FOREIGN KEY (deck_id) REFERENCES deck(deck_id)
);

delimiter //
create procedure set_known_good_state()
begin
    SET SQL_SAFE_UPDATES = 0;
    delete from game_deck;
    alter table game_deck auto_increment = 1;
    delete from game;
    alter table game auto_increment = 1;
    delete from deck;
    alter table deck auto_increment = 1;
    delete from player;
    alter table player auto_increment = 1;
    delete from app_user_role;
    delete from app_role;
    alter table app_role auto_increment = 1;
    delete from app_user;
    alter table app_user auto_increment = 1;
    SET SQL_SAFE_UPDATES = 1;

insert into app_role (`name`) values
    ('USER'),
    ('ADMIN');

-- passwords are set to "P@ssw0rd!"
INSERT INTO app_user (username, password_hash, disabled)
    VALUES
    ('john@smith.com', '$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa', 0),
    ('sally@jones.com', '$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa', 0),
    ('alice@domain.com', '$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa', 0),
    ('bob@domain.com', '$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa', 0),
    ('charlie@domain.com', '$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa', 0),
    ('dana@domain.com', '$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa', 0),
    ('eli@domain.com', '$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa', 0);

insert into app_user_role
    values
    (1, 2),
    (2, 1);

 INSERT INTO player (player_id, first_name, last_name, username)
    VALUES
    (1, 'Alice', 'Smith', 'alice@domain.com'),
    (2, 'Bob', 'Johnson', 'bob@domain.com'),
    (3, 'Charlie', 'Williams', 'charlie@domain.com'),
    (4, 'Dana', 'Brown', 'dana@domain.com'),
    (5, 'Eli', 'Jones', 'eli@domain.com'),
    (6, 'Steve', 'Vai', null);

INSERT INTO deck (deck_id, player_id, name, active, commander_id)
VALUES
    (1, 1, 'Dragons Fury', TRUE, 'Nicol Bolas'),
    (2, 2, 'Elves of the Wood', TRUE, 'Ezuri'),
    (3, 3, 'Mystic Waters', FALSE, 'Thassa'),
    (4, 4, 'Angels and Demons', TRUE, 'Kaalia of the Vast'),
    (5, 5, 'Goblin Horde', TRUE, 'Krenko');

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

    end //
delimiter ;
