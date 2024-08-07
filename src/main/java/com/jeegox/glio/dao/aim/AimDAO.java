package com.jeegox.glio.dao.aim;

import com.jeegox.glio.dao.hibernate.GenericDAO;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.admin.User;
import com.jeegox.glio.entities.aim.Aim;
import com.jeegox.glio.entities.aim.Project;
import com.jeegox.glio.enumerators.Status;
import java.util.List;

public interface AimDAO extends GenericDAO<Aim,Integer>{
    
    List<Aim> findByCompany(Company company);
    
    List<Aim> findByProject(Project project);
    
    List<Aim> findByProject(Project project, Status[] status);
    
    Long countActiveByUserOwner(User user);
    
}
