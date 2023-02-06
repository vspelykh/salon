package ua.vspelykh.salon.controller.command;

import ua.vspelykh.salon.dto.UserMasterDTO;
import ua.vspelykh.salon.model.MastersLevel;
import ua.vspelykh.salon.model.Role;
import ua.vspelykh.salon.util.MasterSort;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.controller.command.CommandNames.MASTERS;
import static ua.vspelykh.salon.controller.filter.LocalizationFilter.LANG;

public class MastersCommand extends Command {

    @Override
    public void process() throws ServletException, IOException {
        try {
            setFilterAttributes();
            List<MastersLevel> levels = setLevelsParam();
            List<Integer> serviceIds = setServiceIds();
            List<Integer> categoriesIds = setCategoriesIds();
            int page = request.getParameter(PAGE) == null ? 1 : Integer.parseInt(request.getParameter(PAGE));
            int size = request.getParameter(SIZE) == null ? 5 : Integer.parseInt(request.getParameter(SIZE));
            MasterSort sort = request.getParameter(SORT) == null ? MasterSort.NAME_ASC
                    : MasterSort.valueOf(request.getParameter(SORT));
            String search = request.getParameter(SEARCH);
            setCheckedLists(levels, serviceIds, search, categoriesIds);
            String locale = String.valueOf(request.getSession().getAttribute(LANG));
            List<UserMasterDTO> mastersDto = getServiceFactory().getUserService().getMastersDto(levels, serviceIds, categoriesIds,
                    search, page, size, sort, locale);
            request.setAttribute(MASTERS, mastersDto);
            request.setAttribute(CATEGORIES, getServiceFactory().getServiceCategoryService().findAll(locale));
            int countOfItems = getServiceFactory().getUserService().getCountOfMasters(levels, serviceIds, categoriesIds, search);
            setPaginationParams(page, size, countOfItems, sort);
            Set<Role> roles = (Set<Role>) request.getSession().getAttribute("roles");
            request.setAttribute(IS_ADMIN, roles.contains(Role.ADMINISTRATOR));
            forward(MASTERS);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    private void setFilterAttributes() throws ServiceException {
        request.setAttribute(LEVELS, MastersLevel.list());
        request.setAttribute(SERVICES, getServiceFactory().getBaseServiceService().
                findAll(String.valueOf(request.getSession().getAttribute(LANG))));
        request.setAttribute(SIZES, SIZE_ARRAY);
        request.setAttribute(SORTS, MasterSort.list());
    }

    private List<MastersLevel> setLevelsParam() {
        if (checkNullParam(request.getParameter(LEVELS))) {
            List<MastersLevel> levels = new ArrayList<>();
            for (String level : request.getParameterValues(LEVELS)) {
                levels.add(MastersLevel.valueOf(level));
            }
            return levels;
        } else return Collections.emptyList();
    }

    private List<Integer> setServiceIds() {
        if (checkNullParam(request.getParameter(SERVICES))) {
            List<Integer> serviceIds = new ArrayList<>();
            for (String service : request.getParameterValues(SERVICES)) {
                serviceIds.add(Integer.valueOf(service));
            }
            return serviceIds;
        } else return Collections.emptyList();
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

    private void setPaginationParams(int page, int size, int countOfItems, MasterSort sort) {
        request.setAttribute(PAGE + CHECKED, page);
        request.setAttribute(SIZE + CHECKED, size);
        request.setAttribute(SORT + CHECKED, sort);
        countAndSet(size, countOfItems);
    }

    private void setCheckedLists(List<MastersLevel> levels, List<Integer> serviceIds, String search, List<Integer> categoriesIds) {
        request.setAttribute(LEVELS + CHECKED, levels);
        request.setAttribute(SERVICES + CHECKED, serviceIds);
        request.setAttribute(SEARCH + CHECKED, search);
        request.setAttribute(CATEGORIES + CHECKED, categoriesIds);
    }
}
