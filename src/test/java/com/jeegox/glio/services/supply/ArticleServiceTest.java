package com.jeegox.glio.services.supply;

import com.jeegox.glio.config.spring.ApplicationContextConfigTest;
import com.jeegox.glio.dao.supply.ArticleDAO;
import com.jeegox.glio.dao.supply.impl.ArticleDAOImpl;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.supply.Article;
import com.jeegox.glio.entities.supply.CategoryArticle;
import com.jeegox.glio.entities.supply.Size;
import com.jeegox.glio.enumerators.Status;
import com.jeegox.glio.enumerators.Unity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;

@WebAppConfiguration
@ContextConfiguration(classes = ApplicationContextConfigTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class ArticleServiceTest {
    private ArticleService articleService;
    private ArticleDAO articleDAO;

    private static final Company mcdonals = new Company(1, "Mcdonals", "burgers", Status.ACTIVE, 3);
    private static final CategoryArticle categoryArticle = new CategoryArticle(1, "Burgers", Status.ACTIVE, mcdonals);
    private static final Size size = new Size(1, "Small", Status.ACTIVE, mcdonals);

    @Before
    public void setUp(){
        articleDAO = mock(ArticleDAOImpl.class);
        articleService = new ArticleService(articleDAO);
    }

    @Test
    public void delete_articleExists_articleWithDeletedStatus(){
        Article article = new Article(2, "Article to delete", "ATDEL","This is un article that will be removed",
                0D, 164D, Status.ACTIVE, Unity.PIEZA, mcdonals, categoryArticle, size,false);
        articleService.delete(article);
        assertThat(article.getStatus()).isEqualTo(Status.DELETED);
    }
}
