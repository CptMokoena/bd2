-- 1) interrotta dopo 5 m
-- full scan of games, achievements, user_game, users

-- 2) 1s 515ms
-- full scan of user_game, games, users

-- 3) 560ms
-- full scan of games * 2

-- 4) 979ms
-- full scan of user_game, games, users

-- 5) 464ms
-- full scan of user_achievement, users

-- 6) 1s 262ms
-- full scan of user_game, users

-- 7) interrotta dopo 5 m
-- full scan of games, achievements, user_achievements, achievements * 2

-- 8) 1s 620ms
-- full scan of user_achievements, achievements, users

CREATE UNIQUE INDEX px_users ON users(username);
CREATE UNIQUE INDEX px_games ON games(code);
CREATE UNIQUE INDEX px_achievements ON achievements(name);
CREATE UNIQUE INDEX px_user_game ON user_game("user", game);
CREATE UNIQUE INDEX px_user_achievement ON user_achievement(achievement, "user");

CREATE INDEX idx__user_game__hours_per_game_per_user ON user_game(hours_played, game, "user");

CREATE INDEX idx__games__price ON games(price);

CREATE INDEX idx__user_achievement__hours ON user_achievement(unlocked_at_played_hours);

CREATE INDEX idx__achievements__difficulty ON achievements(difficulty);

CREATE INDEX idx__achievements__game ON achievements(game, name);



