package ua.vspelykh.salon.controller.command.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ua.vspelykh.salon.controller.command.AbstractCommandTest;
import ua.vspelykh.salon.model.entity.Role;
import ua.vspelykh.salon.service.UserService;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.mockito.Mockito.*;
import static ua.vspelykh.salon.Constants.ID_VALUE;
import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.model.dao.mapper.Column.ROLE;
import static ua.vspelykh.salon.model.dao.mapper.Column.USER_ID;
import static ua.vspelykh.salon.model.dao.postgres.DaoTestData.getTestUser;

class ChangeRoleCommandTest extends AbstractCommandTest {

    @Mock
    private UserService userService;


    @BeforeEach
    protected void setUp() throws ServiceException {
        super.setUp();
        initCommand(new ChangeRoleCommand());
        prepareMocks();
    }

    @Test
    void processChangeRole() throws ServletException, IOException, ServiceException {
        command.process();

        verify(userService, times(1)).updateRole(1, ADD, Role.ADMINISTRATOR);
        verifyAttributes();
        verify(response, times(1)).sendRedirect(anyString());
    }

    @Test
    void processChangeRoleError() throws ServletException, IOException, ServiceException {
        when(userService.findById(ID_VALUE)).thenThrow(ServiceException.class);
        command.process();

        verifyError500();
    }


    @Override
    protected void prepareMocks() throws ServiceException {
        when(request.getParameter(ROLE)).thenReturn(Role.ADMINISTRATOR.name());
        when(request.getParameter(ACTION)).thenReturn(ADD);
        when(request.getParameter(USER_ID)).thenReturn(String.valueOf(ID_VALUE));
        when(serviceFactory.getUserService()).thenReturn(userService);
        when(userService.findById(ID_VALUE)).thenReturn(getTestUser());
    }

    @Override
    protected void verifyAttributes() {
        verify(request.getSession(), times(1)).setAttribute(MESSAGE, SUCCESS);
    }
}