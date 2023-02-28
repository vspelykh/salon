package ua.vspelykh.salon.tags;

import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static ua.vspelykh.salon.model.dao.mapper.Column.UA_LOCALE;

public class LocalDateParserTag extends TagSupport {

    private static final Logger LOG = LogManager.getLogger(LocalDateParserTag.class);

    private String locale;
    private LocalDate date;

    @Override
    @SneakyThrows
    public int doStartTag() {
        Locale l;
        if (UA_LOCALE.equals(locale)) {
            l = Locale.forLanguageTag("uk-UA");
        } else {
            l = Locale.ENGLISH;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, EEEE", l);
        JspWriter out = pageContext.getOut();
        try {
            out.print(date.format(formatter));
        } catch (Exception e) {
            LOG.error("Error parsing LocalDate for schedule");
            throw new ServiceException("500.error");
        }
        return SKIP_BODY;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

}
