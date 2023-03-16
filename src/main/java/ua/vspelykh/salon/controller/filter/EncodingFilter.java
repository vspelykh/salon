package ua.vspelykh.salon.controller.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * A filter that sets the character encoding of incoming requests and outgoing responses to UTF-8.
 * This is important for proper handling of non-ASCII characters in web applications.
 *
 * @version 1.0
 */
public class EncodingFilter implements Filter {

    private static final String ENCODING = "UTF-8";

    /**
     * Sets the character encoding of incoming requests and outgoing responses to UTF-8 and passes the request
     * on to the next filter or servlet in the chain.
     *
     * @param req   the ServletRequest object containing the request information.
     * @param resp  the ServletResponse object containing the response information.
     * @param chain the FilterChain object used to invoke the next filter or servlet in the chain.
     * @throws IOException      if an I/O error occurs while processing the request.
     * @throws ServletException if a servlet error occurs while processing the request.
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        req.setCharacterEncoding(ENCODING);
        resp.setCharacterEncoding(ENCODING);
        chain.doFilter(req, resp);
    }
}