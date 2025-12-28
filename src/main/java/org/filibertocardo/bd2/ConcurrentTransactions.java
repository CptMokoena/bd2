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
            int transactionType = i % MAX_TRANSACTIONS;
            // System.out.printf("Creating transaction of type %d for %d\n", transactionType, i);
            if (transactionType == 0) {
                trans[i] = new Transaction1(i+1);
            } else if (transactionType == 1) {
                trans[i] = new Transaction2(i+1);
            } else if (transactionType == 2) {
                trans[i] = new Transaction3(i+1);
            }
        }

        System.out.printf("Executing transactions...\n\n\n");

        // start all transactions using a connection pool
        ExecutorService pool = Executors.newFixedThreadPool(maxConcurrent);
        for (int i = 0; i < trans.length; i++) {
            BaseTransaction currentTransaction = trans[i];
            // System.out.printf("Executing transaction %s in %d\n", currentTransaction, i);
            pool.execute(currentTransaction);
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

