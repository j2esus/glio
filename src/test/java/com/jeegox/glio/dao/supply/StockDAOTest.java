package com.jeegox.glio.dao.supply;

import com.jeegox.glio.config.spring.ApplicationContextConfigTest;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.admin.User;
import com.jeegox.glio.entities.admin.UserType;
import com.jeegox.glio.entities.supply.*;
import com.jeegox.glio.enumerators.Status;
import com.jeegox.glio.enumerators.StockType;
import com.jeegox.glio.enumerators.Unity;
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
import static com.google.common.truth.Truth.assertThat;

@WebAppConfiguration
@ContextConfiguration(classes = ApplicationContextConfigTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class StockDAOTest {
    @Autowired
    private StockDAO stockDAO;
    @Autowired
    private SessionFactory sessionFactory;

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
    private static final Stock initialStock = new Stock(1, java.sql.Timestamp.valueOf("2020-12-08 19:10:26"), userAdmin,
            main, elegance, 600, "Initial stock", StockType.IN, openShoes);

    @Before
    public void setUp() throws SQLException{
        insertInitialData();
    }

    @Test
    public void findById_idExists_stock(){
        assertThat(stockDAO.findById(1)).isEqualTo(initialStock);
    }

    @Test
    public void findById_idNotExists_null(){
        assertThat(stockDAO.findById(100)).isNull();
    }

    @Test
    public void exists_idExists_true(){
        assertThat(stockDAO.exists(1)).isTrue();
    }

    @Test
    public void exists_idNotExists_false(){
        assertThat(stockDAO.exists(100)).isFalse();
    }

    private void insertInitialData() throws SQLException{
        Session session = sessionFactory.getCurrentSession();
        Connection connection = ((SessionImpl)session.getSession()).connection();

        connection.createStatement()
                .execute("INSERT INTO company(id_company, name, description, status, total_user)"
                        + " VALUES (1,'OpenShoes','Everything for a good walking.', 'ACTIVE', 3)");

        connection.createStatement().execute("insert into user_type (id_user_type, name, id_company, status)"+
                " values(1, 'Admin', 1, 'ACTIVE')");

        connection.createStatement().execute("insert into user (id_user, password, username, id_company, id_user_type," +
                " status, only_one_access, name, email)"+
                " values(1, 'password','admin@openshoes', 1, 1, 'ACTIVE', false, 'admin', 'admin@openshoes.com')");

        connection.createStatement().execute("insert into size(id_size, name, id_company, status)"+
                " values (1, '28', 1, 'ACTIVE')");

        connection.createStatement().execute(" insert into category_article (id_category_article, name, id_company, status)"+
                " values (1, 'Man', 1, 'ACTIVE')");

        connection.createStatement().execute(" insert into article(id_article, name, sku, description, cost, price," +
                " status, unity, id_company, id_category_article, id_size, required_stock) "+
                " values(1, 'Elegance shoe north', 'ESNTH', 'This is a great pair of shoes', 340, 950, 'ACTIVE', 'PIEZA', 1, 1, 1, true) ");

        connection.createStatement().execute("insert into depot(id_depot, name, status, id_company)"+
                " values (1, 'Main', 'ACTIVE', 1)");

        connection.createStatement().execute(
                "insert into stock(id_stock, stock_date, id_user, id_depot, id_article, quantity, description, type, id_company) "+
                " values (1, '2020-12-08 19:10:26', 1, 1, 1, 600, 'Initial stock', 'IN', 1)");
    }
}
