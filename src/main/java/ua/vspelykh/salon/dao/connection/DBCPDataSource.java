package ua.vspelykh.salon.dao.connection;

import org.apache.commons.dbcp2.BasicDataSource;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DBCPDataSource {

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
        ds.setMinIdle(5);
        ds.setMaxIdle(10);
        ds.setMaxOpenPreparedStatements(100);
    }

    public static Connection getConnection(){
        Connection connection = null;
        try {
            connection = ds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    private static void readProperties(Properties properties) {
        try (FileReader reader = new FileReader("src/main/resources/db/dbConnection.properties")) {
            properties.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private DBCPDataSource(){ }
}