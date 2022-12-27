package ua.vspelykh.salon.service;

import ua.vspelykh.salon.service.impl.UserServiceImpl;

public class ServiceFactory {

    private ServiceFactory() {
    }

    private static final UserService userService = new UserServiceImpl();

    public static UserService getUserService(){
        return userService;
    }
}
