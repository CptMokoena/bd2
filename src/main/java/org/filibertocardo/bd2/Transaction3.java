package org.filibertocardo.bd2;

import java.sql.Connection;
import java.sql.SQLException;

public class Transaction3 extends BaseTransaction {

    public Transaction3(int transactionId, Connection conn) {
        super(transactionId, conn);
    }

    @Override
    protected void executeTransaction() throws SQLException {
        String user = "user_1";
        String game = "OChEQjTJNNcoYhAJoO4zdjiGxA4oF8fJEgAM";
        String achievement = "V2_My_First_Wargear";

        var s1 = this.conn.prepareStatement(
                "select hours_played from user_game where \"user\" = ? and game = ?"
        );
        s1.setString(1, user);
        s1.setString(2, game);
        s1.execute();

        var s2 = this.conn.prepareStatement(
                "update user_game set hours_played = hours_played + ?\n" +
                    "where \"user\" = ? and game = ?"
        );
        s2.setDouble(1, 5.50d);
        s2.setString(2, user);
        s2.setString(3, game);
        s2.executeUpdate();

        var s3 = this.conn.prepareStatement(
                "insert into user_achievement (\"user\", achievement, unlocked_date, unlocked_at_played_hours )\n" +
                    "values (?, ?, now(), (select hours_played from user_game where \"user\" = ? and game = ?) );\n"
        );
        s3.setString(1, user);
        s3.setString(2, achievement);
        s3.setString(3, user);
        s3.setString(4, game);
        s3.executeUpdate();
    }
}
