DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS games;
DROP TABLE IF EXISTS achievements;
DROP TABLE IF EXISTS user_achievement;
DROP TABLE IF EXISTS user_game;
create table users (
    username VARCHAR(50),
    password VARCHAR(50),
    email VARCHAR(50),
    gender VARCHAR(50),
    phone VARCHAR(50)
);
create table games
(
	code varchar(20),
	name varchar(100),
	description text,
	price numeric(8,2)
);
create table achievements
(
	name varchar(100),
	description text,
	difficulty int4,
	game varchar(20)
);
create table user_achievement
(
	"user" varchar(50),
	achievement varchar(100),
	unlocked_date timestamp,
	unlocked_at_played_hours numeric(8,2)
);
create table user_game
(
	"user" varchar(50),
	game varchar(100),
	purchase_date timestamp,
	hours_played numeric(8,2)
);
