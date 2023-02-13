package ua.vspelykh.salon.dao.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import ua.vspelykh.salon.model.dao.UserDao;
import ua.vspelykh.salon.util.exception.DaoException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ua.vspelykh.salon.dao.impl.DaoTestData.getTestUser;

@RunWith(MockitoJUnitRunner.class)
class UserDaoImplTest {

    UserDao userDao = mock(UserDao.class);

    @Test
    void findById() throws DaoException {
        when(userDao.findById(1)).thenReturn(getTestUser());
        Assertions.assertEquals(userDao.findById(1), getTestUser());
    }

    @Test
    void create() {
    }
}