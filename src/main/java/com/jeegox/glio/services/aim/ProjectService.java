package com.jeegox.glio.services.aim;

import com.jeegox.glio.dto.GraphStatusVO;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.admin.User;
import com.jeegox.glio.entities.aim.Project;
import com.jeegox.glio.enumerators.Status;
import java.util.List;
import java.util.Map;

/**
 *
 * @author j2esus
 */
public interface ProjectService {
    
    void saveOrUpdate(Project project);
    
    List<Project> findByCompany(Company company);
    
    Project findBydId(Integer id);
    
    void changeStatus(Project project, Status status) throws Exception;
    
    List<Project> findBy(User user, String name, Status status, String description);
    
    List<Project> findBy(User user, String query, Status[] status);
    
    List<GraphStatusVO> findDataGraphProject(Integer idProject);
    
    List<Map> findDataGraphAim(Integer idProject);
    
    List<Project> findBy(Company company, String query, Status[] status);
    
    List<Project> findBy(Company company, Status[] status);
}
