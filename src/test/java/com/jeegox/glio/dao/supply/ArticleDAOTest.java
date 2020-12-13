package com.jeegox.glio.dao.supply;

import com.jeegox.glio.config.spring.ApplicationContextConfigTest;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.supply.Article;
import com.jeegox.glio.entities.supply.CategoryArticle;
import com.jeegox.glio.entities.supply.Size;
import com.jeegox.glio.enumerators.Status;
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
import java.util.ArrayList;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;

@WebAppConfiguration
@ContextConfiguration(classes = ApplicationContextConfigTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ArticleDAOTest {
    @Autowired
    private ArticleDAO articleDAO;
    @Autowired
    private SessionFactory sessionFactory;

    private static final Company mcdonals = new Company(1, "Mcdonals", "burgers", Status.ACTIVE, 3);
    private static final CategoryArticle categoryArticle = new CategoryArticle(1, "Burgers", Status.ACTIVE, mcdonals);
    private static final Size size = new Size(1, "Small", Status.ACTIVE, mcdonals);
    private static final Article bigMacSmall = new Article(1, "Small bigmac", "BGMCC","Bigmac with cheese",
            0D, 115D, Status.ACTIVE, Unity.PIEZA, mcdonals, categoryArticle, size,true);

    @Before
    public void setUp() throws SQLException {
        insertInitialData();
    }

    @Test
    public void findById_idExists_article(){
        assertThat(articleDAO.findById(1)).isEqualTo(bigMacSmall);
    }

    @Test
    public void findById_idNotExists_null(){
        assertThat(articleDAO.findById(100)).isNull();
    }

    @Test
    public void exists_idExists_true(){
        assertThat(articleDAO.exists(1)).isTrue();
    }

    @Test
    public void exists_idNotExists_false(){
        assertThat(articleDAO.exists(100)).isFalse();
    }

    @Test
    public void findByCompany_companyExists_listWithThreeElements(){
        assertThat(articleDAO.findByCompany(mcdonals)).isEqualTo(getArticlesByCompany());
    }

    private List<Article> getArticlesByCompany(){
        List<Article> articles = new ArrayList<>();
        articles.add(bigMacSmall);
        articles.add(new Article(2, "Big bigmac", "BGMCG","Bigmac with cheese", 0D, 145D, Status.ACTIVE,
                Unity.PIEZA, mcdonals, categoryArticle, new Size(2, "Big", Status.ACTIVE, mcdonals), false));
        return articles;
    }

    @Test
    public void findByCompany_companyNotExists_emptyList(){
        Company company = new Company(100, "company", "descriptions", Status.ACTIVE, 3);
        assertThat(articleDAO.findByCompany(company)).isEmpty();
    }

    @Test
    public void count_notRequired_three(){
        assertThat(articleDAO.count()).isEqualTo(3);
    }

    @Test
    public void findAll_notRequired_listWithThreeElements(){
        assertThat(articleDAO.findAll()).isEqualTo(getAllArticles());
    }

    private List<Article> getAllArticles(){
        List<Article> articles = new ArrayList<>();
        articles.add(bigMacSmall);
        articles.add(new Article(2, "Big bigmac", "BGMCG","Bigmac with cheese", 0D, 145D, Status.ACTIVE,
                Unity.PIEZA, mcdonals, categoryArticle, new Size(2, "Big", Status.ACTIVE, mcdonals), false));
        articles.add(new Article(3, "Big bigmac", "BGMCG","Bigmac with cheese", 0D, 145D, Status.DELETED,
                Unity.PIEZA, mcdonals, categoryArticle, new Size(2, "Big", Status.ACTIVE, mcdonals), false));
        return articles;
    }

    @Test
    public void findBySkuAndStockRequired_skuExists_article(){
        assertThat(articleDAO.findBySkuAndStockRequired(mcdonals, "BGMCC")).isEqualTo(bigMacSmall);
    }

    @Test
    public void findBySkuAndStockRequired_skuExists_article_skuNotExists_null(){
        assertThat(articleDAO.findBySkuAndStockRequired(mcdonals, "BGMCNOT")).isNull();
    }

    private void insertInitialData() throws SQLException{
        Session session = sessionFactory.getCurrentSession();
        Connection connection = ((SessionImpl)session.getSession()).connection();

        connection.createStatement()
                .execute("INSERT INTO company(id_company, name, description, status, total_user)"
                        + " VALUES (1,'Mcdonals','burgers', 'ACTIVE', 3)");

        connection.createStatement().execute("insert into size(id_size, name, id_company, status)"+
                " values (1, 'Small', 1, 'ACTIVE')");
        connection.createStatement().execute("insert into size(id_size, name, id_company, status)"+
                " values (2, 'Big', 1, 'ACTIVE')");

        connection.createStatement().execute(" insert into category_article (id_category_article, name, id_company, status)"+
                " values (1, 'Burgers', 1, 'ACTIVE')");

        connection.createStatement().execute(" insert into article(id_article, name, sku, description, cost, price," +
                " status, unity, id_company, id_category_article, id_size, required_stock) "+
                " values(1, 'Small bigmac', 'BGMCC', 'Bigmac with cheese', 0, 115, 'ACTIVE', 'PIEZA', 1, 1, 1, true) ");

        connection.createStatement().execute(" insert into article(id_article, name, sku, description, cost, price," +
                " status, unity, id_company, id_category_article, id_size, required_stock) "+
                " values(2, 'Big bigmac', 'BGMCG', 'Bigmac with cheese', 0, 145, 'ACTIVE', 'PIEZA', 1, 1, 2, false) ");

        connection.createStatement().execute(" insert into article(id_article, name, sku, description, cost, price," +
                " status, unity, id_company, id_category_article, id_size, required_stock) "+
                " values(3, 'Big bigmac', 'BGMCG', 'Bigmac with cheese', 0, 145, 'DELETED', 'PIEZA', 1, 1, 2, false) ");



    }
}
