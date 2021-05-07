package com.jeegox.glio.services.aim;

import static com.google.common.truth.Truth.assertThat;
import com.jeegox.glio.dao.aim.AimDAO;
import com.jeegox.glio.dao.aim.TaskDAO;
import com.jeegox.glio.dao.aim.TimeDAO;
import com.jeegox.glio.dao.aim.impl.AimDAOImpl;
import com.jeegox.glio.dao.aim.impl.TaskDAOImpl;
import com.jeegox.glio.dao.aim.impl.TimeDAOImpl;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.admin.User;
import com.jeegox.glio.entities.admin.UserType;
import com.jeegox.glio.entities.aim.Aim;
import com.jeegox.glio.entities.aim.Project;
import com.jeegox.glio.entities.aim.Task;
import com.jeegox.glio.entities.aim.Time;
import com.jeegox.glio.enumerators.Priority;
import com.jeegox.glio.enumerators.Status;
import com.jeegox.glio.exceptions.FunctionalException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class TaskServiceTest {
    private TaskDAO taskDAO;
    private TimeDAO timeDAO;
    private AimDAO aimDAO;
    private TaskService taskService;
    
    private static final User admin = new User(1, "admin", "password", "Admin", Status.ACTIVE,
            new UserType(1, "Admin", Status.ACTIVE, new Company(1, "company", "description", 
                    Status.ACTIVE, 3)), false,
            new Company(1, "company", "description", Status.ACTIVE, 3), "user@company.com");
    
    private final static Project project = new Project(1, "Project", "This is a really big project", Status.ACTIVE,
            java.sql.Date.valueOf("2010-01-01"), java.sql.Date.valueOf("2020-12-31"), admin);
    
    @Before
    public void setUp() {
        taskDAO = mock(TaskDAOImpl.class);
        timeDAO = mock(TimeDAOImpl.class);
        aimDAO = mock(AimDAOImpl.class);
        taskService = new TaskService(taskDAO, timeDAO, aimDAO);
    }
    
    @Test
    public void finishTask_taskInProcess_taskFinished(){
        when(taskDAO.countActiveByAim(any())).thenReturn(5L);
        when(taskDAO.countFinishByAim(any())).thenReturn(2L);
        Aim sellingAim = new Aim(1, "selling screen", "create a new screen about selling", Status.ACTIVE,
            java.sql.Date.valueOf("2020-01-01"), java.sql.Date.valueOf("2020-01-31"), admin, project);
        Task task = new Task(1, "task 1", "description task 1", Status.IN_PROCESS, Priority.MEDIA, 2, admin, admin, sellingAim);
        when(timeDAO.findCurrentTime(any())).thenReturn(new Time(2, java.sql.Date.valueOf("2021-05-06"), null, task));
        assertThat(taskService.finishTask(task).getStatus()).isEqualTo(Status.FINISHED);
        assertThat(taskService.finishTask(task).getFather().getStatus()).isEqualTo(Status.ACTIVE);
    }
    
    @Test
    public void finishTask_taskInProcess_taskFinishedAndFinishedAim() {
        when(taskDAO.countActiveByAim(any())).thenReturn(0L);
        when(taskDAO.countFinishByAim(any())).thenReturn(4L);
        Aim sellingAim = new Aim(2, "selling screen", "create a new screen about selling", Status.ACTIVE,
            java.sql.Date.valueOf("2020-01-01"), java.sql.Date.valueOf("2020-01-31"), admin, project);
        Task task = new Task(2, "task 2", "description task 2", Status.IN_PROCESS, Priority.MEDIA, 2, admin, admin, sellingAim);
        when(timeDAO.findCurrentTime(any())).thenReturn(new Time(2, java.sql.Date.valueOf("2021-05-06"), null, task));
        assertThat(taskService.finishTask(task).getStatus()).isEqualTo(Status.FINISHED);
        assertThat(taskService.finishTask(task).getFather().getStatus()).isEqualTo(Status.FINISHED);
    }
    
    @Test
    public void finishTask_taskInProcessWithoutTimeAssociated_functionalException() {
        when(taskDAO.countActiveByAim(any())).thenReturn(0L);
        when(taskDAO.countFinishByAim(any())).thenReturn(4L);
        Aim sellingAim = new Aim(2, "selling screen", "create a new screen about selling", Status.ACTIVE,
                java.sql.Date.valueOf("2020-01-01"), java.sql.Date.valueOf("2020-01-31"), admin, project);
        Task task = new Task(2, "task 2", "description task 2", Status.IN_PROCESS, Priority.MEDIA, 2, admin, admin, sellingAim);
        FunctionalException exception = Assert.assertThrows(FunctionalException.class, ()->taskService.finishTask(task));
        assertThat(exception).hasMessageThat().startsWith("La tarea ");
    }
    
    @Test
    public void cancelFinishedTask_taskFinished_taskWithStatusPaused(){
        Aim sellingAim = new Aim(3, "selling screen", "create a new screen about selling", Status.ACTIVE,
                java.sql.Date.valueOf("2020-01-01"), java.sql.Date.valueOf("2020-01-31"), admin, project);
        Task task = new Task(3, "task 3", "description task 3", Status.FINISHED, Priority.MEDIA, 2, admin, admin, sellingAim);
        taskService.cancelFinishedTask(task);
        assertThat(task.getStatus()).isEqualTo(Status.PAUSED);
        assertThat(task.getFather().getStatus()).isEqualTo(Status.ACTIVE);
    }
    
    @Test
    public void cancelFinishedTask_taskAndAimFinished_taskWithStatusPausedAndActiveAim() {
        Aim sellingAim = new Aim(4, "selling screen", "create a new screen about selling", Status.FINISHED,
                java.sql.Date.valueOf("2020-01-01"), java.sql.Date.valueOf("2020-01-31"), admin, project);
        Task task = new Task(4, "task 4", "description task 4", Status.FINISHED, Priority.MEDIA, 2, admin, admin, sellingAim);
        taskService.cancelFinishedTask(task);
        assertThat(task.getStatus()).isEqualTo(Status.PAUSED);
        assertThat(task.getFather().getStatus()).isEqualTo(Status.ACTIVE);
    }
    
    @Test
    public void createNewTask_taskWithIdAndActiveAim_taskWithIdNull(){
        Aim sellingAim = new Aim(4, "selling screen", "create a new screen about selling", Status.ACTIVE,
                java.sql.Date.valueOf("2020-01-01"), java.sql.Date.valueOf("2020-01-31"), admin, project);
        Task task = new Task(5, "task 5", "description task 5", Status.PENDING, Priority.MEDIA, 2, admin, admin, sellingAim);
        taskService.createNewTask(task);
        assertThat(task.getId()).isNull();
        assertThat(task.getFather().getStatus()).isEqualTo(Status.ACTIVE);
    }
    
    @Test
    public void createNewTask_taskWithIdAndFinishedAim_taskWithIdNullAndActiveAim() {
        Aim sellingAim = new Aim(4, "selling screen", "create a new screen about selling", Status.FINISHED,
                java.sql.Date.valueOf("2020-01-01"), java.sql.Date.valueOf("2020-01-31"), admin, project);
        Task task = new Task(6, "task 6", "description task 6", Status.PENDING, Priority.MEDIA, 2, admin, admin, sellingAim);
        taskService.createNewTask(task);
        assertThat(task.getId()).isNull();
        assertThat(task.getFather().getStatus()).isEqualTo(Status.ACTIVE);
    }
}
