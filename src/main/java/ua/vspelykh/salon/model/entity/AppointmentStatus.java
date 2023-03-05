package ua.vspelykh.salon.model.entity;

/**
 * The AppointmentStatus enumeration represents the status of an appointment.
 * The possible values are: {@code RESERVED}, {@code SUCCESS}, {@code CANCELLED},
 * <p>
 * and {@code DIDNT_COME}.
 * <p>
 * This enumeration defines four constant values, each representing a different appointment status.
 * These constant values are:
 * {@code RESERVED}: The appointment has been reserved by the client, but
 * has not yet been completed.
 * {@code SUCCESS}: The appointment has been successfully completed.
 * {@code CANCELLED}: The appointment has been cancelled by the client or the
 * business.
 * {@code DIDNT_COME}: The client did not show up for the appointment.
 *
 * @version 1.0
 */
public enum AppointmentStatus {
    RESERVED, SUCCESS, CANCELLED, DIDNT_COME
}
