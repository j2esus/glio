package com.jeegox.glio.dao.aim;

import com.jeegox.glio.dao.hibernate.GenericDAO;
import com.jeegox.glio.dto.TaskDTO;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.admin.User;
import com.jeegox.glio.entities.aim.Aim;
import com.jeegox.glio.entities.aim.Task;
import com.jeegox.glio.enumerators.Priority;
import com.jeegox.glio.enumerators.Status;
import java.util.Date;
import java.util.List;

/**
 *
 * @author j2esus
 */
public interface TaskDAO extends GenericDAO<Task,Integer>{
    
    List<Task> findByCompany(Company company);
    
    List<Task> findBy(Aim aim);
    
    List<Task> findBy(User user, Status[] status, String query, Priority[] priorities);
    
    List<Task> findBy(Aim aim, Status[] status);
    
    Long count(User user, Status[] status);
    
    Long count(User user, Status[] status, String query, Priority[] priorities);
    
    Long count(Company company, Status[] status, String query, Priority[] priorities, Integer idProject);
    
    List<Task> findBy(Company company, Status[] status, String query, Priority[] priorities, Integer idProject);
    
    List<TaskDTO> findBy(User userOwner, Status[] status, Date initDate, Date endDate, Integer idProject);
}
