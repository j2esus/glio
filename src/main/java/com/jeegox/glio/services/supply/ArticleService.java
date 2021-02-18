package com.jeegox.glio.services.supply;

import com.jeegox.glio.dao.supply.ArticleDAO;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.supply.Article;
import com.jeegox.glio.enumerators.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ArticleService {
    private ArticleDAO articleDAO;

    @Autowired
    public ArticleService(ArticleDAO articleDAO){
        this.articleDAO = articleDAO;
    }

    @Transactional
    public void saveOrUpdate(Article article){
        articleDAO.save(article);
    }

    @Transactional(readOnly = true)
    public Article findBydId(Integer id) {
        return articleDAO.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Article> findArticlesBy(Company company) {
        return articleDAO.findByCompany(company);
    }

    @Transactional(readOnly = true)
    public Article findBySkuAndStockRequired(Company company, String sku) {
        return articleDAO.findBySkuAndStockRequired(company, sku);
    }

    @Transactional
    public void delete(Article article){
        article.setStatus(Status.DELETED);
        articleDAO.save(article);
    }

    @Transactional(readOnly = true)
    public Long countWithStockRequired(Company company){
        return articleDAO.countWithStockRequired(company);
    }
}
