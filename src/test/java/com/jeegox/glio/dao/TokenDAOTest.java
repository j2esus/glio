package com.jeegox.glio.dao;

import com.jeegox.glio.config.spring.ApplicationContextConfigTest;
import com.jeegox.glio.dao.admin.TokenDAO;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.admin.Token;
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

@WebAppConfiguration
@ContextConfiguration(classes = ApplicationContextConfigTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class TokenDAOTest {
    @Autowired
    private TokenDAO tokenDAO;
    @Autowired
    private SessionFactory sessionFactory;
    private static final Token expectedToken = new Token(1, "4j3g1ska8d6", Status.ACTIVE, new User(1, "admin@mcdonals", "password", "admin", Status.ACTIVE,
            new UserType(1, "Admin", Status.ACTIVE, new Company(1, "Mcdonals", "hamburguesas", Status.ACTIVE, 3)),
            false, new Company(1, "Mcdonals", "hamburguesas", Status.ACTIVE, 3), "admin@mcdonals.com"));

    private static final Token closedToken = new Token(2, "j3dya7djc8aj", Status.CLOSED, new User(1, "admin@mcdonals", "password", "admin", Status.ACTIVE,
            new UserType(1, "Admin", Status.ACTIVE, new Company(1, "Mcdonals", "hamburguesas", Status.ACTIVE, 3)),
            false, new Company(1, "Mcdonals", "hamburguesas", Status.ACTIVE, 3), "admin@mcdonals.com"));

    @Before
    public void setUp() throws SQLException {
        insertInitialData();
    }

    @Test
    public void findById_idExists_token(){
        assertThat(tokenDAO.findById(1)).isEqualTo(expectedToken);
    }

    @Test
    public void findById_idNotExists_null(){
        assertThat(tokenDAO.findById(100)).isNull();
    }

    @Test
    public void exists_idExists_true(){
        assertThat(tokenDAO.exists(1)).isTrue();
    }

    @Test
    public void exists_idNotExists_false(){
        assertThat(tokenDAO.exists(100)).isFalse();
    }

    @Test
    public void getActive_userExists_token(){
        User user = new User(1, "admin@mcdonals", "password", "admin", Status.ACTIVE,
                new UserType(1, "Admin", Status.ACTIVE, new Company(1, "Mcdonals", "hamburguesas", Status.ACTIVE, 3)),
                false, new Company(1, "Mcdonals", "hamburguesas", Status.ACTIVE, 3), "admin@mcdonals.com");
        assertThat(tokenDAO.getActive(user)).isEqualTo(expectedToken);
    }

    @Test
    public void getActive_userNotExists_null(){
        User user = new User(100, "admin@notExists", "password", "admin", Status.ACTIVE,
                new UserType(100, "Admin", Status.ACTIVE, new Company(100, "NotExists", "anything", Status.ACTIVE, 3)),
                false, new Company(100, "NotExists", "anything", Status.ACTIVE, 3), "admin@notexists.com");
        assertThat(tokenDAO.getActive(user)).isNull();
    }

    @Test
    public void findByUser_userExists_listWithTwoElements(){
        User user = new User(1, "admin@mcdonals", "password", "admin", Status.ACTIVE,
                new UserType(1, "Admin", Status.ACTIVE, new Company(1, "Mcdonals", "hamburguesas", Status.ACTIVE, 3)),
                false, new Company(1, "Mcdonals", "hamburguesas", Status.ACTIVE, 3), "admin@mcdonals.com");
        assertThat(tokenDAO.findByUser(user)).isEqualTo(expectedTokensList());
    }

    @Test
    public void findByUser_userNotExists_emptyList(){
        User user = new User(100, "admin@notExists", "password", "admin", Status.ACTIVE,
                new UserType(100, "Admin", Status.ACTIVE, new Company(100, "NotExists", "anything", Status.ACTIVE, 3)),
                false, new Company(100, "NotExists", "anything", Status.ACTIVE, 3), "admin@notexists.com");
        assertThat(tokenDAO.findByUser(user)).isEmpty();
    }

    @Test
    public void findByTokenAndStatus_tokenExistsAndClosedStatus_token(){
        assertThat(tokenDAO.find(Status.CLOSED, "j3dya7djc8aj")).isEqualTo(closedToken);
    }

    @Test
    public void findByTokenAndStatus_tokenNotExistsAndClosedStatus_null(){
        assertThat(tokenDAO.find(Status.CLOSED, "notExists")).isNull();
    }

    @Test
    public void findByCompany_companyExists_listWithTwoElements(){
        Company company = new Company(1, "Mcdonals", "hamburguesas", Status.ACTIVE, 3);
        assertThat(tokenDAO.findByCompany(company)).isEqualTo(expectedTokensList());
    }

    @Test
    public void findByCompany_companyNotExists_listWithTwoElements(){
        Company company = new Company(100, "NotExists", "anything", Status.ACTIVE, 3);
        assertThat(tokenDAO.findByCompany(company)).isEmpty();
    }

    @Test
    public void count_notRequired_three(){
        assertThat(tokenDAO.count()).isEqualTo(3);
    }

    @Test
    public void findAll_noRequired_listWithThreeElements(){
        assertThat(tokenDAO.findAll()).isEqualTo(allExpectedTokensList());
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

        connection.createStatement().execute("insert into token (id_token, token, id_user, status)"+
                "values(1, '4j3g1ska8d6', 1, 'ACTIVE')");

        connection.createStatement().execute("insert into token (id_token, token, id_user, status)"+
                "values(2, 'j3dya7djc8aj', 1, 'CLOSED')");

        connection.createStatement()
                .execute("INSERT INTO company(id_company, name, description, status, total_user)"
                        + " VALUES (2,'BurgerKing','hamburguesas', 'ACTIVE', 3)");

        connection.createStatement().execute("insert into user_type (id_user_type, name, id_company, status)"+
                " values(2, 'Admin', 2, 'ACTIVE')");

        connection.createStatement().execute("insert into user (id_user, password, username, id_company, id_user_type," +
                " status, only_one_access, name, email)"+
                " values(2, 'password','admin@burgerking', 2, 2, 'ACTIVE', false, 'admin', 'admin@burgerking.com')");

        connection.createStatement().execute("insert into token (id_token, token, id_user, status)"+
                "values(3, '9afa6ch4dg', 2, 'ACTIVE')");
    }

    private List<Token> expectedTokensList(){
        List<Token> tokensList = new ArrayList<>();
        tokensList.add(expectedToken);
        tokensList.add(closedToken);
        return tokensList;
    }

    private List<Token> allExpectedTokensList(){
        List<Token> tokensList = new ArrayList<>();
        tokensList.add(expectedToken);
        tokensList.add(closedToken);
        tokensList.add(new Token(3, "9afa6ch4dg", Status.ACTIVE, new User(2, "admin@burgerking", "password", "admin", Status.ACTIVE,
                new UserType(2, "Admin", Status.ACTIVE, new Company(2, "BurgerKing", "hamburguesas", Status.ACTIVE, 3)),
                false, new Company(2, "BurgerKing", "hamburguesas", Status.ACTIVE, 3), "admin@burgerking.com")));
        return tokensList;
    }
}
