package ua.vspelykh.salon.controller.command.appointment;

import ua.vspelykh.salon.controller.command.Command;

import javax.servlet.ServletException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ua.vspelykh.salon.controller.command.CommandNames.CALENDAR;

public class CalendarCommand extends Command {
    @Override
    public void process() throws ServletException, IOException {

        LocalDateTime now = LocalDateTime.now();
        List<LocalDateTime> dateTimes = new ArrayList<>();
        LocalDateTime localDateTime = LocalDateTime.of(2023, 1, 16, 11, 11, 11);
        LocalDateTime localDateTime1 = LocalDateTime.of(2023, 2, 18, 11, 11, 11);

        dateTimes.add(now);
        dateTimes.add(localDateTime1);
        dateTimes.add(localDateTime);

        request.setAttribute("days", dateTimes);
        forward(CALENDAR);
    }
}
