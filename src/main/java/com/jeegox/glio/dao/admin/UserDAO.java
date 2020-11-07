package com.jeegox.glio.dao.admin;

import com.jeegox.glio.dao.hibernate.GenericDAO;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.admin.User;
import java.util.List;

public interface UserDAO extends GenericDAO<User, Integer>{
    
    User login(String username,String password);

    User login(String token);
    
    List<User> findByCompany(Company company);
    
    Long count(Company company);
    
    User findByUsername(String username);
    
    List<User> findByCompany(Company company, String nameLike);
}
