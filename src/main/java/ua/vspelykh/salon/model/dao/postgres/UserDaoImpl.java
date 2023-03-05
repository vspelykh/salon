package ua.vspelykh.salon.model.dao.postgres;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jasypt.util.password.BasicPasswordEncryptor;
import ua.vspelykh.salon.model.dao.AbstractDao;
import ua.vspelykh.salon.model.dao.QueryBuilder;
import ua.vspelykh.salon.model.dao.Table;
import ua.vspelykh.salon.model.dao.UserDao;
import ua.vspelykh.salon.model.dao.mapper.Column;
import ua.vspelykh.salon.model.dao.mapper.RowMapperFactory;
import ua.vspelykh.salon.model.entity.MastersLevel;
import ua.vspelykh.salon.model.entity.Role;
import ua.vspelykh.salon.model.entity.User;
import ua.vspelykh.salon.util.MasterFilter;
import ua.vspelykh.salon.util.MasterSort;
import ua.vspelykh.salon.util.exception.DaoException;
import ua.vspelykh.salon.util.exception.ValidationException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import static ua.vspelykh.salon.controller.ControllerConstants.ADD;
import static ua.vspelykh.salon.controller.ControllerConstants.REMOVE;
import static ua.vspelykh.salon.model.dao.Table.*;
import static ua.vspelykh.salon.model.dao.mapper.Column.*;
import static ua.vspelykh.salon.util.validation.Validation.checkPassword;

/**
 * UserDaoImpl class implements UserDao interface and extends AbstractDao class.
 * It provides CRUD operations for User entity and encrypts user password before storing in the database.
 *
 * @version 1.0
 */
public class UserDaoImpl extends AbstractDao<User> implements UserDao {

    private static final Logger LOG = LogManager.getLogger(UserDaoImpl.class);

    /**
     * Constructor which creates an instance of UserDaoImpl class using RowMapperFactory and Table constants.
     */
    public UserDaoImpl() {
        super(RowMapperFactory.getUserRowMapper(), Table.USER);
    }

    /**
     * Finds a user by its id and returns it with its roles set.
     *
     * @param id the id of the user to find
     * @return the user with the given id
     * @throws DaoException if an error occurs while finding the user
     */
    @Override
    public User findById(int id) throws DaoException {
        return setUserRoles(super.findById(id));
    }

    /**
     * Finds all users and returns them with their roles set.
     *
     * @return a list of all users
     * @throws DaoException if an error occurs while finding the users
     */
    @Override
    public List<User> findAll() throws DaoException {
        return setUserRoles(super.findAll());
    }

    /**
     * Creates a new user and returns its id.
     * The password of the user is encrypted before storing it in the database.
     *
     * @param entity the user to create
     * @return the id of the created user
     * @throws DaoException if an error occurs while creating the user
     */
    @Override
    public int create(User entity) throws DaoException {
        String query = new QueryBuilder().insert(tableName, NAME, SURNAME, EMAIL, NUMBER, PASSWORD).build();
        try (PreparedStatement statement = getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            encryptPassword(entity);
            setUserStatement(entity, statement);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                throw new DaoException(NO_ID + tableName);
            }
        } catch (SQLException e) {
            LOG.error(String.format(LOG_PATTERN, FAIL_CREATE, tableName, e.getMessage()));
            throw new DaoException(e);
        }
    }

    /**
     * Updates an existing user.
     * The password of the user is encrypted before storing it in the database.
     *
     * @param entity the user to update
     * @throws DaoException if an error occurs while updating the user
     */
    @Override
    public void update(User entity) throws DaoException {
        String query = new QueryBuilder().update(tableName).set(NAME, SURNAME, EMAIL, NUMBER, PASSWORD).where(ID).build();
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            encryptPassword(entity);
            setUserStatement(entity, statement);
            statement.setInt(6, entity.getId());
            int key = statement.executeUpdate();
            if (key != 1) {
                throw new DaoException(FAIL_UPDATE + tableName + ", id=" + entity.getId());
            }
        } catch (SQLException e) {
            LOG.error(String.format(LOG_PATTERN, FAIL_UPDATE, tableName, e.getMessage()));
            throw new DaoException(e);
        }
    }

    /**
     * This method encrypts the password of the given User object using the BasicPasswordEncryptor from the jasypt library.
     * The method checks if the user is new or if the user's password has been changed before updating it.
     * If the password does not meet the password requirements, a DaoException is thrown with the message "Password is invalid".
     *
     * @param user the User object whose password needs to be encrypted
     * @throws DaoException if there is an error while encrypting the password or if the password does not meet the requirements
     */
    private void encryptPassword(User user) throws DaoException {
        if (user.isNew() || !findById(user.getId()).getPassword().equals(user.getPassword())) {
            BasicPasswordEncryptor encryptor = new BasicPasswordEncryptor();
            try {
                checkPassword(user.getPassword());
            } catch (ValidationException e) {
                throw new DaoException(e.getMessage());
            }
            user.setPassword(encryptor.encryptPassword(user.getPassword()));
        }
    }

    @Override
    public User findByEmail(String email) throws DaoException {
        return setUserRoles(findByParam(email, Column.EMAIL));
    }

    @Override
    public List<User> findClients() throws DaoException {
        return findByRole(Role.CLIENT);
    }

    @Override
    public List<User> findMasters() throws DaoException {
        return findByRole(Role.HAIRDRESSER);
    }

    /**
     * This method finds a list of User objects from the database based on the provided filter, page, size, and
     * sorting parameters.
     * The method uses the MasterFilteredQueryBuilder to build the SQL query.
     * <p>
     * If the sorting is based on the user's rating, the method calls buildQueryWithRatingSorting()
     * on the queryBuilder to build the SQL query with the rating sorting.
     * Otherwise, the method calls buildQuery() on the queryBuilder to build the SQL query without the rating sorting.
     * The method then calls getUsersFromDB() with the built query to retrieve the User objects from the database.
     *
     * @param filter the MasterFilter object containing the filter parameters
     * @param page   the page number of the results to retrieve
     * @param size   the number of results to retrieve per page
     * @param sort   the MasterSort object containing the sorting parameters
     * @return a list of User objects that match the filter and sorting parameters
     * @throws DaoException if there is an error while retrieving the User objects from the database
     */
    @Override
    public List<User> findFiltered(MasterFilter filter, int page, int size, MasterSort sort) throws DaoException {
        MasterFilteredQueryBuilder queryBuilder = new MasterFilteredQueryBuilder(filter, page, size, sort);
        String query;
        if (sort.equals(MasterSort.RATING_ASC) || sort.equals(MasterSort.RATING_DESC)) {
            query = queryBuilder.buildQueryWithRatingSorting();
        } else {
            query = queryBuilder.buildQuery();
        }
        return getUsersFromDB(queryBuilder, query);
    }

    /**
     * This method returns the count of the User objects in the database that match the provided MasterFilter object.
     * The method uses the MasterFilteredQueryBuilder to build the SQL query to retrieve the count of User objects
     * based on the filter parameter.
     * The method then executes the built query and retrieves the count of User objects from the ResultSet.
     *
     * @param filter the MasterFilter object containing the filter parameters
     * @return an integer representing the count of User objects that match the filter parameters
     * @throws DaoException if there is an error while retrieving the count from the database
     */
    @Override
    public int getCountOfMasters(MasterFilter filter) throws DaoException {
        int count;
        MasterFilteredQueryBuilder queryBuilder = new MasterFilteredQueryBuilder(filter);
        String query = queryBuilder.buildCountQuery();
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            queryBuilder.setParams(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            } else {
                if (LOG.isEnabled(Level.ERROR)) {
                    LOG.error(String.format("%s%s", FAIL_COUNT, tableName));
                }
                throw new DaoException(FAIL_COUNT + tableName);
            }
        } catch (SQLException e) {
            LOG.error(String.format(LOG_PATTERN, FAIL_COUNT, tableName, e.getMessage()));
            throw new DaoException(e);
        }
        return count;
    }

    /**
     * This method returns a list of User objects that match the provided search term.
     * The method uses the MasterFilteredQueryBuilder to build the SQL query to retrieve User objects based on
     * the search parameter.The method then executes the built query and retrieves the list of User objects from
     * the ResultSet.
     *
     * @param search the search term to use for finding User objects
     * @return a list of User objects that match the search term
     * @throws DaoException if there is an error while retrieving the User objects from the database
     */
    @Override
    public List<User> findBySearch(String search) throws DaoException {
        MasterFilteredQueryBuilder queryBuilder = new MasterFilteredQueryBuilder(search);
        String query = queryBuilder.buildSearchQuery();
        return setUserRoles(getUsersFromDB(queryBuilder, query));
    }

    /**
     * Updates the role of a given user in the database based on the specified action.
     *
     * @param userId the id of the user whose role is being updated
     * @param action the action to be performed (either "ADD" or "REMOVE")
     * @param role   the role to be added or removed from the user
     * @throws DaoException if there is an error in performing the update operation
     */
    @Override
    public void updateRole(int userId, String action, Role role) throws DaoException {
        String query;
        if (ADD.equals(action)) {
            query = new QueryBuilder().insert(USER_ROLES, USER_ID, ROLE).build();
        } else if (REMOVE.equals(action)) {
            query = new QueryBuilder().delete(USER_ROLES).where(USER_ID).and(ROLE).build();
        } else throw new DaoException();
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            setUserRoleStatement(statement, userId, role);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOG.error(String.format("Fail to update role. Issue: %s", e.getMessage()));
            throw new DaoException(e);
        }
    }

    /**
     * Sets the parameters of a given PreparedStatement for updating a user's role in the database.
     *
     * @param statement the PreparedStatement whose parameters are being set
     * @param userId    the ID of the user whose role is being updated
     * @param role      the Role object representing the new role to be assigned to the user
     * @throws SQLException if there is an error in setting the parameters of the PreparedStatement
     */
    private void setUserRoleStatement(PreparedStatement statement, int userId, Role role) throws SQLException {
        int k = 0;
        statement.setInt(++k, userId);
        statement.setString(++k, String.valueOf(role));
    }

    /**
     * Retrieves a list of User objects from the database based on the specified query.
     *
     * @param queryBuilder the MasterFilteredQueryBuilder object used to build the query and set its parameters
     * @param query        the SQL query string to be executed
     * @return a List of User objects retrieved from the database
     * @throws DaoException if there is an error in executing the SQL query
     */
    private List<User> getUsersFromDB(MasterFilteredQueryBuilder queryBuilder, String query) throws DaoException {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            queryBuilder.setParams(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                User entity = rowMapper.map(resultSet);
                users.add(entity);
            }
            return users;
        } catch (SQLException e) {
            LOG.error(String.format("Fail to get users from DB. Issue: %s", e.getMessage()));
            throw new DaoException(e);
        }
    }

    /**
     * Retrieves a list of User objects from the database that have the specified role.
     *
     * @param role the Role object representing the role to search for
     * @return a List of User objects that have the specified role
     * @throws DaoException if there is an error in executing the SQL query
     */
    private List<User> findByRole(Role role) throws DaoException {
        String query = new QueryBuilder().select(tableName).alias(tableName).innerJoin(USER_ROLES, "u.id=ur.user_id")
                .andWithAlias(USER_ROLES, ROLE).build();
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            preparedStatement.setString(1, role.name());
            ResultSet resultSet = preparedStatement.executeQuery();
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                User user = rowMapper.map(resultSet);
                setUserRoles(user);
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            LOG.error(String.format("%sby role. Issue: %s", FAIL_FIND_LIST, e.getMessage()));
            throw new DaoException(e);
        }
    }

    /**
     * Sets the parameters of a given PreparedStatement for inserting or updating a User object in the database.
     *
     * @param entity    the User object whose data is being set in the PreparedStatement
     * @param statement the PreparedStatement whose parameters are being set
     * @throws SQLException if there is an error in setting the parameters of the PreparedStatement
     */
    private void setUserStatement(User entity, PreparedStatement statement) throws SQLException {
        int k = 0;
        statement.setString(++k, entity.getName());
        statement.setString(++k, entity.getSurname());
        statement.setString(++k, entity.getEmail());
        statement.setString(++k, entity.getNumber());
        statement.setString(++k, entity.getPassword());
    }

    /**
     * Retrieves the roles associated with a User object from the database and adds them to the User object.
     *
     * @param user the User object whose roles are being retrieved from the database and added to the User object
     * @return the User object with the roles added
     * @throws DaoException if there is an error in executing the SQL query
     */
    private User setUserRoles(User user) throws DaoException {
        String query = new QueryBuilder().selectFields(USER_ROLES, ROLE).where(USER_ID).build();
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            statement.setInt(1, user.getId());
            ResultSet resultSet = statement.executeQuery();
            Set<Role> roles = user.getRoles();
            while (resultSet.next()) {
                roles.add(Role.valueOf(resultSet.getString(Column.ROLE)));
            }
            return user;
        } catch (SQLException e) {
            LOG.error(String.format("Error during setting roles for user. Issue: %s", e.getMessage()));
            throw new DaoException(e);
        }
    }

    /**
     * Retrieves the roles associated with each User object in a list from the database and adds them
     * to the respective User objects.
     *
     * @param users the list of User objects whose roles are being retrieved from the database and added
     *              to the respective User objects
     * @return the list of User objects with the roles added
     * @throws DaoException if there is an error in executing the SQL query
     */
    private List<User> setUserRoles(List<User> users) throws DaoException {
        for (User user : users) {
            setUserRoles(user);
        }
        return users;
    }

    /**
     * A helper class that extends the QueryBuilder class to provide methods for setting query parameters based on
     * filter criteria.
     *
     * @version 1.0
     */
    private class MasterFilteredQueryBuilder extends QueryBuilder {

        private static final String COUNT_MASTERS_QUERY = "SELECT COUNT(1) FROM users u INNER JOIN user_level ul " +
                "ON u.id = ul.id";
        private static final String SELECT_USERS = "SELECT u.id, name, surname, email, number, password, AVG(coalesce(mark,0)) " +
                "as average FROM users u INNER JOIN user_level ul ON u.id = ul.id LEFT JOIN feedbacks m ON u.id=" +
                "(SELECT master_id FROM appointments a WHERE m.appointment_id=a.id)";

        private final List<MastersLevel> levels;
        private final List<Integer> serviceIds;
        private List<Integer> categoriesIds;
        private int page;
        private int size;
        private MasterSort sort;
        private final String search;

        private boolean isSearch = false;

        /**
         * Constructs a MasterFilteredQueryBuilder object with the specified filter criteria, page number, page size,
         * and sort criteria.
         * <p>
         * Uses to create filtered query.
         *
         * @param filter the filter criteria for the query
         * @param page   the page number of the query results
         * @param size   the page size of the query results
         * @param sort   the sort criteria for the query results
         */
        public MasterFilteredQueryBuilder(MasterFilter filter, int page, int size, MasterSort sort) {
            this.levels = filter.getLevels();
            this.serviceIds = filter.getServiceIds();
            this.categoriesIds = filter.getCategoriesIds();
            this.page = page;
            this.size = size;
            this.sort = sort;
            this.search = filter.getSearch();
        }

        /**
         * Constructs a MasterFilteredQueryBuilder object with the specified filter criteria for count queries.
         *
         * @param filter the filter criteria for the query
         */
        public MasterFilteredQueryBuilder(MasterFilter filter) {
            this.levels = filter.getLevels();
            this.serviceIds = filter.getServiceIds();
            this.categoriesIds = filter.getCategoriesIds();
            this.search = filter.getSearch();
        }

        /**
         * Constructs a MasterFilteredQueryBuilder object with the specified search criteria.
         *
         * @param search the search criteria for the query
         */
        public MasterFilteredQueryBuilder(String search) {
            this.search = search;
            levels = Collections.emptyList();
            serviceIds = Collections.emptyList();
            isSearch = true;
        }

        /**
         * Builds a SQL query based on the current state of the MasterFilteredQueryBuilder object.
         * If all filter lists and search string are empty, returns a simplified query.
         * Otherwise, builds a query with inner joins and where clauses based on the non-empty filter lists and search string.
         * Includes order by and pagination clauses based on the current sort and pagination values.
         *
         * @return a SQL query as a string
         */
        private String buildQuery() {
            select(tableName).alias(tableName);
            if (isAllListsAndSearchAreEmpty()) {
                return shortQuery();
            } else {
                innerJoin(USER_LEVEL, "u.id=ul.id ");
                appendLevels().appendServices().appendSearch(WHERE);
                if (search != null && !search.isEmpty()) {
                    and(ACTIVE);
                } else {
                    where(ACTIVE);
                }
                return orderBy(sort).pagination(page, size).build();
            }
        }

        /**
         * Builds a query string for retrieving a list of users with average rating and applying sorting by rating
         * in ascending or descending order.
         *
         * @return the constructed SQL query string.
         */
        private String buildQueryWithRatingSorting() {
            append(SELECT_USERS);
            appendServices().appendLevelsForRatingQuery();
            append(" GROUP BY u.id ");
            appendSearch(HAVING);
            append(" ORDER BY average");
            if (sort.equals(MasterSort.RATING_DESC)) {
                desc();
            }
            return pagination(page, size).build();
        }

        /**
         * Builds a SQL query for searching users by phone number or email using the ILIKE operator.
         *
         * @return a string representing the SQL query.
         */
        private String buildSearchQuery() {
            return select(tableName).whereILike(NUMBER)
                    .orILike(EMAIL).build();
        }

        /**
         * Builds a short query used when all filter lists and search string are empty.
         * The query joins the user and user_level tables and selects only active users.
         *
         * @return the built query as a string
         */
        private String shortQuery() {
            innerJoin(USER_LEVEL, "u.id=ul.id").where("ul.active");
            return orderBy(sort).pagination(page, size).build();
        }

        /**
         * Builds a count query for the master list based on the provided filter criteria.
         * If all filter criteria are empty, returns the count of all active masters.
         *
         * @return the count query as a string
         */
        private String buildCountQuery() {
            append(COUNT_MASTERS_QUERY);
            if (isAllListsAndSearchAreEmpty()) {
                return where(ACTIVE).build();
            }
            appendLevels().appendServices().appendSearch(WHERE);
            if (search != null && !search.isEmpty()) {
                and(ACTIVE);
            } else {
                where(ACTIVE);
            }
            return build();
        }

        /**
         * Appends the master services to the query if there are serviceIds specified in the filter.
         * If both serviceIds and categoriesIds are empty, no changes will be made to the query.
         * If only categoriesIds are specified, the query will still join master_services table but
         * will not include serviceIds in the where clause.
         *
         * @return the MasterFilteredQueryBuilder object for chaining
         */
        private MasterFilteredQueryBuilder appendServices() {
            if (!serviceIds.isEmpty()) {
                innerJoinCondition("SELECT master_id from master_services ms");
                query.replace(query.length() - 1, query.length(), "");
                whereAliasIn(MASTER_SERVICES, BASE_SERVICE_ID, serviceIds.size());
                if (!categoriesIds.isEmpty()) {
                    query.append(AND);
                }
                appendCategories();
                query.append(" GROUP BY ms.master_id) AS q ON q.master_id = u.id");
            } else if (!categoriesIds.isEmpty()) {
                innerJoinCondition("SELECT master_id from master_services ms");
                query.replace(query.length() - 1, query.length(), "");
                append(WHERE);
                appendCategories();
                query.append(" GROUP BY ms.master_id) AS q ON q.master_id = u.id");
            }
            return this;
        }

        private MasterFilteredQueryBuilder appendLevels() {
            if (!levels.isEmpty()) {
                andInCondition(USER_LEVEL, LEVEL, levels.size());
            }
            return this;
        }

        private void appendCategories() {
            if (!categoriesIds.isEmpty()) {
                String condition = new QueryBuilder().selectFields(BASE_SERVICE, ID)
                        .whereIn(CATEGORY_ID, categoriesIds.size()).build();
                aliasIn(MASTER_SERVICES, BASE_SERVICE_ID, condition);
            }
        }

        private boolean isAllListsAndSearchAreEmpty() {
            return levels.isEmpty() && serviceIds.isEmpty() && (search == null || search.isEmpty())
                    && (categoriesIds == null || categoriesIds.isEmpty());
        }

        /**
         * Sets the parameters for a prepared statement based on the current state of the query builder.
         *
         * @param preparedStatement the prepared statement to set the parameters for
         * @throws SQLException if an error occurs while setting the parameters
         */
        private void setParams(PreparedStatement preparedStatement) throws SQLException {
            int paramNum = 1;
            if (isSearch) {
                setSearch(preparedStatement, paramNum);
            } else if (MasterSort.RATING_ASC.equals(sort) || MasterSort.RATING_DESC.equals(sort)) {
                paramNum = setServices(preparedStatement, paramNum);
                paramNum = setCategories(preparedStatement, paramNum);
                paramNum = setLevels(preparedStatement, paramNum);
                preparedStatement.setBoolean(paramNum, true);
                paramNum++;
                setSearch(preparedStatement, paramNum);

            } else {
                paramNum = setLevels(preparedStatement, paramNum);
                paramNum = setServices(preparedStatement, paramNum);
                paramNum = setCategories(preparedStatement, paramNum);
                paramNum = setSearch(preparedStatement, paramNum);
                preparedStatement.setBoolean(paramNum, true);
            }
        }

        private int setCategories(PreparedStatement preparedStatement, int paramNum) throws SQLException {
            if (categoriesIds != null && !categoriesIds.isEmpty()) {
                for (Integer id : categoriesIds) {
                    preparedStatement.setInt(paramNum++, id);
                }
            }
            return paramNum;
        }

        private int setSearch(PreparedStatement preparedStatement, int paramNum) throws SQLException {
            if (search != null && !search.isEmpty()) {
                String[] strings = search.split("[ ]+");
                for (String s : strings) {
                    preparedStatement.setString(paramNum++, String.format(SEARCH_PATTERN, s));
                }
                for (String s : strings) {
                    preparedStatement.setString(paramNum++, String.format(SEARCH_PATTERN, s));
                }
            }
            return paramNum;
        }

        private int setLevels(PreparedStatement preparedStatement, int paramNum) throws SQLException {
            if (levels != null && !levels.isEmpty()) {
                for (MastersLevel level : levels) {
                    preparedStatement.setString(paramNum++, level.name());
                }
            }
            return paramNum;
        }

        private int setServices(PreparedStatement preparedStatement, int paramNum) throws SQLException {
            if (serviceIds != null && !serviceIds.isEmpty()) {
                for (Integer serviceId : serviceIds) {
                    preparedStatement.setInt(paramNum++, serviceId);
                }
            }
            return paramNum;
        }

        private void appendLevelsForRatingQuery() {
            if (!levels.isEmpty()) {
                whereAliasIn(USER_LEVEL, LEVEL, levels.size());
                and(ACTIVE);
            } else {
                where(ACTIVE);
            }
        }

        private void appendSearch(String key) {
            if (search != null && !search.isEmpty()) {
                String[] strings = search.split("[ ]+");
                query.append(key);
                Arrays.stream(strings).forEach(s -> iLike(NAME).append(OR));
                Arrays.stream(strings).forEach(s -> iLike(SURNAME).append(OR));
                query.replace(query.length() - 4, query.length(), "");
            }
        }
    }
}
