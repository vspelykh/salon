package ua.vspelykh.salon.model.dao.mapper;

import ua.vspelykh.salon.model.dao.mapper.impl.*;
import ua.vspelykh.salon.model.entity.*;

/**
 * This class provides static methods to create RowMapper instances for different entity classes.
 *
 * @version 1.0
 */
public class RowMapperFactory {

    private static final UserRowMapper USER_ROW_MAPPER = new UserRowMapper();
    private static final UserLevelRowMapper USER_LEVEL_ROW_MAPPER = new UserLevelRowMapper();
    private static final BaseServiceRowMapper BASE_SERVICE_ROW_MAPPER = new BaseServiceRowMapper();
    private static final MasterServiceRowMapper MASTER_SERVICE_ROW_MAPPER = new MasterServiceRowMapper();
    private static final AppointmentRowMapper APPOINTMENT_ROW_MAPPER = new AppointmentRowMapper();
    private static final AppointmentItemRowMapper APPOINTMENT_ITEM_ROW_MAPPER = new AppointmentItemRowMapper();
    private static final FeedbackRowMapper FEEDBACK_ROW_MAPPER = new FeedbackRowMapper();
    private static final ConsultationRowMapper CONSULTATION_ROW_MAPPER = new ConsultationRowMapper();
    private static final WorkingDayRowMapper WORKING_DAY_ROW_MAPPER = new WorkingDayRowMapper();
    private static final ServiceCategoryRowMapper SERVICE_CATEGORY_ROW_MAPPER = new ServiceCategoryRowMapper();
    private static final InvitationRowMapper INVITATION_ROW_MAPPER = new InvitationRowMapper();

    private RowMapperFactory() {

    }

    public static RowMapper<User> getUserRowMapper() {
        return USER_ROW_MAPPER;
    }

    public static RowMapper<UserLevel> getUserLevelRowMapper() {
        return USER_LEVEL_ROW_MAPPER;
    }

    public static RowMapper<BaseService> getBaseServiceRowMapper() {
        return BASE_SERVICE_ROW_MAPPER;
    }

    public static RowMapper<MasterService> getMasterServiceRowMapper() {
        return MASTER_SERVICE_ROW_MAPPER;
    }

    public static RowMapper<Appointment> getAppointmentRowMapper() {
        return APPOINTMENT_ROW_MAPPER;
    }

    public static RowMapper<AppointmentItem> getAppointmentItemRowMapper() {
        return APPOINTMENT_ITEM_ROW_MAPPER;
    }

    public static RowMapper<Feedback> getMarkRowMapper() {
        return FEEDBACK_ROW_MAPPER;
    }

    public static RowMapper<Consultation> getConsultationRowMapper() {
        return CONSULTATION_ROW_MAPPER;
    }

    public static RowMapper<WorkingDay> getWorkingDayRowMapper() {
        return WORKING_DAY_ROW_MAPPER;
    }

    public static RowMapper<ServiceCategory> getServiceCategoryRowMapper() {
        return SERVICE_CATEGORY_ROW_MAPPER;
    }

    public static RowMapper<Invitation> getInvitationRowMapper() {
        return INVITATION_ROW_MAPPER;
    }
}
