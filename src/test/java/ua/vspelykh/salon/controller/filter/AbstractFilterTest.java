package ua.vspelykh.salon.controller.filter;

import org.mockito.Mock;
import ua.vspelykh.salon.AbstractSalonTest;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

abstract class AbstractFilterTest extends AbstractSalonTest {

    @Mock
    protected HttpServletRequest request;

    @Mock
    protected HttpServletResponse response;

    @Mock
    protected HttpSession session;

    @Mock
    protected FilterChain chain;

    protected Filter filter;

    protected void doFilter() throws ServletException, IOException {
        filter.doFilter(request, response, chain);
    }
}