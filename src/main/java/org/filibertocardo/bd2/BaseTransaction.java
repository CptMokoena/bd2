package org.filibertocardo.bd2;

import lombok.Getter;

import java.sql.*;
import static org.filibertocardo.bd2.ConcurrentTransactions.DB_URL;
import static org.filibertocardo.bd2.ConcurrentTransactions.DB_USER;
import static org.filibertocardo.bd2.ConcurrentTransactions.DB_PASS;

/**
 * Dummy transaction that prints a start message, waits for a random time
 * (up to 100ms) and finally prints a status message at termination.
 */
@Getter
public abstract class BaseTransaction extends Thread {

    // identifier of the transaction
    private final int transactionId;
    protected Connection conn;
    private Savepoint savepoint;

    BaseTransaction(int transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public final void run() {
        createConnection();

        System.out.printf("%s [%d] started", this.getClass().getSimpleName(), transactionId);

        try {
            this.savepoint = this.conn.setSavepoint();
        } catch (Exception e) {
            System.err.printf("Cannot set a savepoint: "+ e.getMessage());
        }

        simulateOps();
        
        try {
            executeTransaction();
            this.conn.commit();
        } catch(Exception e) {
            System.err.printf("Transaction error: " +  e.getMessage());
            try {
                this.conn.rollback(this.savepoint);
            } catch(Exception ex) {
                System.err.printf("Cannot rollback tho the previous savepoint:" + ex.getMessage());
            }
        }

        /********** CODICE DA MODIFICARE PER REALIZZARE EFFETTIVE TRANSAZIONI *************
         try{
         // PreparedStatement st1 = conn.prepareStatement("ADD YOUR STATEMENT HERE");
         // st1.executeUpdate();     SE LO STATEMENT E' UN UPDATE
         // st1.executeQuery();       SE LO STATEMENT E' UNA QUERY
         }catch(SQLException se){
         se.printStackTrace();
         }catch(Exception e){
         e.printStackTrace();
         }
         *************************************************************************************/


        System.out.printf("%s [%d] terminated", this.getClass().getSimpleName(), transactionId);
        closeConnection();
    }

    protected abstract void executeTransaction() throws SQLException;

    protected void simulateOps() {
        int ms = (int) (Math.random() * 100);
        try {
            sleep(ms);
        } catch(Exception ignored) {}
    }

    private void createConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            this.conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            this.conn.setAutoCommit(false);
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void closeConnection() {
        try {
            this.conn.close();
            this.conn = null;
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
