package ua.vspelykh.salon.tags;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static ua.vspelykh.salon.model.dao.mapper.Column.UA_LOCALE;
import static ua.vspelykh.salon.util.exception.Messages.ERROR_FORMATTING_LDT;
import static ua.vspelykh.salon.util.exception.Messages.ERROR_WRITING_TO_JSP;

/**
 * A custom JSP tag that formats a LocalDateTime according to a specified locale and pattern.
 * This tag is used to display dates and times in a comfortable view.
 *
 * @version 1.0
 */
public class LocalDateTimeParserTag extends TagSupport {

    private static final Logger LOG = LogManager.getLogger(LocalDateTimeParserTag.class);

    private String locale;
    private LocalDateTime date;

    private static final String UA_TAG = "uk-UA";
    private static final String DATE_TIME_PATTERN = "dd-MM-yyyy HH:mm";

    /**
     * Processes the start tag for this instance.
     *
     * @return SKIP_BODY to skip the body of the tag.
     * @throws JspException if an error occurred while processing this tag.
     */
    @Override
    public int doStartTag() throws JspException {
        Locale parsedLocale = parseLocale();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN, parsedLocale);
        printToPageContext(formatter);
        return SKIP_BODY;
    }

    /**
     * Returns a parsed locale based on the locale value.
     *
     * @return the parsed locale.
     */
    private Locale parseLocale() {
        if (UA_LOCALE.equals(locale)) {
            return Locale.forLanguageTag(UA_TAG);
        }
        return Locale.ENGLISH;
    }

    /**
     * Writes the formatted LocalDateTime to the JspWriter.
     *
     * @param formatter the DateTimeFormatter used to format the LocalDateTime.
     * @throws JspException if an error occurred while processing this tag.
     */
    private void printToPageContext(DateTimeFormatter formatter) throws JspException {
        JspWriter out = pageContext.getOut();
        try {
            out.print(date.format(formatter));
        } catch (DateTimeException e) {
            LOG.error(ERROR_FORMATTING_LDT, e);
            throw new JspException(ERROR_FORMATTING_LDT, e);
        } catch (IOException e) {
            LOG.error(ERROR_WRITING_TO_JSP, e);
            throw new JspException(ERROR_WRITING_TO_JSP, e);
        }
    }


    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

}
