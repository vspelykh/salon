package ua.vspelykh.salon.controller.command.consultation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ua.vspelykh.salon.controller.command.AbstractCommandTest;
import ua.vspelykh.salon.model.entity.Consultation;
import ua.vspelykh.salon.service.ConsultationService;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.mockito.Mockito.*;
import static ua.vspelykh.salon.Constants.*;
import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.controller.command.CommandTestData.getTestConsultation;
import static ua.vspelykh.salon.model.dao.mapper.Column.NAME;
import static ua.vspelykh.salon.model.dao.mapper.Column.NUMBER;
import static ua.vspelykh.salon.model.dao.mapper.Column.*;
import static ua.vspelykh.salon.util.exception.Messages.CONSULTATION_ERROR;
import static ua.vspelykh.salon.util.exception.Messages.CONSULTATION_SUCCESS;

class ConsultationPostCommandTest extends AbstractCommandTest {

    @Mock
    private ConsultationService consultationService;

    private final Consultation consultation = getTestConsultation();

    @BeforeEach
    public void setUp() throws ServiceException {
        super.setUp();
        initCommand(new ConsultationPostCommand());
        prepareMocks();
        consultation.setId(null);
        consultation.setDate(null);
    }

    @Test
    void processSuccess() throws ServletException, IOException, ServiceException {
        command.process();

        verify(consultationService).save(consultation);
        verify(session).setAttribute(MESSAGE, CONSULTATION_SUCCESS);
        verify(response).sendRedirect(SUCCESS_REDIRECT);
    }

    @Test
    void processError() throws ServletException, IOException, ServiceException {
        doThrow(ServiceException.class).when(consultationService).save(consultation);
        command.process();

        verify(consultationService).save(any(Consultation.class));
        verify(session).setAttribute(MESSAGE, CONSULTATION_ERROR);
        verifyRedirect(ERROR_REDIRECT);
    }

    @Override
    protected void prepareMocks() throws ServiceException {
        when(serviceFactory.getConsultationService()).thenReturn(consultationService);
        when(request.getParameter(ID)).thenReturn(String.valueOf(ID_VALUE));
        when(request.getParameter(NAME)).thenReturn(NAME_VALUE);
        when(request.getParameter(NUMBER)).thenReturn(NUMBER_VALUE);
    }

    @Override
    protected void verifyAttributes() {

    }
}