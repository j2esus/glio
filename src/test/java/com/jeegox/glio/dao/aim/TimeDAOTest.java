package com.jeegox.glio.dao.aim;

import com.jeegox.glio.config.spring.ApplicationContextConfigTest;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.admin.User;
import com.jeegox.glio.entities.admin.UserType;
import com.jeegox.glio.entities.aim.Aim;
import com.jeegox.glio.entities.aim.Project;
import com.jeegox.glio.entities.aim.Task;
import com.jeegox.glio.entities.aim.Time;
import com.jeegox.glio.enumerators.Priority;
import com.jeegox.glio.enumerators.Status;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.internal.SessionImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;

@WebAppConfiguration
@ContextConfiguration(classes = ApplicationContextConfigTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class TimeDAOTest {
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private TimeDAO timeDAO;

    private final static Company company = new Company(1, "company", "description", Status.ACTIVE, 3);
    private final static User admin = new User(1, "admin@company", "password", "admin", Status.ACTIVE,
            new UserType(1, "Admin",Status.ACTIVE, company),
            false, company, "admin@company.com");
    private static final User worker = new User(2, "worker@company", "password", "worker", Status.ACTIVE,
            new UserType(2, "Worker",Status.ACTIVE, new Company(1, "company", "description",Status.ACTIVE, 3)),
            false, company, "worker@company.com");
    private final static Project smallProject = new Project(1, "Small project", "This is a really small project", Status.ACTIVE,
            java.sql.Date.valueOf("2010-01-01"), java.sql.Date.valueOf("2020-12-31"), admin);
    private final static Aim smallAim = new Aim(1, "Do workout", "Go outside and do exercise.",
            Status.ACTIVE, java.sql.Date.valueOf("2010-01-01"), java.sql.Date.valueOf("2010-01-05"), admin, smallProject);
    private static Task openDoorTask = new Task(1, "Open the door", "wake up and open the door", Status.ACTIVE, Priority.MEDIA, 8, admin, worker, smallAim);

    private static Time time = new Time(1, java.sql.Timestamp.valueOf("2010-01-01 09:00:00"), java.sql.Timestamp.valueOf("2010-01-01 09:16:12"), openDoorTask);
    private static Time currentTime = new Time(3, java.sql.Timestamp.valueOf("2010-01-01 10:00:00"), null, openDoorTask);

    @Before
    public void setUp() throws SQLException{
        insertInitialData();
    }

    @Test
    public void findById_idExists_time(){
        assertThat(timeDAO.findById(1)).isEqualTo(time);
    }

    @Test
    public void findById_idNotExists_null(){
        assertThat(timeDAO.findById(100)).isNull();
    }

    @Test
    public void exists_idExists_true(){
        assertThat(timeDAO.exists(1)).isTrue();
    }

    @Test
    public void exists_idNotExists_false(){
        assertThat(timeDAO.exists(100)).isFalse();
    }

    @Test
    public void count_notRequired_three(){
        assertThat(timeDAO.count()).isEqualTo(3);
    }

    @Test
    public void findAll_notRequired_listWithThreeElements(){
        assertThat(timeDAO.findAll()).isEqualTo(getAllTime());
    }

    @Test
    public void findCurrentTime_taskExists_time(){
        assertThat(timeDAO.findCurrentTime(openDoorTask)).isEqualTo(currentTime);
    }

    @Test
    public void findCurrentTime_taskNotExists_null(){
        Task taskNotExists = new Task(100, "new task", "this is a new task without time yet", Status.ACTIVE, Priority.MEDIA, 6, admin, worker, smallAim);
        assertThat(timeDAO.findCurrentTime(taskNotExists)).isNull();
    }

    private List<Time> getAllTime(){
        List<Time> times = new ArrayList<>();
        times.add(time);
        times.add(new Time(2, java.sql.Timestamp.valueOf("2010-01-01 09:20:00"), java.sql.Timestamp.valueOf("2010-01-01 09:50:45"), openDoorTask));
        times.add(currentTime);
        return times;
    }

    private void insertInitialData() throws SQLException {
        Session session = sessionFactory.getCurrentSession();
        Connection connection = ((SessionImpl)session.getSession()).connection();
        connection.createStatement()
                .execute("INSERT INTO company(id_company, name, description, status, total_user)"
                        + " VALUES (1,'company','description', 'ACTIVE', 3)");

        connection.createStatement().execute("insert into user_type (id_user_type, name, id_company, status)"+
                " values(1, 'Admin', 1, 'ACTIVE')");

        connection.createStatement().execute("insert into user_type (id_user_type, name, id_company, status)"+
                " values(2, 'Worker', 1, 'ACTIVE')");

        connection.createStatement().execute("insert into user (id_user, password, username, id_company, id_user_type," +
                " status, only_one_access, name, email)"+
                " values(1, 'password','admin@company', 1, 1, 'ACTIVE', false, 'admin', 'admin@company.com')");

        connection.createStatement().execute("insert into user (id_user, password, username, id_company, id_user_type," +
                " status, only_one_access, name, email)"+
                " values(2, 'password','worker@company', 1, 2, 'ACTIVE', false, 'worker', 'worker@company.com')");

        connection.createStatement().execute("insert into project(id_project, name, description, init_date, end_date, status, id_user) "+
                " values (1, 'Small project', 'This is a really small project', '2010-01-01', '2020-12-31', 'ACTIVE', 1)");

        connection.createStatement().execute("insert into aim(id_aim, name, description, init_date, end_date, status, id_project, id_user)"+
                " values (1, 'Do workout', 'Go outside and do exercise.', '2010-01-01', '2010-01-05', 'ACTIVE', 1, 1)");

        connection.createStatement().execute("insert into task(id_task, name, description, status, priority, estimated_time, id_aim, id_user_requester, id_user_owner)"+
                " values(1, 'Open the door', 'wake up and open the door', 'ACTIVE', 1, 8, 1, 1, 2)");

        connection.createStatement().execute("insert into time (id_time, init_date, end_date, id_task)"+
                " values (1, '2010-01-01 09:00:00', '2010-01-01 09:16:12', 1)");

        connection.createStatement().execute("insert into time (id_time, init_date, end_date, id_task)"+
                " values (2, '2010-01-01 09:20:00', '2010-01-01 09:50:45', 1)");

        connection.createStatement().execute("insert into time (id_time, init_date, id_task)"+
                " values (3, '2010-01-01 10:00:00', 1)");
    }
}
