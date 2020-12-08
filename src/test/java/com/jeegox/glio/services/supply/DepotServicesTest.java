package com.jeegox.glio.services.supply;

import com.jeegox.glio.config.spring.ApplicationContextConfigTest;
import com.jeegox.glio.dao.supply.DepotDAO;
import com.jeegox.glio.dao.supply.impl.DepotDAOImpl;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.supply.Depot;
import com.jeegox.glio.enumerators.Status;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import java.util.ArrayList;
import java.util.List;
import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@WebAppConfiguration
@ContextConfiguration(classes = ApplicationContextConfigTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class DepotServicesTest {
    private DepotService depotService;
    private DepotDAO depotDAO;
    private static final Company mcdonals = new Company(1, "Mcdonals", "burgers", Status.ACTIVE, 3);

    @Before
    public void setUp(){
        depotDAO = mock(DepotDAOImpl.class);
        depotService = new DepotService(depotDAO);
    }

    @Test
    public void delete_depot_depotWithDeletedStatus(){
        Depot depot = new Depot(1,"main", Status.ACTIVE, mcdonals);
        depotService.delete(depot);
        assertThat(depot.getStatus()).isEqualTo(Status.DELETED);
    }

    @Test
    public void findByNameAndStatus_withoutStatus_depotListWithAllStatus(){
        when(depotDAO.findByName(any(), any())).thenReturn(getDepotsWitAllStatus());
        assertThat(depotService.findByNameAndStatus(mcdonals, "", null)).isEqualTo(getDepotsWitAllStatus());
    }

    private List<Depot> getDepotsWitAllStatus(){
        List<Depot> depots = new ArrayList<>();
        depots.add(new Depot(1,"main", Status.ACTIVE, mcdonals));
        depots.add(new Depot(2,"main", Status.DELETED, mcdonals));
        depots.add(new Depot(3,"main", Status.ACTIVE, mcdonals));
        depots.add(new Depot(4,"main", Status.INACTIVE, mcdonals));
        return depots;
    }

    @Test
    public void findByNameAndStatus_withStatus_depotListWithActiveStatus(){
        when(depotDAO.findByNameAndStatus(any(), any(), any())).thenReturn(getDepotsWithActiveStatus());
        assertThat(depotService.findByNameAndStatus(mcdonals, "", Status.ACTIVE)).isEqualTo(getDepotsWithActiveStatus());
    }

    private List<Depot> getDepotsWithActiveStatus(){
        List<Depot> depots = new ArrayList<>();
        depots.add(new Depot(1,"main", Status.ACTIVE, mcdonals));
        depots.add(new Depot(3,"main", Status.ACTIVE, mcdonals));
        return depots;
    }
}
