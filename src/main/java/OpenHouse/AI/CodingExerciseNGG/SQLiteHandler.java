package OpenHouse.AI.CodingExerciseNGG;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * The SQLiteHandler Class is a class created to be used for testing
 * out the idea of adding a lightweight database to use for this API.
 * Ultimately, not used because of time constraint reasons
 *
 * @author  Neil Gilbert Gallardo
 * @version 1.0
 * @since   2022-06-10
 */
public class SQLiteHandler {
    public static Connection connect() {
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:sqlite/userLogs.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");

            setUpTables(conn);
            return conn;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return null;
    }

    private static void setUpTables(Connection c){
        Statement stmt = null;
        try {
            stmt = c.createStatement();
            String sql = "CREATE TABLE Logs " +
                    "(logId INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " userId TEXT NOT NULL, " +
                    " sessionId TEXT NOT NULL) " ;
            stmt.executeUpdate(sql);

            sql = "CREATE TABLE LogAction " +
                    "(actionId INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " logId INTEGER NOT NULL, " +
                    " time TEXT NOT NULL, " +
                    " type TEXT NOT NULL) " ;
            stmt.executeUpdate(sql);
            stmt.close();
        }
        catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            //e.printStackTrace();
        }
    }
}
