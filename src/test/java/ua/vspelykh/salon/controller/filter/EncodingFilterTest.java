package ua.vspelykh.salon.controller.filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.mockito.Mockito.*;

class EncodingFilterTest extends AbstractFilterTest {

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        filter = new EncodingFilter();
    }

    @Test
    void doFilterSetsCharacterEncoding() throws IOException, ServletException {
        String existingEncoding = "ISO-8859-1";
        when(request.getCharacterEncoding()).thenReturn(existingEncoding);

        doFilter();

        String encoding = "UTF-8";
        verify(request, times(1)).setCharacterEncoding(encoding);
        verify(response, times(1)).setCharacterEncoding(encoding);
        verify(chain, times(1)).doFilter(request, response);
    }
}