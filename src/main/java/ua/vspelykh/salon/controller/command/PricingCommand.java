package ua.vspelykh.salon.controller.command;

import ua.vspelykh.salon.service.BaseServiceService;
import ua.vspelykh.salon.service.ServiceFactory;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;

import static ua.vspelykh.salon.controller.ControllerConstants.SERVICES;
import static ua.vspelykh.salon.controller.command.CommandNames.PRICING;
import static ua.vspelykh.salon.controller.filter.LocalizationFilter.LANG;

public class PricingCommand extends Command {
    BaseServiceService service = ServiceFactory.getBaseServiceService();

    @Override
    public void process() throws ServletException, IOException {
        try {
            request.setAttribute(SERVICES,service.findAll(String.valueOf(request.getSession().getAttribute(LANG))));
            forward(PRICING);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }
}
