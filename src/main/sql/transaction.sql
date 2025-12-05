--(a) ciascuna transazione dovrà contenere almeno 3 operazioni
-- (b) una transazione dovrà contenere solo operazioni di lettura
-- (c) due transazioni dovranno contenere almeno due operazioni di aggiornamento (inserimenti, cancellazioni, modifiche) che coinvolgano un
-- sottoinsieme delle tuple di una o più tabella della base di dati
-- (d) per ogni transazione dovrà essere scelto il livello di isolamento ritenuto più adeguato, giustificandone la scelta
-- (e) almeno due transazioni devono operare in scrittura su un insieme comune di tuple
-- (f) almeno due transazioni devono operare in lettura su un insieme comune di tuple
-- (g) almeno due transazioni devono operare rispettivamente in lettura e scrittura su un insieme comune di tuple
-- ( h) almeno una transazione deve leggere almeno due volte in punti diversi del codice uno stesso insieme di tuple
-- (i) per ogni transazione dovrà essere individua to un adeguato livello di isolamento


-- 1
begin transaction;
set transaction read commited;

insert into games (code, name, description, price)
values ('OChEQjTJNNcoYhAJoO4zdjiGxA4oF8fJEgAM', 'Vermintide 2', 'Il sequel del successo di critica Vermintide è un gioco d’azione corpo a corpo visivamente sbalorditivo e innovativo che supera i confini della modalità co-op in prima persona. Unisciti alla battaglia ora!', 27.99);

insert into achievements ("name", description, difficulty, game)
values ('V2_My_First_Wargear', 'Equip a Common item', 1, 'OChEQjTJNNcoYhAJoO4zdjiGxA4oF8fJEgAM');

insert into user_game ("user", game, purchase_date, hours_played )
select u.username, 'OChEQjTJNNcoYhAJoO4zdjiGxA4oF8fJEgAM', now(), 0.0
from users u
where u.email ilike '%beta_tester%'

commit;

-- 2
begin transaction;
set transaction repeatable read, read only;

select "user", sum(hours_played) as total_user_hours
from user_game
group by "user" 
order by total_user_hours desc
limit 10;

select game, sum(hours_played) as total_hours_played, avg(hours_played) as avg_hours_per_game
from user_game
group by game;

select "user", sum(hours_played) as total_user_hours
from user_game
group by "user" 
order by total_user_hours desc
limit 10;

commit;

-- 3
begin transaction;
set transaction serializable;

select hours_played from user_game where "user" = 'user_1' and game = 'OChEQjTJNNcoYhAJoO4zdjiGxA4oF8fJEgAM';

update user_game set hours_played = hours_played + 5.50
where "user" = 'user_1' and game = 'OChEQjTJNNcoYhAJoO4zdjiGxA4oF8fJEgAM';

insert into user_achievement ("user", achievement, unlocked_date, unlocked_at_played_hours )
values ('user_1', 'V2_My_First_Wargear', now(), (select hours_played from user_game where "user" = 'user_1' and game = 'OChEQjTJNNcoYhAJoO4zdjiGxA4oF8fJEgAM') );

commit;

