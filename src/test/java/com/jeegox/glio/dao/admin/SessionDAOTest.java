package com.jeegox.glio.dao.admin;

import com.jeegox.glio.config.spring.ApplicationContextConfigTest;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.admin.Session;
import com.jeegox.glio.entities.admin.User;
import com.jeegox.glio.entities.admin.UserType;
import com.jeegox.glio.enumerators.Status;
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
public class SessionDAOTest {
    @Autowired
    private SessionDAO sessionDAO;
    @Autowired
    private SessionFactory sessionFactory;
    private static final Session expectedSession = new Session(1, "trhp41lc8", Status.CLOSED, java.sql.Timestamp.valueOf("2020-06-10 08:42:25"),
            java.sql.Timestamp.valueOf("2020-06-10 10:42:53"), new User(1, "admin@mcdonals", "password", "admin", Status.ACTIVE,
            new UserType(1, "Admin", Status.ACTIVE, new Company(1, "Mcdonals", "hamburguesas", Status.ACTIVE, 3)),
            false, new Company(1, "Mcdonals", "hamburguesas", Status.ACTIVE, 3), "admin@mcdonals.com"));

    private static final Session openedSession = new Session(3, "d9ejttkk5s", Status.ACTIVE, java.sql.Timestamp.valueOf("2020-06-12 08:42:25"),
            null, new User(1, "admin@mcdonals", "password", "admin", Status.ACTIVE,
            new UserType(1, "Admin", Status.ACTIVE, new Company(1, "Mcdonals", "hamburguesas", Status.ACTIVE, 3)),
            false, new Company(1, "Mcdonals", "hamburguesas", Status.ACTIVE, 3), "admin@mcdonals.com"));

    @Before
    public void setUp() throws SQLException{
        insertInitialData();
    }

    @Test
    public void findById_idExists_session(){
        assertThat(sessionDAO.findById(1)).isEqualTo(expectedSession);
    }

    @Test
    public void findById_idNotExists_null(){
        assertThat(sessionDAO.findById(100)).isNull();
    }

    @Test
    public void exists_idExists_true(){
        assertThat(sessionDAO.exists(1)).isTrue();
    }

    @Test
    public void exists_idNotExists_false(){
        assertThat(sessionDAO.exists(100)).isFalse();
    }

    @Test
    public void count_notRequired_three(){
        assertThat(sessionDAO.count()).isEqualTo(3);
    }

    @Test
    public void findAll_notRequired_listWithThreeElements(){
        assertThat(sessionDAO.findAll()).isEqualTo(getAllExpectedSessions());
    }

    @Test
    public void findByCompany_companyExists_listWithTwoElement(){
        Company company = new Company(1, "Mcdonals", "hamburguesas", Status.ACTIVE, 3);
        assertThat(sessionDAO.findByCompany(company)).isEqualTo(getAllExpectedSessionsBySameCompanyAndSameUser());
    }

    @Test
    public void findByCompany_companyNotExists_emptyList(){
        Company company = new Company(100, "NotExists", "not exists", Status.ACTIVE, 3);
        assertThat(sessionDAO.findByCompany(company)).isEmpty();
    }

    @Test
    public void findByUser_userExists_listWithTwoElements(){
        User user = new User(1, "admin@mcdonals", "password", "admin", Status.ACTIVE,
                new UserType(1, "Admin", Status.ACTIVE, new Company(1, "Mcdonals", "hamburguesas", Status.ACTIVE, 3)),
                false, new Company(1, "Mcdonals", "hamburguesas", Status.ACTIVE, 3), "admin@mcdonals.com");
        assertThat(sessionDAO.findByUser(user)).containsExactlyElementsIn(getAllExpectedSessionsBySameCompanyAndSameUser());
    }

    @Test
    public void findByUser_userNotExists_emptyList(){
        User user = new User(100, "admin@notExists", "password", "admin", Status.ACTIVE,
                new UserType(100, "Admin", Status.ACTIVE, new Company(100, "NotExists", "anything", Status.ACTIVE, 3)),
                false, new Company(100, "NotExists", "anything", Status.ACTIVE, 3), "admin@notexists.com");
        assertThat(sessionDAO.findByUser(user)).isEmpty();
    }

    @Test
    public void findOpenSession_userExists_session(){
        User user = new User(1, "admin@mcdonals", "password", "admin", Status.ACTIVE,
                new UserType(1, "Admin", Status.ACTIVE, new Company(1, "Mcdonals", "hamburguesas", Status.ACTIVE, 3)),
                false, new Company(1, "Mcdonals", "hamburguesas", Status.ACTIVE, 3), "admin@mcdonals.com");
        assertThat(sessionDAO.findOpenSession(user)).isEqualTo(openedSession);
    }

    @Test
    public void findOpenSession_userNotExists_null(){
        User user = new User(100, "admin@notExists", "password", "admin", Status.ACTIVE,
                new UserType(100, "Admin", Status.ACTIVE, new Company(100, "NotExists", "anything", Status.ACTIVE, 3)),
                false, new Company(100, "NotExists", "anything", Status.ACTIVE, 3), "admin@notexists.com");
        assertThat(sessionDAO.findOpenSession(user)).isNull();
    }

    private void insertInitialData() throws SQLException{
        org.hibernate.Session session = sessionFactory.getCurrentSession();
        Connection connection = ((SessionImpl)session.getSession()).connection();

        connection.createStatement()
                .execute("INSERT INTO company(id_company, name, description, status, total_user)"
                        + " VALUES (1,'Mcdonals','hamburguesas', 'ACTIVE', 3)");

        connection.createStatement().execute("insert into user_type (id_user_type, name, id_company, status)"+
                " values(1, 'Admin', 1, 'ACTIVE')");

        connection.createStatement().execute("insert into user (id_user, password, username, id_company, id_user_type," +
                " status, only_one_access, name, email)"+
                " values(1, 'password','admin@mcdonals', 1, 1, 'ACTIVE', false, 'admin', 'admin@mcdonals.com')");

        connection.createStatement().execute("insert into session (id_session,session, init_date, end_date, id_user, status) "+
                " values (1, 'trhp41lc8', '2020-06-10 08:42:25', '2020-06-10 10:42:53', 1, 'CLOSED')");

        connection.createStatement().execute("insert into session (id_session,session, init_date, end_date, id_user, status) "+
                " values (3, 'd9ejttkk5s', '2020-06-12 08:42:25', null, 1, 'ACTIVE')");

        connection.createStatement()
                .execute("INSERT INTO company(id_company, name, description, status, total_user)"
                        + " VALUES (2,'BurgerKing','hamburguesas', 'ACTIVE', 3)");

        connection.createStatement().execute("insert into user_type (id_user_type, name, id_company, status)"+
                " values(2, 'Admin', 2, 'ACTIVE')");

        connection.createStatement().execute("insert into user (id_user, password, username, id_company, id_user_type," +
                " status, only_one_access, name, email)"+
                " values(2, 'password','admin@burgerking', 2, 2, 'ACTIVE', false, 'admin', 'admin@burgerking.com')");

        connection.createStatement().execute("insert into session (id_session,session, init_date, end_date, id_user, status) "+
                " values (2, '1h10mu85n9', '2020-06-11 08:42:25', '2020-06-11 10:42:53', 2, 'CLOSED')");
    }

    private List<Session> getAllExpectedSessions(){
        List<Session> sessions = new ArrayList<>();
        sessions.add(expectedSession);
        sessions.add(new Session(2, "1h10mu85n9", Status.CLOSED, java.sql.Timestamp.valueOf("2020-06-11 08:42:25"),
                java.sql.Timestamp.valueOf("2020-06-11 10:42:53"), new User(2, "admin@burgerking", "password", "admin", Status.ACTIVE,
                new UserType(2, "Admin", Status.ACTIVE, new Company(2, "BurgerKing", "hamburguesas", Status.ACTIVE, 3)),
                false, new Company(2, "BurgerKing", "hamburguesas", Status.ACTIVE, 3), "admin@burgerking.com")));
        sessions.add(openedSession);
        return sessions;
    }

    private List<Session> getAllExpectedSessionsBySameCompanyAndSameUser(){
        List<Session> sessions = new ArrayList<>();
        sessions.add(expectedSession);
        sessions.add(openedSession);
        return sessions;
    }
}
