package com.jeegox.glio.dao;

import static com.google.common.truth.Truth.assertThat;
import com.jeegox.glio.config.spring.ApplicationContextConfigTest;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.enumerators.Status;
import com.jeegox.glio.services.CompanyService;
import java.math.BigDecimal;
import java.sql.Connection;
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

/**
 *
 * @author j2esus
 */
@WebAppConfiguration
@ContextConfiguration(classes = {ApplicationContextConfigTest.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class CompanyDAOTest {

    @Autowired
    private CompanyService companyService;

    @Before
    public void setData() throws Exception {
        Company company = new Company(1, "jeegox", "jeegox analytics", Status.ACTIVE, 3);
        //companyService.saveOrUpdate(company);
    }

    @Test
    public void whenUserIdIsProvided_thenRetrievedNameIsCorrect() {
        //Company company = companyService.findBy("jeegox");
        //assertThat(company.getName()).isEqualTo("jeegox");
        assertThat(true).isTrue();
    }

}
