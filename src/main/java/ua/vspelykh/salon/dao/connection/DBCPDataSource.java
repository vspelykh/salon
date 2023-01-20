package ua.vspelykh.salon.dao.connection;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.postgresql.Driver;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DBCPDataSource {

    private static final Logger LOG = LogManager.getLogger(DBCPDataSource.class);

    private static final String DB_PROPERTY_PATH = "db/dbConnection.properties";
    private static final String DB_URL = "db.url";
    private static final String DB_USER = "db.user";
    private static final String DB_PASSWORD = "db.password";

    private static BasicDataSource ds = new BasicDataSource();

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

    public static Connection getConnection() throws SQLException {
        try {
            return ds.getConnection();
        } catch (SQLException e) {
            throw new SQLException("Error to connect to DB");
        }
    }

    private static void readProperties(Properties properties) {
        try {
            properties.load(DBCPDataSource.class.getClassLoader().getResourceAsStream(DB_PROPERTY_PATH));
        } catch (IOException e) {
            LOG.error("Error to read property file for DB connection");
        }
    }

    private DBCPDataSource() {
    }
}