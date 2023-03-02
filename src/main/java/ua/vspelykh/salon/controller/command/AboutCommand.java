package ua.vspelykh.salon.controller.command;

import javax.servlet.ServletException;
import java.io.IOException;

import static ua.vspelykh.salon.controller.command.CommandNames.ABOUT;

/**
 * This command displays the about page, which shows information about the salon, such as its address and telephone number.
 *
 * @version 1.0
 */
public class AboutCommand extends Command {

    /**
     * Displays the about page.
     *
     * @throws ServletException if an error occurs during the processing of the request.
     * @throws IOException      if an I/O error occurs while processing the request.
     */
    @Override
    public void process() throws ServletException, IOException {
        forward(ABOUT);
    }
}
