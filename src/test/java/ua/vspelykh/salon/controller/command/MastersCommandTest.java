package ua.vspelykh.salon.controller.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ua.vspelykh.salon.model.entity.MastersLevel;
import ua.vspelykh.salon.service.BaseServiceService;
import ua.vspelykh.salon.service.ServiceCategoryService;
import ua.vspelykh.salon.service.UserService;
import ua.vspelykh.salon.util.MasterSort;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.vspelykh.salon.Constants.NAME_VALUE;
import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.controller.command.CommandNames.MASTERS;
import static ua.vspelykh.salon.controller.filter.LocalizationFilter.LANG;
import static ua.vspelykh.salon.model.dao.impl.DaoTestData.getTestUser;
import static ua.vspelykh.salon.model.dao.mapper.Column.UA_LOCALE;

class MastersCommandTest extends AbstractCommandTest {

    @Mock
    private BaseServiceService baseServiceService;

    @Mock
    private UserService userService;

    @Mock
    private ServiceCategoryService serviceCategoryService;

    private final List<MastersLevel> levels = List.of(MastersLevel.YOUNG);
    private final List<Integer> serviceIds = List.of(1);
    private final List<Integer> categoriesIds = List.of(2, 3);
    private final int page = 1;
    private final int size = 5;
    private final MasterSort sort = MasterSort.RATING_DESC;
    private final String search = NAME_VALUE;

    @BeforeEach
    protected void setUp() throws ServiceException {
        super.setUp();
        initCommand(new MastersCommand());
        prepareMocks();
    }

    @Test
    void mastersProcess() throws ServletException, IOException {
        command.process();
        verifyAttributes();
        verifyForward(MASTERS);
    }

    @Test
    void mastersProcessErrorPage() throws ServiceException, ServletException, IOException {
        when(baseServiceService.findAll(anyString())).thenThrow(ServiceException.class);
        command.process();
        verifyError404();
    }

    @Test
    void mastersProcessEmptyParams() throws ServletException, IOException {
        when(request.getParameter(LEVELS)).thenReturn(null);
        when(request.getParameter(SERVICES)).thenReturn(null);
        when(request.getParameter(CATEGORIES)).thenReturn(null);
        command.process();
        verifyForward(MASTERS);
    }

    protected void prepareMocks() throws ServiceException {
        String locale = UA_LOCALE;
        when(serviceFactory.getBaseServiceService()).thenReturn(baseServiceService);
        when(serviceFactory.getUserService()).thenReturn(userService);
        when(serviceFactory.getServiceCategoryService()).thenReturn(serviceCategoryService);
        when(request.getSession().getAttribute(CURRENT_USER)).thenReturn(getTestUser());
        when(request.getSession().getAttribute(LANG)).thenReturn(locale);
        when(request.getParameter(PAGE)).thenReturn(String.valueOf(page));
        when(request.getParameter(SIZE)).thenReturn(String.valueOf(size));
        when(request.getParameter(SORT)).thenReturn(sort.toString());
        when(request.getParameter(SEARCH)).thenReturn(search);
        when(request.getParameter(LEVELS)).thenReturn(MastersLevel.YOUNG.name());
        when(request.getParameterValues(LEVELS)).thenReturn(new String[]{MastersLevel.YOUNG.name()});
        when(request.getParameter(SERVICES)).thenReturn("1");
        when(request.getParameterValues(SERVICES)).thenReturn(new String[]{String.valueOf(serviceIds.get(0))});
        when(request.getParameter(CATEGORIES)).thenReturn("2,3");
        when(request.getParameterValues(CATEGORIES)).thenReturn(new String[]{String.valueOf(categoriesIds.get(0)), String.valueOf(categoriesIds.get(1))});
        when(serviceFactory.getBaseServiceService().findAll(locale))
                .thenReturn(Collections.emptyList());
        when(serviceFactory.getBaseServiceService().findAll(locale))
                .thenReturn(Collections.emptyList());
        when(serviceFactory.getUserService().getMastersDto(
                levels, serviceIds, categoriesIds, search, page, size, sort, locale))
                .thenReturn(Collections.emptyList());
        when(serviceFactory.getServiceCategoryService().findAll(locale))
                .thenReturn(Collections.emptyList());
        when(serviceFactory.getUserService().getCountOfMasters(levels, serviceIds, categoriesIds, search)).thenReturn(0);
    }

    protected void verifyAttributes() {
        verify(request).setAttribute(LEVELS, MastersLevel.list());
        verify(request).setAttribute(eq(SERVICES), any());
        verify(request).setAttribute(eq(SIZES), any());
        verify(request).setAttribute(eq(SORTS), any());
        verify(request).setAttribute(SEARCH + CHECKED, search);
        verify(request).setAttribute(LEVELS + CHECKED, levels);
        verify(request).setAttribute(SERVICES + CHECKED, serviceIds);
        verify(request).setAttribute(CATEGORIES + CHECKED, categoriesIds);
        verify(request).setAttribute(PAGE + CHECKED, page);
        verify(request).setAttribute(SIZE + CHECKED, size);
        verify(request).setAttribute(SORT + CHECKED, sort);
        verify(request).setAttribute(MASTERS, Collections.emptyList());
        verify(request).setAttribute(CATEGORIES, Collections.emptyList());
    }
}