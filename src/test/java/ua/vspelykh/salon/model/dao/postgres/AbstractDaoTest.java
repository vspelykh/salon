package ua.vspelykh.salon.model.dao.postgres;

import ua.vspelykh.salon.AbstractSalonTest;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

abstract class AbstractDaoTest extends AbstractSalonTest {

    protected Connection mockConnection;
    protected ResultSet mockResultSet;

    protected void mockResultSetIfAbsent() throws SQLException {
        when(mockResultSet.next()).thenReturn(false);
    }

    protected void verifyQuery(String sql) throws SQLException {
        verify(mockConnection).prepareStatement(sql);
    }

    protected void verifyQueryWithGeneratedKey(String sql) throws SQLException {
        verify(mockConnection).prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    }
}
