package org.filibertocardo.bd2;

import java.sql.Connection;
import java.sql.SQLException;

public class Transaction1 extends BaseTransaction {

    public Transaction1(int transactionId, Connection conn) {
        super(transactionId, conn);
    }
    
    @Override
    protected void executeTransaction() throws SQLException {
        String game = "OChEQjTJNNcoYhAJoO4zdjiGxA4oF8fJEgAM";

        var s1 = this.conn.prepareStatement(
                "insert into games (code, name, description, price) values(?, ?, ?, ?)"
        );
        s1.setString(1, game);
        s1.setString(2, "Vermintide 2");
        s1.setString(3, "Il sequel del successo di critica Vermintide è un gioco d’azione corpo a corpo visivamente sbalorditivo e innovativo che supera i confini della modalità co-op in prima persona. Unisciti alla battaglia ora!");
        s1.setDouble(4, 27.99);
        s1.executeUpdate();

        var s2 = this.conn.prepareStatement(
                "insert into achievements (\"name\", description, difficulty, game) values(?, ?, ?, ?)"
        );
        s2.setString(1, "V2_My_First_Wargear");
        s2.setString(2, "Equip a Common item");
        s2.setInt(3, 1);
        s2.setString(4, game);
        s2.executeUpdate();

        var s3 = this.conn.prepareStatement(
                "insert into user_game (\"user\", game, purchase_date, hours_played ) \n" +
                    "select u.username, ?, now(), 0.0 \n" +
                    "from users u where where u.email ilike ?"
        );
        s3.setString(1, game);
        s3.setString(2, "%beta_tester%");
        s3.executeUpdate();
    }
}
