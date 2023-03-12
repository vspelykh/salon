package ua.vspelykh.salon.controller.command.header;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.controller.command.CommandNames.PRICING;

/**
 * The PricingCommand class extends the abstract Command class and is responsible for processing pricing page requests.
 *
 * @version 1.0
 */
public class PricingCommand extends Command {

    /**
     * This method is responsible for processing the pricing page request.
     * It retrieves the list of available categories from the service layer and populates the request attribute with this data.
     * It also retrieves the list of services based on the specified filters and populates the request attribute with this data.
     * Additionally, it sets pagination parameters and checked lists for the filters.
     * <p>
     * If any exception occurs, it sends a 500 error response.
     *
     * @throws ServletException if the servlet cannot handle the request for some reason
     * @throws IOException      if an I/O error occurs during the processing of the request
     */
    @Override
    public void process() throws ServletException, IOException {
        try {
            setRequestAttribute(CATEGORIES, getServiceFactory().getServiceCategoryService().findAll(getLocale()));
            List<Integer> categoriesIds = setCategoriesIds();
            int page = getPageParameter();
            int size = getSizeParameter();
            setRequestAttribute(SERVICES, getServiceFactory().getBaseServiceService().findByFilter(categoriesIds, page, size, getLocale()));
            int countOfItems = getServiceFactory().getBaseServiceService().getCountOfCategories(categoriesIds, page, size);
            setPaginationParams(page, size, countOfItems);
            setCheckedList(categoriesIds);
            forward(PRICING);
        } catch (ServiceException e) {
            sendError500();
        }
    }

    /**
     * Method sets the checked categories list in the request attribute.
     *
     * @param categoriesIds the list of checked category IDs
     */
    private void setCheckedList(List<Integer> categoriesIds) {
        setRequestAttribute(CATEGORIES + CHECKED, categoriesIds);
    }

    /**
     * Method retrieves the selected category IDs from the request parameter and returns them in a list.
     * If no categories are selected, an empty list is returned.
     *
     * @return the list of selected category IDs, or an empty list if none are selected
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
     * Method sets the pagination parameters in the request attribute.
     *
     * @param page         the current page number
     * @param size         the number of items per page
     * @param countOfItems the total number of items
     */
    private void setPaginationParams(int page, int size, int countOfItems) {
        setRequestAttribute(SIZES, SIZE_LIST);
        setRequestAttribute(PAGE + CHECKED, page);
        setRequestAttribute(SIZE + CHECKED, size);
        setPaginationAttrs(size, countOfItems);
    }
}
