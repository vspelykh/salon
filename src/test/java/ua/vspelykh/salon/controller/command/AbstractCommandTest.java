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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.vspelykh.salon.controller.ControllerConstants.EMPTY_STRING;
import static ua.vspelykh.salon.controller.ControllerConstants.PATH_STR;
import static ua.vspelykh.salon.controller.filter.LocalizationFilter.LANG;
import static ua.vspelykh.salon.model.dao.mapper.Column.UA_LOCALE;
import static ua.vspelykh.salon.util.PageConstants.JSP_PATTERN;

public abstract class AbstractCommandTest extends AbstractSalonTest {

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
    protected void setUp() throws ServiceException {
        MockitoAnnotations.openMocks(this);
        when(request.getSession()).thenReturn(session);
        when(request.getSession().getAttribute(LANG)).thenReturn(UA_LOCALE);
        when(request.getQueryString()).thenReturn(PATH_STR);
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        when(servletContext.getContextPath()).thenReturn(EMPTY_STRING);
    }

    protected void initCommand(Command command) {
        this.command = command;
        command.init(servletContext, request, response, serviceFactory);
    }

    protected void verifyForward(String command) throws ServletException, IOException {
        verify(request).getRequestDispatcher(String.format(JSP_PATTERN, command));
        verify(dispatcher).forward(request, response);
    }

    protected void verifyRedirect(String target) throws IOException {
        verify(response).sendRedirect(target);
    }

    protected void verifyRedirectAnyString() throws IOException {
        verify(response).sendRedirect(anyString());
    }

    protected void verifyError500() throws IOException {
        verify(response).sendError(500);
    }

    protected void verifyError404() throws IOException {
        verify(response).sendError(404);
    }

    protected void verifyError403() throws IOException {
        verify(response).sendError(403);
    }

    protected abstract void prepareMocks() throws ServiceException;

    protected abstract void verifyAttributes();
}
