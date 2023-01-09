package ua.vspelykh.salon.dao.mapper;

import ua.vspelykh.salon.dao.mapper.impl.*;
import ua.vspelykh.salon.model.*;

public class RowMapperFactory {

    private static final UserRowMapper userRowMapper = new UserRowMapper();
    private static final UserLevelRowMapper userLevelRowMapper = new UserLevelRowMapper();
    private static final BaseServiceRowMapper baseServiceRowMapper = new BaseServiceRowMapper();
    private static final MasterServiceRowMapper masterServiceRowMapper = new MasterServiceRowMapper();
    private static final AppointmentRowMapper appointmentRowMapper = new AppointmentRowMapper();
    private static final OrderingRowMapper orderingRowMapper = new OrderingRowMapper();
    private static final MarkRowMapper markRowMapper = new MarkRowMapper();
    private static final  ConsultationRowMapper consultationRowMapper = new ConsultationRowMapper();
    private RowMapperFactory() {

    }

    public static RowMapper<User> getUserRowMapper() {
        return userRowMapper;
    }

    public static RowMapper<UserLevel> getUserLevelRowMapper() {
        return userLevelRowMapper;
    }

    public static RowMapper<BaseService> getBaseServiceRowMapper() {
        return baseServiceRowMapper;
    }

    public static RowMapper<Service> getMasterServiceRowMapper() {
        return masterServiceRowMapper;
    }

    public static RowMapper<Appointment> getAppointmentRowMapper() {
        return appointmentRowMapper;
    }

    public static RowMapper<Ordering> getOrderingRowMapper() {
        return orderingRowMapper;
    }

    public static RowMapper<Mark> getMarkRowMapper() {
        return markRowMapper;
    }

    public static RowMapper<Consultation> getConsultationRowMapper() {
        return consultationRowMapper;
    }
}
