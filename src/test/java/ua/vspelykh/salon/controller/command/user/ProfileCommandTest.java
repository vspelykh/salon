package ua.vspelykh.salon.controller.command.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ua.vspelykh.salon.controller.command.AbstractCommandTest;
import ua.vspelykh.salon.service.UserService;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.controller.command.CommandTestData.ID_VALUE_2;
import static ua.vspelykh.salon.controller.command.CommandTestData.getTestMaster;
import static ua.vspelykh.salon.model.dao.postgres.DaoTestData.getTestUserLevel;

class ProfileCommandTest extends AbstractCommandTest {

    @Mock
    private UserService userService;

    @BeforeEach
    protected void setUp() throws ServiceException {
        super.setUp();
        initCommand(new ProfileCommand());
        prepareMocks();
    }

    @Test
    void testProcess() throws IOException, ServletException {
        command.process();

        verifyAttributes();
        verifyForward(PROFILE);
    }

    @Test
    void testProcessError() throws IOException, ServletException, ServiceException {
        when(userService.getUserLevelByUserId(ID_VALUE_2)).thenThrow(ServiceException.class);
        command.process();

        verifyError500();
    }

    @Override
    protected void prepareMocks() throws ServiceException {
        when(session.getAttribute(CURRENT_USER)).thenReturn(getTestMaster());
        when(serviceFactory.getUserService()).thenReturn(userService);
        when(userService.getUserLevelByUserId(ID_VALUE_2)).thenReturn(getTestUserLevel());
    }

    @Override
    protected void verifyAttributes() {
        verify(request).setAttribute(USER, getTestMaster());
        verify(request).setAttribute(ROLES, getTestMaster().getRoles());
        verify(request).setAttribute(IS_MASTER, true);
        verify(request).setAttribute(USER_LEVEL, getTestUserLevel());
        verify(request).setAttribute(IS_ADMIN, false);
        verify(request).setAttribute(IS_CLIENT, false);
    }
}