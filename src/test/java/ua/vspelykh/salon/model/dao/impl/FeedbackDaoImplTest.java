package ua.vspelykh.salon.model.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.vspelykh.salon.model.dao.FeedbackDao;
import ua.vspelykh.salon.model.entity.Feedback;
import ua.vspelykh.salon.util.exception.DaoException;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static ua.vspelykh.salon.Constants.*;
import static ua.vspelykh.salon.model.dao.impl.DaoTestData.getTestFeedback;
import static ua.vspelykh.salon.model.dao.impl.SqlConstants.Feedbacks.*;
import static ua.vspelykh.salon.model.dao.mapper.Column.*;

class FeedbackDaoImplTest extends AbstractDaoTest {

    private FeedbackDao mockFeedbackDao;

    @BeforeEach
    void setUp() {
        mockConnection = mock(Connection.class);
        mockFeedbackDao = new FeedbackDaoImpl();
        mockFeedbackDao.setConnection(mockConnection);
        mockResultSet = mock(ResultSet.class);
    }

    @Test
    @SuppressWarnings("MagicConstant")
    void create() throws SQLException, DaoException {
        try (PreparedStatement statement = mockPrepareStatement()) {
            when(mockConnection.prepareStatement(anyString(), anyInt())).thenReturn(statement);
            Feedback testFeedback = getTestFeedback();
            testFeedback.setId(null);
            int id = mockFeedbackDao.create(testFeedback);

            verifyQueryWithGeneratedKey(INSERT_FEEDBACK);
            verifyNoMoreInteractions(mockConnection);

            int k = 0;
            verify(statement).setInt(++k, ID_VALUE);
            verify(statement).setInt(++k, MARK_VALUE);
            verify(statement).setString(++k, COMMENT_VALUE);
            verify(statement).setTimestamp(++k, Timestamp.valueOf(DATE_VALUE));
            assertEquals(ID_VALUE, id);
        }
    }

    @Test
    @SuppressWarnings("MagicConstant")
    void createWithException() throws SQLException {
        try (PreparedStatement statement = mockPrepareStatement()) {
            when(mockConnection.prepareStatement(anyString(), anyInt())).thenReturn(statement);
            mockResultSetIfAbsent();
            assertThrows(DaoException.class, () -> mockFeedbackDao.create(getTestFeedback()));

            verifyQueryWithGeneratedKey(INSERT_FEEDBACK);
            verifyNoMoreInteractions(mockConnection);
        }
    }

    @Test
    void updateIsUnsupported() {
        Feedback testFeedback = getTestFeedback();
        assertThrows(UnsupportedOperationException.class, () -> mockFeedbackDao.update(testFeedback));

    }

    @Test
    void getFeedbacksByMasterId() throws SQLException, DaoException {
        try (PreparedStatement statement = mockPrepareStatement()) {
            when(statement.executeQuery()).thenReturn(mockResultSet);
            mockResultSetIfPresent();
            List<Feedback> feedbacks = mockFeedbackDao.getFeedbacksByMasterId(ID_VALUE);
            verifyQuery(SELECT_FEEDBACKS_FOR_RATING);

            assertEquals(1, feedbacks.size());
            assertEquals(getTestFeedback(), feedbacks.get(0));
        }
    }

    @Test
    void getFeedbacksByMasterIdWithPagination() throws SQLException, DaoException {
        try (PreparedStatement statement = mockPrepareStatement()) {
            when(statement.executeQuery()).thenReturn(mockResultSet);
            mockResultSetIfPresent();
            List<Feedback> feedbacks = mockFeedbackDao.getFeedbacksByMasterId(ID_VALUE, 1);
            verifyQuery(SELECT_FEEDBACKS_MY_MASTER_ID);

            assertEquals(1, feedbacks.size());
            assertEquals(getTestFeedback(), feedbacks.get(0));
        }
    }

    @Test
    void countFeedbacksByMasterId() throws SQLException, DaoException {
        try (PreparedStatement statement = mockPrepareStatement()) {
            when(statement.executeQuery()).thenReturn(mockResultSet);
            mockResultSetIfPresent();

            int count = mockFeedbackDao.countFeedbacksByMasterId(ID_VALUE);
            assertEquals(1, count);
            verifyQuery(COUNT_FEEDBACKS);
        }
    }

    private PreparedStatement mockPrepareStatement() throws SQLException {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(isA(String.class))).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(isA(int.class), isA(int.class));
        doNothing().when(preparedStatement).setString(isA(int.class), isA(String.class));
        doNothing().when(preparedStatement).setTimestamp(isA(int.class), isA(Timestamp.class));
        when(preparedStatement.execute()).thenReturn(true);
        when(mockResultSet.next()).thenReturn(true);
        when(preparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(preparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.getInt(1)).thenReturn(ID_VALUE);
        return preparedStatement;
    }

    private void mockResultSetIfPresent() throws SQLException {
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getInt(ID)).thenReturn(ID_VALUE);
        when(mockResultSet.getInt(APPOINTMENT_ID)).thenReturn(ID_VALUE);
        when(mockResultSet.getInt(MARK)).thenReturn(MARK_VALUE);
        when(mockResultSet.getString(COMMENT)).thenReturn(COMMENT_VALUE);
        when(mockResultSet.getTimestamp(DATE)).thenReturn(Timestamp.valueOf(DATE_VALUE));
    }
}