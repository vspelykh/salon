package ua.vspelykh.salon.tags;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

public class RatingTag extends TagSupport {

    private int mark;

    public int doStartTag() {
        JspWriter out = pageContext.getOut();
        try {
            for (int i = 0; i < mark; i++) {
                out.print("&#9733");
            }
            int emptyStars = 5 - mark;
            for (int i = 0; i < emptyStars; i++) {
                out.print("&#9734");
            }
        } catch (IOException e) {
            e.printStackTrace();
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
