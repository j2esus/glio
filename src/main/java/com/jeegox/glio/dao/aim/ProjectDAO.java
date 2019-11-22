package com.jeegox.glio.dao.aim;

import com.jeegox.glio.dao.hibernate.GenericDAO;
import com.jeegox.glio.dto.GraphStatusVO;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.admin.User;
import com.jeegox.glio.entities.aim.Project;
import com.jeegox.glio.enumerators.Status;
import java.util.List;

/**
 *
 * @author j2esus
 */
public interface ProjectDAO extends GenericDAO<Project, Integer>{
    
    List<Project> findByCompany(Company company);
    
    List<Project> findBy(User user, String name, Status status, String description);
    
    List<Project> findBy(User user, String query, Status[] status);
    
    List<GraphStatusVO> findDataGraphProject(Integer idProject);
    
    List<Project> findBy(Company company, String query, Status[] status);
    
    List<Project> findBy(Company company, Status[] status);
}
