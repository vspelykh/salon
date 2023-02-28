package ua.vspelykh.salon.model.dao.mapper;

import ua.vspelykh.salon.model.dao.mapper.impl.*;
import ua.vspelykh.salon.model.entity.*;

public class RowMapperFactory {

    private static final UserRowMapper userRowMapper = new UserRowMapper();
    private static final UserLevelRowMapper userLevelRowMapper = new UserLevelRowMapper();
    private static final BaseServiceRowMapper baseServiceRowMapper = new BaseServiceRowMapper();
    private static final MasterServiceRowMapper masterServiceRowMapper = new MasterServiceRowMapper();
    private static final AppointmentRowMapper appointmentRowMapper = new AppointmentRowMapper();
    private static final OrderingRowMapper orderingRowMapper = new OrderingRowMapper();
    private static final MarkRowMapper markRowMapper = new MarkRowMapper();
    private static final  ConsultationRowMapper consultationRowMapper = new ConsultationRowMapper();
    private static final WorkingDayRowMapper workingDayRowMapper = new WorkingDayRowMapper();
    private static final ServiceCategoryRowMapper serviceCategoryRowMapper = new ServiceCategoryRowMapper();
    private static final InvitationRowMapper invitationRowMapper = new InvitationRowMapper();

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

    public static RowMapper<MasterService> getMasterServiceRowMapper() {
        return masterServiceRowMapper;
    }

    public static RowMapper<Appointment> getAppointmentRowMapper() {
        return appointmentRowMapper;
    }

    public static RowMapper<AppointmentItem> getOrderingRowMapper() {
        return orderingRowMapper;
    }

    public static RowMapper<Feedback> getMarkRowMapper() {
        return markRowMapper;
    }

    public static RowMapper<Consultation> getConsultationRowMapper() {
        return consultationRowMapper;
    }

    public static RowMapper<WorkingDay> getWorkingDayRowMapper() {
        return workingDayRowMapper;
    }

    public static RowMapper<ServiceCategory> getServiceCategoryRowMapper() {
        return serviceCategoryRowMapper;
    }

    public static RowMapper<Invitation> getInvitationRowMapper() {
        return invitationRowMapper;
    }
}