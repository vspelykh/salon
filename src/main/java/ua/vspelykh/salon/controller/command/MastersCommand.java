package ua.vspelykh.salon.controller.command;

import ua.vspelykh.salon.dto.UserMasterDTO;
import ua.vspelykh.salon.model.MastersLevel;
import ua.vspelykh.salon.service.BaseServiceService;
import ua.vspelykh.salon.service.ServiceFactory;
import ua.vspelykh.salon.service.UserService;
import ua.vspelykh.salon.util.MasterSort;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MastersCommand extends Command {

    private final UserService userService = ServiceFactory.getUserService();
    private final BaseServiceService baseService = ServiceFactory.getBaseServiceService();
    private static final String LEVELS = "levels";
    private static final String SERVICES = "services";
    private static final String MASTERS = "masters";

    //TODO String constants for params!
    @Override
    public void process() throws ServletException, IOException {
        try {
            request.setAttribute(LEVELS, MastersLevel.list());
            request.setAttribute(SERVICES, baseService.findAll());
            request.setAttribute("sizes", new int[]{1, 2, 5, 10});
            request.setAttribute("sorts", MasterSort.list());
            List<MastersLevel> levels = setLevelsParam();
            List<Integer> serviceIds = setServiceIds();
            int page = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));
            int size = request.getParameter("size") == null ? 5 : Integer.parseInt(request.getParameter("size"));
            MasterSort sort = request.getParameter("sort") == null ? MasterSort.NAME_ASC : MasterSort.valueOf(request.getParameter("sort"));
            String search = request.getParameter("search");
            List<UserMasterDTO> mastersDto = userService.getMastersDto(levels, serviceIds, search, page, size, sort);
            request.setAttribute(MASTERS, mastersDto);
            setCheckedLists(levels, serviceIds, search);
            setPaginationParams(page, size, userService.getCountOfMasters(), sort);
            forward(MASTERS);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    private void setPaginationParams(int page, int size, int countOfItems, MasterSort sort) {
        request.setAttribute("pageChecked", page);
        request.setAttribute("sizeChecked", size);
        request.setAttribute("sortChecked", sort);
        int[] pages = new int[(int) Math.ceil(countOfItems * 1.0 / size)];
        for (int i = 1, j = 0; i <= pages.length; j++, i++) {
            pages[j] = i;
        }
        request.setAttribute("lastPage", pages.length);
        request.setAttribute("pagesArray", pages);
        request.setAttribute("numOfPages", Math.ceil(countOfItems * 1.0 / size));
        String path = "?" + request.getQueryString().replaceAll("&page=[0-9]*", "");
        request.setAttribute("pathStr", path);
    }

    private void setCheckedLists(List<MastersLevel> levels, List<Integer> serviceIds, String search) {
        request.setAttribute("levelsChecked", levels);
        request.setAttribute("servicesChecked", serviceIds);
        request.setAttribute("searchChecked", search);

    }

    private List<MastersLevel> setLevelsParam() {
        if (checkNullParam(request.getParameter(LEVELS))) {
            List<MastersLevel> levels = new ArrayList<>();
            for (String level : request.getParameterValues(LEVELS)) {
                levels.add(MastersLevel.valueOf(level));
            }
            return levels;
        } else return null;
    }

    private List<Integer> setServiceIds() {
        if (checkNullParam(request.getParameter(SERVICES))) {
            List<Integer> serviceIds = new ArrayList<>();
            for (String level : request.getParameterValues(SERVICES)) {
                serviceIds.add(Integer.valueOf(level));
            }
            return serviceIds;
        } else return null;
    }

    private boolean checkNullParam(String param) {
        return param != null && !param.isEmpty();
    }

    private void setMastersToAttr() {
    }
}
