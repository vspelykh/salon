package ua.vspelykh.salon.controller.command.user.login;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ua.vspelykh.salon.controller.command.AbstractCommandTest;
import ua.vspelykh.salon.controller.command.login.CheckLoginCommand;
import ua.vspelykh.salon.service.UserService;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static ua.vspelykh.salon.Constants.EMAIL_VALUE;
import static ua.vspelykh.salon.Constants.PASSWORD_VALUE;
import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.model.dao.impl.DaoTestData.getTestUser;

class CheckLoginCommandTest extends AbstractCommandTest {

    @Mock
    private UserService userService;

    @BeforeEach
    protected void setUp() throws ServiceException {
        super.setUp();
        initCommand(new CheckLoginCommand());
        prepareMocks();
    }

    @Test
    void checkLoginSuccess() throws ServletException, IOException, ServiceException {
        when(userService.findByEmailAndPassword(EMAIL_VALUE, PASSWORD_VALUE)).thenReturn(getTestUser());

        command.process();

        verifyAttributes();
        verifyRedirectAnyString();
    }

    @Test
    void checkLoginSuccessWithLastPage() throws ServletException, IOException, ServiceException {
        when(session.getAttribute(LAST_PAGE)).thenReturn(LAST_PAGE);
        when(userService.findByEmailAndPassword(EMAIL_VALUE, PASSWORD_VALUE)).thenReturn(getTestUser());

        command.process();

        verify(session, times(2)).getAttribute(LAST_PAGE);
        verify(session).removeAttribute(LAST_PAGE);
        verifyAttributes();
        verifyRedirect(LAST_PAGE);
    }

    @Test
    void checkLoginFailure() throws ServletException, IOException, ServiceException {
        when(userService.findByEmailAndPassword(EMAIL_VALUE, PASSWORD_VALUE)).thenThrow(ServiceException.class);

        command.process();

        verify(session).setAttribute(MESSAGE, MESSAGE_INCORRECT_LOGIN_PASSWORD);
        verify(request).setAttribute(INS_LOGIN, EMAIL_VALUE);
        verify(request).setAttribute(INS_PASSWORD, PASSWORD_VALUE);
        verifyRedirect(HOME_REDIRECT + COMMAND_PARAM + LOGIN);
    }

    @Test
    void testProcessInvalidParameters() throws ServletException, IOException {
        when(request.getParameter(LOGIN)).thenReturn(null);
        when(request.getParameter(PASSWORD)).thenReturn(null);

        command.process();

        verify(session, never()).setAttribute(anyString(), any());
        verify(response, never()).sendRedirect(anyString());
        verifyError500();
    }


    @Override
    protected void prepareMocks() throws ServiceException {
        when(request.getSession()).thenReturn(session);
        when(serviceFactory.getUserService()).thenReturn(userService);
        when(request.getParameter(LOGIN)).thenReturn(EMAIL_VALUE);
        when(request.getParameter(PASSWORD)).thenReturn(PASSWORD_VALUE);
    }

    @Override
    protected void verifyAttributes() {
        verify(session).setAttribute(CURRENT_USER, getTestUser());
        verify(session).setAttribute(IS_LOGGED, true);
    }
}