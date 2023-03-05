package ua.vspelykh.salon.controller.command.user.login;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ua.vspelykh.salon.controller.command.AbstractCommandTest;
import ua.vspelykh.salon.controller.command.login.RegistrationCommand;
import ua.vspelykh.salon.model.entity.User;
import ua.vspelykh.salon.service.UserService;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.vspelykh.salon.Constants.*;
import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.model.dao.mapper.Column.KEY;
import static ua.vspelykh.salon.model.dao.postgres.DaoTestData.getTestUser;
import static ua.vspelykh.salon.util.exception.Messages.*;

class RegistrationCommandTest extends AbstractCommandTest {

    @Mock
    private UserService userService;

    @BeforeEach
    protected void setUp() throws ServiceException {
        super.setUp();
        initCommand(new RegistrationCommand());
        prepareMocks();
    }

    @Test
    void registrationSuccessful() throws ServletException, IOException, ServiceException {
        command.process();
        User testUser = getTestUser();
        testUser.setId(null);
        verify(userService).save(testUser);
        verify(request.getSession()).setAttribute(MESSAGE, MESSAGE_REGISTRATION_SUCCESS);
        verifyRedirect(servletContext.getContextPath() + SUCCESS_REDIRECT);
    }

    @Test
    void registrationSuccessfulWithKey() throws ServletException, IOException, ServiceException {
        when(request.getParameter(KEY)).thenReturn(KEY_VALUE);
        command.process();
        verify(userService).save(any(User.class), eq(KEY_VALUE));
        verify(request.getSession()).setAttribute(MESSAGE, MESSAGE_REGISTRATION_SUCCESS);
        verifyRedirect(servletContext.getContextPath() + SUCCESS_REDIRECT);
    }

    @Test
    void registrationEmptyFields() throws ServletException, IOException {
        when(request.getParameter(NAME)).thenReturn("");
        command.process();
        verify(request).setAttribute(MESSAGE, MESSAGE_FIELDS_EMPTY);
        verifyAttributes();
        verifyForward(SIGN_UP);
    }

    @Test
    void registrationPasswordMismatch() throws ServletException, IOException {
        when(request.getParameter(PASSWORD_REPEAT)).thenReturn(PASSWORD_REPEAT);
        command.process();
        verify(request).setAttribute(MESSAGE, MESSAGE_PASSWORDS_MISMATCH);
        verifyAttributes();
        verifyForward(SIGN_UP);
    }

    @Test
    void registrationEmailExist() throws ServletException, IOException, ServiceException {
        when(serviceFactory.getUserService()).thenThrow(new ServiceException("(email)"));
        command.process();
        verify(request).setAttribute(MESSAGE, MESSAGE_REGISTRATION_EMAIL_EXISTS);
        verifyAttributes();
        verifyForward(SIGN_UP);
    }

    @Test
    void registrationNumberExist() throws ServletException, IOException, ServiceException {
        when(serviceFactory.getUserService()).thenThrow(new ServiceException("(number)"));
        command.process();
        verify(request).setAttribute(MESSAGE, MESSAGE_REGISTRATION_NUMBER_EXISTS);
        verifyAttributes();
        verifyForward(SIGN_UP);
    }

    @Test
    void registrationError() throws ServletException, IOException, ServiceException {
        when(serviceFactory.getUserService()).thenThrow(new ServiceException(MESSAGE));
        command.process();
        verify(request).setAttribute(MESSAGE, MESSAGE_REGISTRATION_OTHER_ERROR);
        verifyAttributes();
        verifyForward(SIGN_UP);
    }

    @Override
    protected void prepareMocks() throws ServiceException {
        when(serviceFactory.getUserService()).thenReturn(userService);
        when(request.getParameter(NAME)).thenReturn(NAME_VALUE);
        when(request.getParameter(SURNAME)).thenReturn(SURNAME_VALUE);
        when(request.getParameter(EMAIL)).thenReturn(EMAIL_VALUE);
        when(request.getParameter(NUMBER)).thenReturn(NUMBER_VALUE);
        when(request.getParameter(PASSWORD)).thenReturn(PASSWORD_VALUE);
        when(request.getParameter(PASSWORD_REPEAT)).thenReturn(PASSWORD_VALUE);
    }

    @Override
    protected void verifyAttributes() {
        verify(request).setAttribute(eq(NAME), anyString());
        verify(request).setAttribute(SURNAME, SURNAME_VALUE);
        verify(request).setAttribute(EMAIL, EMAIL_VALUE);
        verify(request).setAttribute(NUMBER, NUMBER_VALUE);
        verify(request).setAttribute(PASSWORD, PASSWORD_VALUE);
    }
}