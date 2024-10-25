-- 1) 5s
-- full scan of games, achievements, user_game, users
-- 2) 50ms
-- full scan of user_game, games, users
-- 3) 21ms
-- full scan of games * 2
-- 4) 46ms
-- full scan of user_game, games, users
-- 5) 65ms
-- full scan of user_achievement, users
-- 6) 259ms
-- full scan of user_game, users
-- 7) 1m 55s
-- full scan of games, achievements, user_achievements, achievements * 2
-- 8) 163ms
-- full scan of user_achievements, achievements, users

CREATE INDEX idx__game__achievement ON achievements(game);
CREATE UNIQUE INDEX idx__name__achievement ON achievements(name);

CREATE INDEX idx__achievement__user_achievement ON user_achievement(achievement)

CREATE INDEX idx__code__games ON games(code)
