package ua.vspelykh.salon.controller.command.appointment;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.model.entity.*;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.model.dao.mapper.Column.MASTER_ID;
import static ua.vspelykh.salon.util.SalonUtils.getTime;
import static ua.vspelykh.salon.util.TimeSlotsUtils.*;
import static ua.vspelykh.salon.util.exception.Messages.MESSAGE_APPOINTMENT_FAIL;
import static ua.vspelykh.salon.util.exception.Messages.MESSAGE_APPOINTMENT_SUCCESS;

/**
 * The CreateAppointmentCommand class is a concrete implementation of the Command pattern, responsible for handling
 * requests related to creating a new appointment for a specific master user.
 * It processes the request parameters and interacts with the application services to validate and save the appointment.
 * If an error occurs during the processing of the request, it sets an error message and redirects to the calendar page.
 *
 * @version 1.0
 */
public class CreateAppointmentCommand extends Command {

    /**
     * Processes the request parameters to parse the selected services, validate and save the appointment,
     * and set a success message before redirecting to the success page. If an error occurs during the processing
     * of the request, it sets an error message and redirects to the calendar page.
     *
     * @throws ServletException if an error occurs during the processing of the request.
     * @throws IOException      if an I/O error occurs while processing the request.
     */
    @Override
    public void process() throws ServletException, IOException {
        try {
            List<MasterService> masterServices = parseServices();
            validateAndSaveAppointment(masterServices);
            request.getSession().setAttribute(MESSAGE, MESSAGE_APPOINTMENT_SUCCESS);
            redirect(SUCCESS_REDIRECT);
        } catch (ServiceException e) {
            setErrorMessageAndRedirect();
        }
    }

    /**
     * Parses the selected services from the request parameters and returns a list of MasterService objects.
     * If the services parameter is null, an empty list is returned. Each service is parsed using its ID,
     * retrieved from the parameter value using a regex pattern.
     *
     * @return a list of MasterService objects parsed from the request parameters.
     * @throws ServiceException if an error occurs while parsing the services or retrieving the service objects.
     */
    private List<MasterService> parseServices() throws ServiceException {
        List<MasterService> masterServices = new ArrayList<>();
        if (isParameterNotNull(SERVICES)) {
            for (String service : request.getParameterValues(SERVICES)) {
                int serviceId = Integer.parseInt(service.split("[|]")[3]);
                masterServices.add(getServiceFactory().getServiceService().findById(serviceId));
            }
        }
        return masterServices;
    }

    /**
     * Validates and saves the appointment using the provided parameters and services. The appointment is built
     * using the buildAppointment method and then validated against the master's working day, existing appointments,
     * and available time slots. If the appointment is valid, it is saved to the database using the appointment service.
     *
     * @param masterServices a list of MasterService objects selected for the appointment.
     * @throws ServiceException if an error occurs while validating or saving the appointment.
     */
    private void validateAndSaveAppointment(List<MasterService> masterServices) throws ServiceException {
        User master = getServiceFactory().getUserService().findById(getParameterInt(MASTER_ID));
        User client = getCurrentUser();
        LocalDate date = getParameterLocalDate(DAY);
        LocalTime time = getParameterLocalTime(TIME);
        PaymentStatus paymentStatus = PaymentStatus.valueOf(getParameter(PAYMENT));
        Appointment appointment = buildAppointment(masterServices, master, client, date, time, paymentStatus);

        validateAppointment(time, appointment, master.getId(), date);
        getServiceFactory().getAppointmentService().save(appointment, masterServices);
    }

    /**
     * Validates the appointment to ensure that it is within the master's working hours and that there are no conflicting
     * appointments.
     *
     * @param time        The appointment time to be validated.
     * @param appointment The appointment to be validated.
     * @param id          The ID of the master user.
     * @param date        The appointment date to be validated.
     * @throws ServiceException If the appointment is not valid.
     */
    private void validateAppointment(LocalTime time, Appointment appointment, int id, LocalDate date) throws ServiceException {
        WorkingDay day = getServiceFactory().getWorkingDayService().getByUserIdAndDate(id, date);
        List<Appointment> appointments = getServiceFactory().getAppointmentService().getByDateAndMasterId(date, id);
        List<LocalTime> slots = getSlots(day.getTimeStart(), day.getTimeEnd(), INTERVAL);
        removeOccupiedSlots(slots, getServiceFactory().getAppointmentService().getByDateAndMasterId(day.getDate(),
                day.getUserId()), INTERVAL);
        removeSlotsIfDateIsToday(slots, day.getDate());
        int allowedMinutes = countAllowedMinutes(getTime(String.valueOf(time)), appointments, day);
        if (!slots.contains(time) || allowedMinutes < appointment.getContinuance()) {
            throw new ServiceException(MESSAGE_APPOINTMENT_FAIL);
        }
    }

    /**
     * Builds an appointment object based on the provided parameters and returns it.
     *
     * @param masterServices The list of services selected by the client for the appointment.
     * @param master         The master user for the appointment.
     * @param client         The client user for the appointment.
     * @param date           The date of the appointment.
     * @param time           The time of the appointment.
     * @param paymentStatus  The payment status of the appointment.
     * @return The appointment object.
     * @throws ServiceException If an error occurs while building the appointment object.
     */
    private Appointment buildAppointment(List<MasterService> masterServices, User master, User client, LocalDate date, LocalTime time, PaymentStatus paymentStatus) throws ServiceException {
        return Appointment.builder()
                .masterId(master.getId())
                .clientId(client.getId())
                .continuance(getTotalContinuance(masterServices))
                .date(LocalDateTime.of(date, time))
                .price(getTotalPrice(masterServices, getServiceFactory().getUserService().getUserLevelByUserId(master.getId())))
                .discount(DEFAULT_DISCOUNT)
                .paymentStatus(paymentStatus)
                .status(AppointmentStatus.RESERVED).build();
    }

    /**
     * Calculates the total price of the appointment based on the selected services and the user level of the master.
     *
     * @param masterServices The list of services selected by the client for the appointment.
     * @param userLevel      The user level of the master.
     * @return The total price of the appointment.
     * @throws ServiceException If an error occurs while calculating the total price.
     */
    private int getTotalPrice(List<MasterService> masterServices, UserLevel userLevel) throws ServiceException {
        double totalPrice = 0;

        for (MasterService masterService : masterServices) {
            BaseService baseService = getServiceFactory().getBaseServiceService().findById(masterService.getBaseServiceId());
            totalPrice += baseService.getPrice() * userLevel.getLevel().getIndex();
        }
        return (int) totalPrice;
    }

    /**
     * Calculates and returns the total duration of all the services included in the appointment.
     *
     * @param masterServices a list of MasterService objects representing the services included in the appointment
     * @return an integer value representing the total duration of the appointment
     */
    private int getTotalContinuance(List<MasterService> masterServices) {
        int totalContinuance = 0;
        for (MasterService masterService : masterServices) {
            totalContinuance += masterService.getContinuance();
        }
        return totalContinuance;
    }

    /**
     * Sets an error message attribute in the current session and redirects the user to the calendar page for the
     * specified master user on the specified date.
     *
     * @throws IOException if an I/O error occurs while processing the request.
     */
    private void setErrorMessageAndRedirect() throws IOException {
        request.getSession().setAttribute(ERROR, HAS_ERROR);
        redirect(CALENDAR_REDIRECT + request.getParameter(DAY) + ID_PARAM_REDIRECT + getParameterInt(MASTER_ID));
    }
}
