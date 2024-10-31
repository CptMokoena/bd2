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


-- 5) Selezionare gli utenti che hanno sbloccato un achievement nelle prime ore di gioco
-- Condizioni rispettate: a, c, m
SELECT DISTINCT u.username
FROM users u
    JOIN user_achievement ua on u.username = ua."user"
WHERE ua.unlocked_at_played_hours <= 10;


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
SELECT g.name
FROM games g
WHERE ((SELECT count(a.name)
        FROM achievements a
        WHERE a.game = g.code) / 3) <= (SELECT count(aos.achievement_name)
                                        FROM achievement_obtained_stats aos
                                            JOIN achievements a ON a.game = g.code AND aos.achievement_name = a.name
                                        WHERE user_count = 0);


-- 8) Selezionare gli utenti che hanno ottenuto un achievement di difficoltà = 5
-- Condizioni rispettate: a, c, d, m
SELECT DISTINCT u.username
FROM users u
    JOIN user_achievement ua on ua."user" = u.username
    JOIN achievements a on ua.achievement = a.name
WHERE a.difficulty = 5
ORDER BY u.username;
