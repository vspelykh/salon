package ua.vspelykh.salon.tags;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LocalDateTimeParserTag extends TagSupport {

    private String locale;
    private LocalDateTime date;

    public int doStartTag() {
        Locale l;
        if ("ua".equals(locale)) {
            l = Locale.forLanguageTag("uk-UA");
        } else {
            l = Locale.ENGLISH;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm", l);
        JspWriter out = pageContext.getOut();
        try {
            out.print(date.format(formatter));
        } catch (Exception e) {
            e.printStackTrace();
            //TODO
        }
        return SKIP_BODY;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

}
