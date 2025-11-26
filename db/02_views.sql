
CREATE VIEW game_owned_stats as SELECT g.name as game_name, COUNT(ug.game) as user_count
                                FROM games g
                                         JOIN user_game ug on ug.game = g.code
                                GROUP BY g.name;
CREATE VIEW achievement_obtained_stats as SELECT a.name as achievement_name, COUNT(ua.user) as user_count, COALESCE(ROUND(AVG(ua.unlocked_at_played_hours),2),0) as avg_unlocked_at_played_hours
                                          FROM achievements a
                                                   LEFT JOIN user_achievement ua on ua.achievement = a.name
                                          GROUP BY a.name;


