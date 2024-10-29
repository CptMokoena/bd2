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

CREATE VIEW game_owned_stats as SELECT g.name as game_name, COUNT(ug.game) as user_count
                                FROM games g
                                         JOIN user_game ug on ug.game = g.code
                                GROUP BY g.name;
CREATE VIEW achievement_obtained_stats as SELECT a.name as achievement_name, COUNT(ua.user) as user_count, COALESCE(ROUND(AVG(ua.unlocked_at_played_hours),2),0) as avg_unlocked_at_played_hours
                                          FROM achievements a
                                                   LEFT JOIN user_achievement ua on ua.achievement = a.name
                                          GROUP BY a.name;