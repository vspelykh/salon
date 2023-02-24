package ua.vspelykh.salon.controller.command;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ua.vspelykh.salon.AbstractSalonTest;
import ua.vspelykh.salon.service.ServiceFactory;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static ua.vspelykh.salon.controller.ControllerConstants.PATH_STR;

abstract class AbstractCommandTest extends AbstractSalonTest {

    @Mock
    protected HttpServletRequest request;

    @Mock
    protected HttpServletResponse response;

    @Mock
    protected ServiceFactory serviceFactory;

    @Mock
    protected HttpSession session;

    @Mock
    protected ServletContext servletContext;

    @Mock
    protected RequestDispatcher dispatcher;

    protected Command command;

    @BeforeEach
    void setUp() throws ServiceException {
        MockitoAnnotations.openMocks(this);
        when(request.getSession()).thenReturn(session);
        when(request.getQueryString()).thenReturn(PATH_STR);
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
    }

    protected void initCommand(Command command) {
        this.command = command;
        command.init(servletContext, request, response, serviceFactory);
    }

    protected abstract void prepareMocks() throws ServiceException;

    protected abstract void verifyAttrsAndDispatcher() throws ServletException, IOException;
}
