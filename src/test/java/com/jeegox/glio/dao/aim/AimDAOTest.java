package com.jeegox.glio.dao.aim;

import com.jeegox.glio.config.spring.ApplicationContextConfigTest;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.admin.User;
import com.jeegox.glio.entities.admin.UserType;
import com.jeegox.glio.entities.aim.Aim;
import com.jeegox.glio.entities.aim.Project;
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
public class AimDAOTest {
    @Autowired
    private AimDAO aimDAO;
    @Autowired
    private SessionFactory sessionFactory;
    private final static User user = new User(1, "admin@mcdonals", "password", "admin", Status.ACTIVE,
            new UserType(1, "Admin",Status.ACTIVE, new Company(1, "Mcdonals", "burgers",Status.ACTIVE, 3)),
            false, new Company(1, "Mcdonals", "burgers", Status.ACTIVE, 3), "admin@mcdonals.com");

    private final static Project project = new Project(1, "Project", "This is a really big project", Status.ACTIVE,
            java.sql.Date.valueOf("2010-01-01"), java.sql.Date.valueOf("2020-12-31"), user);

    private final static Aim expectedAim = new Aim(1, "Login dummy", "Create a login dummy for demo presentation",
            Status.ACTIVE, java.sql.Date.valueOf("2010-01-01"), java.sql.Date.valueOf("2010-01-05"), user, project);

    @Before
    public void setUp() throws SQLException{
        insertInitialData();
    }

    @Test
    public void findById_idExists_aim(){
        assertThat(aimDAO.findById(1)).isEqualTo(expectedAim);
    }

    @Test
    public void findById_idNotExists_null(){
        assertThat(aimDAO.findById(100)).isNull();
    }

    @Test
    public void exists_idExists_true(){
        assertThat(aimDAO.exists(1)).isTrue();
    }

    @Test
    public void exists_idNotExists_false(){
        assertThat(aimDAO.exists(100)).isFalse();
    }

    @Test
    public void findByCompany_companyExists_listWithOnlyOneElement(){
        Company company = new Company(1, "Mcdonals", "burgers", Status.ACTIVE, 3);
        assertThat(aimDAO.findByCompany(company)).isEqualTo(expectedListByCompany());
    }
    
    private List<Aim> expectedListByCompany() {
        List<Aim> aimList = new ArrayList<>();
        aimList.add(expectedAim);
        return aimList;
    }

    @Test
    public void findByCompany_companyNotExists_emptyList(){
        Company company = new Company(100, "NotExists", "this company doesn't exists", Status.ACTIVE, 3);
        assertThat(aimDAO.findByCompany(company)).isEmpty();
    }

    @Test
    public void findByProject_projectExists_listWithOnlyOneElement(){
        assertThat(aimDAO.findByProject(project)).containsExactly(expectedAim);
    }

    @Test
    public void findByProject_projectNotExists_emptyList(){
        Project project = new Project(100, "NotExists", "This project doesn't have any aim", Status.ACTIVE,
                java.sql.Date.valueOf("2015-01-01"), java.sql.Date.valueOf("2020-11-30"), user);
        assertThat(aimDAO.findByProject(project)).isEmpty();
    }

    @Test
    public void findByProject_projectExistsAndActiveStatus_listWithOnlyOneElement(){
        assertThat(aimDAO.findByProject(project, new Status[]{Status.ACTIVE})).containsExactly(expectedAim);
    }

    @Test
    public void findByProject_projectExistsAndDeletedStatus_emptyList(){
        assertThat(aimDAO.findByProject(project, new Status[]{Status.DELETED})).isEmpty();
    }

    @Test
    public void count_notRequired_four(){
        assertThat(aimDAO.count()).isEqualTo(4);
    }

    @Test
    public void findAll_noRequired_listWithThreeElements(){
        assertThat(aimDAO.findAll()).isEqualTo(allExpectedAimList());
    }
    
    private List<Aim> allExpectedAimList() {
        User user = new User(2, "admin@burgerking", "password", "admin", Status.ACTIVE,
                new UserType(2, "Admin", Status.ACTIVE, new Company(2, "BurgerKing", "burgers", Status.ACTIVE, 3)),
                false, new Company(2, "BurgerKing", "burgers", Status.ACTIVE, 3), "admin@burgerking.com");

        Project project = new Project(2, "Aquacorp", "full of quality", Status.ACTIVE,
                java.sql.Date.valueOf("2010-06-01"), java.sql.Date.valueOf("2020-12-31"), user);

        List<Aim> aimList = new ArrayList<>();
        aimList.add(expectedAim);
        aimList.add(new Aim(2, "OneToOne", "One aim, one success",
                Status.ACTIVE, java.sql.Date.valueOf("2015-01-01"), java.sql.Date.valueOf("2015-07-05"), user, project));
        aimList.add(new Aim(3, "ManyToOne", "Many aim, one success",
                Status.ACTIVE, java.sql.Date.valueOf("2017-07-01"), java.sql.Date.valueOf("2017-12-05"), user, project));
        aimList.add(new Aim(4, "ManyToOne", "Many aim, one success",
                Status.INACTIVE, java.sql.Date.valueOf("2017-07-01"), java.sql.Date.valueOf("2017-12-05"), user, project));
        return aimList;
    }
    
    @Test
    public void countTasksActiveByUserOwner_userNotExists_zero(){
        User user = new User(100, "unnexists@burgerking", "password", "unnexists", Status.ACTIVE,
                new UserType(2, "Admin", Status.ACTIVE, new Company(2, "BurgerKing", "burgers", Status.ACTIVE, 3)),
                false, new Company(2, "BurgerKing", "burgers", Status.ACTIVE, 3), "admin@burgerking.com");
        assertThat(aimDAO.countActiveByUserOwner(user)).isEqualTo(0);
    }
    
    @Test
    public void countTasksActiveByUserOwner_userExists_two(){
        assertThat(aimDAO.countActiveByUserOwner(user)).isEqualTo(2);
    }

    private void insertInitialData() throws SQLException {
        Session session = sessionFactory.getCurrentSession();
        Connection connection = ((SessionImpl)session.getSession()).connection();

        connection.createStatement()
                .execute("INSERT INTO company(id_company, name, description, status, total_user)"
                        + " VALUES (1,'Mcdonals','burgers', 'ACTIVE', 3)");

        connection.createStatement().execute("insert into user_type (id_user_type, name, id_company, status)"+
                " values(1, 'Admin', 1, 'ACTIVE')");

        connection.createStatement().execute("insert into user (id_user, password, username, id_company, id_user_type," +
                " status, only_one_access, name, email)"+
                " values(1, 'password','admin@mcdonals', 1, 1, 'ACTIVE', false, 'admin', 'admin@mcdonals.com')");

        connection.createStatement().execute("insert into project(id_project, name, description, init_date, end_date, status, id_user) "+
                " values (1, 'Project', 'This is a really big project', '2010-01-01', '2020-12-31', 'ACTIVE', 1)");

        connection.createStatement().execute("insert into aim(id_aim, name, description, init_date, end_date, status, id_project, id_user)"+
                " values (1, 'Login dummy', 'Create a login dummy for demo presentation', '2010-01-01', '2010-01-05', 'ACTIVE', 1, 1)");

        connection.createStatement()
                .execute("INSERT INTO company(id_company, name, description, status, total_user)"
                        + " VALUES (2,'BurgerKing','burgers', 'ACTIVE', 3)");

        connection.createStatement().execute("insert into user_type (id_user_type, name, id_company, status)"+
                " values(2, 'Admin', 2, 'ACTIVE')");

        connection.createStatement().execute("insert into user (id_user, password, username, id_company, id_user_type," +
                " status, only_one_access, name, email)"+
                " values(2, 'password','admin@burgerking', 2, 2, 'ACTIVE', false, 'admin', 'admin@burgerking.com')");

        connection.createStatement().execute("insert into project(id_project, name, description, init_date, end_date, status, id_user) "+
                " values (2, 'Aquacorp', 'full of quality', '2010-06-01', '2020-12-31', 'ACTIVE', 2)");

        connection.createStatement().execute("insert into aim(id_aim, name, description, init_date, end_date, status, id_project, id_user)"+
                " values (2, 'OneToOne', 'One aim, one success', '2015-01-01', '2015-07-05', 'ACTIVE', 2, 2)");

        connection.createStatement().execute("insert into aim(id_aim, name, description, init_date, end_date, status, id_project, id_user)"+
                " values (3, 'ManyToOne', 'Many aim, one success', '2017-07-01', '2017-12-05', 'ACTIVE', 2, 2)");
        
        connection.createStatement().execute("insert into aim(id_aim, name, description, init_date, end_date, status, id_project, id_user)"
                + " values (4, 'ManyToOne', 'Many aim, one success', '2017-07-01', '2017-12-05', 'INACTIVE', 2, 2)");
        
        insertTaskData(connection);
    }
    
    private void insertTaskData(Connection connection) throws SQLException{
        connection.createStatement().execute("insert into task(id_task, name, description, status, priority, estimated_time, id_aim, id_user_requester, id_user_owner)"
                + " values(1, 'screen', 'create screen', 'IN_PROCESS', 1, 8, 1, 1, 1)");

        connection.createStatement().execute("insert into task(id_task, name, description, status, priority, estimated_time, id_aim, id_user_requester, id_user_owner)"
                + " values(2, 'screen', 'create screen', 'DELETED', 1, 8, 2, 1, 1)");

        connection.createStatement().execute("insert into task(id_task, name, description, status, priority, estimated_time, id_aim, id_user_requester, id_user_owner)"
                + " values(3, 'service', 'create service', 'PENDING', 2, 8, 3, 1, 1)");

        connection.createStatement().execute("insert into task(id_task, name, description, status, priority, estimated_time, id_aim, id_user_requester, id_user_owner)"
                + " values(4, 'dao', 'create dao', 'ACCEPTED', 0, 8, 4, 1, 1)");
    }
}
