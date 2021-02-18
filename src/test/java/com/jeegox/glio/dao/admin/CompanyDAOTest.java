package com.jeegox.glio.dao.admin;

import static com.google.common.truth.Truth.assertThat;
import com.jeegox.glio.config.spring.ApplicationContextConfigTest;
import com.jeegox.glio.entities.admin.Company;
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

@WebAppConfiguration
@ContextConfiguration(classes = {ApplicationContextConfigTest.class})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class CompanyDAOTest {

    @Autowired
    private CompanyDAO companyDAO;
    @Autowired
    private SessionFactory sessionFactory;

    private static Company expectedCompany = new Company(1, "Mcdonals", "hamburguesas", Status.ACTIVE, 3);

    @Before
    public void setUp() throws Exception{
        insertData();
    }

    @Test
    public void findById_idExists_company() {
        assertThat(companyDAO.findById(1)).isEqualTo(expectedCompany);
    }

    @Test
    public void findById_idNotExists_company() {
        assertThat(companyDAO.findById(10)).isNull();
    }

    @Test
    public void count_empty_one() {
        assertThat(companyDAO.count()).isEqualTo(1);
    }

    @Test
    public void findByName_nameExists_company(){
        assertThat(companyDAO.findByName("Mcdonals")).isEqualTo(expectedCompany);
    }

    @Test
    public void findByName_nameNotExists_null(){
        assertThat(companyDAO.findByName("prueba")).isNull();
    }

    @Test
    public void findAll_empty_listWithOnlyOneElement(){
        assertThat(companyDAO.findAll()).containsExactly(expectedCompany);
    }

    private void insertData() throws Exception{
        Session session = sessionFactory.getCurrentSession();
        Connection connection = ((SessionImpl) session.getSession()).connection();

        connection.createStatement()
                .execute("INSERT INTO company"
                        + "(id_company, name, description, status, total_user)"
                        + " VALUES (1,'Mcdonals','hamburguesas', 'ACTIVE', 3)");
    }
}
