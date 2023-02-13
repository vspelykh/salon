package ua.vspelykh.salon.controller.command.appointment;

import ua.vspelykh.salon.controller.command.Command;

import javax.servlet.ServletException;
import java.io.IOException;

import static ua.vspelykh.salon.controller.command.CommandNames.CONSULTATION;

public class ConsultationCommand extends Command {

    @Override
    public void process() throws ServletException, IOException {
        forward(CONSULTATION);
    }
}
