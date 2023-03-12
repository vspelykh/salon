package ua.vspelykh.salon.controller.command.appointment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ua.vspelykh.salon.controller.command.AbstractCommandTest;
import ua.vspelykh.salon.service.FeedbackService;
import ua.vspelykh.salon.util.exception.Messages;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.mockito.Mockito.*;
import static ua.vspelykh.salon.Constants.*;
import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.model.dao.mapper.Column.*;

class FeedbackPostCommandTest extends AbstractCommandTest {

    @Mock
    private FeedbackService feedbackService;

    @BeforeEach
    protected void setUp() throws ServiceException {
        super.setUp();
        initCommand(new FeedbackPostCommand());
        prepareMocks();
    }

    @Test
    void processFeedbackPost() throws ServletException, IOException {
        command.process();
        verifyAttributes();
        verifyRedirect(SUCCESS_REDIRECT);
    }

    @Test
    void ProcessFeedbackPostError() throws ServletException, IOException, ServiceException {
        doThrow(ServiceException.class).when(feedbackService).save(any());
        command.process();
        verify(session).setAttribute(MESSAGE, Messages.POST_FEEDBACK_ERROR);
        verifyRedirect(ERROR_REDIRECT);
    }

    @Override
    protected void prepareMocks() throws ServiceException {
        when(request.getParameter(MARK)).thenReturn(String.valueOf(MARK_VALUE));
        when(request.getParameter(COMMENT)).thenReturn(COMMENT_VALUE);
        when(request.getParameter(APPOINTMENT_ID)).thenReturn(String.valueOf(ID_VALUE));
        when(serviceFactory.getFeedbackService()).thenReturn(feedbackService);
    }

    @Override
    protected void verifyAttributes() {
        verify(session).setAttribute(MESSAGE, "message.mark");
    }
}