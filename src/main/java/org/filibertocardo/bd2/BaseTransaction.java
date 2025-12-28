package org.filibertocardo.bd2;

import lombok.Getter;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Random;

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
    protected int isolationLevel;

    BaseTransaction(int transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public final void run() {
        createConnection();

        log("started");


        try {
            this.savepoint = this.conn.setSavepoint();
        } catch (Exception e) {
            System.err.printf("Cannot set a savepoint: %s\n", e.getMessage());
            closeConnection();
            return;
        }

        simulateOps();
        
        try {
            executeTransaction();
            this.conn.commit();
        } catch(Exception e) {
            System.err.printf("Transaction error: %s\n", e.getMessage());
            try {
                if (this.savepoint != null)
                    this.conn.rollback(this.savepoint);
                else
                    this.conn.rollback();
            } catch(Exception ex) {
                System.err.printf("Cannot rollback tho the previous savepoint: %s\n", ex.getMessage());
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


        log("terminated");
        closeConnection();
    }

    protected void log(String message) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:sss.SS");
        System.out.printf("%s - [%d] %s : %s\n", sdf.format(new java.util.Date()), transactionId, this.getClass().getSimpleName(), message);
    }

    protected abstract void executeTransaction() throws SQLException;

    protected void simulateOps() {
        int ms = (int) (Math.random() * 100);
        try {
            sleep(ms);
        } catch(Exception ignored) {}
    }

    protected String generateRandomString(int length) {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < length) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

    private void createConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            this.conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            this.conn.setAutoCommit(false);
            this.conn.setTransactionIsolation(isolationLevel);
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
