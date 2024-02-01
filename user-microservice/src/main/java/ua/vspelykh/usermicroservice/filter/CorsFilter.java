package ua.vspelykh.usermicroservice.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter {

    private static final String CUSTOM_HEADER_FOR_SWAGGER = "Source";
    private static final HttpMethod PREFLIGHT_REQUEST_METHOD = HttpMethod.OPTIONS;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestSourceHeader = httpRequest.getHeader(CUSTOM_HEADER_FOR_SWAGGER);
        boolean isRequestMethodPreflight = PREFLIGHT_REQUEST_METHOD.matches(httpRequest.getMethod());

        if (requestSourceHeader != null || isRequestMethodPreflight) {
            httpResponse.setHeader("Access-Control-Allow-Origin", "*");
            httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
            httpResponse.setHeader("Access-Control-Allow-Methods",
                    "DELETE, GET, POST, PUT");
            httpResponse.setHeader("Access-Control-Max-Age", "3600");
            httpResponse.setHeader("Access-Control-Allow-Headers",
                    "Origin, Source, X-Requested-With, Content-Type, Accept, Key, Authorization");
        }

        chain.doFilter(request, response);
    }
}