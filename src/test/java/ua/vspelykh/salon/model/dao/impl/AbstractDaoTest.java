package ua.vspelykh.salon.model.dao.impl;

import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractDaoTest {

    protected Connection mockConnection;
    protected ResultSet mockResultSet;

    protected void mockResultSetIfAbsent() throws SQLException {
        when(mockResultSet.next()).thenReturn(false);
    }

    protected void verifySql(String sql) throws SQLException {
        verify(mockConnection).prepareStatement(sql);
    }

    protected void verifySqlWithGeneratedKey(String sql) throws SQLException {
        verify(mockConnection).prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    }
}
