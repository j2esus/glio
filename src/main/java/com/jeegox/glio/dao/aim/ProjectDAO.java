package com.jeegox.glio.dao.aim;

import com.jeegox.glio.dao.hibernate.GenericDAO;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.admin.User;
import com.jeegox.glio.entities.aim.Project;
import com.jeegox.glio.enumerators.Status;
import java.util.List;

public interface ProjectDAO extends GenericDAO<Project, Integer>{
    
    List<Project> findByCompany(Company company);
    
    List<Project> findByUser(User user, String value);
    
    List<Project> findByUser(User user, String value, Status status);
    
    List<Project> findByCompany(Company company, String value, Status[] status);

    List<Project> findByCompany(Company company, Status[] status);
}
