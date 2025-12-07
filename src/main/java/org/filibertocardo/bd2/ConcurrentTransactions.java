package org.filibertocardo.bd2;

/*
Codice di partenza per transazioni concorrenti —
Adattato da Nikolas Augsten 
 */

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.sql.*;

/**
 * <p>
 * Run numThreads transactions, where at most maxConcurrent transactions
 * can run in parallel.
 *
 * <p>params: numThreads maxConcurrent
 */
public class ConcurrentTransactions {

    private static final int MAX_TRANSACTIONS = 3;
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/bd2";
    private static final String DB_USER = "bd2";
    private static final String DB_PASS = "passwordSicura";

    public static void main(String[] args) {

        Connection conn = null;

        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            conn.setAutoCommit(false);
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
        BaseTransaction[] trans = new BaseTransaction[numThreads];
        for (int i = 0; i < trans.length; i++) {
            if (i % MAX_TRANSACTIONS == 0) {
                trans[i] = new Transaction1(i+1, conn);
            } else if (i % MAX_TRANSACTIONS == 1) {
                trans[1] = new Transaction2(i+1, conn);
            } else if (i % MAX_TRANSACTIONS == 2) {
                trans[2] = new Transaction3(i+1, conn);
            }
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

        try {
            conn.close();
        } catch(SQLException se){
            se.printStackTrace();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}

