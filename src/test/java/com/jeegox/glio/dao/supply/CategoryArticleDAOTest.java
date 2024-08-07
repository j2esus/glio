package com.jeegox.glio.dao.supply;

import com.jeegox.glio.config.spring.ApplicationContextConfigTest;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.supply.CategoryArticle;
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
public class CategoryArticleDAOTest {
    @Autowired
    private CategoryArticleDAO categoryArticleDAO;
    @Autowired
    private SessionFactory sessionFactory;

    private static final Company mcdonals = new Company(1, "Mcdonals", "burgers", Status.ACTIVE, 3);
    private static final CategoryArticle categoryArticle = new CategoryArticle(1, "Burgers", Status.ACTIVE, mcdonals);

    @Before
    public void setUp() throws SQLException {
        insertInitialData();
    }

    @Test
    public void findById_idExists_categoryArticle(){
        assertThat(categoryArticleDAO.findById(1)).isEqualTo(categoryArticle);
    }

    @Test
    public void findById_idNotExists_null(){
        assertThat(categoryArticleDAO.findById(100)).isNull();
    }

    @Test
    public void exists_idExists_true(){
        assertThat(categoryArticleDAO.exists(1)).isTrue();
    }

    @Test
    public void exists_idNotExists_false(){
        assertThat(categoryArticleDAO.exists(100)).isFalse();
    }

    @Test
    public void findByCompany_companyExists_listWithOneElement(){
        assertThat(categoryArticleDAO.findByCompany(mcdonals)).containsExactly(categoryArticle);
    }

    @Test
    public void findByCompany_companyNotExists_emptyList(){
        Company company = new Company(100, "name", "description", Status.ACTIVE, 3);
        assertThat(categoryArticleDAO.findByCompany(company)).isEmpty();
    }

    @Test
    public void findByCompany_companyExistsAndNameExists_listWithOneElement(){
        assertThat(categoryArticleDAO.findByCompany(mcdonals, "burgers")).containsExactly(categoryArticle);
    }

    @Test
    public void findByCompany_companyExistsAndNameNotExists_emptyList(){
        assertThat(categoryArticleDAO.findByCompany(mcdonals, "drinks")).isEmpty();
    }

    @Test
    public void count_notRequired_three(){
        assertThat(categoryArticleDAO.count()).isEqualTo(3);
    }

    @Test
    public void findAll_notRequired_listWithThreeElements(){
        assertThat(categoryArticleDAO.findAll()).isEqualTo(getAllCategoryArticles());
    }

    private List<CategoryArticle> getAllCategoryArticles(){
        List<CategoryArticle> categoryArticles = new ArrayList<>();
        categoryArticles.add(categoryArticle);
        categoryArticles.add(new CategoryArticle(2, "Burgers", Status.DELETED, mcdonals));
        categoryArticles.add(new CategoryArticle(3, "Burgers", Status.ACTIVE,
                new Company(2, "BurgerKing", "burgers", Status.ACTIVE, 3)));
        return categoryArticles;
    }

    private void insertInitialData() throws SQLException{
        Session session = sessionFactory.getCurrentSession();
        Connection connection = ((SessionImpl)session.getSession()).connection();

        connection.createStatement()
                .execute("INSERT INTO company(id_company, name, description, status, total_user)"
                        + " VALUES (1,'Mcdonals','burgers', 'ACTIVE', 3)");

        connection.createStatement().execute(" insert into category_article (id_category_article, name, id_company, status)"+
                " values (1, 'Burgers', 1, 'ACTIVE')");

        connection.createStatement().execute(" insert into category_article (id_category_article, name, id_company, status)"+
                " values (2, 'Burgers', 1, 'DELETED')");

        connection.createStatement()
                .execute("INSERT INTO company(id_company, name, description, status, total_user)"
                        + " VALUES (2,'BurgerKing','burgers', 'ACTIVE', 3)");

        connection.createStatement().execute(" insert into category_article (id_category_article, name, id_company, status)"+
                " values (3, 'Burgers', 2, 'ACTIVE')");
    }
}
