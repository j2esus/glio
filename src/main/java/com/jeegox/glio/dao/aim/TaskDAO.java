package com.jeegox.glio.dao.aim;

import com.jeegox.glio.dao.hibernate.GenericDAO;
import com.jeegox.glio.dto.TaskDTO;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.admin.User;
import com.jeegox.glio.entities.aim.Aim;
import com.jeegox.glio.entities.aim.Project;
import com.jeegox.glio.entities.aim.Task;
import com.jeegox.glio.enumerators.Priority;
import com.jeegox.glio.enumerators.Status;
import java.util.Date;
import java.util.List;

public interface TaskDAO extends GenericDAO<Task,Integer>{
    
    List<Task> findByAim(Aim aim);

    List<Task> findByProject(Project project);
    
    Long count(User user, Status[] status);

    List<Task> findBy(Company company, Status[] status, String value, Priority[] priorities, Project project);

    List<Task> findBy(Company company, Status[] status, String value, Priority[] priorities);
    
    List<TaskDTO> findBy(User userOwner, Status[] status, Date initDate, Date endDate, Integer idProject, Integer idAim);
}
