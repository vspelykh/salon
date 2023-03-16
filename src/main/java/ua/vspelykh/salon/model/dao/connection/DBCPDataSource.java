package ua.vspelykh.salon.model.dao.connection;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.postgresql.Driver;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * This class provides a connection to a database using Apache DBCP (Database Connection Pool).
 * It reads the database properties from the db/dbConnection.properties file.
 *
 * @version 1.0
 */
public class DBCPDataSource {

    private static final Logger LOG = LogManager.getLogger(DBCPDataSource.class);

    private static final String DB_PROPERTY_PATH = "db/dbConnection.properties";
    private static final String DB_URL = "db.url";
    private static final String DB_USER = "db.user";
    private static final String DB_PASSWORD = "db.password";

    private static final BasicDataSource ds = new BasicDataSource();

    /* Set up the data source object with the properties from the property file.*/
    static {
        Properties properties = new Properties();
        readProperties(properties);
        ds.setUrl(properties.getProperty(DB_URL));
        ds.setUsername(properties.getProperty(DB_USER));
        ds.setPassword(properties.getProperty(DB_PASSWORD));
        ds.setDriver(new Driver());
        ds.setMinIdle(5);
        ds.setMaxIdle(10);
        ds.setMaxOpenPreparedStatements(100);
    }

    /**
     * Get a database connection from the data source.
     *
     * @return a Connection object
     * @throws SQLException if there is an error connecting to the database
     */
    public static Connection getConnection() throws SQLException {
        try {
            Connection connection = ds.getConnection();
            LOG.info("Get connection");
            return connection;
        } catch (SQLException e) {
            throw new SQLException("Error to connect to DB");
        }
    }

    /**
     * Load the database properties from the property file.
     *
     * @param properties a Properties object to store the properties in
     */
    private static void readProperties(Properties properties) {
        try {
            properties.load(DBCPDataSource.class.getClassLoader().getResourceAsStream(DB_PROPERTY_PATH));
        } catch (IOException e) {
            LOG.error("Error to read property file for DB connection");
        }
    }

    /**
     * Private constructor to prevent instantiation of this class.
     */
    private DBCPDataSource() {
    }
}