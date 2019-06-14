package com.jeegox.glio.services.admin;

import com.jeegox.glio.dto.GraphProductivityDTO;
import com.jeegox.glio.dto.admin.UserResponse;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.admin.Session;
import com.jeegox.glio.entities.admin.User;
import com.jeegox.glio.enumerators.Status;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author j2esus
 */
public interface UserService {
    
    List<User> findAll();
    
    Session login(String username, String password, String token) throws Exception;
    
    List<User> findByCompany(Company company);
    
    Long count(Company company);
    
    User changePassword(User user, Integer idUser, String password, String newPassword, String confirmPassword) throws Exception;
    
    User changeUserData(Integer idUser, String username, String name, String email) throws Exception;
    
    User findById(Integer id);
    
    void changeStatus(User user,Status status) throws Exception;
    
    void saveOrUpdate(User user) throws Exception;
    
    UserResponse getToken(String username, String password);
    
    User findById(String username);
    
    void save(User user) throws Exception;
    
    List<GraphProductivityDTO> findDataGraphProductivity(Company company, Date initDate, Date endDate, Integer idProject);
    
    List<Map> findActivityData(Company company, Date initDate, Date endDate, Integer idProject);
}
