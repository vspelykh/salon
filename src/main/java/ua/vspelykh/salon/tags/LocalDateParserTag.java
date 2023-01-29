package ua.vspelykh.salon.tags;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LocalDateParserTag extends TagSupport {

    private String locale;
    private LocalDate date;

    public int doStartTag() {
        Locale l;
        if ("ua".equals(locale)) {
            l = Locale.forLanguageTag("uk-UA");
        } else {
            l = Locale.ENGLISH;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, EEEE", l);
        JspWriter out = pageContext.getOut();
        try {
            out.print(date.format(formatter));
        } catch (Exception e) {
            e.printStackTrace();
            //TODO
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
