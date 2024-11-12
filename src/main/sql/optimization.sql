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

CREATE INDEX idx__game__achievement ON achievements(game);
CREATE UNIQUE INDEX idx__name__achievement ON achievements(name);

CREATE INDEX idx__achievement__user_achievement ON user_achievement(achievement)

CREATE INDEX idx__code__games ON games(code)
