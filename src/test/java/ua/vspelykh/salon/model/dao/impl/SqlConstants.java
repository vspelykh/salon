package ua.vspelykh.salon.model.dao.impl;

public interface SqlConstants {

    interface User {
        String SELECT_USER_BY_ID = "SELECT * FROM users WHERE id=?";
        String SELECT_USER_ROLE = "SELECT role FROM user_roles WHERE user_id=?";
        String SELECT_ALL_USERS = "SELECT * FROM users";
        String DELETE_USER_BY_ID = "DELETE FROM users WHERE id=?";
        String SELECT_USERS_BY_ROLE = "SELECT * FROM users u INNER JOIN user_roles ur " +
                "ON u.id = ur.user_id AND ur.role=?";
        String INSERT_USER = "INSERT INTO users (name, surname, email, number, password) " +
                "VALUES (?,?,?,?,?)";
        String UPDATE_USER = "UPDATE users SET name = ?, surname = ?, email = ?, " +
                "number = ?, password = ? WHERE id = ?";
        String SELECT_FILTERED_1 = "SELECT * FROM users u INNER JOIN user_level ul ON u.id = ul.id WHERE ul.active='true' " +
                "ORDER BY name asc LIMIT 5 OFFSET 0";

        String SELECT_FILTERED_2 = "SELECT u.id, name, surname, email, number, password, AVG(coalesce(mark,0)) " +
                "as average FROM users u INNER JOIN user_level ul ON u.id = ul.id " +
                "LEFT JOIN feedbacks m ON u.id=(SELECT master_id FROM appointments a WHERE m.appointment_id=a.id) " +
                "WHERE ul.active='true' GROUP BY u.id ORDER BY average LIMIT 5 OFFSET 0";
        String SELECT_FILTERED_3 = "SELECT * FROM users u INNER JOIN user_level ul ON u.id=ul.id and ul.level IN(?) " +
                "INNER JOIN (SELECT master_id from master_services s WHERE s.base_service_id IN(?,?,?) " +
                "AND  s.base_service_id IN(SELECT id from base_services WHERE category_id IN(?,?,?)) GROUP BY s.master_id) " +
                "AS q ON q.master_id = u.id WHERE name ILIKE '%Anna%' OR surname ILIKE '%Anna%' AND ul.active='true' " +
                "ORDER BY name desc LIMIT 5 OFFSET 0";
        String SELECT_FILTERED_4 = "SELECT u.id, name, surname, email, number, password, AVG(coalesce(mark,0)) as average " +
                "FROM users u INNER JOIN user_level ul ON u.id = ul.id LEFT JOIN feedbacks m " +
                "ON u.id=(SELECT master_id FROM appointments a WHERE m.appointment_id=a.id) " +
                "INNER JOIN (SELECT master_id from master_services s WHERE s.base_service_id IN(?,?,?) " +
                "AND  s.base_service_id IN(SELECT id from base_services WHERE category_id IN(?,?,?)) GROUP BY s.master_id) " +
                "AS q ON q.master_id = u.id WHERE ul.level IN(?) AND ul.active='true' " +
                "GROUP BY u.id  HAVING name ILIKE '%Anna%' OR surname ILIKE '%Anna%'ORDER BY average desc LIMIT 5 OFFSET 0";
        String SELECT_FILTERED_5 = "SELECT * FROM users u INNER JOIN user_level ul ON u.id=ul.id  " +
                "INNER JOIN (SELECT master_id from master_services s WHERE s.base_service_id IN(?,?,?) GROUP BY s.master_id) " +
                "AS q ON q.master_id = u.id WHERE name ILIKE '%Anna%' OR surname ILIKE '%Anna%' AND ul.active='true'" +
                " ORDER BY level desc LIMIT 5 OFFSET 0";
        String SELECT_FILTERED_6 = "SELECT * FROM users u INNER JOIN user_level ul ON u.id=ul.id and ul.level IN(?) " +
                "INNER JOIN (SELECT master_id from master_services s " +
                "WHERE  s.base_service_id IN(SELECT id from base_services WHERE category_id IN(?,?,?)) " +
                "GROUP BY s.master_id) AS q ON q.master_id = u.id WHERE ul.active='true' " +
                "ORDER BY level asc LIMIT 5 OFFSET 0";
        String SELECT_COUNT_1 = "SELECT COUNT(1) FROM users u INNER JOIN user_level ul ON u.id = ul.id  WHERE ul.active='true'";
        String SELECT_COUNT_2 = "SELECT COUNT(1) FROM users u INNER JOIN user_level ul ON u.id = ul.id and ul.level IN(?) " +
                "INNER JOIN (SELECT master_id from master_services s WHERE s.base_service_id IN(?,?,?) " +
                "AND  s.base_service_id IN(SELECT id from base_services WHERE category_id IN(?,?,?)) " +
                "GROUP BY s.master_id) AS q ON q.master_id = u.id " +
                "WHERE name ILIKE '%Anna%' OR surname ILIKE '%Anna%' AND ul.active='true'";
        String SELECT_COUNT_3 = "SELECT COUNT(1) FROM users u INNER JOIN user_level ul ON u.id = ul.id  " +
                "INNER JOIN (SELECT master_id from master_services s WHERE s.base_service_id IN(?,?,?) " +
                "GROUP BY s.master_id) AS q ON q.master_id = u.id WHERE name ILIKE '%Anna%' OR surname ILIKE '%Anna%' " +
                "AND ul.active='true'";
        String SELECT_USER_BY_SEARCH = "SELECT * FROM users WHERE number ILIKE '%papahet@gmail.com%' " +
                "OR email ILIKE '%papahet@gmail.com%'";
        String INSERT_USER_ROLE = "INSERT INTO user_roles VALUES (?,?)";
    }

    interface Appointment {
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
        String UPDATE_APPOINTMENT = "UPDATE appointments SET master_id = ?, client_id = ?, continuance = ?, date = ?, price = ?, discount = ?, status = ?, payment_status = ? WHERE id = ?";
        String SELECT_BY_DATE_AND_ID = "SELECT * FROM appointments WHERE date = ? AND master_id = ?";
    }

    interface UserLevel {
        String INSERT_USER_LEVEL = "INSERT INTO user_level (id, level, active, about, about_ua) VALUES (?,?,?,?,?)";
        String UPDATE_USER_LEVEL = "UPDATE user_level SET level=?, about=?, about_ua=?, active=? WHERE id = ?";
        String SELECT_USER_LEVEL_BY_ID = "SELECT * FROM user_level WHERE id=?";
        String SELECT_USER_LEVEL_EXISTS = "SELECT EXISTS (SELECT id FROM user_level WHERE id=?)";
    }

    interface AppointmentItem {
        String INSERT_APPOINTMENT_ITEM = "INSERT INTO appointment_items (appointment_id, service_id) VALUES (?,?)";
    }
}
