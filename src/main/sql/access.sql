-- 1)
-- base_user: utente base che utilizza l'applicazione
-- game_owner: può creare giochi e gestirli, in più può creare achievements per i suoi giochi
-- support: da supporto a user e game_owner
-- data_admin: 

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
CREATE USER iacopo WITH PASSWORD 'secret_password';
CREATE USER luca WITH PASSWORD 'secret_password';
GRANT base_user TO anna;
GRANT base_user TO maria;
GRANT base_user TO iacopo;
GRANT base_user TO luca;
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
GRANT select ON game_owned_stats TO game_owner;
GRANT select ON achievement_obtained_stats TO game_owner;


-- 5)
-- cascade: rimuove i permessi dal ruolo specificato e se il ruolo era in possesso del grant option rimuove i permessi assegnati dal ruolo specificato
-- restrict: rimuove i permessi dal ruolo specificato sse il ruolo non ha assegnato permessi
REVOKE select ON achievement_obtained_stats FROM game_owner;
REVOKE game_owner FROM support;


GRANT delete ON user_achievement TO iacopo WITH GRANT OPTION;
--    bd2 -- delete_user_achievement (wgo) --> iacopo
SET ROLE iacopo;
GRANT delete ON user_achievement TO luca;
--    bd2 --delete_user_achievement_wgo--> iacopo --delete_user_achievement--> luca
RESET ROLE;
REVOKE delete ON user_achievement FROM iacopo CASCADE;
--SELECT grantee, privilege_type, grantor FROM information_schema.role_table_grants WHERE table_name = 'user_achievement';