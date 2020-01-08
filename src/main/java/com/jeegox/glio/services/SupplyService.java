package com.jeegox.glio.services;

import com.jeegox.glio.dao.supply.ArticleDAO;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.supply.Article;
import com.jeegox.glio.enumerators.Status;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author j2esus
 */
@Service
public class SupplyService {

    @Autowired
    private ArticleDAO articleDAO;
    
    @Transactional(readOnly = true)
    public Article findBydId(Integer id) {
        return articleDAO.findById(id);
    }

    @Transactional
    public List<Article> findArticlesBy(Company company, Status[] status) {
        return articleDAO.findArticlesBy(company, status);
    }

    @Transactional
    public void saveOrUpdate(Article article) throws Exception {
        articleDAO.save(article);
    }
    
    @Transactional
    public void changeStatus(Article article, Status status) throws Exception {
        article.setStatus(status);
        saveOrUpdate(article);
    }
}
