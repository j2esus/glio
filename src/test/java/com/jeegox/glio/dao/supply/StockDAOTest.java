package com.jeegox.glio.dao.supply;

import com.jeegox.glio.config.spring.ApplicationContextConfigTest;
import com.jeegox.glio.dto.supply.ArticleStockDTO;
import com.jeegox.glio.dto.supply.CategoryStockDTO;
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

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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

    @Test
    public void getTotalIn_articleAndDepot_750(){
        assertThat(stockDAO.getTotalIn(elegance, main)).isEqualTo(750);
    }

    @Test
    public void getTotalIn_articleAndDepotNotExists_0(){
        Depot second = new Depot(2,"Second", Status.ACTIVE, openShoes);
        assertThat(stockDAO.getTotalIn(elegance, second)).isEqualTo(0);
    }

    @Test
    public void getTotalOut_articleAndDepot_20(){
        assertThat(stockDAO.getTotalOut(elegance, main)).isEqualTo(20);
    }

    @Test
    public void getTotalOut_articleAndDepotNotExists_0(){
        Depot second = new Depot(2,"Second", Status.ACTIVE, openShoes);
        assertThat(stockDAO.getTotalOut(elegance, second)).isEqualTo(0);
    }

    @Test
    public void findAvailableStockGroupedByArticle_companyAndNameExists_listWithTwoElements(){
        assertThat(stockDAO.findAvailableStockGroupedByArticle(openShoes, "elegance")).isEqualTo(getExpectedAvailableStocksWithCoincidence());
    }

    private List<ArticleStockDTO> getExpectedAvailableStocksWithCoincidence(){
        List<ArticleStockDTO> articleStockDTOS = new ArrayList<>();
        articleStockDTOS.add(new ArticleStockDTO(1, "Elegance shoe north", "ESNTH", Unity.PIEZA,
                "Man", "28", 750L, 20L));
        articleStockDTOS.add(new ArticleStockDTO(2, "Elegance bigshoe", "ESNTB", Unity.PIEZA,
                "Man", "28", 0L, 0L));
        return articleStockDTOS;
    }

    @Test
    public void findAvailableStockGroupedByArticle_companyExistsAndEmptyName_listWithThreeElements(){
        assertThat(stockDAO.findAvailableStockGroupedByArticle(openShoes, "")).isEqualTo(getExpectedAvailableStocksWithoutCoincidence());
    }

    private List<ArticleStockDTO> getExpectedAvailableStocksWithoutCoincidence(){
        List<ArticleStockDTO> articleStockDTOS = new ArrayList<>();
        articleStockDTOS.add(new ArticleStockDTO(1, "Elegance shoe north", "ESNTH", Unity.PIEZA,
                "Man", "28", 750L, 20L));
        articleStockDTOS.add(new ArticleStockDTO(2, "Elegance bigshoe", "ESNTB", Unity.PIEZA,
                "Man", "28", 0L, 0L));
        articleStockDTOS.add(new ArticleStockDTO(3, "another shoe", "ANOTH", Unity.PIEZA,
                "Man", "28", 0L, 0L));
        articleStockDTOS.add(new ArticleStockDTO(5, "another shoe woman", "ANOTHW", Unity.PIEZA,
                "Woman", "28", 0L, 0L));
        return articleStockDTOS;
    }

    @Test
    public void findAvailableStockGroupedByArticle_companyExistsAndNameNotExists_emptyList(){
        assertThat(stockDAO.findAvailableStockGroupedByArticle(openShoes, "test")).isEmpty();
    }

    @Test
    public void count_3(){
        assertThat(stockDAO.count()).isEqualTo(3);
    }

    @Test
    public void findAvailableStockGroupedByArticle_companyAndNameAndCategoryExists_listWithTwoElements(){
        assertThat(stockDAO.findAvailableStockGroupedByArticle(openShoes, "elegance", categoryMan)).isEqualTo(getExpectedAvailableStocksWithCoincidence());
    }

    @Test
    public void findAvailableStockGroupedByArticle_companyExistsAndEmptyNameAndCategoryExists_listWithThreeElements(){
        assertThat(stockDAO.findAvailableStockGroupedByArticle(openShoes, "", categoryMan)).isEqualTo(getExpectedAvailableStocksWithoutCoincidenceAndOnlyManCategory());
    }

    private List<ArticleStockDTO> getExpectedAvailableStocksWithoutCoincidenceAndOnlyManCategory(){
        List<ArticleStockDTO> articleStockDTOS = new ArrayList<>();
        articleStockDTOS.add(new ArticleStockDTO(1, "Elegance shoe north", "ESNTH", Unity.PIEZA,
                "Man", "28", 750L, 20L));
        articleStockDTOS.add(new ArticleStockDTO(2, "Elegance bigshoe", "ESNTB", Unity.PIEZA,
                "Man", "28", 0L, 0L));
        articleStockDTOS.add(new ArticleStockDTO(3, "another shoe", "ANOTH", Unity.PIEZA,
                "Man", "28", 0L, 0L));
        return articleStockDTOS;
    }

    public void findAvailableStockGroupedByArticle_companyExistsAndCategoryExistsAndNameNotExists_emptyList(){
        assertThat(stockDAO.findAvailableStockGroupedByArticle(openShoes, "test", categoryMan)).isEmpty();
    }

    @Test
    public void findAvailableStockGroupedByArticle_companyAndNameAndCategoryNotExists_emptyList(){
        CategoryArticle category = new CategoryArticle(100, "Not exists", Status.ACTIVE, openShoes);
        assertThat(stockDAO.findAvailableStockGroupedByArticle(openShoes, "elegance", category)).isEmpty();
    }

    @Test
    public void getTotalInByCompany_companyExists_750(){
        assertThat(stockDAO.getTotalInByCompany(openShoes)).isEqualTo(750);
    }

    @Test
    public void getTotalInByCompany_companyNotExists_zero(){
        Company notExists = new Company(2, "test", "company test", Status.ACTIVE, 3);
        assertThat(stockDAO.getTotalInByCompany(notExists)).isEqualTo(0);
    }

    @Test
    public void getTotalOutByCompany_companyExists_20(){
        assertThat(stockDAO.getTotalOutByCompany(openShoes)).isEqualTo(20);
    }

    @Test
    public void getTotalOutByCompany_companyNotExists_zero(){
        Company notExists = new Company(2, "test", "company test", Status.ACTIVE, 3);
        assertThat(stockDAO.getTotalOutByCompany(notExists)).isEqualTo(0);
    }

    @Test
    public void getTotalValueIn_companyExists_712500(){
        assertThat(stockDAO.getTotalValueIn(openShoes)).isEqualTo(712500);
    }

    @Test
    public void getTotalValueIn_companyNotExists_zero(){
        Company notExists = new Company(2, "test", "company test", Status.ACTIVE, 3);
        assertThat(stockDAO.getTotalValueIn(notExists)).isEqualTo(0);
    }

    @Test
    public void getTotalValueOut_companyExists_19000(){
        assertThat(stockDAO.getTotalValueOut(openShoes)).isEqualTo(19000);
    }

    @Test
    public void getTotalValueOut_companyNotExists_zero(){
        Company notExists = new Company(2, "test", "company test", Status.ACTIVE, 3);
        assertThat(stockDAO.getTotalValueOut(notExists)).isEqualTo(0);
    }

    @Test
    public void getTotalStockGroupedByCategory_companyExists_listWithTwoElements(){
        assertThat(stockDAO.getTotalStockGroupedByCategory(openShoes)).isEqualTo(getTotalStockByCategoryRequired());
    }

    private List<CategoryStockDTO> getTotalStockByCategoryRequired(){
        List<CategoryStockDTO> categoryStocksDTO = new ArrayList<>();
        categoryStocksDTO.add(new CategoryStockDTO(1, "Man", BigDecimal.valueOf(750),BigDecimal.valueOf(20)));
        categoryStocksDTO.add(new CategoryStockDTO(3, "Woman", BigDecimal.valueOf(0),BigDecimal.valueOf(0)));
        return categoryStocksDTO;
    }

    @Test
    public void getTotalStockGroupedByCategory_companyNotExists_emptyList(){
        Company notExists = new Company(2, "test", "company test", Status.ACTIVE, 3);
        assertThat(stockDAO.getTotalStockGroupedByCategory(notExists)).isEmpty();
    }

    @Test
    public void getTotalValueStockGroupedByCategory_companyExists_listWithTwoElements(){
        assertThat(stockDAO.getTotalValueStockGroupedByCategory(openShoes)).isEqualTo(getTotalValueStockByCategoryRequired());
    }

    private List<CategoryStockDTO> getTotalValueStockByCategoryRequired(){
        List<CategoryStockDTO> categoryStocksDTO = new ArrayList<>();
        categoryStocksDTO.add(new CategoryStockDTO(1, "Man", BigDecimal.valueOf(712500.0),BigDecimal.valueOf(19000.0)));
        categoryStocksDTO.add(new CategoryStockDTO(3, "Woman", BigDecimal.valueOf(0.0),BigDecimal.valueOf(0.0)));
        return categoryStocksDTO;
    }

    @Test
    public void getTotalValueStockGroupedByCategory_companyNotExists_emptyList(){
        Company notExists = new Company(2, "test", "company test", Status.ACTIVE, 3);
        assertThat(stockDAO.getTotalValueStockGroupedByCategory(notExists)).isEmpty();
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

        connection.createStatement().execute(" insert into category_article (id_category_article, name, id_company, status)"+
                " values (2, 'Man error', 1, 'DELETED')");

        connection.createStatement().execute(" insert into category_article (id_category_article, name, id_company, status)"+
                " values (3, 'Woman', 1, 'ACTIVE')");

        connection.createStatement().execute(" insert into article(id_article, name, sku, description, cost, price," +
                " status, unity, id_company, id_category_article, id_size, required_stock) "+
                " values(1, 'Elegance shoe north', 'ESNTH', 'This is a great pair of shoes', 340, 950, 'ACTIVE', 'PIEZA', 1, 1, 1, true) ");

        connection.createStatement().execute(" insert into article(id_article, name, sku, description, cost, price," +
                " status, unity, id_company, id_category_article, id_size, required_stock) "+
                " values(2, 'Elegance bigshoe', 'ESNTB', 'This is a great pair of shoes', 310, 890, 'ACTIVE', 'PIEZA', 1, 1, 1, true) ");

        connection.createStatement().execute(" insert into article(id_article, name, sku, description, cost, price," +
                " status, unity, id_company, id_category_article, id_size, required_stock) "+
                " values(3, 'another shoe', 'ANOTH', 'This is another kind of shoe', 280, 460, 'ACTIVE', 'PIEZA', 1, 1, 1, true) ");

        connection.createStatement().execute(" insert into article(id_article, name, sku, description, cost, price," +
                " status, unity, id_company, id_category_article, id_size, required_stock) "+
                " values(4, 'another shoe', 'ANOTH', 'This is another kind of shoe', 280, 460, 'DELETED', 'PIEZA', 1, 1, 1, true) ");

        connection.createStatement().execute(" insert into article(id_article, name, sku, description, cost, price," +
                " status, unity, id_company, id_category_article, id_size, required_stock) "+
                " values(5, 'another shoe woman', 'ANOTHW', 'This is another kind of shoe for woman', 190, 260, 'ACTIVE', 'PIEZA', 1, 3, 1, true) ");

        connection.createStatement().execute("insert into depot(id_depot, name, status, id_company)"+
                " values (1, 'Main', 'ACTIVE', 1)");

        connection.createStatement().execute("insert into depot(id_depot, name, status, id_company)"+
                " values (2, 'Second', 'ACTIVE', 1)");

        connection.createStatement().execute(
                "insert into stock(id_stock, stock_date, id_user, id_depot, id_article, quantity, description, type, id_company) "+
                " values (1, '2020-12-08 19:10:26', 1, 1, 1, 600, 'Initial stock', 'IN', 1)");
        connection.createStatement().execute(
                "insert into stock(id_stock, stock_date, id_user, id_depot, id_article, quantity, description, type, id_company) "+
                        " values (2, '2020-12-10 19:10:26', 1, 1, 1, 150, 'Add new stock', 'IN', 1)");
        connection.createStatement().execute(
                "insert into stock(id_stock, stock_date, id_user, id_depot, id_article, quantity, description, type, id_company) "+
                        " values (3, '2020-12-10 19:10:26', 1, 1, 1, 20, 'take stock', 'OUT', 1)");
    }
}
