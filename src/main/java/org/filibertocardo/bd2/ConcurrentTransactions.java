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
    public static final String DB_URL = "jdbc:postgresql://localhost:5432/bd2";
    public static final String DB_USER = "bd2";
    public static final String DB_PASS = "passwordSicura";

    public static void main(String[] args) {
        // read command line parameters
        if (args.length != 2) {
            System.err.println("params: numThreads maxConcurrent");
            System.exit(-1);
        }
        int numThreads = Integer.parseInt(args[0]);
        int maxConcurrent = Integer.parseInt(args[1]);

        System.out.println(String.format("Starting program with numThread=%d and maxConcurrent=%d", numThreads, maxConcurrent));

        // create numThreads transactions
        BaseTransaction[] trans = new BaseTransaction[numThreads];
        for (int i = 0; i < trans.length; i++) {
            if (i % MAX_TRANSACTIONS == 0) {
                trans[i] = new Transaction1(i+1);
            } else if (i % MAX_TRANSACTIONS == 1) {
                trans[1] = new Transaction2(i+1);
            } else if (i % MAX_TRANSACTIONS == 2) {
                trans[2] = new Transaction3(i+1);
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
    }
}

