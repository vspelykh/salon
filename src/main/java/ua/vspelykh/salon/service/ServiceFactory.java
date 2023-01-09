package ua.vspelykh.salon.service;

import ua.vspelykh.salon.model.BaseService;
import ua.vspelykh.salon.service.impl.BaseServiceServiceImpl;
import ua.vspelykh.salon.service.impl.ConsultationServiceImpl;
import ua.vspelykh.salon.service.impl.UserServiceImpl;

public class ServiceFactory {

    private ServiceFactory() {
    }

    private static final UserService userService = new UserServiceImpl();
    private static final BaseServiceService bss = new BaseServiceServiceImpl();
    private static final ConsultationService consultationService = new ConsultationServiceImpl();

    public static UserService getUserService(){
        return userService;
    }

    public static BaseServiceService getBaseServiceService() {
        return bss;
    }

    public static ConsultationService getConsultationService(){
        return consultationService;
    }
}
