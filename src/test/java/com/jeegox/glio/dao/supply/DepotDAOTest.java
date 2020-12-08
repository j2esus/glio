package com.jeegox.glio.dao.supply;

import com.jeegox.glio.config.spring.ApplicationContextConfigTest;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.supply.Depot;
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
public class DepotDAOTest {
    @Autowired
    private DepotDAO depotDAO;
    @Autowired
    private SessionFactory sessionFactory;

    private static final Company mcdonals = new Company(1, "Mcdonals", "burgers", Status.ACTIVE, 3);
    private static final Depot mainDepot = new Depot(1,"main", Status.ACTIVE, mcdonals);
    private static final Depot mainDeletedDepot = new Depot(2,"main", Status.DELETED, mcdonals);

    @Before
    public void setUp() throws SQLException{
        insertInitialData();
    }

    @Test
    public void findById_idExists_depot(){
        assertThat(depotDAO.findById(1)).isEqualTo(mainDepot);
    }

    @Test
    public void findById_idNotExists_null(){
        assertThat(depotDAO.findById(100)).isNull();
    }

    @Test
    public void exists_idExists_true(){
        assertThat(depotDAO.exists(1)).isTrue();
    }

    @Test
    public void exists_idNotExists_false(){
        assertThat(depotDAO.exists(100)).isFalse();
    }

    @Test
    public void findByName_nameExists_listWithOneElement(){
        assertThat(depotDAO.findByName(mcdonals, "main")).containsExactly(mainDepot);
    }

    @Test
    public void findByName_nameNotExists_listWithOneElement(){
        assertThat(depotDAO.findByName(mcdonals, "other")).isEmpty();
    }

    @Test
    public void findByNameAndStatus_nameExistsAndStatusExists_listWithOneElement(){
        assertThat(depotDAO.findByNameAndStatus(mcdonals, "main", Status.DELETED)).containsExactly(mainDeletedDepot);
    }

    @Test
    public void findByNameAndStatus_nameNotExistsAndStatusExists_listWithOneElement(){
        assertThat(depotDAO.findByNameAndStatus(mcdonals, "other", Status.ACTIVE)).isEmpty();
    }

    @Test
    public void count_notRequired_two(){
        assertThat(depotDAO.count()).isEqualTo(2);
    }

    @Test
    public void findAll_notRequired_listWithTwoElements(){
        assertThat(depotDAO.findAll()).isEqualTo(getAllDepots());
    }

    private List<Depot> getAllDepots(){
        List<Depot> depots = new ArrayList<>();
        depots.add(mainDepot);
        depots.add(mainDeletedDepot);
        return depots;
    }

    private void insertInitialData() throws SQLException{
        Session session = sessionFactory.getCurrentSession();
        Connection connection = ((SessionImpl)session.getSession()).connection();

        connection.createStatement()
                .execute("INSERT INTO company(id_company, name, description, status, total_user)"
                        + " VALUES (1,'Mcdonals','burgers', 'ACTIVE', 3)");

        connection.createStatement().execute("insert into depot(id_depot, name, status, id_company)"+
                " values (1, 'main', 'ACTIVE', 1)");
        connection.createStatement().execute("insert into depot(id_depot, name, status, id_company)"+
                " values (2, 'main', 'DELETED', 1)");
    }
}
