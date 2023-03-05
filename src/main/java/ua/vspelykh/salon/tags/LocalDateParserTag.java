package ua.vspelykh.salon.tags;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static ua.vspelykh.salon.model.dao.mapper.Column.UA_LOCALE;
import static ua.vspelykh.salon.util.exception.Messages.ERROR_PARSING_LOCAL_DATE;

/**
 * A custom JSP tag that formats a LocalDate according to a specified locale and pattern.
 * This tag is used on the schedule page for masters, to display dates in a comfortable view.
 *
 * @version 1.0
 */
public class LocalDateParserTag extends TagSupport {

    private static final Logger LOG = LogManager.getLogger(LocalDateParserTag.class);

    private String locale;
    private LocalDate date;

    private static final String UA_TAG = "uk-UA";
    private static final String SCHEDULE_PATTERN = "dd MMMM yyyy, EEEE";

    /**
     * Formats the LocalDate and prints it to the JspWriter.
     *
     * @return SKIP_BODY to skip the body of the tag.
     * @throws JspException if an error occurs while processing the tag
     */
    @Override
    public int doStartTag() throws JspException {
        Locale parsedLocale = parseLocale();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(SCHEDULE_PATTERN, parsedLocale);
        printToPageContext(formatter);
        return SKIP_BODY;
    }

    /**
     * Parses the locale string and returns a Locale object.
     *
     * @return the parsed Locale object
     */
    private Locale parseLocale() {
        if (UA_LOCALE.equals(locale)) {
            return Locale.forLanguageTag(UA_TAG);
        }
        return Locale.ENGLISH;
    }

    /**
     * Formats the date using the specified formatter and writes it to the JspWriter.
     *
     * @param formatter the DateTimeFormatter used to format the date
     * @throws JspException if an error occurs while writing to the JspWriter
     */
    private void printToPageContext(DateTimeFormatter formatter) throws JspException {
        JspWriter out = pageContext.getOut();
        try {
            out.print(date.format(formatter));
        } catch (Exception e) {
            LOG.error(ERROR_PARSING_LOCAL_DATE);
            throw new JspException();
        }
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

}
