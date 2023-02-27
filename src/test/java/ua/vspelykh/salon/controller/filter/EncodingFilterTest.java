package ua.vspelykh.salon.controller.filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.mockito.Mockito.*;

class EncodingFilterTest extends AbstractFilterTest {

    private final String encoding = "UTF-8";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        filter = new EncodingFilter();
    }

    @Test
    void doFilterSetsCharacterEncodingIfNull() throws IOException, ServletException {
        when(request.getCharacterEncoding()).thenReturn(null);

        filter.doFilter(request, response, chain);

        verify(request, times(1)).setCharacterEncoding(encoding);
        verify(response, times(1)).setCharacterEncoding(encoding);
        verify(chain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterSetsCharacterEncoding() throws IOException, ServletException {
        String existingEncoding = "ISO-8859-1";
        when(request.getCharacterEncoding()).thenReturn(existingEncoding);

        filter.doFilter(request, response, chain);

        verify(request, times(1)).setCharacterEncoding(encoding);
        verify(response, times(1)).setCharacterEncoding(encoding);
        verify(chain, times(1)).doFilter(request, response);
    }
}