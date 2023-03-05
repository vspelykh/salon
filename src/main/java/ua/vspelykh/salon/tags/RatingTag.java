package ua.vspelykh.salon.tags;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

import static ua.vspelykh.salon.util.exception.Messages.ERROR_PRINT_STARS;

/**
 * A custom JSP tag that prints out a rating with stars for feedback purposes.
 *
 * @version 1.0
 */
public class RatingTag extends TagSupport {

    private static final Logger LOG = LogManager.getLogger(RatingTag.class);

    private int mark;

    private static final String FULL_STAR_SIGN = "&#9733";
    private static final String EMPTY_STAR_SIGN = "&#9734";

    /**
     * Prints stars based on the mark attribute value.
     *
     * @return SKIP_BODY
     */
    @Override
    public int doStartTag() {
        JspWriter out = pageContext.getOut();
        try {
            for (int i = 0; i < mark; i++) {
                out.print(FULL_STAR_SIGN);
            }
            int emptyStars = 5 - mark;
            for (int i = 0; i < emptyStars; i++) {
                out.print(EMPTY_STAR_SIGN);
            }
        } catch (IOException e) {
            LOG.error(ERROR_PRINT_STARS);
        }
        return SKIP_BODY;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }
}
