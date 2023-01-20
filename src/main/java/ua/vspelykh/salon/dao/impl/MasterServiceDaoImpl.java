package ua.vspelykh.salon.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.dao.*;
import ua.vspelykh.salon.dao.connection.DBCPDataSource;
import ua.vspelykh.salon.dao.mapper.Column;
import ua.vspelykh.salon.dao.mapper.RowMapperFactory;
import ua.vspelykh.salon.dto.MasterServiceDto;
import ua.vspelykh.salon.model.BaseService;
import ua.vspelykh.salon.model.Service;
import ua.vspelykh.salon.util.exception.DaoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MasterServiceDaoImpl extends AbstractDao<Service> implements MasterServiceDao {

    private BaseServiceDao baseServiceDao = DaoFactory.getBaseServiceDao();

    private static final Logger LOG = LogManager.getLogger(MasterServiceDaoImpl.class);

    public MasterServiceDaoImpl() {
        super(RowMapperFactory.getMasterServiceRowMapper(), Table.SERVICE);
    }

    @Override
    public int create(Service entity) throws DaoException {
        String query = INSERT + tableName + " (master_id, base_service_id, continuance)" + VALUES + "(?,?,?)";
        try (PreparedStatement statement = DBCPDataSource.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            setServiceStatement(entity, statement);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                throw new DaoException(NO_ID + tableName);
            }
        } catch (SQLException e) {
            LOG.error(FAIL_CREATE + tableName, e);
            throw new DaoException(FAIL_CREATE + tableName, e);
        }
    }

    private void setServiceStatement(Service entity, PreparedStatement statement) throws SQLException {
        int k = 0;
        statement.setInt(++k, entity.getId());
        statement.setInt(++k, entity.getMasterId());
        statement.setInt(++k, entity.getBaseServiceId());
        statement.setInt(++k, entity.getContinuance());
    }

    @Override
    public void update(Service entity) throws DaoException {
        String query = "UPDATE services SET master_id = ?, base_service_id = ?, continuance = ? WHERE id = ?";
        try (PreparedStatement statement = DBCPDataSource.getConnection().prepareStatement(query)) {
            statement.setInt(4, entity.getId());
            int key = statement.executeUpdate();
            if (key != 1) {
                throw new DaoException(FAIL_UPDATE + tableName + ", id=" + entity.getId());
            }
        } catch (SQLException e) {
            LOG.error(FAIL_UPDATE, e);
            throw new DaoException(FAIL_UPDATE + tableName, e);
        }
    }

    @Override
    public List<Service> getAllByUserId(Integer userId) throws DaoException {
        return findAllByParam(userId, Column.MASTER_ID);
    }

    @Override
    public List<Service> getAllByBaseServiceId(Integer baseServiceId) throws DaoException {
        return findAllByParam(baseServiceId, Column.BASE_SERVICE_ID);
    }

    @Override
    public List<Service> findByFilter(List<Integer> userIds, List<Integer> serviceIds,
                                      Integer continuanceFrom, Integer continuanceTo) throws DaoException {
        MasterServiceFilteredQueryBuilder queryBuilder =
                new MasterServiceFilteredQueryBuilder(userIds, serviceIds, continuanceFrom, continuanceTo);
        String query = queryBuilder.buildQuery();
        try (Connection connection = DBCPDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            queryBuilder.setParams(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Service> services = new ArrayList<>();
            while (resultSet.next()) {
                Service entity = rowMapper.map(resultSet);
                services.add(entity);
            }
            return services;
        } catch (SQLException e) {
            LOG.error(e);
            throw new DaoException(e);
        }
    }

    public List<MasterServiceDto> getDTOsByMasterId(int masterId) throws DaoException{
        List<Service> services = getAllByUserId(masterId);
        List<MasterServiceDto> dtos = new ArrayList<>();
        for (Service service : services){
            BaseService baseService = baseServiceDao.findById(service.getBaseServiceId());
            MasterServiceDto dto = new MasterServiceDto(service.getId(), service.getMasterId(), baseService, service.getContinuance());
            dtos.add(dto);
        }
        return dtos;
    }

    private class MasterServiceFilteredQueryBuilder {

        private final List<Integer> userIds;
        private final List<Integer> serviceIds;
        private final Integer continuanceFrom;
        private final Integer continuanceTo;

        private int count;

        private MasterServiceFilteredQueryBuilder(List<Integer> userIds, List<Integer> serviceIds,
                                                  Integer continuanceFrom, Integer continuanceTo) {
            this.userIds = userIds;
            this.serviceIds = serviceIds;
            this.continuanceFrom = continuanceFrom;
            this.continuanceTo = continuanceTo;
        }

        private String buildQuery() {
            StringBuilder query = new StringBuilder(SELECT + tableName);
            if (isAllParamsAreNull()) {
                return query.toString();
            } else {
                query.append(WHERE);
                if (userIds != null) {
                    query.append(" master_id IN (");
                    appendQuestionMarks(query, userIds);
                    count++;
                }
                if (serviceIds != null) {
                    appendAnd(query);
                    query.append(" base_service_id IN (");
                    appendQuestionMarks(query, serviceIds);
                    count++;
                }
                if (continuanceFrom != null) {
                    appendAnd(query);
                    query.append(Column.CONTINUANCE).append(">=").append("?");
                    count++;
                }
                if (continuanceTo != null) {
                    appendAnd(query);
                    query.append(Column.CONTINUANCE).append("<=").append("?");
                }
            }
            return query.toString();
        }

        private void setParams(PreparedStatement preparedStatement) {
            try {
                int paramNum = 1;
                if (userIds != null) {
                    for (Integer userId : userIds) {
                        preparedStatement.setInt(paramNum++, userId);
                    }
                }
                if (serviceIds != null) {
                    for (Integer serviceId : serviceIds) {
                        preparedStatement.setInt(paramNum++, serviceId);
                    }
                }
                if (continuanceFrom != null) {
                    preparedStatement.setInt(paramNum++, continuanceFrom);
                }
                if (continuanceTo != null) {
                    preparedStatement.setInt(paramNum, continuanceTo);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        private void appendQuestionMarks(StringBuilder query, List<Integer> userIds) {
            for (int i = 0; i < userIds.size(); i++) {
                query.append("?");
                if (i != userIds.size() - 1)
                    query.append(",");
            }
            query.append(")");
        }

        private void appendAnd(StringBuilder query) {
            if (count > 0) {
                query.append(" AND ");
            }
        }

        private boolean isAllParamsAreNull() {
            return userIds == null && serviceIds == null && continuanceFrom == null && continuanceTo == null;
        }
    }
}
