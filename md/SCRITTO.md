---
dest: ./result.pdf
pdf_options:
    format: A4
---

# Progetto BD2
Authors:
* Iacopo Filiberto <iacopo.filiberto@gmail.com> (S4472942)
* Davide Cardo <davidecardo98@gmail.com> (S4516525)

## PARTE A
1) Come DBMS abbiamo scelto di utilizzare PostgreSQL
2) Abbiamo scelto di modellare una possibile base dati di un sito web volto al
tenere traccia degli obbiettivi di videogiochi ottenuti dagli utenti della piattaforma.

```mermaid
erDiagram
    USERS {
        string(50) username PK
        string(50) password
        string(50) email
        string(50) gender
        string(50) phone
    }
    GAMES {
        string(20) code PK
        string(100) name
        text description
        double price
    }
    ACHIEVEMENTS {
        string(100) name PK
        text description
        int difficulty
        string(20) game
    }
    USER_ACHIEVEMENT {
        string(50) user PK
        string(100) achievement PK
        timestamp unlocked_date
        double unlocked_at_played_hours
    }
    USER_GAME {
        string(50) user PK
        string(100) game PK
        timestamp purchase_date
        double hours_played
    }
    USERS 1 to zero or more USER_GAME : "possiede"
    USER_GAME zero or more to 1 GAMES : "è posseduto"
    USERS 1 to zero or more USER_ACHIEVEMENT : "ottiene"
    USER_ACHIEVEMENT zero or more to 1 ACHIEVEMENTS : "è ottenuto"
    GAMES 1 to zero or more ACHIEVEMENTS : "è di"
```

## PARTE B
## PARTE C
## PARTE D
