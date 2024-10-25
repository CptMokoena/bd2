/* Esempio di programma che si connette a DB attraverso JDBC e esegue una insert di conto  */


import java.sql.*;
import java.io.*;
import java.util.*;

class labo {


    public static void main(String[] args) {

        // APERTURA CONNESSIONE -- CODICE DA MODIFICARE CON VOSTRI DATI

        String url = "jdbc:postgresql://localhost:5432/bd2";
        //es. db bd2user_n
        String user = "bd2"; //es. bd2userxx
        String pass = "passwordSicura"; //es. BD2userxx


        Connection conn = null;

        // CARICAMENTO DRIVER

        try {
            Class.forName("org.postgresql.Driver");

            // CONNESSIONE

            conn = DriverManager.getConnection(url, user, pass);

            // EVENTUALE VARIAZIONE SCHEMA

            //PreparedStatement st = conn.prepareStatement("set search_path to account");
            //st.executeUpdate();

            // INIZIALIZZAZIONE AUTOCOMMIT A FALSE PER IMPOSTARE COMPORTAMENTO TRANSAZIONALE
            //conn.setAutoCommit(false);

            // ESECUZIONE COMANDI
//            PreparedStatement st1 = conn.prepareStatement("INSERT INTO Account VALUES (0,0)");
//            st1.executeUpdate();
            //PreparedStatement st2 = conn.prepareStatement("SELECT * FROM Account");
            //st2.executeQuery();

            // chiusura connessione
            //	conn.commit();
            conn.close();

        } catch (java.lang.ClassNotFoundException e) {
            System.err.print("ClassNotFoundException: ");
            System.err.println(e.getMessage());
        } catch (SQLException e1) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException e) {
                while (e != null) {
                    System.out.println("SQLState: " + e.getSQLState());
                    System.out.println("    Code: " + e.getErrorCode());
                    System.out.println(" Message: " + e.getMessage());
                    e = e.getNextException();
                }
            }

        }
    }
}
