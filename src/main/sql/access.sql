-- 1)
-- base_user: utente base che utilizza l'applicazione
-- game_owner: può creare giochi e gestirli, in più può creare achievements per i suoi giochi
-- support: da supporto a user e game_owner
-- data_admin: DIO

-- data_admin > support > base_user, game_owner


-- 2)
CREATE ROLE base_user;
CREATE ROLE game_owner;
CREATE ROLE support;
CREATE ROLE data_admin;

GRANT base_user TO support;
GRANT game_owner TO support;
GRANT support TO data_admin;

CREATE USER anna WITH PASSWORD 'secret_password';
CREATE USER maria WITH PASSWORD 'secret_password';
CREATE USER davide WITH PASSWORD 'secret_password';
CREATE USER lorenzo WITH PASSWORD 'secret_password';
CREATE USER giampiero WITH PASSWORD 'secret_password';
CREATE USER jotaro WITH PASSWORD 'secret_password';
GRANT base_user TO anna;
GRANT base_user TO maria;
GRANT game_owner TO davide;
GRANT game_owner TO lorenzo;
GRANT support TO giampiero;
GRANT data_admin TO jotaro;


-- 3)
-- base_user
GRANT select, update ON users TO base_user;
GRANT select, insert, update(hours_played) ON user_game TO base_user;
GRANT select ON games TO base_user;
GRANT select, insert ON user_achievement TO base_user;
GRANT select ON achievements TO base_user;

-- game_owner
GRANT select, insert, update ON games TO game_owner;
GRANT select, insert, update ON achievements TO game_owner;

-- support
GRANT insert ON users TO support;
GRANT update ON user_game TO support;
GRANT update ON user_achievement TO support;

-- data_admin
GRANT delete ON users, games, achievements, user_game, user_achievement TO data_admin;


-- 4)
CREATE VIEW game_owned_stats as SELECT g.name as game_name, COUNT(ug.game) as user_count
                                FROM games g
                                         JOIN user_game ug on ug.game = g.code
                                GROUP BY g.name;
CREATE VIEW achievement_obtained_stats as SELECT a.name as achievement_name, COUNT(ua.user) as user_count, COALESCE(ROUND(AVG(ua.unlocked_at_played_hours),2),0) as avg_unlocked_at_played_hours
                                          FROM achievements a
                                                   LEFT JOIN user_achievement ua on ua.achievement = a.name
                                          GROUP BY a.name;
GRANT select ON game_owned_stats TO game_owner;
GRANT select ON achievement_obtained_stats TO game_owner;


-- 5)
-- cascade: rimuove i permessi dal ruolo specificato e se il ruolo era in possesso del grant option rimuove i permessi assegnati dal ruolo specificato
-- restrict: rimuove i permessi dal ruolo specificato sse il ruolo non ha assegnato permessi
REVOKE select ON achievement_obtained_stats FROM game_owner;
REVOKE game_owner FROM support;
