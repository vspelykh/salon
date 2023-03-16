package ua.vspelykh.salon.listener;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.model.dto.AppointmentDto;
import ua.vspelykh.salon.model.entity.AppointmentStatus;
import ua.vspelykh.salon.service.ServiceFactory;
import ua.vspelykh.salon.service.email.EmailService;
import ua.vspelykh.salon.service.impl.ServiceFactoryImpl;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.text.MessageFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static ua.vspelykh.salon.listener.ListenerConstants.*;

/**
 * This class is a ServletContextListener that schedules a task to send emails every morning to all visitors who
 * had appointments the previous day with a status of SUCCESS. The task is scheduled to run every day at 9:00 AM.
 *
 * @version 1.0
 */
public class ScheduledEmailExecutorListener implements ServletContextListener {

    private static final Logger LOG = LogManager.getLogger(ScheduledEmailExecutorListener.class);

    /**
     * This method is called by the servlet container when the context is initialized. It schedules the email task to run
     * every day at 9:00 AM.
     *
     * @param sce The ServletContextEvent object.
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        executeTask();
    }

    /**
     * This method is called by the servlet container when the context is destroyed.
     *
     * @param sce The ServletContextEvent object.
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }

    /**
     * This method schedules the email task to run every day at 9:00 AM.
     */
    private void executeTask() {

        ZonedDateTime now = ZonedDateTime.now(ZoneId.of(KYIV));
        ZonedDateTime nextRun = now.withHour(9).withMinute(0).withSecond(0);
        if (now.compareTo(nextRun) > 0)
            nextRun = nextRun.plusDays(1);

        Duration duration = Duration.between(now, nextRun);
        long initialDelay = duration.getSeconds();

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate((getTask()),
                initialDelay,
                TimeUnit.DAYS.toSeconds(1),
                TimeUnit.SECONDS);
    }

    /**
     * This method retrieves all appointments from the previous day and sends feedback emails to visitors who had
     * appointment with a status of SUCCESS.
     *
     * @return A Runnable object that represents the email task.
     */
    private Runnable getTask() {
        try {
            ServiceFactory serviceFactory = ServiceFactoryImpl.getServiceFactory();
            List<AppointmentDto> appointments = serviceFactory.getAppointmentService().getAllByDate(LocalDate.now().minusDays(1));
            if (!appointments.isEmpty()) {
                return () -> {
                    for (AppointmentDto appointment : appointments) {
                        if (appointment.getStatus() == AppointmentStatus.SUCCESS) {
                            String clientName = appointment.getClient().getName() + " " + appointment.getClient().getSurname();
                            String masterName = appointment.getMaster().getName() + " " + appointment.getMaster().getSurname();
                            String feedbackLink = "http://localhost:8080/salon?command=feedback&id=" + appointment.getId();
                            String email = String.format(FEEDBACK_EMAIL, clientName, masterName, feedbackLink);
                            EmailService.sendEmail(appointment.getClient().getEmail(), FEEDBACK_THEME, email);
                            if (LOG.isEnabled(Level.INFO)) {
                                LOG.info(MessageFormat.format("Email for appointment id={0} sent.", appointment.getId()));
                            }
                        }
                    }
                };
            }
        } catch (ServiceException e) {
            LOG.error("Error to get appointments for emails");
        }
        LOG.info("No appointments for send emails");
        return () -> {

        };
    }
}
