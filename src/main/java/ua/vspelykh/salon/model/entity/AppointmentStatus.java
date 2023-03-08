package ua.vspelykh.salon.model.entity;

/**
 * The AppointmentStatus enumeration represents the status of an appointment.
 * The possible values are: RESERVED, SUCCESS, CANCELLED and DIDNT_COME.
 * <p>
 * This enumeration defines four constant values, each representing a different appointment status.
 * These constant values are:
 * RESERVED: The appointment has been reserved by the client, but
 * has not yet been completed.
 * SUCCESS: The appointment has been successfully completed.
 * CANCELLED: The appointment has been cancelled by the client or the
 * business.
 * DIDNT_COME: The client did not show up for the appointment.
 *
 * @version 1.0
 */
public enum AppointmentStatus {
    RESERVED, SUCCESS, CANCELLED, DIDNT_COME
}
