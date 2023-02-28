package ua.vspelykh.salon.controller.command.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ua.vspelykh.salon.controller.command.AbstractCommandTest;
import ua.vspelykh.salon.service.UserService;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.controller.command.CommandTestData.getTestMaster;
import static ua.vspelykh.salon.model.dao.impl.DaoTestData.getTestUser;

class RolesCommandTest extends AbstractCommandTest {

    @Mock
    private UserService userService;

    @BeforeEach
    protected void setUp() throws ServiceException {
        super.setUp();
        initCommand(new RolesCommand());
        prepareMocks();
    }

    @Test
    void rolesProcess() throws ServletException, IOException {
        command.process();
        verifyAttributes();
        verifyForward(ROLES);
    }

    @Override
    protected void prepareMocks() throws ServiceException {
        when(request.getParameter(SEARCH)).thenReturn(SEARCH);
        when(session.getAttribute(CURRENT_USER)).thenReturn(getTestUser());
        when(serviceFactory.getUserService()).thenReturn(userService);
        when(userService.findBySearch(anyString())).thenReturn(List.of(getTestMaster()));
    }

    @Override
    protected void verifyAttributes() {
        verify(request).setAttribute(USERS, List.of(getTestMaster()));
    }
}