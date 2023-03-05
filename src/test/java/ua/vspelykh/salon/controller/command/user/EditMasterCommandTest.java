package ua.vspelykh.salon.controller.command.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ua.vspelykh.salon.controller.command.AbstractCommandTest;
import ua.vspelykh.salon.model.entity.MastersLevel;
import ua.vspelykh.salon.service.UserService;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.vspelykh.salon.Constants.*;
import static ua.vspelykh.salon.controller.ControllerConstants.SCHEDULE_REDIRECT;
import static ua.vspelykh.salon.model.dao.mapper.Column.*;
import static ua.vspelykh.salon.model.dao.postgres.DaoTestData.getTestUserLevel;

class EditMasterCommandTest extends AbstractCommandTest {

    @Mock
    private UserService userService;

    @BeforeEach
    protected void setUp() throws ServiceException {
        super.setUp();
        initCommand(new EditMasterCommand());
        prepareMocks();
    }

    @Test
    void processEditMaster() throws ServiceException, ServletException, IOException {
        command.process();
        verify(userService).update(getTestUserLevel());
        verify(response).sendRedirect(SCHEDULE_REDIRECT + ID_VALUE);
    }

    @Test
    void processEditMasterError() throws ServletException, IOException, ServiceException {
        when(serviceFactory.getUserService()).thenThrow(ServiceException.class);
        command.process();
        verifyError500();
    }

    @Override
    protected void prepareMocks() throws ServiceException {
        when(serviceFactory.getUserService()).thenReturn(userService);
        when(request.getParameter(ID)).thenReturn(String.valueOf(ID_VALUE));
        when(request.getParameter(LEVEL)).thenReturn(MastersLevel.YOUNG.name());
        when(request.getParameter(ABOUT)).thenReturn(ABOUT_VALUE);
        when(request.getParameter(ABOUT + UA)).thenReturn(ABOUT_UA_VALUE);
        when(request.getParameter(ACTIVE)).thenReturn(String.valueOf(true));
    }

    @Override
    protected void verifyAttributes() {

    }
}