package com.jeegox.glio.services.aim.impl;

import com.jeegox.glio.dao.aim.TaskDAO;
import com.jeegox.glio.dto.TaskDTO;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.admin.User;
import com.jeegox.glio.entities.aim.Aim;
import com.jeegox.glio.entities.aim.Task;
import com.jeegox.glio.entities.aim.Time;
import com.jeegox.glio.enumerators.Priority;
import com.jeegox.glio.enumerators.Status;
import com.jeegox.glio.services.aim.TaskService;
import com.jeegox.glio.services.aim.TimeService;
import com.jeegox.glio.util.Util;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author j2esus
 */
@Service
public class TaskServiceImpl implements TaskService{
    @Autowired
    private TaskDAO taskDAO;
    @Autowired
    private TimeService timeService;

    @Transactional
    @Override
    public void saveOrUpdate(Task task) {
        taskDAO.save(task);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Task> findByCompany(Company company) {
        return taskDAO.findByCompany(company);
    }

    @Transactional(readOnly = true)
    @Override
    public Task findBydId(Integer id) {
        return taskDAO.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Task> findBy(Aim aim) {
        return taskDAO.findBy(aim);
    }

    @Transactional
    @Override
    public void changeStatus(Task task, Status status) throws Exception {
        task.setStatus(status);
        this.saveOrUpdate(task);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Task> findBy(User user, Status[] status, String query, Priority[] priorities) {
        return taskDAO.findBy(user, status, query, priorities);
    }
    
    @Transactional
    @Override
    public void work(User user, Integer idTask, Status status) throws Exception {
        try{
            Task task = findBydId(idTask);
            User userOwner = task.getUserOwner();
            if(!userOwner.getId().equals(user.getId())){
                throw new RuntimeException("No puedes trabajar con esta actividad, no está asociada a ti.");
            }
            task.setStatus(status);
            Time time = timeService.findCurrentTime(task);
            if(time == null){
                time = new Time();
                time.setFather(task);
                time.setInitDate(Util.getCurrentDate());
            }else{
                time.setEndDate(Util.getCurrentDate());
            }
            saveOrUpdate(task);
            timeService.saveOrUpdate(time);
        }catch(Exception e){
            throw e;        
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<Task> findBy(Aim aim, Status[] status) {
        return taskDAO.findBy(aim, status);
    }

    @Transactional(readOnly = true)
    @Override
    public Long count(User user, Status[] status) {
        return taskDAO.count(user, status);
    }

    @Transactional(readOnly = true)
    @Override
    public Long count(User user, Status[] status, String query, Priority[] priorities) {
        return taskDAO.count(user, status, query, priorities);
    }

    @Transactional(readOnly = true)
    @Override
    public Long count(Company company, Status[] status, String query, Priority[] priorities, Integer idProject) {
        return taskDAO.count(company, status, query, priorities, idProject);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Task> findBy(Company company, Status[] status, String query, Priority[] priorities, Integer idProject) {
        return taskDAO.findBy(company, status, query, priorities, idProject);
    }

    @Transactional(readOnly = true)
    @Override
    public List<TaskDTO> findBy(User userOwner, Status[] status, Date initDate, Date endDate, Integer idProject ) {
        return taskDAO.findBy(userOwner, status, initDate, endDate, idProject);
    }
}
