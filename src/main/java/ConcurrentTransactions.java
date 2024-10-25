/*
Codice di partenza per transazioni concorrenti —
Adattato da Nikolas Augsten 
 */

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.sql.*;

/**
 * Dummy transaction that prints a start message, waits for a random time
 * (up to 100ms) and finally prints a status message at termination.
 */

class Transaction extends Thread {

    // identifier of the transaction
    int id;
    Connection conn;

    Transaction(int id, Connection conn) {
        this.id = id;
        this.conn = conn;
    }

    @Override
    public void run() {
        System.out.println("transaction " + id + " started");

        // replace this with a transaction
        int ms = (int) (Math.random() * 100);
        try {
            sleep(ms);
        } catch (Exception e) {
        }
        ;
        // end of portion to be replaced

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


        System.out.println("transaction " + id + " terminated");
    }

}

/**
 * <p>
 * Run numThreads transactions, where at most maxConcurrent transactions
 * can run in parallel.
 *
 * <p>params: numThreads maxConcurrent
 */
public class ConcurrentTransactions {

    public static void main(String[] args) {

        String url = "jdbc:postgresql://localhost:5432/bd2";
        String user = "bd2";
        String pass = "passwordSicura";

        Connection conn = null;

        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(url, user, pass);
//            PreparedStatement st = conn.prepareStatement("set search_path to account");
//            st.executeUpdate();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // read command line parameters
        if (args.length != 2) {
            System.err.println("params: numThreads maxConcurrent");
            System.exit(-1);
        }
        int numThreads = Integer.parseInt(args[0]);
        int maxConcurrent = Integer.parseInt(args[1]);

        // create numThreads transactions
        Transaction[] trans = new Transaction[numThreads];
        for (int i = 0; i < trans.length; i++) {
            trans[i] = new Transaction(i + 1, conn);
        }

        // start all transactions using a connection pool
        ExecutorService pool = Executors.newFixedThreadPool(maxConcurrent);
        for (int i = 0; i < trans.length; i++) {
            pool.execute(trans[i]);
        }
        pool.shutdown(); // end program after all transactions are done

        /**************************************
         CHIUSURA CONNESSIONE
         try {
         if (!pool.awaitTermination(10,TimeUnit.SECONDS))
         {
         pool.shutdownNow();
         try{
         conn.close();
         }catch(SQLException se){
         se.printStackTrace();
         }catch(Exception e){
         e.printStackTrace();
         }
         }
         } catch (InterruptedException e) {
         e.printStackTrace();
         }
         **************************************/
    }
}

