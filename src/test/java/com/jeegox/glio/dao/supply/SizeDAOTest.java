package com.jeegox.glio.dao.supply;

import com.jeegox.glio.config.spring.ApplicationContextConfigTest;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.supply.Size;
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
public class SizeDAOTest {
    @Autowired
    private SizeDAO sizeDAO;
    @Autowired
    private SessionFactory sessionFactory;

    private static final Company burgerKing = new Company(1, "Mcdonals", "burgers",Status.ACTIVE, 3);

    private static final Size size = new Size(1, "Small", Status.ACTIVE, burgerKing);
    @Before
    public void setUp() throws SQLException{
        insertInitialData();
    }

    @Test
    public void findById_idExists_size(){
        assertThat(sizeDAO.findById(1)).isEqualTo(size);
    }

    @Test
    public void findById_idNotExists_null(){
        assertThat(sizeDAO.findById(100)).isNull();
    }

    @Test
    public void exists_idExists_true(){
        assertThat(sizeDAO.exists(1)).isTrue();
    }

    @Test
    public void exists_idNotExists_false(){
        assertThat(sizeDAO.exists(100)).isFalse();
    }

    @Test
    public void findByCompany_companyExists_listWithOneElement(){
        assertThat(sizeDAO.findByCompany(burgerKing)).isEqualTo(sizesByCompany());
    }

    @Test
    public void findByCompany_companyNotExists_emptyList(){
        Company notExists = new Company(100, "notExists", "", Status.ACTIVE, 3);
        assertThat(sizeDAO.findByCompany(notExists)).isEmpty();
    }

    @Test
    public void findByCompany_companyExistsAndName_listWithOneElement(){
        assertThat(sizeDAO.findByCompany(burgerKing, "small")).isEqualTo(sizesByCompany());
    }

    @Test
    public void findByCompany_companyNotExistsAndNameNotExists_emptyList(){
        assertThat(sizeDAO.findByCompany(burgerKing, "big")).isEmpty();
    }

    @Test
    public void count_notRequired_three(){
        assertThat(sizeDAO.count());
    }

    @Test
    public void findAll_notRequired_listWithThreeElements(){
        assertThat(sizeDAO.findAll()).isEqualTo(allSizes());
    }

    private List<Size> sizesByCompany(){
        List<Size> sizes = new ArrayList<>();
        sizes.add(size);
        return sizes;
    }

    private List<Size> allSizes(){
        List<Size> sizes = new ArrayList<>();
        sizes.add(size);
        sizes.add(new Size(2, "Big", Status.DELETED, burgerKing));
        sizes.add(new Size(3, "Small", Status.ACTIVE,
                new Company(2, "BurgerKing", "burgers",Status.ACTIVE, 3)));
        return sizes;
    }

    private void insertInitialData() throws SQLException {
        Session session = sessionFactory.getCurrentSession();
        Connection connection = ((SessionImpl)session.getSession()).connection();

        connection.createStatement()
                .execute("INSERT INTO company(id_company, name, description, status, total_user)"
                        + " VALUES (1,'Mcdonals','burgers', 'ACTIVE', 3)");

        connection.createStatement().execute("insert into size(id_size, name, id_company, status)"+
                " values (1, 'Small', 1, 'ACTIVE')");
        connection.createStatement().execute("insert into size(id_size, name, id_company, status)"+
                " values (2, 'Big', 1, 'DELETED')");

        connection.createStatement()
                .execute("INSERT INTO company(id_company, name, description, status, total_user)"
                        + " VALUES (2,'BurgerKing','burgers', 'ACTIVE', 3)");

        connection.createStatement().execute("insert into size(id_size, name, id_company, status)"+
                " values (3, 'Small', 2, 'ACTIVE')");
    }
}
