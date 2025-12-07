package org.filibertocardo.bd2;

import lombok.Getter;

import java.sql.*;

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

    BaseTransaction(int transactionId, Connection conn) {
        this.transactionId = transactionId;
        this.conn = conn;
    }

    @Override
    public final void run() {
        System.out.println("transaction " + transactionId + " started");

        try {
            this.savepoint = this.conn.setSavepoint();
        } catch (Exception e) {
            System.err.println("Cannot set a savepoint: "+ e.getMessage());
        }

        simulateOps();
        
        try {
            executeTransaction();
            this.conn.commit();
        } catch(Exception e) {
            System.err.println("Transaction error: " +  e.getMessage());
            try {
                this.conn.rollback(this.savepoint);
            } catch(Exception ex) {
                System.err.println("Cannot rollback tho the previous savepoint:" + ex.getMessage());
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


        System.out.println("transaction " + transactionId + " terminated");
    }

    protected abstract void executeTransaction() throws SQLException;

    protected void simulateOps() {
        int ms = (int) (Math.random() * 100);
        try {
            sleep(ms);
        } catch(Exception ignored) {}
    }

}
