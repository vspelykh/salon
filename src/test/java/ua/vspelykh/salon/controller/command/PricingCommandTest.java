package ua.vspelykh.salon.controller.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ua.vspelykh.salon.service.BaseServiceService;
import ua.vspelykh.salon.service.ServiceCategoryService;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.controller.command.CommandNames.PRICING;
import static ua.vspelykh.salon.controller.filter.LocalizationFilter.LANG;
import static ua.vspelykh.salon.model.dao.mapper.Column.UA_LOCALE;

class PricingCommandTest extends AbstractCommandTest {

    @Mock
    private BaseServiceService baseServiceService;

    @Mock
    private ServiceCategoryService serviceCategoryService;

    private final List<Integer> categoriesIds = List.of(1);
    private final int page = 1;
    private final int size = 5;

    @BeforeEach
    protected void setUp() throws ServiceException {
        super.setUp();
        initCommand(new PricingCommand());
        when(serviceFactory.getBaseServiceService()).thenReturn(baseServiceService);
        when(serviceFactory.getServiceCategoryService()).thenReturn(serviceCategoryService);
    }

    @Test
    void pricingProcess() throws ServletException, IOException, ServiceException {
        prepareMocks();
        command.process();
        verifyAttributes();
        verify(request).setAttribute(CATEGORIES + CHECKED, List.of(1));
        verifyForward(PRICING);
    }

    @Test
    void pricingProcessEmptyList() throws ServletException, IOException, ServiceException {
        prepareMocks();
        when(request.getParameter(CATEGORIES)).thenReturn(null);
        command.process();
        verifyAttributes();
        verify(request).setAttribute(CATEGORIES + CHECKED, Collections.emptyList());
        verifyForward(PRICING);
    }

    @Test
    void pricingProcessErrorPage() throws ServiceException, IOException, ServletException {
        prepareMocks();
        when(serviceCategoryService.findAll(anyString())).thenThrow(ServiceException.class);
        command.process();
        verifyError500();
    }

    protected void prepareMocks() throws ServiceException {
        String locale = UA_LOCALE;
        when(request.getSession().getAttribute(LANG)).thenReturn(locale);
        when(request.getParameter(PAGE)).thenReturn(String.valueOf(page));
        when(request.getParameter(SIZE)).thenReturn(String.valueOf(size));
        when(request.getParameter(CATEGORIES)).thenReturn("1");
        when(request.getParameterValues(CATEGORIES)).thenReturn(new String[]{"1"});
        when(request.getParameterValues(CATEGORIES))
                .thenReturn(new String[]{String.valueOf(categoriesIds.get(0))});
        when(serviceFactory.getBaseServiceService().findByFilter(
                categoriesIds, page, size, locale))
                .thenReturn(Collections.emptyList());
        when(serviceFactory.getBaseServiceService().getCountOfCategories(
                categoriesIds, page, size)).thenReturn(0);
        when(serviceFactory.getServiceCategoryService().findAll(locale))
                .thenReturn(Collections.emptyList());
    }

    protected void verifyAttributes() {
        verify(request).setAttribute(eq(CATEGORIES), any());
        verify(request).setAttribute(eq(SERVICES), any());
        verify(request).setAttribute(eq(SIZES), any());
        verify(request).setAttribute(PAGE + CHECKED, page);
        verify(request).setAttribute(SIZE + CHECKED, size);
    }
}