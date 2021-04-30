package com.jeegox.glio.services;

import com.jeegox.glio.dao.aim.AimDAO;
import com.jeegox.glio.dao.aim.ProjectDAO;
import com.jeegox.glio.dao.aim.TaskDAO;
import com.jeegox.glio.dao.aim.TimeDAO;
import com.jeegox.glio.dto.TaskDTO;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.admin.User;
import com.jeegox.glio.entities.aim.Aim;
import com.jeegox.glio.entities.aim.Project;
import com.jeegox.glio.entities.aim.Task;
import com.jeegox.glio.entities.aim.Time;
import com.jeegox.glio.enumerators.Priority;
import com.jeegox.glio.enumerators.Status;
import com.jeegox.glio.util.Util;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProjectService {
    
    private final ProjectDAO projectDAO;
    private final AimDAO aimDAO;
    private final TaskDAO taskDAO;
    private final TimeDAO timeDAO;

    @Autowired
    public ProjectService(ProjectDAO projectDAO, AimDAO aimDAO, TaskDAO taskDAO, TimeDAO timeDAO) {
        this.projectDAO = projectDAO;
        this.aimDAO = aimDAO;
        this.taskDAO = taskDAO;
        this.timeDAO = timeDAO;
    }

    @Transactional
    public void saveOrUpdate(Project project) {
        projectDAO.save(project);
    }

    @Transactional(readOnly = true)
    public Project findBydId(Integer id) {
        return projectDAO.findById(id);
    }

    @Transactional
    public void changeStatus(Project project, Status status) throws Exception {
        project.setStatus(status);
        this.saveOrUpdate(project);
    }

    @Transactional(readOnly = true)
    public List<Project> findBy(User user, String query, Status status) {
        if(status == null)
            return projectDAO.findByUser(user, query);
        return projectDAO.findByUser(user, query, status);
    }

    @Transactional(readOnly = true)
    public Map<Status, Long> countTasksGroupedByStatus(Project project) {
        List<Task> tasks = taskDAO.findByProject(project);
        return tasks.stream().collect(Collectors.groupingBy(Task::getStatus, Collectors.counting()));
    }

    @Transactional(readOnly = true)
    public List<Project> findByCompany(Company company, String query, Status[] status) {
        return projectDAO.findByCompany(company, query, status);
    }

    @Transactional(readOnly = true)
    public List<Project> findByCompany(Company company, Status[] status) {
        return projectDAO.findByCompany(company, status);
    }
    
    @Transactional
    public void saveOrUpdate(Aim aim) {
        aimDAO.save(aim);
    }

    @Transactional(readOnly = true)
    public Aim findAimBydId(Integer id) {
        return aimDAO.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Aim> findBy(Project project) {
        return aimDAO.findByProject(project);
    }

    @Transactional
    public void changeStatus(Aim aim, Status status) throws Exception {
        aim.setStatus(status);
        this.saveOrUpdate(aim);
    }

    @Transactional(readOnly = true)
    public Map<Status, Long> countTasksGroupedByStatus(Aim aim){
        List<Task> tasks = taskDAO.findByAim(aim);
        return tasks.stream().collect(Collectors.groupingBy(Task::getStatus, Collectors.counting()));
    }

    @Transactional(readOnly = true)
    public List<Aim> findBy(Project project, Status[] status) {
        return aimDAO.findByProject(project, status);
    }
    
    @Transactional
    public void saveOrUpdate(Task task) {
        taskDAO.save(task);
    }

    @Transactional(readOnly = true)
    public Task findTaskBydId(Integer id) {
        return taskDAO.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Task> findBy(Aim aim) {
        return taskDAO.findByAim(aim);
    }

    @Transactional
    public void changeStatus(Task task, Status status) throws Exception {
        task.setStatus(status);
        this.saveOrUpdate(task);
    }
    
    @Transactional
    public void work(User user, Integer idTask, Status status) throws Exception {
        try{
            Task task = findTaskBydId(idTask);
            User userOwner = task.getUserOwner();
            if(!userOwner.getId().equals(user.getId())){
                throw new RuntimeException("No puedes trabajar con esta actividad, no está asociada a ti.");
            }
            task.setStatus(status);
            Time time = timeDAO.findCurrentTime(task);
            if(time == null){
                time = new Time();
                time.setFather(task);
                time.setInitDate(Util.getCurrentDate());
            }else{
                time.setEndDate(Util.getCurrentDate());
            }
            saveOrUpdate(task);
            timeDAO.save(time);
        }catch(Exception e){
            throw e;        
        }
    }

    @Transactional(readOnly = true)
    public Long countTasksInProcess(User user) {
        return taskDAO.countInProcess(user);
    }

    @Transactional(readOnly = true)
    public List<Task> findByUser(User userOwner, Status[] status, String query, 
            Priority[] priorities, Integer idProject, Integer idAim) {
        if(idAim != 0)
            return taskDAO.findByUser(userOwner, status, query, priorities, aimDAO.findById(idAim));
        else if(idProject != 0)
            return taskDAO.findByUser(userOwner, status, query, priorities, projectDAO.findById(idProject));
        return taskDAO.findByUser(userOwner, status, query, priorities);
    }
    
    @Transactional(readOnly = true)
    public Long countActiveTasksByUserOwner(User user) {
        return taskDAO.countActiveByUserOwner(user);
    }
    
    @Transactional(readOnly = true)
    public Long countActiveAimsByUserOwner(User user) {
        return aimDAO.countActiveByUserOwner(user);
    }
    
    @Transactional(readOnly = true)
    public Long countActiveProjectsByUserOwner(User user) {
        return projectDAO.countActiveByUserOwner(user);
    }
    
    @Transactional(readOnly = true)
    public TaskDTO findSummaryTime(Integer idTask){
        return taskDAO.findSummaryTime(idTask);
    }
    
    @Transactional(readOnly = true)
    public Long countFinishedByUserOwner(User user) {
        return taskDAO.countFinishedByUserOwner(user);
    }
}
