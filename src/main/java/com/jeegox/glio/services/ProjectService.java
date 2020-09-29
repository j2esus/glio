package com.jeegox.glio.services;

import com.jeegox.glio.dao.aim.AimDAO;
import com.jeegox.glio.dao.aim.ProjectDAO;
import com.jeegox.glio.dao.aim.TaskDAO;
import com.jeegox.glio.dao.aim.TimeDAO;
import com.jeegox.glio.dto.GraphStatusVO;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public List<Project> findByCompany(Company company) {
        return projectDAO.findByCompany(company);
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
    public List<GraphStatusVO> findDataGraphProject(Integer idProject) {
        return projectDAO.findDataGraphProject(idProject);
    }

    @Transactional(readOnly = true)
    public List<Map> findDataGraphAimByProject(Integer idProject) {
        List<Map> dataMap = new ArrayList<Map>();
        Project project = findBydId(idProject);
        List<Aim> aims = findBy(project, new Status[]{Status.DELETED, Status.INACTIVE});
        List<GraphStatusVO> graphs = null;
        Map<String, Object> map = null;
        for(Aim aim : aims){
            map = new HashMap<>();
            graphs = findDataGraphAim(aim.getId());
            map.put("aim", aim);
            map.put("data", graphs);
            dataMap.add(map);
        }
        return dataMap;
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
    public List<Aim> findAimByCompany(Company company) {
        return aimDAO.findByCompany(company);
    }

    @Transactional(readOnly = true)
    public Aim findAimBydId(Integer id) {
        return aimDAO.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Aim> findBy(Project project) {
        return aimDAO.findBy(project);
    }

    @Transactional
    public void changeStatus(Aim aim, Status status) throws Exception {
        aim.setStatus(status);
        this.saveOrUpdate(aim);
    }

    @Transactional(readOnly = true)
    public List<GraphStatusVO> findDataGraphAim(Integer idAim) {
        return aimDAO.findDataGraphAim(idAim);
    }

    @Transactional(readOnly = true)
    public List<Aim> findBy(Project project, Status[] status) {
        return aimDAO.findBy(project, status);
    }
    
    @Transactional
    public void saveOrUpdate(Task task) {
        taskDAO.save(task);
    }

    @Transactional(readOnly = true)
    public List<Task> findTasksByCompany(Company company) {
        return taskDAO.findByCompany(company);
    }

    @Transactional(readOnly = true)
    public Task findTaskBydId(Integer id) {
        return taskDAO.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Task> findBy(Aim aim) {
        return taskDAO.findBy(aim);
    }

    @Transactional
    public void changeStatus(Task task, Status status) throws Exception {
        task.setStatus(status);
        this.saveOrUpdate(task);
    }

    @Transactional(readOnly = true)
    public List<Task> findBy(User user, Status[] status, String query, Priority[] priorities) {
        return taskDAO.findBy(user, status, query, priorities);
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
    public List<Task> findBy(Aim aim, Status[] status) {
        return taskDAO.findBy(aim, status);
    }

    @Transactional(readOnly = true)
    public Long count(User user, Status[] status) {
        return taskDAO.count(user, status);
    }

    @Transactional(readOnly = true)
    public Long count(User user, Status[] status, String query, Priority[] priorities) {
        return taskDAO.count(user, status, query, priorities);
    }

    @Transactional(readOnly = true)
    public Long count(Company company, Status[] status, String query, Priority[] priorities, Integer idProject) {
        return taskDAO.count(company, status, query, priorities, idProject);
    }

    @Transactional(readOnly = true)
    public List<Task> findBy(Company company, Status[] status, String query, Priority[] priorities, Integer idProject) {
        return taskDAO.findBy(company, status, query, priorities, idProject);
    }

    @Transactional(readOnly = true)
    public List<TaskDTO> findBy(User userOwner, Status[] status, Date initDate, Date endDate, Integer idProject, Integer idAim) {
        return taskDAO.findBy(userOwner, status, initDate, endDate, idProject, idAim);
    }
    
    @Transactional
    public void saveOrUpdate(Time time) {
        timeDAO.save(time);
    }

    @Transactional(readOnly = true)
    public Time findTimeById(Integer id) {
        return timeDAO.findById(id);
    }

    @Transactional(readOnly = true)
    public Time findCurrentTime(Task task) {
        return timeDAO.findCurrentTime(task);
    }
}
