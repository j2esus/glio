package com.jeegox.glio.services.supply;

import com.jeegox.glio.config.spring.ApplicationContextConfigTest;
import com.jeegox.glio.dao.supply.StockDAO;
import com.jeegox.glio.dao.supply.impl.StockDAOImpl;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.admin.User;
import com.jeegox.glio.entities.admin.UserType;
import com.jeegox.glio.entities.supply.*;
import com.jeegox.glio.enumerators.Status;
import com.jeegox.glio.enumerators.StockType;
import com.jeegox.glio.enumerators.Unity;
import com.jeegox.glio.exceptions.BusinessException;
import com.jeegox.glio.exceptions.FunctionalException;
import com.jeegox.glio.util.Util;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;

@WebAppConfiguration
@ContextConfiguration(classes = ApplicationContextConfigTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class StockServiceTest {
    private StockService stockService;
    private StockDAO stockDAO;

    private static final Company openShoes = new Company(1, "OpenShoes", "Everything for a good walking."
            , Status.ACTIVE, 3);
    private static final User userAdmin = new User(1, "admin@openshoes", "password", "admin", Status.ACTIVE,
            new UserType(1, "Admin", Status.ACTIVE, openShoes),
            false, openShoes, "admin@openshoes.com");
    private static final Size size24 = new Size(1, "28", Status.ACTIVE, openShoes);
    private static final CategoryArticle categoryMan = new CategoryArticle(1, "Man", Status.ACTIVE, openShoes);
    private static final Article elegance = new Article(1, "Elegance shoe north", "ESNTH",
            "This is a great pair of shoes", 340D, 950D, Status.ACTIVE, Unity.PIEZA, openShoes, categoryMan,
            size24,true);
    private static final Depot main = new Depot(1,"Main", Status.ACTIVE, openShoes);

    @Before
    public void setUpd(){
        stockDAO = mock(StockDAOImpl.class);
        stockService = new StockService(stockDAO);
    }

    @Test
    public void add_stockExists_thrownException(){
        Stock stockWithId = new Stock(1, Util.getCurrentDate(), userAdmin, main, elegance, 100,
                "Attempt to add stock an exists stock", StockType.IN, openShoes);
        FunctionalException functionalException = assertThrows(FunctionalException.class, ()->stockService.add(stockWithId));
        assertThat(functionalException).hasMessageThat().isEqualTo("The stock should not have an id.");
    }

    @Test
    public void add_wrongStockType_thrownException(){
        Stock outStock = new Stock(Util.getCurrentDate(), userAdmin, main, elegance, 100,
                "Attempt to add stock with OUT as type of the stock", StockType.OUT, openShoes);
        BusinessException businessException = assertThrows(BusinessException.class, ()->stockService.add(outStock));
        assertThat(businessException).hasMessageThat().isEqualTo("The type of the stock should be IN.");
    }

    @Test
    public void add_negativeQuantity_thrownException(){
        Stock negativeStock = new Stock(Util.getCurrentDate(), userAdmin, main, elegance, -10,
                "Attempt to add stock an exists stock", StockType.IN, openShoes);
        BusinessException businessException = assertThrows(BusinessException.class, ()->stockService.add(negativeStock));
        assertThat(businessException).hasMessageThat().isEqualTo("The quantity should not be negative or zero.");
    }

}
