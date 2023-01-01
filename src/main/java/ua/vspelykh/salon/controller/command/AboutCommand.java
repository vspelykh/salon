package ua.vspelykh.salon.controller.command;

import javax.servlet.ServletException;
import java.io.IOException;

public class AboutCommand extends Command{
    @Override
    public void process() throws ServletException, IOException {
        forward("about");
    }
}
