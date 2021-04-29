package com.jeegox.glio.dao.admin;

import com.jeegox.glio.config.spring.ApplicationContextConfigTest;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.admin.User;
import com.jeegox.glio.entities.admin.UserType;
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
import java.util.Set;
import java.util.stream.Collectors;

@WebAppConfiguration
@ContextConfiguration(classes = ApplicationContextConfigTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class UserDAOTest {
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private SessionFactory sessionFactory;
    private static final User expectedUser = new User(1, "admin@mcdonals", "password", "admin", Status.ACTIVE,
            new UserType(1, "Admin", Status.ACTIVE, new Company(1, "Mcdonals", "hamburguesas", Status.ACTIVE, 3)),
            false, new Company(1, "Mcdonals", "hamburguesas", Status.ACTIVE, 3), "admin@mcdonals.com");
    private static final User expectedUser2 = new User(2, "admin2@mcdonals", "password", "admin2", Status.ACTIVE,
            new UserType(1, "Admin", Status.ACTIVE, new Company(1, "Mcdonals", "hamburguesas", Status.ACTIVE, 3)),
            false, new Company(1, "Mcdonals", "hamburguesas", Status.ACTIVE, 3), "admin2@mcdonals.com");

    @Before
    public void setUp() throws SQLException{
        insertInitialData();
    }

    @Test
    public void findById_idExists_user(){
        assertThat(userDAO.findById(1)).isEqualTo(expectedUser);
    }

    @Test
    public void findById_idNotExists_null(){
        assertThat(userDAO.findById(100)).isNull();
    }

    @Test
    public void exists_idExists_true(){
        assertThat(userDAO.exists(1)).isTrue();
    }

    @Test
    public void exists_idNotExists_false(){
        assertThat(userDAO.exists(100)).isFalse();
    }

    @Test
    public void countByCompany_companyExists_three(){
        Company company = new Company(1, "Mcdonals", "hamburguesas", Status.ACTIVE, 3);
        assertThat(userDAO.count(company)).isEqualTo(3);
    }

    @Test
    public void countByCompany_companyNotExists_zero(){
        Company company = new Company(100, "NotExists", "anything", Status.ACTIVE, 3);
        assertThat(userDAO.count(company)).isEqualTo(0);
    }

    @Test
    public void findByCompany_companyExists_listWithTwoElements(){
        Company company = new Company(1, "Mcdonals", "hamburguesas", Status.ACTIVE, 3);
        assertThat(userDAO.findByCompany(company)).isEqualTo(expectedUserListByCompany());
    }
    
    private List<User> expectedUserListByCompany() {
        List<User> userList = new ArrayList<>();
        userList.add(expectedUser);
        userList.add(expectedUser2);
        userList.add(new User(4, "admin4@mcdonals", "password", "admin4", Status.INACTIVE,
                new UserType(1, "Admin", Status.ACTIVE, new Company(1, "Mcdonals", "hamburguesas", Status.ACTIVE, 3)),
                false, new Company(1, "Mcdonals", "hamburguesas", Status.ACTIVE, 3), "admin4@mcdonals.com"));
        return userList;
    }

    @Test
    public void findByCompany_companyNotExists_emptyList(){
        Company company = new Company(100, "NotExists", "anything", Status.ACTIVE, 3);
        assertThat(userDAO.findByCompany(company)).isEmpty();
    }

    @Test
    public void findByUsername_usernameExists_user(){
        assertThat(userDAO.findByUsername("admin@mcdonals")).isEqualTo(expectedUser);
    }

    @Test
    public void findByUsername_usernameNotExists_null(){
        assertThat(userDAO.findByUsername("notExists")).isNull();
    }

    @Test
    public void count_notRequired_four(){
        assertThat(userDAO.count()).isEqualTo(4);
    }

    @Test
    public void findAll_notRequired_listWithThreeElements(){
        assertThat(userDAO.findAll()).isEqualTo(allExpectedUserList());
    }
    
    private List<User> allExpectedUserList() {
        List<User> userList = new ArrayList<>();
        userList.add(expectedUser);
        userList.add(expectedUser2);
        userList.add(new User(3, "admin3@mcdonals", "password", "admin3", Status.DELETED,
                new UserType(1, "Admin", Status.ACTIVE, new Company(1, "Mcdonals", "hamburguesas", Status.ACTIVE, 3)),
                false, new Company(1, "Mcdonals", "hamburguesas", Status.ACTIVE, 3), "admin3@mcdonals.com"));
        userList.add(new User(4, "admin4@mcdonals", "password", "admin4", Status.INACTIVE,
                new UserType(1, "Admin", Status.ACTIVE, new Company(1, "Mcdonals", "hamburguesas", Status.ACTIVE, 3)),
                false, new Company(1, "Mcdonals", "hamburguesas", Status.ACTIVE, 3), "admin4@mcdonals.com"));
        return userList;
    }

    @Test
    public void login_validUserAndPassword_user(){
        assertThat(userDAO.login("admin@mcdonals", "password")).isEqualTo(expectedUser);
    }

    @Test
    public void login_invalidUserAndPassword_null(){
        assertThat(userDAO.login("admin@mcdonals", "otropassword")).isNull();
    }

    @Test
    public void login_validToken_user(){
        assertThat(userDAO.login("4j3g1ska8d6")).isEqualTo(expectedUser);
    }

    @Test
    public void login_invalidToken_null(){
        assertThat(userDAO.login("4ha6dg5a64aga4")).isNull();
    }
    
    @Test
    public void findActivesByCompany_companyExists_onlyActiveStatus(){
        Company company = new Company(1, "Mcdonals", "hamburguesas", Status.ACTIVE, 3);
        Set<Status> activeStatusSet = userDAO.findActivesByCompany(company).stream()
                .map(item->item.getStatus()).collect(Collectors.toSet());
        assertThat(activeStatusSet).containsExactly(Status.ACTIVE);
    }
    
    @Test
    public void findActivesByCompany_companyNotExists_emptyList() {
        Company company = new Company(100, "NotExists", "anything", Status.ACTIVE, 3);
        assertThat(userDAO.findActivesByCompany(company)).isEmpty();
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

        connection.createStatement().execute("insert into user (id_user, password, username, id_company, id_user_type," +
                " status, only_one_access, name, email)"+
                " values(2, 'password','admin2@mcdonals', 1, 1, 'ACTIVE', false, 'admin2', 'admin2@mcdonals.com')");

        connection.createStatement().execute("insert into user (id_user, password, username, id_company, id_user_type," +
                " status, only_one_access, name, email)"+
                " values(3, 'password','admin3@mcdonals', 1, 1, 'DELETED', false, 'admin3', 'admin3@mcdonals.com')");
        
        connection.createStatement().execute("insert into user (id_user, password, username, id_company, id_user_type,"
                + " status, only_one_access, name, email)"
                + " values(4, 'password','admin4@mcdonals', 1, 1, 'INACTIVE', false, 'admin4', 'admin4@mcdonals.com')");

        connection.createStatement().execute("insert into token (id_token, token, id_user, status)"+
                "values(1, '4j3g1ska8d6', 1, 'ACTIVE')");
    }
}
