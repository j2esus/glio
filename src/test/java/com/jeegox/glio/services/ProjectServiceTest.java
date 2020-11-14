package com.jeegox.glio.services;

import com.google.common.collect.Lists;
import com.jeegox.glio.config.spring.ApplicationContextConfigTest;
import com.jeegox.glio.dao.aim.AimDAO;
import com.jeegox.glio.dao.aim.ProjectDAO;
import com.jeegox.glio.dao.aim.TaskDAO;
import com.jeegox.glio.dao.aim.TimeDAO;
import com.jeegox.glio.dao.aim.impl.AimDAOImpl;
import com.jeegox.glio.dao.aim.impl.ProjectDAOImpl;
import com.jeegox.glio.dao.aim.impl.TaskDAOImpl;
import com.jeegox.glio.dao.aim.impl.TimeDAOImpl;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.admin.User;
import com.jeegox.glio.entities.admin.UserType;
import com.jeegox.glio.entities.aim.Aim;
import com.jeegox.glio.entities.aim.Project;
import com.jeegox.glio.entities.aim.Task;
import com.jeegox.glio.enumerators.Priority;
import com.jeegox.glio.enumerators.Status;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@WebAppConfiguration
@ContextConfiguration(classes = ApplicationContextConfigTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class ProjectServiceTest {
    private ProjectService projectService;
    private ProjectDAO projectDAO;
    private AimDAO aimDAO;
    private TaskDAO taskDAO;
    private TimeDAO timeDAO;

    private static final User admin = new User(1, "admin", "password", "Admin", Status.ACTIVE,
            new UserType(1, "Admin",Status.ACTIVE, new Company(1, "company", "description",Status.ACTIVE, 3)),
            false,
            new Company(1, "company", "description", Status.ACTIVE, 3), "user@company.com");
    private static final User worker = new User(1, "worker", "password", "Worker", Status.ACTIVE,
            new UserType(2, "Worker",Status.ACTIVE, new Company(1, "company", "description",Status.ACTIVE, 3)),
            false,
            new Company(1, "company", "description", Status.ACTIVE, 3), "user@company.com");
    private final static Project project = new Project(1, "Project", "This is a really big project", Status.ACTIVE,
            java.sql.Date.valueOf("2010-01-01"), java.sql.Date.valueOf("2020-12-31"), admin);
    private static final Aim aim = new Aim(1, "selling screen", "create a new screen about selling", Status.ACTIVE,
            java.sql.Date.valueOf("2020-01-01"), java.sql.Date.valueOf("2020-01-31"), admin, project);

    private static List<Task> tasks = new ArrayList<>();
    static{
        tasks.add(new Task(1, "task 1", "description task 1", Status.ACTIVE, Priority.MEDIA, 2, admin, worker, aim));
        tasks.add(new Task(2, "task 2", "description task 2", Status.ACTIVE, Priority.MEDIA, 2, admin, worker, aim));
        tasks.add(new Task(3, "task 3", "description task 3", Status.ACTIVE, Priority.MEDIA, 2, admin, worker, aim));
        tasks.add(new Task(4, "task 4", "description task 4", Status.IN_PROCESS, Priority.MEDIA, 2, admin, worker, aim));
        tasks.add(new Task(5, "task 5", "description task 5", Status.IN_PROCESS, Priority.MEDIA, 2, admin, worker, aim));
        tasks.add(new Task(6, "task 6", "description task 6", Status.FINISHED, Priority.BAJA, 3, admin, worker, aim));
        tasks.add(new Task(7, "task 7", "description task 7", Status.FINISHED, Priority.BAJA, 3, admin, worker, aim));
        tasks.add(new Task(8, "task 8", "description task 8", Status.FINISHED, Priority.ALTA, 5, admin, worker, aim));
        tasks.add(new Task(9, "task 9", "description task 9", Status.FINISHED, Priority.BAJA, 7, admin, worker, aim));
        tasks.add(new Task(10, "task 9", "description task 10", Status.PAUSED, Priority.ALTA, 7, admin, worker, aim));
        tasks.add(new Task(11, "task 11", "description task 11", Status.INACTIVE, Priority.MEDIA, 2, admin, worker, aim));
    }


    @Before
    public void setUp(){
        projectDAO = mock(ProjectDAOImpl.class);
        aimDAO = mock(AimDAOImpl.class);
        taskDAO = mock(TaskDAOImpl.class);
        timeDAO = mock(TimeDAOImpl.class);
        projectService = new ProjectService(projectDAO, aimDAO, taskDAO, timeDAO);
    }

    @Test
    public void countTasksByStatus_aimWithTasks_notEmptyMap(){
        when(taskDAO.findBy(any())).thenReturn(tasks);
        assertThat(projectService.countTasksByStatus(aim)).isEqualTo(resultsExpected());
    }

    @Test
    public void countTasksByStatus_aimWithoutTasks_emptyMap(){
        when(taskDAO.findBy(any())).thenReturn(Lists.newArrayList());
        assertThat(projectService.countTasksByStatus(aim)).isEmpty();
    }

    private Map<Status, Long> resultsExpected(){
        Map<Status, Long> result = new HashMap<>();
        result.put(Status.FINISHED, 4L);
        result.put(Status.IN_PROCESS, 2L);
        result.put(Status.INACTIVE, 1L);
        result.put(Status.PAUSED, 1L);
        result.put(Status.ACTIVE, 3L);
        return result;
    }

}
