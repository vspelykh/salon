package ua.vspelykh.salon.controller.command.login;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.vspelykh.salon.controller.command.AbstractCommandTest;
import ua.vspelykh.salon.model.entity.Role;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Set;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.vspelykh.salon.Constants.ROLES_VALUE;
import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.model.dao.impl.DaoTestData.getTestUser;

class LogoutCommandTest extends AbstractCommandTest {

    @BeforeEach
    protected void setUp() throws ServiceException {
        super.setUp();
        initCommand(new LogoutCommand());
        prepareMocks();
    }

    @Test
    void processLogout() throws ServletException, IOException {
        command.process();
        verifyAttributes();
        Set<Role> expectedRoles = Set.of(Role.GUEST);
        verify(session).setAttribute(ROLES, expectedRoles);
        verifyRedirectAnyString();
    }

    @Override
    protected void prepareMocks() throws ServiceException {
        when(session.getAttribute(ROLES)).thenReturn(ROLES_VALUE);
        when(session.getAttribute(IS_LOGGED)).thenReturn(true);
        when(session.getAttribute(CURRENT_USER)).thenReturn(getTestUser());
    }

    @Override
    protected void verifyAttributes() {
        verify(session).removeAttribute(ROLES);
        verify(session).removeAttribute(IS_LOGGED);
        verify(session).removeAttribute(CURRENT_USER);
    }
}