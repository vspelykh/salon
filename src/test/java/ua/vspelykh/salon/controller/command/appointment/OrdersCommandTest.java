package ua.vspelykh.salon.controller.command.appointment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ua.vspelykh.salon.controller.command.AbstractCommandTest;
import ua.vspelykh.salon.model.entity.AppointmentStatus;
import ua.vspelykh.salon.model.entity.PaymentStatus;
import ua.vspelykh.salon.model.entity.User;
import ua.vspelykh.salon.service.AppointmentService;
import ua.vspelykh.salon.service.UserService;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.vspelykh.salon.Constants.DATE_VALUE;
import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.controller.command.CommandNames.MASTERS;
import static ua.vspelykh.salon.controller.command.CommandNames.*;
import static ua.vspelykh.salon.controller.command.CommandTestData.ID_VALUE_2;
import static ua.vspelykh.salon.controller.command.CommandTestData.getTestMaster;
import static ua.vspelykh.salon.model.dao.mapper.Column.*;
import static ua.vspelykh.salon.service.impl.ServiceTestData.getTestAppointmentDto;

class OrdersCommandTest extends AbstractCommandTest {


    @Mock
    private UserService userService;

    @Mock
    private AppointmentService appointmentService;

    private final List<User> masters = List.of(getTestMaster());

    @BeforeEach
    protected void setUp() throws ServiceException {
        super.setUp();
        initCommand(new OrdersCommand());
        prepareMocks();
    }

    @Test
    void processOrders() throws ServletException, IOException {
        command.process();
        verifyAttributes();
        verifyForward(ORDERS);
    }

    @Test
    void processOrderError() throws ServletException, IOException, ServiceException {
        when(userService.findMasters(true)).thenThrow(ServiceException.class);
        command.process();
        verifyError404();
    }

    @Override
    protected void prepareMocks() throws ServiceException {
        when(request.getParameter(ID)).thenReturn(String.valueOf(ID_VALUE_2));
        when(serviceFactory.getUserService()).thenReturn(userService);
        when(serviceFactory.getAppointmentService()).thenReturn(appointmentService);
        when(userService.findMasters(true)).thenReturn(masters);
        when(request.getParameter(DATE + "From")).thenReturn(DATE_VALUE.toLocalDate().toString());
        when(request.getParameter(DATE + "To")).thenReturn(DATE_VALUE.toLocalDate().toString());
        when(request.getParameter(STATUS)).thenReturn(AppointmentStatus.RESERVED.name());
        when(request.getParameter(PAYMENT_STATUS)).thenReturn(PaymentStatus.PAID_BY_CARD.name());
        when(request.getParameter(PAGE)).thenReturn("1");
        when(request.getParameter(SIZE)).thenReturn("5");
        when(appointmentService.getFiltered(anyInt(), any(), any(), any(), any(), anyInt(), anyInt()))
                .thenReturn(List.of(getTestAppointmentDto()));
    }

    @Override
    protected void verifyAttributes() {
        verify(request).setAttribute(MASTERS, masters);
        verify(request).setAttribute(PAGE + CHECKED, 1);
        verify(request).setAttribute(SIZE + CHECKED, 5);
        verify(request).setAttribute(APPOINTMENTS, List.of(getTestAppointmentDto()));
        verify(request).setAttribute(ID + CHECKED, ID_VALUE_2);
        verify(request).setAttribute(DATE + "From" + CHECKED, DATE_VALUE.toLocalDate());
        verify(request).setAttribute(DATE + "To" + CHECKED, DATE_VALUE.toLocalDate());
        verify(request).setAttribute(STATUS + CHECKED, AppointmentStatus.RESERVED.name());
        request.setAttribute(PAYMENT_STATUS + CHECKED, PaymentStatus.PAID_BY_CARD.name());
    }
}