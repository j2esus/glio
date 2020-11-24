package com.jeegox.glio.dao.admin;

import com.jeegox.glio.config.spring.ApplicationContextConfigTest;
import com.jeegox.glio.entities.admin.Company;
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
public class UserTypeDAOTest {
    @Autowired
    private UserTypeDAO userTypeDAO;
    @Autowired
    private SessionFactory sessionFactory;
    private static final UserType expectedUserType = new UserType(1, "Admin",Status.ACTIVE, new Company(1,
            "Mcdonals", "burguers",Status.ACTIVE, 3));

    @Before
    public void setUp() throws SQLException{
        insertInitialData();
    }

    @Test
    public void findById_idExists_userType(){
        assertThat(userTypeDAO.findById(1)).isEqualTo(expectedUserType);
    }

    @Test
    public void findById_idNotExists_null(){
        assertThat(userTypeDAO.findById(100)).isNull();
    }

    @Test
    public void exists_idExists_true(){
        assertThat(userTypeDAO.exists(1)).isTrue();
    }

    @Test
    public void exists_idNotExists_false(){
        assertThat(userTypeDAO.exists(100)).isFalse();
    }

    @Test
    public void count_notRequired_three(){
        assertThat(userTypeDAO.count()).isEqualTo(3);
    }

    @Test
    public void findByCompany_companyNotExists_emptyList(){
        Company company = new Company(100, "NotExists", "anything", Status.ACTIVE, 3);
        assertThat(userTypeDAO.findByCompany(company)).isEmpty();
    }

    @Test
    public void findByCompany_companyExists_listWithTwoElements(){
        Company company = new Company(1, "Mcdonals", "burguers", Status.ACTIVE, 3);
        assertThat(userTypeDAO.findByCompany(company)).isEqualTo(expectedUserTypesByCompany());
    }

    @Test
    public void findAll_notRequired_listWithThreeElements(){
        assertThat(userTypeDAO.findAll()).isEqualTo(allExpectedUserTypes());
    }

    private void insertInitialData() throws SQLException{
        Session session = sessionFactory.getCurrentSession();
        Connection connection = ((SessionImpl)session.getSession()).connection();

        connection.createStatement()
                .execute("INSERT INTO company(id_company, name, description, status, total_user)"
                        + " VALUES (1,'Mcdonals','burguers', 'ACTIVE', 3)");

        connection.createStatement().execute("insert into user_type (id_user_type, name, id_company, status)"+
                " values(1, 'Admin', 1, 'ACTIVE')");

        connection.createStatement().execute("insert into user_type (id_user_type, name, id_company, status)"+
                " values(2, 'Programer', 1, 'ACTIVE')");

        connection.createStatement().execute("insert into user_type (id_user_type, name, id_company, status)"+
                " values(3, 'Provider', 1, 'DELETED')");
    }

    private List<UserType> expectedUserTypesByCompany(){
        List<UserType> userTypeList = new ArrayList<>();
        userTypeList.add(expectedUserType);
        userTypeList.add(new UserType(2, "Programer",Status.ACTIVE, new Company(1,
                "Mcdonals", "burguers",Status.ACTIVE, 3)));
        return userTypeList;
    }

    private List<UserType> allExpectedUserTypes(){
        List<UserType> userTypeList = new ArrayList<>();
        userTypeList.add(expectedUserType);
        userTypeList.add(new UserType(2, "Programer",Status.ACTIVE, new Company(1,
                "Mcdonals", "burguers",Status.ACTIVE, 3)));
        userTypeList.add(new UserType(3, "Provider",Status.DELETED, new Company(1,
                "Mcdonals", "burguers",Status.ACTIVE, 3)));
        return userTypeList;
    }
}
