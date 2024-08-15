drop database if exists mtg_app;
create database mtg_app;
use mtg_app;

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

