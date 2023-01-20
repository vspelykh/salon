package ua.vspelykh.salon.controller.command;

import ua.vspelykh.salon.dto.UserMasterDTO;
import ua.vspelykh.salon.model.MastersLevel;
import ua.vspelykh.salon.model.Role;
import ua.vspelykh.salon.service.BaseServiceService;
import ua.vspelykh.salon.service.ServiceFactory;
import ua.vspelykh.salon.service.UserService;
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

public class MastersCommand extends Command {

    private final UserService userService = ServiceFactory.getUserService();
    private final BaseServiceService baseService = ServiceFactory.getBaseServiceService();


    @Override
    public void process() throws ServletException, IOException {
        try {
            setFilterAttributes();
            List<MastersLevel> levels = setLevelsParam();
            List<Integer> serviceIds = setServiceIds();
            int page = request.getParameter(PAGE) == null ? 1 : Integer.parseInt(request.getParameter(PAGE));
            int size = request.getParameter(SIZE) == null ? 5 : Integer.parseInt(request.getParameter(SIZE));
            MasterSort sort = request.getParameter(SORT) == null ? MasterSort.NAME_ASC
                    : MasterSort.valueOf(request.getParameter(SORT));
            String search = request.getParameter(SEARCH);
            List<UserMasterDTO> mastersDto = userService.getMastersDto(levels, serviceIds, search, page, size, sort);
            request.setAttribute(MASTERS, mastersDto);
            setCheckedLists(levels, serviceIds, search);
            int countOfItems = userService.getCountOfMasters(levels, serviceIds, search);
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
        request.setAttribute(SERVICES, baseService.findAll());
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

    private void setPaginationParams(int page, int size, int countOfItems, MasterSort sort) {
        request.setAttribute(PAGE + CHECKED, page);
        request.setAttribute(SIZE + CHECKED, size);
        request.setAttribute(SORT + CHECKED, sort);
        int[] pages = new int[(int) Math.ceil(countOfItems * 1.0 / size)];
        for (int i = 1, j = 0; i <= pages.length; j++, i++) {
            pages[j] = i;
        }
        request.setAttribute(LAST_PAGE, pages.length);
        request.setAttribute(PAGES_ARRAY, pages);
        request.setAttribute(NUMBER_OF_PAGES, Math.ceil(countOfItems * 1.0 / size));
        String path = "?" + request.getQueryString().replaceAll("&page=[0-9]*", "");
        request.setAttribute(PATH_STR, path);
    }

    private void setCheckedLists(List<MastersLevel> levels, List<Integer> serviceIds, String search) {
        request.setAttribute(LEVELS + CHECKED, levels);
        request.setAttribute(SERVICES + CHECKED, serviceIds);
        request.setAttribute(SEARCH + CHECKED, search);

    }

}
