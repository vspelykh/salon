package ua.vspelykh.salon.controller.command;

import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.controller.command.CommandNames.PRICING;
import static ua.vspelykh.salon.controller.filter.LocalizationFilter.LANG;

public class PricingCommand extends Command {

    @Override
    public void process() throws ServletException, IOException {
        try {
            String locale = String.valueOf(request.getSession().getAttribute(LANG));
            request.setAttribute(CATEGORIES, getServiceFactory().getServiceCategoryService().findAll(locale));
            List<Integer> categoriesIds = setCategoriesIds();
            int page = request.getParameter(PAGE) == null ? 1 : Integer.parseInt(request.getParameter(PAGE));
            int size = request.getParameter(SIZE) == null ? 5 : Integer.parseInt(request.getParameter(SIZE));

            request.setAttribute(SERVICES, getServiceFactory().getBaseServiceService().findByFilter(categoriesIds, page, size, locale));
            int countOfItems = getServiceFactory().getBaseServiceService().getCountOfCategories(categoriesIds, page, size);
            setPaginationParams(page, size, countOfItems);
            setCheckedList(categoriesIds);
            forward(PRICING);
        } catch (ServiceException e) {
            response.sendError(500);
        }
    }

    private void setCheckedList(List<Integer> categoriesIds) {
        request.setAttribute(CATEGORIES + CHECKED, categoriesIds);
    }

    private List<Integer> setCategoriesIds() {
        if (checkNullParam(request.getParameter(CATEGORIES))) {
            List<Integer> categoriesIds = new ArrayList<>();
            for (String categories : request.getParameterValues(CATEGORIES)) {
                categoriesIds.add(Integer.valueOf(categories));
            }
            return categoriesIds;
        } else return Collections.emptyList();
    }

    private void setPaginationParams(int page, int size, int countOfItems) {
        request.setAttribute(SIZES, SIZE_LIST);
        request.setAttribute(PAGE + CHECKED, page);
        request.setAttribute(SIZE + CHECKED, size);
        countAndSet(size, countOfItems);
    }

}
