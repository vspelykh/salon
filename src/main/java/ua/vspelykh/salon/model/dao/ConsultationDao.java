package ua.vspelykh.salon.model.dao;

import ua.vspelykh.salon.model.entity.Consultation;
import ua.vspelykh.salon.util.exception.DaoException;

import java.util.List;

/**
 * This interface extends the Dao interface defines methods for accessing consultation data from a data source.
 *
 * @version 1.0
 */
public interface ConsultationDao extends Dao<Consultation> {

    /**
     * Retrieves a list of new consultations from the database.
     *
     * @return a list of Consultation objects that have not been processed yet
     * @throws DaoException if an error occurs while accessing the persistence layer
     */
    List<Consultation> getNewConsultations() throws DaoException;
}
