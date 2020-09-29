package com.jeegox.glio.dao;

import com.jeegox.glio.config.spring.ApplicationContextConfigTest;
import com.jeegox.glio.dao.aim.ProjectDAO;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.admin.User;
import com.jeegox.glio.entities.admin.UserType;
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
public class ProjectDAOTest {
    @Autowired
    private ProjectDAO projectDAO;
    @Autowired
    private SessionFactory sessionFactory;
    private final static Project expectedProject = new Project(1, "Glio", "Glio ERP", Status.ACTIVE,
            java.sql.Date.valueOf("2020-06-12"), java.sql.Date.valueOf("2020-12-23"), new User(1, "admin@mcdonals", "password", "admin",Status.ACTIVE,
            new UserType(1, "Admin",Status.ACTIVE, new Company(1, "Mcdonals", "hamburguesas",Status.ACTIVE, 3)),
            false, new Company(1, "Mcdonals", "hamburguesas", Status.ACTIVE, 3), "admin@mcdonals.com"));

    @Before
    public void setUp() throws SQLException {
        insertInitialData();
    }

    @Test
    public void findById_idExists_project(){
        assertThat(projectDAO.findById(1)).isEqualTo(expectedProject);
    }

    @Test
    public void findById_idNotExists_null(){
        assertThat(projectDAO.findById(100)).isNull();
    }

    @Test
    public void exists_idExists_true(){
        assertThat(projectDAO.exists(1)).isTrue();
    }

    @Test
    public void exists_idNotExists_false(){
        assertThat(projectDAO.exists(100)).isFalse();
    }

    @Test
    public void count_notRequired_four(){
        assertThat(projectDAO.count()).isEqualTo(4);
    }

    @Test
    public void findAll_notRequired_listWithFourElements(){
        assertThat(projectDAO.findAll()).isEqualTo(allExpectedProjectList());
    }

    @Test
    public void findByCompany_companyExists_listWithTwoElements(){
        Company company = new Company(1, "Mcdonals", "hamburguesas", Status.ACTIVE, 3);
        assertThat(projectDAO.findByCompany(company)).isEqualTo(companyExpectedProjectList());
    }

    @Test
    public void findByCompany_companyNotExists_emptyList(){
        Company company = new Company(100, "NotExists", "not exists", Status.ACTIVE, 3);
        assertThat(projectDAO.findByCompany(company)).isEmpty();
    }

    @Test
    public void findByUser_userExistsAndQueryCoincidence_listWithOnlyOneElement(){
        User user = new User(1, "admin@mcdonals", "password", "admin", Status.ACTIVE,
                new UserType(1, "Admin", Status.ACTIVE, new Company(1, "Mcdonals", "hamburguesas", Status.ACTIVE, 3)),
                false, new Company(1, "Mcdonals", "hamburguesas", Status.ACTIVE, 3), "admin@mcdonals.com");
        assertThat(projectDAO.findByUser(user, "erp")).containsExactly(expectedProject);
    }

    @Test
    public void findByUser_userExistsAndQueryNotCoincidence_emptyList(){
        User user = new User(1, "admin@mcdonals", "password", "admin", Status.ACTIVE,
                new UserType(1, "Admin", Status.ACTIVE, new Company(1, "Mcdonals", "hamburguesas", Status.ACTIVE, 3)),
                false, new Company(1, "Mcdonals", "hamburguesas", Status.ACTIVE, 3), "admin@mcdonals.com");
        assertThat(projectDAO.findByUser(user, "Other")).isEmpty();
    }

    @Test
    public void findByUser_userExistsAndQueryCoincidenceAndDeletedStatus_emptyList(){
        User user = new User(1, "admin@mcdonals", "password", "admin", Status.ACTIVE,
                new UserType(1, "Admin", Status.ACTIVE, new Company(1, "Mcdonals", "hamburguesas", Status.ACTIVE, 3)),
                false, new Company(1, "Mcdonals", "hamburguesas", Status.ACTIVE, 3), "admin@mcdonals.com");
        assertThat(projectDAO.findByUser(user, "erp", Status.DELETED)).isEmpty();
    }

    @Test
    public void findByUser_userExistsAndQueryCoincidenceAndActiveStatus_emptyList(){
        User user = new User(1, "admin@mcdonals", "password", "admin", Status.ACTIVE,
                new UserType(1, "Admin", Status.ACTIVE, new Company(1, "Mcdonals", "hamburguesas", Status.ACTIVE, 3)),
                false, new Company(1, "Mcdonals", "hamburguesas", Status.ACTIVE, 3), "admin@mcdonals.com");
        assertThat(projectDAO.findByUser(user, "erp", Status.ACTIVE)).containsExactly(expectedProject);
    }

    @Test
    public void findByCompany_companyExistsAndQueryCoincidenceAndActiveDeletedStatus_listWithTwoElements(){
        Company company = new Company(1, "Mcdonals", "hamburguesas", Status.ACTIVE, 3);
        assertThat(projectDAO.findByCompany(company, "th", new Status[]{Status.DELETED, Status.ACTIVE})).isEqualTo(companyWithCoincidenceExpectedProjectList());
    }

    @Test
    public void findByCompany_companyExistsAndActiveDeletedStatus_listWithThreeElements(){
        Company company = new Company(1, "Mcdonals", "hamburguesas", Status.ACTIVE, 3);
        assertThat(projectDAO.findByCompany(company, new Status[]{Status.DELETED, Status.ACTIVE})).isEqualTo(companyAllExpectedProjectList());
    }

    private void insertInitialData() throws SQLException{
        Session session = sessionFactory.getCurrentSession();

        Connection connection = ((SessionImpl)session.getSession()).connection();

        connection.createStatement()
                .execute("INSERT INTO company(id_company, name, description, status, total_user)"
                        + " VALUES (1,'Mcdonals','hamburguesas', 'ACTIVE', 3)");

        connection.createStatement().execute("insert into user_type (id_user_type, name, id_company, status)"+
                " values(1, 'Admin', 1, 'ACTIVE')");

        connection.createStatement().execute("insert into user (id_user, password, username, id_company, id_user_type," +
                " status, only_one_access, name, email)"+
                " values(1, 'password','admin@mcdonals', 1, 1, 'ACTIVE', false, 'admin', 'admin@mcdonals.com')");

        connection.createStatement().execute("insert into project(id_project, name, description, init_date, end_date, status, id_user) "+
                " values (1, 'Glio', 'Glio ERP', '2020-06-12', '2020-12-23', 'ACTIVE', 1)");

        connection.createStatement().execute("insert into project(id_project, name, description, init_date, end_date, status, id_user) "+
                " values (2, 'Then', 'Then excuse me', '2016-11-15', '2020-12-31', 'ACTIVE', 1)");

        connection.createStatement().execute("insert into project(id_project, name, description, init_date, end_date, status, id_user) "+
                " values (3, 'Other', 'another failure', '2020-01-01', '2020-12-31', 'DELETED', 1)");

        connection.createStatement()
                .execute("INSERT INTO company(id_company, name, description, status, total_user)"
                        + " VALUES (2,'BurgerKing','hamburguesas', 'ACTIVE', 3)");

        connection.createStatement().execute("insert into user_type (id_user_type, name, id_company, status)"+
                " values(2, 'Admin', 2, 'ACTIVE')");

        connection.createStatement().execute("insert into user (id_user, password, username, id_company, id_user_type," +
                " status, only_one_access, name, email)"+
                " values(2, 'password','admin@burgerking', 2, 2, 'ACTIVE', false, 'admin', 'admin@burgerking.com')");

        connection.createStatement().execute("insert into project(id_project, name, description, init_date, end_date, status, id_user) "+
                " values (4, 'Aquacorp', 'full of quality', '2010-06-01', '2020-12-31', 'ACTIVE', 2)");
    }

    private List<Project> allExpectedProjectList() {
        List<Project> projectList = new ArrayList<>();

        User userMcdonals = new User(1, "admin@mcdonals", "password", "admin", Status.ACTIVE,
                new UserType(1, "Admin", Status.ACTIVE, new Company(1, "Mcdonals", "hamburguesas", Status.ACTIVE, 3)),
                false, new Company(1, "Mcdonals", "hamburguesas", Status.ACTIVE, 3), "admin@mcdonals.com");

        projectList.add(new Project(1, "Glio", "Glio ERP", Status.ACTIVE,
                java.sql.Date.valueOf("2020-06-12"), java.sql.Date.valueOf("2020-12-23"), userMcdonals));
        projectList.add(new Project(2, "Then", "Then excuse me", Status.ACTIVE,
                java.sql.Date.valueOf("2016-11-15"), java.sql.Date.valueOf("2020-12-31"), userMcdonals));
        projectList.add(new Project(3, "Other", "another failure", Status.DELETED,
                java.sql.Date.valueOf("2020-01-01"), java.sql.Date.valueOf("2020-12-31"), userMcdonals));

        User userBurgerking = new User(2, "admin@burgerking", "password", "admin",Status.ACTIVE,
                new UserType(2, "Admin",Status.ACTIVE, new Company(2, "BurgerKing", "hamburguesas",Status.ACTIVE, 3)),
                false, new Company(2, "BurgerKing", "hamburguesas", Status.ACTIVE, 3), "admin@burgerking.com");

        projectList.add(new Project(4, "Aquacorp", "full of quality", Status.ACTIVE,
                java.sql.Date.valueOf("2010-06-01"), java.sql.Date.valueOf("2020-12-31"), userBurgerking));

        return projectList;
    }

    private List<Project> companyExpectedProjectList() {
        List<Project> projectList = new ArrayList<>();
        User userMcdonals = new User(1, "admin@mcdonals", "password", "admin", Status.ACTIVE,
                new UserType(1, "Admin", Status.ACTIVE, new Company(1, "Mcdonals", "hamburguesas", Status.ACTIVE, 3)),
                false, new Company(1, "Mcdonals", "hamburguesas", Status.ACTIVE, 3), "admin@mcdonals.com");
        projectList.add(new Project(1, "Glio", "Glio ERP", Status.ACTIVE,
                java.sql.Date.valueOf("2020-06-12"), java.sql.Date.valueOf("2020-12-23"), userMcdonals));
        projectList.add(new Project(2, "Then", "Then excuse me", Status.ACTIVE,
                java.sql.Date.valueOf("2016-11-15"), java.sql.Date.valueOf("2020-12-31"), userMcdonals));
        return projectList;
    }

    private List<Project> companyWithCoincidenceExpectedProjectList() {
        List<Project> projectList = new ArrayList<>();
        User userMcdonals = new User(1, "admin@mcdonals", "password", "admin", Status.ACTIVE,
                new UserType(1, "Admin", Status.ACTIVE, new Company(1, "Mcdonals", "hamburguesas", Status.ACTIVE, 3)),
                false, new Company(1, "Mcdonals", "hamburguesas", Status.ACTIVE, 3), "admin@mcdonals.com");
        projectList.add(new Project(2, "Then", "Then excuse me", Status.ACTIVE,
                java.sql.Date.valueOf("2016-11-15"), java.sql.Date.valueOf("2020-12-31"), userMcdonals));
        projectList.add(new Project(3, "Other", "another failure", Status.DELETED,
                java.sql.Date.valueOf("2020-01-01"), java.sql.Date.valueOf("2020-12-31"), userMcdonals));
        return projectList;
    }

    private List<Project> companyAllExpectedProjectList() {
        List<Project> projectList = new ArrayList<>();
        User userMcdonals = new User(1, "admin@mcdonals", "password", "admin", Status.ACTIVE,
                new UserType(1, "Admin", Status.ACTIVE, new Company(1, "Mcdonals", "hamburguesas", Status.ACTIVE, 3)),
                false, new Company(1, "Mcdonals", "hamburguesas", Status.ACTIVE, 3), "admin@mcdonals.com");

        projectList.add(new Project(1, "Glio", "Glio ERP", Status.ACTIVE,
                java.sql.Date.valueOf("2020-06-12"), java.sql.Date.valueOf("2020-12-23"), userMcdonals));
        projectList.add(new Project(2, "Then", "Then excuse me", Status.ACTIVE,
                java.sql.Date.valueOf("2016-11-15"), java.sql.Date.valueOf("2020-12-31"), userMcdonals));
        projectList.add(new Project(3, "Other", "another failure", Status.DELETED,
                java.sql.Date.valueOf("2020-01-01"), java.sql.Date.valueOf("2020-12-31"), userMcdonals));
        return projectList;
    }
}
