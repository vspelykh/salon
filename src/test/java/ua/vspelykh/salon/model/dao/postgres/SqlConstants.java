package ua.vspelykh.salon.model.dao.postgres;

public interface SqlConstants {

    interface Users {
        String SELECT_USER_BY_ID = "SELECT * FROM users WHERE id=?";
        String SELECT_USER_ROLE = "SELECT role FROM user_roles WHERE user_id=?";
        String SELECT_ALL_USERS = "SELECT * FROM users";
        String DELETE_USER_BY_ID = "DELETE FROM users WHERE id=?";
        String SELECT_USERS_BY_ROLE = "SELECT * FROM users u INNER JOIN user_roles ur " +
                "ON u.id=ur.user_id AND ur.role=?";
        String INSERT_USER = "INSERT INTO users (name, surname, email, number, birthday, password) " +
                "VALUES (?,?,?,?,?,?)";
        String UPDATE_USER = "UPDATE users SET name=?, surname=?, email=?, " +
                "number=?, birthday=?, password=? WHERE id=?";
        String SELECT_FILTERED_1 = "SELECT * FROM users u INNER JOIN user_level ul ON u.id=ul.id WHERE ul.active=? " +
                "ORDER BY name LIMIT 5 OFFSET 0";
        String SELECT_FILTERED_2 = "SELECT u.id, name, surname, email, number, birthday, password, AVG(coalesce(mark,0)) " +
                "as average FROM users u INNER JOIN user_level ul ON u.id = ul.id " +
                "LEFT JOIN feedbacks m ON u.id=(SELECT master_id FROM appointments a WHERE m.appointment_id=a.id) " +
                "WHERE active=? GROUP BY u.id  ORDER BY average LIMIT 5 OFFSET 0";
        String SELECT_FILTERED_3 = "SELECT * FROM users u INNER JOIN user_level ul ON u.id=ul.id  AND ul.level IN(?) " +
                "INNER JOIN (SELECT master_id from master_services ms WHERE  ms.base_service_id IN(?,?,?) " +
                "AND  ms.base_service_id IN(SELECT id FROM base_services WHERE category_id IN(?,?,?)) " +
                "GROUP BY ms.master_id) AS q ON q.master_id = u.id WHERE name ILIKE ? OR surname ILIKE ? AND active=? " +
                "ORDER BY name DESC LIMIT 5 OFFSET 0";
        String SELECT_FILTERED_4 = "SELECT u.id, name, surname, email, number, birthday, password, AVG(coalesce(mark,0)) as average " +
                "FROM users u INNER JOIN user_level ul ON u.id = ul.id " +
                "LEFT JOIN feedbacks m ON u.id=(SELECT master_id FROM appointments a WHERE m.appointment_id=a.id) " +
                "INNER JOIN (SELECT master_id from master_services ms WHERE  ms.base_service_id IN(?,?,?) " +
                "AND  ms.base_service_id IN(SELECT id FROM base_services WHERE category_id IN(?,?,?)) " +
                "GROUP BY ms.master_id) AS q ON q.master_id = u.id WHERE  ul.level IN(?) AND active=? GROUP BY u.id  " +
                "HAVING name ILIKE ? OR surname ILIKE ? ORDER BY average DESC LIMIT 5 OFFSET 0";
        String SELECT_FILTERED_5 = "SELECT * FROM users u INNER JOIN user_level ul ON u.id=ul.id  " +
                "INNER JOIN (SELECT master_id from master_services ms WHERE  ms.base_service_id IN(?,?,?) " +
                "GROUP BY ms.master_id) AS q ON q.master_id = u.id WHERE name ILIKE ? OR surname ILIKE ? AND active=? " +
                "ORDER BY level DESC LIMIT 5 OFFSET 0";
        String SELECT_FILTERED_6 = "SELECT * FROM users u INNER JOIN user_level ul ON u.id=ul.id  AND ul.level IN(?) " +
                "INNER JOIN (SELECT master_id from master_services ms WHERE  ms.base_service_id " +
                "IN(SELECT id FROM base_services WHERE category_id IN(?,?,?)) GROUP BY ms.master_id) " +
                "AS q ON q.master_id = u.id WHERE active=? ORDER BY level LIMIT 5 OFFSET 0";
        String SELECT_COUNT_1 = "SELECT COUNT(1) FROM users u INNER JOIN user_level ul ON u.id = ul.id WHERE active=?";
        String SELECT_COUNT_2 = "SELECT COUNT(1) FROM users u INNER JOIN user_level ul ON u.id = ul.id AND ul.level IN(?) " +
                "INNER JOIN (SELECT master_id from master_services ms WHERE  ms.base_service_id IN(?,?,?) " +
                "AND  ms.base_service_id IN(SELECT id FROM base_services WHERE category_id IN(?,?,?)) " +
                "GROUP BY ms.master_id) AS q ON q.master_id = u.id WHERE name ILIKE ? OR surname ILIKE ? AND active=?";
        String SELECT_COUNT_3 = "SELECT COUNT(1) FROM users u INNER JOIN user_level ul ON u.id = ul.id " +
                "INNER JOIN (SELECT master_id from master_services ms WHERE  ms.base_service_id IN(?,?,?) " +
                "GROUP BY ms.master_id) AS q ON q.master_id = u.id WHERE name ILIKE ? OR surname ILIKE ? AND active=?";
        String SELECT_USER_BY_SEARCH = "SELECT * FROM users WHERE number ILIKE ? " +
                "OR email ILIKE ?";
        String INSERT_USER_ROLE = "INSERT INTO user_roles (user_id, role) VALUES (?,?)";
    }

    interface Appointments {
        String GET_APPOINTMENTS_ALL = "SELECT * FROM appointments WHERE master_id=? ORDER BY date DESC LIMIT 5 OFFSET 0";
        String GET_APPOINTMENTS_FILTERED_1 = "SELECT * FROM appointments WHERE master_id=? AND status=? " +
                "AND payment_status=? AND DATE(date) >= ? AND DATE(date) <= ? " +
                "ORDER BY date DESC LIMIT 5 OFFSET 0";
        String GET_APPOINTMENTS_FILTERED_2 = "SELECT * FROM appointments WHERE master_id=? AND payment_status=? " +
                "AND DATE(date) <= ? ORDER BY date DESC LIMIT 5 OFFSET 0";
        String GET_APPOINTMENTS_FILTERED_3 = "SELECT * FROM appointments WHERE master_id=? AND status=?DATE(date) >= ? " +
                "ORDER BY date DESC LIMIT 5 OFFSET 0";
        String SELECT_COUNT_1 = "SELECT COUNT(1) FROM appointments WHERE master_id=?";
        String SELECT_COUNT_2 = "SELECT COUNT(1) FROM appointments WHERE master_id=? AND status=? AND payment_status=? " +
                "AND DATE(date) >= ? AND DATE(date) <= ?";
        String SELECT_COUNT_3 = "SELECT COUNT(1) FROM appointments WHERE master_id=? AND payment_status=? AND DATE(date) <= ?";
        String SELECT_COUNT_4 = "SELECT COUNT(1) FROM appointments WHERE master_id=? AND status=?DATE(date) >= ?";
        String SELECT_BY_DATE_AND_MASTER_ID = "SELECT * FROM appointments WHERE DATE(date)=? AND master_id=? " +
                "AND status!=? ORDER BY date";
        String INSERT_APPOINTMENT = "INSERT INTO appointments (master_id, client_id, continuance, date, price, discount, status, payment_status) VALUES (?,?,?,?,?,?,?,?)";
        String UPDATE_APPOINTMENT = "UPDATE appointments SET master_id=?, client_id=?, continuance=?, date=?, price=?, discount=?, status=?, payment_status=? WHERE id=?";
    }

    interface UserLevels {
        String INSERT_USER_LEVEL = "INSERT INTO user_level (id, level, active, about, about_ua) VALUES (?,?,?,?,?)";
        String UPDATE_USER_LEVEL = "UPDATE user_level SET level=?, about=?, about_ua=?, active=? WHERE id=?";
        String SELECT_USER_LEVEL_BY_ID = "SELECT * FROM user_level WHERE id=?";
        String SELECT_USER_LEVEL_EXISTS = "SELECT EXISTS (SELECT id FROM user_level WHERE id=?)";
    }

    interface AppointmentItems {
        String INSERT_APPOINTMENT_ITEM = "INSERT INTO appointment_items (appointment_id, service_id) VALUES (?,?)";
    }

    interface BasService {
        String INSERT_BASE_SERVICE = "INSERT INTO base_services (category_id, service, service_ua, price) VALUES (?,?,?,?)";
        String UPDATE_BASE_SERVICE = "UPDATE base_services SET category_id=?, service=?, service_ua=?, price=? WHERE id=?";
        String SELECT_BASE_SERVICES_1 = "SELECT * FROM base_services WHERE category_id IN(?) LIMIT 5 OFFSET 0";
        String SELECT_BASE_SERVICES_2 = "SELECT * FROM base_services LIMIT 5 OFFSET 0";
        String SELECT_COUNT_1 = "SELECT COUNT(1) FROM base_services WHERE category_id IN(?)";
        String SELECT_COUNT_2 = "SELECT COUNT(1) FROM base_services";
    }

    interface Feedbacks {
        String INSERT_FEEDBACK = "INSERT INTO feedbacks (appointment_id, mark, comment, date) VALUES (?,?,?,?)";
        String SELECT_FEEDBACKS_FOR_RATING = "SELECT * FROM feedbacks WHERE appointment_id " +
                "IN(SELECT id FROM appointments WHERE master_id=?) ORDER BY date DESC";
        String SELECT_FEEDBACKS_MY_MASTER_ID = "SELECT * FROM feedbacks WHERE appointment_id " +
                "IN(SELECT id FROM appointments WHERE master_id=?) " + "ORDER BY date DESC LIMIT 5 OFFSET 0";
        String COUNT_FEEDBACKS = "SELECT COUNT(1) FROM feedbacks WHERE appointment_id IN(SELECT id FROM appointments WHERE master_id=?)";
    }

    interface MasterServices {
        String INSERT_MASTER_SERVICE = "INSERT INTO master_services (master_id, base_service_id, continuance) VALUES (?,?,?)";
        String UPDATE_MASTER_SERVICE = "UPDATE master_services SET master_id=?, base_service_id=?, continuance=? WHERE id=?";
        String SELECT_SERVICES_BY_MASTER_ID = "SELECT * FROM master_services WHERE master_id=?";
    }

    interface WorkingDays {
        String INSERT_WORKING_DAY = "INSERT INTO working_days (user_id, date, time_start, time_end) " +
                "VALUES (?,?,?,?)";
        String SAVE_WORKING_DAYS = "INSERT INTO working_days (user_id, date, time_start, time_end) " +
                "VALUES (?,?,?,?), (?,?,?,?)  ON CONFLICT (user_id, date)  DO UPDATE SET time_start=?, time_end=?";
        String SELECT_WORKING_DAYS_BY_ID = "SELECT * FROM working_days WHERE user_id=?";
        String DELETE_WORKING_DAYS = "DELETE FROM working_days WHERE date IN(?,?) AND user_id=?";
        String SELECT_WORKING_DAY = "SELECT * FROM working_days WHERE user_id=? AND date=?";
    }
}
