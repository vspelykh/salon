package ua.vspelykh.salon.controller.command;

import ua.vspelykh.salon.model.dto.UserMasterDTO;
import ua.vspelykh.salon.model.entity.MastersLevel;
import ua.vspelykh.salon.model.entity.Role;
import ua.vspelykh.salon.util.MasterSort;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.controller.command.CommandNames.MASTERS;

/**
 * The MastersCommand class is a concrete implementation of the Command pattern, responsible for handling
 * requests related to retrieving information about available masters and filtering them based on various criteria.
 *
 * @version 1.0
 */
public class MastersCommand extends Command {

    /**
     * Processes the request parameters to retrieve the necessary data about available masters and their services,
     * filters them based on the requested criteria, and generates the page to display the filtered results.
     * If an error occurs during the processing of the request, it sends a 404 error response to the client.
     *
     * @throws ServletException if an error occurs during the processing of the request.
     * @throws IOException      if an I/O error occurs while processing the request.
     */
    @Override
    public void process() throws ServletException, IOException {
        try {
            setFilterAttributes();
            List<MastersLevel> levels = setLevelsParam();
            List<Integer> serviceIds = setServiceIds();
            List<Integer> categoriesIds = setCategoriesIds();

            MasterSort sort = getParameter(SORT) == null ? MasterSort.NAME_ASC : MasterSort.valueOf(getParameter(SORT));
            String search = getParameter(SEARCH);
            setCheckedLists(levels, serviceIds, search, categoriesIds);
            List<UserMasterDTO> mastersDto = getServiceFactory().getUserService().getMastersDto(levels, serviceIds, categoriesIds,
                    search, getPageParameter(), getSizeParameter(), sort, getLocale());
            setRequestAttribute(MASTERS, mastersDto);

            setRequestAttribute(CATEGORIES, getServiceFactory().getServiceCategoryService().findAll(getLocale()));
            int countOfItems = getServiceFactory().getUserService().getCountOfMasters(levels, serviceIds, categoriesIds, search);
            setPaginationParams(getPageParameter(), getSizeParameter(), countOfItems, sort);
            request.setAttribute(IS_ADMIN, getCurrentUser().getRoles().contains(Role.ADMINISTRATOR));
            forward(MASTERS);
        } catch (ServiceException e) {
            sendError404();
        }
    }

    /**
     * Sets the necessary filter attributes to be displayed on the page to filter the available masters.
     *
     * @throws ServiceException if an error occurs while retrieving the necessary data from the application services.
     */
    private void setFilterAttributes() throws ServiceException {
        setRequestAttribute(LEVELS, MastersLevel.list());
        setRequestAttribute(SERVICES, getServiceFactory().getBaseServiceService().findAll(getLocale()));
        setRequestAttribute(SIZES, SIZE_LIST);
        setRequestAttribute(SORTS, MasterSort.list());
    }

    /**
     * Retrieves the selected masters levels from the request parameters and returns them as a list.
     *
     * @return the selected masters levels as a list.
     */
    private List<MastersLevel> setLevelsParam() {
        if (!isParameterNull(LEVELS)) {
            List<MastersLevel> levels = new ArrayList<>();
            for (String level : request.getParameterValues(LEVELS)) {
                levels.add(MastersLevel.valueOf(level));
            }
            return levels;
        } else return Collections.emptyList();
    }

    /**
     * Retrieves the selected service IDs from the request parameters and returns them as a list.
     *
     * @return the selected service IDs as a list.
     */
    private List<Integer> setServiceIds() {
        if (!isParameterNull(SERVICES)) {
            List<Integer> serviceIds = new ArrayList<>();
            for (String service : request.getParameterValues(SERVICES)) {
                serviceIds.add(Integer.valueOf(service));
            }
            return serviceIds;
        } else return Collections.emptyList();
    }

    /**
     * Sets the categoriesIds list based on the "categories" request parameter.
     * If the parameter is null, returns an empty list.
     *
     * @return the categoriesIds list.
     */
    private List<Integer> setCategoriesIds() {
        if (!isParameterNull(CATEGORIES)) {
            List<Integer> categoriesIds = new ArrayList<>();
            for (String categories : request.getParameterValues(CATEGORIES)) {
                categoriesIds.add(Integer.valueOf(categories));
            }
            return categoriesIds;
        } else return Collections.emptyList();
    }

    /**
     * Sets the pagination parameters in the request attributes based on the given parameters.
     *
     * @param page         the current page number.
     * @param size         the number of items per page.
     * @param countOfItems the total number of items.
     * @param sort         the sorting order.
     */
    private void setPaginationParams(int page, int size, int countOfItems, MasterSort sort) {
        setRequestAttribute(PAGE + CHECKED, page);
        setRequestAttribute(SIZE + CHECKED, size);
        setRequestAttribute(SORT + CHECKED, sort);
        setPaginationAttrs(size, countOfItems);
    }

    /**
     * Sets the checked lists in the request attributes based on the given parameters.
     *
     * @param levels        the list of masters levels to filter by.
     * @param serviceIds    the list of service IDs to filter by.
     * @param search        the search string.
     * @param categoriesIds the list of category IDs to filter by.
     */
    private void setCheckedLists(List<MastersLevel> levels, List<Integer> serviceIds, String search, List<Integer> categoriesIds) {
        setRequestAttribute(LEVELS + CHECKED, levels);
        setRequestAttribute(SERVICES + CHECKED, serviceIds);
        setRequestAttribute(SEARCH + CHECKED, search);
        setRequestAttribute(CATEGORIES + CHECKED, categoriesIds);
    }
}
