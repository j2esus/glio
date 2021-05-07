package com.jeegox.glio.services.aim;

import com.jeegox.glio.dao.aim.AimDAO;
import com.jeegox.glio.dao.aim.TaskDAO;
import com.jeegox.glio.dao.aim.TimeDAO;
import com.jeegox.glio.entities.aim.Aim;
import com.jeegox.glio.entities.aim.Task;
import com.jeegox.glio.entities.aim.Time;
import com.jeegox.glio.enumerators.Status;
import com.jeegox.glio.exceptions.FunctionalException;
import com.jeegox.glio.util.Util;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskService {
    private final TaskDAO taskDAO;
    private final TimeDAO timeDAO;
    private final AimDAO aimDAO;
    
    @Autowired
    public TaskService(TaskDAO taskDAO, TimeDAO timeDAO, AimDAO aimDAO){
        this.taskDAO = taskDAO;
        this.timeDAO = timeDAO;
        this.aimDAO = aimDAO;
    }
    
    @Transactional(readOnly = true)
    public Task findById(Integer id){
        return taskDAO.findById(id);
    }
    
    @Transactional
    public Task finishTask(Task task){
        task.setStatus(Status.FINISHED);
        taskDAO.save(task);
        if(isAimFinished(task.getFather())){
            task.getFather().setStatus(Status.FINISHED);
            aimDAO.save(task.getFather());
        }
        closeTimeOfTask(task);
        return task;
    }
    
    private boolean isAimFinished(Aim aim){
        return taskDAO.countActiveByAim(aim) == 0 &&
                taskDAO.countFinishByAim(aim) > 0;
    }
    
    private void closeTimeOfTask(Task task){
        Time time = timeDAO.findCurrentTime(task);
        if (time == null) {
            throw new FunctionalException("La tarea "+task.getName()
                    +" no ha sido iniciada previamente.");
        } else {
            time.setEndDate(Util.getCurrentDate());
        }
        timeDAO.save(time);
    }
    
    @Transactional
    public void cancelFinishedTask(Task task) {
        task.setStatus(Status.PAUSED);
        taskDAO.save(task);
        reactiveAim(task.getFather());
    }
    
    @Transactional
    public void createNewTask(Task task) {
        if (Objects.nonNull(task.getId())) {
            task.setId(null);
        }
        taskDAO.save(task);
        reactiveAim(task.getFather());
    }
    
    private void reactiveAim(Aim aim){
        if(aim.getStatus() == Status.FINISHED){
            aim.setStatus(Status.ACTIVE);
            aimDAO.save(aim);
        }
    }
}
