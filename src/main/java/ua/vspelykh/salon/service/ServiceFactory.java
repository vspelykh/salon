package ua.vspelykh.salon.service;

import ua.vspelykh.salon.service.impl.*;

public class ServiceFactory {

    private ServiceFactory() {
    }

    private static final UserService userService = new UserServiceImpl();
    private static final BaseServiceService bss = new BaseServiceServiceImpl();
    private static final ConsultationService consultationService = new ConsultationServiceImpl();
    private static final WorkingDayService workingDayService = new WorkingDayServiceImpl();
    private static final AppointmentService appointmentService = new AppointmentServiceImpl();
    private static final ServiceService serviceService = new ServiceServiceImpl();
    private static final MarkService markService = new MarkServiceImpl();

    public static UserService getUserService() {
        return userService;
    }

    public static BaseServiceService getBaseServiceService() {
        return bss;
    }

    public static ConsultationService getConsultationService() {
        return consultationService;
    }

    public static WorkingDayService getWorkingDayService() {
        return workingDayService;
    }

    public static AppointmentService getAppointmentService() {
        return appointmentService;
    }

    public static ServiceService getServiceService() {
        return serviceService;
    }

    public static MarkService gerMarkService() {
        return markService;
    }
}
