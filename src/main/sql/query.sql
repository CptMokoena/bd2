-- (a) almeno una query con condizione di selezione ad alta selettività
-- (b) almeno una query con condizione di selezione a bassa selettività
-- (c) almeno una query con join di due tabelle
-- (d) almeno una query con join di tre tabelle
-- (e) almeno una query con raggruppamento
-- (f) almeno una query con sottointerrogazione semplice
-- (g) almeno una query con sottointerrogazione correlata
-- (h) almeno una query con almeno un join e almeno due condizioni di selezione
-- (i) almeno un attributo deve comparire nelle condizioni di selezione di almeno due query
-- (l) almeno un attributo per ogni tabella deve essere coinvolto in una condizione di selezione
-- (m) almeno una query deve contenere la clausola DISTINCT

-- 1) Query che restituisce gli utenti e il numero di giochi che posseggono,
-- i quali possiedono almeno un achievements
-- Condizioni rispettate : a, c, d, e, g,
SELECT u.username, COUNT(ug.user)
FROM users u
     JOIN user_game ug ON ug.user = u.username
     JOIN games g ON g.code = ug.game
WHERE g.code = ANY(SELECT a.game FROM achievements a WHERE a.game = g.code)
GROUP BY u.username
ORDER BY u.username;


-- 2) Seleziona tutti gli utenti che hanno giocato più di 449 ore ad un gioco
-- Condizioni rispettate: b, c, d, i
SELECT u.username, g.name, ug.hours_played
FROM users u
    JOIN user_game ug on u.username = ug."user"
    JOIN games g on g.code = ug.game
WHERE ug.hours_played > 449;


-- 3) Seleziona tutti i giochi il cui prezzo è maggiore della media dei prezzi.
-- Condizione rispettata: f
SELECT name, price FROM games WHERE price >= (SELECT avg(price) FROM games);


-- 4) Seleziona tutti gli utenti che hanno giocato più di 449 ore ad un gioco e che costa tra 300 e 500
-- Condizioni rispettate: b, c, d, h, i
SELECT u.username, g.name, ug.hours_played
FROM users u
         JOIN user_game ug on u.username = ug."user"
         JOIN games g on g.code = ug.game
WHERE ug.hours_played > 449 AND g.price BETWEEN 300 AND 500;


-- 5) Selezionare gli utenti che hanno sbloccato un achievement nelle prime 3 ore di gioco
-- Condizioni rispettate: a, c, m
SELECT DISTINCT u.username
FROM users u
    JOIN user_achievement ua on u.username = ua."user"
WHERE ua.unlocked_at_played_hours <= 3;


-- 6) Query che restituisce gli utenti che possiedono almeno 10 giochi
-- Condizioni rispettate : a, c, e,
SELECT u.username, COUNT(ug.user)
FROM users u
         JOIN user_game ug ON ug.user = u.username
GROUP BY u.username
HAVING COUNT(ug.user) >= 10
ORDER BY u.username;


-- 7) Selezionare il nome di tutti i giochi i quali hanno almeno 1/3 degli achievement non sbloccati
-- Condizioni rispettate: c, g,
SELECT
    g.name
FROM
    games g
JOIN
    (
        SELECT
            a.game AS game_code,
            COUNT(a.name) AS total_achievements,
            COUNT(CASE WHEN ua."user" IS NULL THEN 1 END) AS unlocked_by_zero_users
        FROM
            achievements a
        LEFT JOIN 
            user_achievement ua ON ua.achievement = a.name
        GROUP BY
            a.game
        HAVING 
            COUNT(DISTINCT ua.achievement) IS NOT NULL 
            OR COUNT(a.name) > 0
    ) AS AchCounts ON g.code = AchCounts.game_code
WHERE
    AchCounts.unlocked_by_zero_users >= (AchCounts.total_achievements / 3.0);


-- 8) Selezionare gli utenti che hanno ottenuto un achievement di difficoltà = 5
-- Condizioni rispettate: a, c, d, m
SELECT DISTINCT u.username
FROM users u
    JOIN user_achievement ua on ua."user" = u.username
    JOIN achievements a on ua.achievement = a.name
WHERE a.difficulty = 5
ORDER BY u.username;
