package org.filibertocardo.bd2;

import java.sql.Connection;
import java.sql.SQLException;

public class Transaction2 extends BaseTransaction {

    public Transaction2(int transactionId) {
        super(transactionId);
        this.isolationLevel = Connection.TRANSACTION_REPEATABLE_READ;
    }

    @Override
    protected void executeTransaction() throws SQLException {
        var s1 = this.conn.prepareStatement(
                "select \"user\", sum(hours_played) as total_user_hours from user_game group by \"user\" order by total_user_hours desc"
        );
        s1.setMaxRows(10);
        s1.execute();

        simulateOps();

        var s2 = this.conn.prepareStatement(
                "select game, sum(hours_played) as total_hours_played, avg(hours_played) as avg_hours_per_game\n" +
                    "from user_game\n" +
                    "group by game"
        );
        s2.execute();

        simulateOps();
        
        var s3 = this.conn.prepareStatement(
                "select \"user\", sum(hours_played) as total_user_hours from user_game group by \"user\" order by total_user_hours desc"
        );
        s3.setMaxRows(10);
        s3.execute();
    }
}
