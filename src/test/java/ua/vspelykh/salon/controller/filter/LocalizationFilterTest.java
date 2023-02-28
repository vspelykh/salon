package ua.vspelykh.salon.controller.filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.mockito.Mockito.*;
import static ua.vspelykh.salon.controller.ControllerConstants.HOME_REDIRECT;
import static ua.vspelykh.salon.controller.ControllerConstants.MASTERS_REDIRECT;
import static ua.vspelykh.salon.controller.filter.LocalizationFilter.LANG;
import static ua.vspelykh.salon.model.dao.mapper.Column.UA_LOCALE;

class LocalizationFilterTest extends AbstractFilterTest {

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        filter = new LocalizationFilter();
    }

    @Test
    void filterLocalizationChangeLang() throws IOException, ServletException {
        String lang = "en";
        when(request.getSession()).thenReturn(session);
        when(request.getContextPath()).thenReturn("");
        when(request.getParameter(LANG)).thenReturn(lang);
        when(request.getQueryString()).thenReturn("");

        doFilter();

        verify(session).setAttribute(LANG, lang);
        verify(response).sendRedirect(HOME_REDIRECT);
    }

    @Test
    void filterLocalizationChangeLangFromCommand() throws IOException, ServletException {
        String lang = "en";
        when(request.getSession()).thenReturn(session);
        when(request.getContextPath()).thenReturn("");
        when(request.getParameter(LANG)).thenReturn(lang);
        when(request.getQueryString()).thenReturn("command=masters");

        doFilter();

        verify(session).setAttribute(LANG, lang);
        verify(response).sendRedirect(MASTERS_REDIRECT);
    }

    @Test
    void filterLocalizationInvalidQuery() throws IOException, ServletException {
        String lang = "en";
        String queryString = "invalidQuery";
        when(request.getParameter(LANG)).thenReturn(lang);
        when(request.getQueryString()).thenReturn(queryString);
        when(request.getContextPath()).thenReturn("");
        when(request.getSession()).thenReturn(session);

        doFilter();

        verify(session).setAttribute(LANG, lang);
        verify(response).sendRedirect(HOME_REDIRECT);
    }

    @Test
    void filterLocalizationNoParam() throws IOException, ServletException {
        when(request.getSession()).thenReturn(session);

        doFilter();

        verify(session).getAttribute(LANG);
        verify(session).setAttribute(LANG, UA_LOCALE);
        verify(chain).doFilter(request, response);
        verifyNoMoreInteractions(session, chain);
        verifyNoInteractions(response);
    }
}
