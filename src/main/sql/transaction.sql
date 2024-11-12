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
begin transaction
select * from public.achievements where difficulty > 3;
select * from public.games where price > 35 order by name asc;
select count(users.username) from public.users;
commit;