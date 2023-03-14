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
import static ua.vspelykh.salon.Constants.ID_VALUE;
import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.controller.command.CommandTestData.getTestConsultation;
import static ua.vspelykh.salon.controller.command.consultation.ConsultationEditCommand.DELETE;
import static ua.vspelykh.salon.controller.command.consultation.ConsultationEditCommand.READ;
import static ua.vspelykh.salon.model.dao.mapper.Column.ID;
import static ua.vspelykh.salon.util.exception.Messages.CONSULTATION_EDIT_ERROR;

class ConsultationEditCommandTest extends AbstractCommandTest {

    @Mock
    private ConsultationService consultationService;

    @BeforeEach
    public void setUp() throws ServiceException {
        super.setUp();
        initCommand(new ConsultationEditCommand());
        prepareMocks();
    }

    @Override
    protected void prepareMocks() throws ServiceException {
        when(serviceFactory.getConsultationService()).thenReturn(consultationService);
        when(request.getParameter(ID)).thenReturn(String.valueOf(ID_VALUE));
        when(consultationService.findById(ID_VALUE)).thenReturn(getTestConsultation());
    }

    @Test
    void testProcessDelete() throws ServletException, IOException, ServiceException {
        when(request.getParameter(ACTION)).thenReturn(DELETE);

        command.process();

        verify(consultationService).delete(ID_VALUE);
    }

    @Test
    void testProcessRead() throws ServletException, IOException, ServiceException {
        when(request.getParameter(ACTION)).thenReturn(READ);
        Consultation testConsultation = getTestConsultation();
        testConsultation.setRead(true);

        command.process();

        verify(consultationService).save(testConsultation);
    }

    @Test
    void testProcessError() throws ServletException, IOException, ServiceException {
        when(request.getParameter(ACTION)).thenReturn(READ);
        when(consultationService.findById(ID_VALUE)).thenThrow(ServiceException.class);

        command.process();

        verify(consultationService, never()).save(any());
        verifyAttributes();
        verifyRedirect(ERROR_REDIRECT);
    }

    @Override
    protected void verifyAttributes() {
        verify(session).setAttribute(MESSAGE, CONSULTATION_EDIT_ERROR);
    }
}