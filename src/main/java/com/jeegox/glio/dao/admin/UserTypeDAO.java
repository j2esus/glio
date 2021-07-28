package com.jeegox.glio.dao.admin;

import com.jeegox.glio.dao.hibernate.GenericDAO;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.admin.UserType;
import java.util.List;

public interface UserTypeDAO extends GenericDAO<UserType, Integer>{
    
    List<UserType> findByCompany(Company company);
}
