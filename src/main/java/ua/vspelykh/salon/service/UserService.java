package ua.vspelykh.salon.service;

import ua.vspelykh.salon.model.dto.UserMasterDTO;
import ua.vspelykh.salon.model.entity.MastersLevel;
import ua.vspelykh.salon.model.entity.Role;
import ua.vspelykh.salon.model.entity.User;
import ua.vspelykh.salon.model.entity.UserLevel;
import ua.vspelykh.salon.util.MasterSort;
import ua.vspelykh.salon.util.exception.ServiceException;

import java.util.List;

public interface UserService {

    User findById(Integer id) throws ServiceException;

    User findByEmailAndPassword(String email, String password) throws ServiceException;

    List<User> findAll() throws ServiceException;

    List<User> findMasters(boolean isActive) throws ServiceException;

    void save(User user) throws ServiceException;

    void save(User user, String parameter) throws ServiceException;

    void delete(Integer id) throws ServiceException;

    UserLevel getUserLevelByUserId(Integer userId) throws ServiceException;

    void create(UserLevel userLevel) throws ServiceException;

    void update(UserLevel userLevel) throws ServiceException;

    List<UserMasterDTO> getMastersDto(List<MastersLevel> levels, List<Integer> serviceIds, List<Integer> categoriesIds,
                                      String search, int page, int size, MasterSort sort, String locale) throws ServiceException;

    int getCountOfMasters(List<MastersLevel> levels, List<Integer> serviceIds, List<Integer> categoriesIds, String search) throws ServiceException;

    List<User> findBySearch(String search) throws ServiceException;

    void updateRole(int userId, String action, Role role) throws ServiceException;

}