package ua.vspelykh.salon;

import ua.vspelykh.salon.dto.UserMasterDTO;
import ua.vspelykh.salon.model.MastersLevel;
import ua.vspelykh.salon.service.UserService;
import ua.vspelykh.salon.service.impl.UserServiceImpl;
import ua.vspelykh.salon.util.exception.ServiceException;

import java.sql.SQLException;
import java.time.LocalTime;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws SQLException {

        String  s = "men's haircut 1st group|20|180|1";
        System.out.println(s.split("[|]")[2]);
    }
}
