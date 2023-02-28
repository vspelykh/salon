package ua.vspelykh.salon.controller.filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ua.vspelykh.salon.model.entity.Role;
import ua.vspelykh.salon.model.entity.User;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import static org.mockito.Mockito.*;
import static ua.vspelykh.salon.Constants.ID_VALUE;
import static ua.vspelykh.salon.controller.Controller.COMMAND;
import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.controller.command.CommandNames.GET_SCHEDULE;
import static ua.vspelykh.salon.controller.command.CommandTestData.ID_VALUE_2;
import static ua.vspelykh.salon.model.dao.impl.DaoTestData.getTestUser;
import static ua.vspelykh.salon.model.dao.mapper.Column.ID;

class SecurityFilterTest extends AbstractFilterTest {

    @Mock
    private ServletContext context;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(request.getSession()).thenReturn(session);
        when(request.getServletContext()).thenReturn(context);
        when(request.getServletContext().getContextPath()).thenReturn("");
        filter = new SecurityFilter();
    }

    @Test
    void testCommandIsWrong() throws ServletException, IOException {
        when(session.getAttribute(CURRENT_USER)).thenReturn(getTestUser());
        when(request.getParameter(COMMAND)).thenReturn(COMMAND);
        doFilter();
        verify(chain).doFilter(request, response);
    }

    @Test
    void testCurrentUserIsNull() throws ServletException, IOException {
        when(session.getAttribute(CURRENT_USER)).thenReturn(null).thenReturn(getTestUser());
        doFilter();
        verify(chain).doFilter(request, response);
    }

    @Test
    void testSetGuestRoleIfRolesIsEmpty() throws ServletException, IOException {
        User actualUser = User.builder().roles(null).build();
        User userForSet = User.builder().roles(Set.of(Role.GUEST)).build();
        when(session.getAttribute(CURRENT_USER)).thenReturn(actualUser);
        when(request.getParameter(COMMAND)).thenReturn(MASTERS);

        doFilter();

        verify(session).setAttribute(CURRENT_USER, userForSet);
        verify(chain).doFilter(request, response);
    }

    @Test
    void testRedirectToHomeIfFromLoginPageIfUserIsLogged() throws ServletException, IOException {
        User master = User.builder().id(ID_VALUE).roles(Set.of(Role.ADMINISTRATOR)).build();
        when(session.getAttribute(CURRENT_USER)).thenReturn(master);
        when(request.getParameter(COMMAND)).thenReturn(LOGIN);

        doFilter();

        when(request.getParameter(COMMAND)).thenReturn(SIGN_UP);

        doFilter();
        verify(response, times(2)).sendRedirect(HOME_REDIRECT);
    }

    @Test
    void testAccessForAdminsScheduleCommand() throws ServletException, IOException {
        User master = User.builder().id(ID_VALUE).roles(Set.of(Role.ADMINISTRATOR)).build();
        when(session.getAttribute(CURRENT_USER)).thenReturn(master);
        when(request.getParameter(COMMAND)).thenReturn(GET_SCHEDULE);
        when(request.getParameter(ID)).thenReturn(String.valueOf(ID_VALUE_2));

        doFilter();

        verify(response, never()).sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
        verify(chain).doFilter(request, response);
    }

    @Test
    void testRedirectToLoginPage() throws ServletException, IOException {
        User master = User.builder().roles(Set.of(Role.GUEST)).build();
        when(session.getAttribute(CURRENT_USER)).thenReturn(master);
        when(request.getParameter(COMMAND)).thenReturn(PROFILE);

        doFilter();

        verify(response, never()).sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
        verify(chain, never()).doFilter(request, response);
        verify(response).sendRedirect(HOME_REDIRECT + COMMAND_PARAM + LOGIN);
    }

    @Test
    void testAccessForMastersAndScheduleCommand() throws ServletException, IOException {
        User master = User.builder().id(ID_VALUE).roles(Set.of(Role.HAIRDRESSER)).build();
        when(session.getAttribute(CURRENT_USER)).thenReturn(master);
        when(request.getParameter(COMMAND)).thenReturn(GET_SCHEDULE);
        when(request.getParameter(ID)).thenReturn(String.valueOf(ID_VALUE));

        doFilter();

        verify(response, never()).sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
        verify(chain).doFilter(request, response);
    }

    @Test
    void testAccessDeniedForMastersAndScheduleCommand() throws ServletException, IOException {
        User master = User.builder().roles(Set.of(Role.HAIRDRESSER)).build();
        when(session.getAttribute(CURRENT_USER)).thenReturn(master);
        when(request.getParameter(COMMAND)).thenReturn(GET_SCHEDULE);
        when(request.getParameter(ID)).thenReturn(String.valueOf(ID_VALUE_2));

        doFilter();

        verify(response).sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
    }

    @Test
    void testAccessDenied() throws ServletException, IOException {
        User clientUser = User.builder().roles(Set.of(Role.CLIENT)).build();
        when(session.getAttribute(CURRENT_USER)).thenReturn(clientUser);
        when(request.getParameter(COMMAND)).thenReturn(GET_SCHEDULE);

        doFilter();

        verify(response).sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
    }

    @Test
    void testLogoutIfUserIsBanned() throws ServletException, IOException {
        User guestUser = User.builder().roles(Set.of(Role.GUEST)).build();

        User user = getTestUser();
        user.setRoles(Collections.emptySet());
        when(session.getAttribute(CURRENT_USER)).thenReturn(user);

        doFilter();

        verify(session).removeAttribute(IS_LOGGED);
        verify(session).removeAttribute(CURRENT_USER);
        verify(session).setAttribute(CURRENT_USER, guestUser);
        verify(response).sendRedirect(HOME_REDIRECT);
    }
}