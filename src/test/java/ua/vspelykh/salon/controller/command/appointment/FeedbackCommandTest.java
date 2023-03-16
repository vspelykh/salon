package ua.vspelykh.salon.controller.command.appointment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ua.vspelykh.salon.controller.command.AbstractCommandTest;
import ua.vspelykh.salon.model.entity.User;
import ua.vspelykh.salon.service.AppointmentService;
import ua.vspelykh.salon.service.FeedbackService;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.vspelykh.salon.Constants.CLIENT_ID_VALUE;
import static ua.vspelykh.salon.Constants.ID_VALUE;
import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.controller.command.CommandNames.FEEDBACK;
import static ua.vspelykh.salon.model.dao.mapper.Column.ID;
import static ua.vspelykh.salon.model.dao.postgres.DaoTestData.*;
import static ua.vspelykh.salon.util.exception.Messages.MESSAGE_FEEDBACK_EXISTS;

class FeedbackCommandTest extends AbstractCommandTest {

    @Mock
    private AppointmentService appointmentService;

    @Mock
    private FeedbackService feedbackService;

    private final User user = getTestUser();


    @BeforeEach
    protected void setUp() throws ServiceException {
        super.setUp();
        initCommand(new FeedbackCommand());
        prepareMocks();
    }

    @Test
    void processIdIsEmpty() throws IOException, ServletException {
        when(request.getParameter(ID)).thenReturn(null);
        command.process();
        verifyError403();
    }

    @Test
    void feedbackAlreadyExists() throws ServletException, IOException {
        when(feedbackService.getByAppointmentId(ID_VALUE)).thenReturn(getTestFeedback());
        command.process();
        verifyAttributes();
        verifyRedirect(SUCCESS_REDIRECT);
    }

    @Test
    void feedbackAccessDenied() throws ServletException, IOException {
        when(feedbackService.getByAppointmentId(ID_VALUE)).thenReturn(null);
        command.process();
        verifyError403();
    }

    @Test
    void process() throws ServletException, IOException {
        when(feedbackService.getByAppointmentId(ID_VALUE)).thenReturn(null);
        user.setId(CLIENT_ID_VALUE);
        command.process();
        verifyForward(FEEDBACK);
    }

    @Test
    void processError() throws ServiceException, ServletException, IOException {
        when(appointmentService.findById(ID_VALUE)).thenThrow(ServiceException.class);
        command.process();
        verifyError404();
    }

    @Override
    protected void prepareMocks() throws ServiceException {
        when(request.getParameter(ID)).thenReturn(String.valueOf(ID_VALUE));
        when(serviceFactory.getAppointmentService()).thenReturn(appointmentService);
        when(serviceFactory.getFeedbackService()).thenReturn(feedbackService);
        when(appointmentService.findById(ID_VALUE)).thenReturn(getTestAppointment());
        when(session.getAttribute(CURRENT_USER)).thenReturn(user);
    }

    @Override
    protected void verifyAttributes() {
        verify(session).setAttribute(MESSAGE, MESSAGE_FEEDBACK_EXISTS);
    }
}