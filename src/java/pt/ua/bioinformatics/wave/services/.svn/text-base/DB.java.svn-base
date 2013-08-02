package pt.ua.bioinformatics.wave.services;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pedrolopes
 * @version 1.2, 2010-12-17
 */
public class DB {

    private Connection connection;
    private Statement statement;
    private String connectionString = "";
    private String database = "WAVe";
    private String build = "";

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Statement getStatement() {
        return statement;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }

    // constructor
    public DB() {
    }

    /**
     *
     * @param database The database name
     * @param connectionString The connection string (if distinct from WAVe)
     */
    public DB(String database, String connectionString) {
        this.database = database;
        this.connectionString = connectionString;
    }

    public DB(String connectionString) {
        this.connectionString = connectionString;
    }

    /**
     * Establishes a new connection to the database.
     * <p><b>Feature:</b><br />
     * Performs the required operations to establish a new database connection
     * </p>
     *
     * @return Success of the operation (true if connects, false if fails to connect)
     */
    public boolean connect() {
        boolean success = false;

        if (!Settings.isLoaded()) {
            Settings.load();
        }

        if (Settings.getModules().get("database")) {
            try {
                // get WAVe database build in use
                build = Settings.getDbBuild();

                // connect to WAVe database in use
                Class.forName("com.mysql.jdbc.Driver");
                connection = (com.mysql.jdbc.Connection) DriverManager.getConnection(Settings.getDbServer(), Settings.getDbUser(), Settings.getDbPassword());
                statement = connection.createStatement();
                success = true;
            } catch (SQLException e) {
                System.out.println("[DB] Unable to connect to " + database + "\n\t" + e.toString());
                success = false;
            } finally {
                return success;
            }
        } else {
            System.out.println("[DB] WAVe database module disabled");
            return false;
        }
    }

    /**
     * Establishes a new connection to the database.
     * <p><b>Feature:</b><br />
     * Performs the required operations to establish a new database connection to non-default database
     * </p>
     *
     * @return Success of the operation (true if connects, false if fails to connect)
     */
    public boolean connectX() {
        boolean success = false;
        try {
            connection = DriverManager.getConnection(connectionString);
            statement = connection.createStatement();
            success = true;
        } catch (SQLException e) {
            System.out.println("[DB] Unable to connect to " + database + "\n\t" + e.toString());
            success = false;
        } finally {
            // System.out.println("[DB] Connected to " + database);
            return success;
        }
    }

    /**
     * Closes current database connection.
     * <p><b>Feature:</b><br />
     * Performs the required operations to close an open database connection
     * </p>
     *
     * @return Success of the operation (true if it closes the connection, false if it fails to close the connection)
     */
    public boolean close() {
        try {
            if (!statement.isClosed()) {
                statement.close();
            }
            if (!connection.isClosed()) {
                connection.close();
            }

        } catch (SQLException e) {
            System.out.println("[DB] Unable to close " + database + " connection\n\t" + e.toString());
            return false;
        } finally {
            // System.out.println("[DB] Closed " + database + " connection");
            return true;
        }

    }

    /**
     * Inserts data in the instanced database based on the passed query.
     * <p><b>Feature:</b><br/>
     * Executes required tests and operations to insert novel data in the database
     * </p>
     *
     * @param what String to describe what is being inserted in the database
     * @param query The insertion query
     * @return Success of the operation (true if inserts correctly, false if fails to insert)
     */
    public boolean insert(String what, String query) {
        query = query.replace("#build#", build);

        try {
            if (!connection.isClosed() && !statement.isClosed()) {
                statement.executeUpdate(query);
            }

        } catch (SQLException e) {
            System.out.println("[DB] Unable to insert " + what + "\n\t" + e.toString());
            return false;
        } finally {
            // System.out.println("[DB] Inserted " + query);
            return true;
        }
    }

    /**
     * Updates data in the instanced database based on the passed query.
     * <p><b>Feature:</b><br/>
     * Executes required tests and operations to update existing data in the database
     * </p>
     *
     * @param what String to describe what is being updated in the database
     * @param query The update query
     * @return Success of the operation (true if updates correctly, false if fails to update)
     */
    public boolean update(String what, String query) {
        query = query.replace("#build#", build);
        try {
            if (!connection.isClosed() && !statement.isClosed()) {
                statement.executeUpdate(query);
            }

        } catch (SQLException e) {
            System.out.println("[DB] Unable to update " + what + "\n\t" + e.toString());
            return false;
        } finally {
            System.out.println("[DB] updated " + what);
            return true;
        }
    }

    /**
     * Updates data in the instanced database based on the passed query.
     * <p><b>Feature:</b><br/>
     * Executes required tests and operations to update existing data in the database
     * </p>
     *
     * @param what String to describe what is being updated in the database
     * @param query The update query
     * @return Success of the operation (true if updates correctly, false if fails to update)
     */
    public boolean delete(String what, String query) {
        query = query.replace("#build#", build);
        try {
            if (!connection.isClosed() && !statement.isClosed()) {
                statement.executeUpdate(query);
            }

        } catch (SQLException e) {
            System.out.println("[DB] Unable to delete " + what + "\n\t" + e.toString());
            return false;
        } finally {
            // System.out.println("[DB] updated " + what);
            return true;
        }
    }

    /**
     * Retrieves data from the instance database.
     * <p><b>Feature:</b><br/>
     * Retrives a Result Set table containing the results from the provided <code>SELECT</code> statement
     *
     * @param query <code>SELECT</code> query to get the desired data
     * @return Result Set containing the output of the executed <code>SELECT</code> statement
     */
    public ResultSet getData(String query) {
        query = query.replace("#build#", build);

        ResultSet rs = null;

        try {
            if (!connection.isClosed() && !statement.isClosed()) {
                rs = statement.executeQuery(query);
            }
        } catch (SQLException e) {
            System.out.println("[DB] Unable to retrieve data\n\t" + e.toString());
            return rs;
        }
        //  System.out.println("[DB] Retrieved data");
        return rs;

    }

    public ResultSet indexGet(String what, int limit) {
        what = what + "%";
        String query = "SELECT DISTINCT v, refnodeid, gene FROM wave.wave#build#_index WHERE v LIKE ? ORDER BY v LIMIT ?";
        query = query.replace("#build#", build);
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            stat = connection.prepareStatement(query);
            stat.setString(1, what);
            stat.setInt(2, limit);
            rs = stat.executeQuery();

        } catch (Exception ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }

    public ResultSet indexSearch(String what, int limit) {
        what = what + "%";
        String query = "SELECT DISTINCT v, refnodeid, gene FROM wave.wave#build#_index WHERE v LIKE ? GROUP BY v ORDER BY v LIMIT ?";
        query = query.replace("#build#", build);
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            stat = connection.prepareStatement(query);
            stat.setString(1, what);
            stat.setInt(2, limit);
            rs = stat.executeQuery();

        } catch (Exception ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }
}
