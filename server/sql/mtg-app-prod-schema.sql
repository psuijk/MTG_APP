drop database if exists mtg_app;
create database mtg_app;
use mtg_app;

CREATE TABLE player (
    player_id INT AUTO_INCREMENT PRIMARY KEY,
    `first_name` varchar (30) not null,
    `last_name` varchar (30) not null,
    username varchar(50) unique
);

create table app_user (
    app_user_id int primary key auto_increment,
    username varchar(50) not null unique,
    password_hash varchar(2048) not null,
    disabled boolean not null default(0),
    constraint fk_player_username
        foreign key(username)
        references player(username)
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

CREATE TABLE deck (
    deck_id INT AUTO_INCREMENT PRIMARY KEY,
    player_id INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    commander_id VARCHAR(255),
    FOREIGN KEY (player_id) REFERENCES player(player_id)
);

CREATE TABLE game (
    game_id INT AUTO_INCREMENT PRIMARY KEY,
    date_played DATE NOT NULL,
    winner_deck_id INT NOT NULL,
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

-- TEST DATA INSERTION -- 

-- Insert players
INSERT INTO player (first_name, last_name, username) VALUES
('John', 'Doe', 'johndoe'),
('Jane', 'Smith', 'janesmith'),
('Bob', 'Brown', 'bobbrown'),
('Alice', 'Johnson', 'alice@google.com'),
('Charlie', 'Davis', 'charliedavis');

-- Insert app users
INSERT INTO app_user (username, password_hash) VALUES
('johndoe', 'hashedpassword1'),
('janesmith', 'hashedpassword2'),
('bobbrown', 'hashedpassword3'),
('alice@google.com', 'hashedpassword4');

-- Insert roles
INSERT INTO app_role (name) VALUES
('USER'),
('ADMIN');

-- Assign roles to users
INSERT INTO app_user_role (app_user_id, app_role_id) VALUES
(1, 1),
(2, 1),
(3, 1),
(4, 1),
(1, 2); -- John Doe is also an admin

-- Insert decks
INSERT INTO deck (player_id, name, active, commander_id) VALUES
(1, 'Five-Color Dragons', TRUE, 'Niv-Mizzet'),
(1, 'Superfriends Assemble', TRUE, 'Atraxa'),
(2, 'Vampire Horde', TRUE, 'Edgar Markov'),
(2, 'Elder Dragon Storm', TRUE, 'The Ur-Dragon'),
(3, 'Sacrifice Shenanigans', TRUE, 'Korvold'),
(3, 'Graveyard Reclamation', TRUE, 'Muldrotha'),
(4, 'Monk Mastery', TRUE, 'Narset'),
(4, 'Elemental Fury', TRUE, 'Omnath'),
(5, 'Frog Tribal', TRUE, 'The Gitrog Monster'),
(5, 'Angel of Vengeance', TRUE, 'Kaalia');



-- Insert games with playerCount of 4 or 5
INSERT INTO game (date_played, winner_deck_id, player_count) VALUES
('2024-08-01', 1, 4),
('2024-08-02', 2, 4),
('2024-08-03', 3, 4),
('2024-08-04', 4, 5),
('2024-08-05', 5, 5),
('2024-08-06', 6, 4),
('2024-08-07', 7, 5),
('2024-08-08', 8, 5),
('2024-08-09', 9, 4),
('2024-08-10', 10, 4);

-- Insert game_deck mappings
INSERT INTO game_deck (game_id, deck_id, position) VALUES
(1, 1, 1),
(1, 3, 2),
(1, 5, 3),
(1, 7, 4),
(2, 2, 1),
(2, 4, 2),
(2, 6, 3),
(2, 8, 4),
(3, 3, 1),
(3, 7, 2),
(3, 9, 3),
(3, 1, 4),
(4, 4, 1),
(4, 8, 2),
(4, 2, 3),
(4, 6, 4),
(4, 10, 5),
(5, 5, 1),
(5, 1, 2),
(5, 9, 3),
(5, 3, 4),
(5, 7, 5),
(6, 6, 1),
(6, 2, 2),
(6, 4, 3),
(6, 10, 4),
(7, 7, 1),
(7, 5, 2),
(7, 3, 3),
(7, 1, 4),
(7, 9, 5),
(8, 8, 1),
(8, 2, 2),
(8, 6, 3),
(8, 4, 4),
(8, 10, 5),
(9, 9, 1),
(9, 3, 2),
(9, 5, 3),
(9, 7, 4),
(10, 10, 1),
(10, 6, 2),
(10, 8, 3),
(10, 2, 4);

