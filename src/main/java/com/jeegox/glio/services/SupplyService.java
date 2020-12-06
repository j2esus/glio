package com.jeegox.glio.services;

import com.jeegox.glio.dao.supply.ArticleDAO;
import com.jeegox.glio.dao.supply.CategoryArticleDAO;
import com.jeegox.glio.dao.supply.SizeDAO;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.supply.Article;
import com.jeegox.glio.entities.supply.CategoryArticle;
import com.jeegox.glio.entities.supply.Size;
import com.jeegox.glio.enumerators.Status;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SupplyService {

    private final ArticleDAO articleDAO;
    private final CategoryArticleDAO categoryArticleDAO;
    private final SizeDAO sizeDAO;

    @Autowired
    public SupplyService(ArticleDAO articleDAO, CategoryArticleDAO categoryArticleDAO, SizeDAO sizeDAO) {
        this.articleDAO = articleDAO;
        this.categoryArticleDAO = categoryArticleDAO;
        this.sizeDAO = sizeDAO;
    }
    
    @Transactional(readOnly = true)
    public Article findBydId(Integer id) {
        return articleDAO.findById(id);
    }

    @Transactional(readOnly = true)
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
    
    @Transactional(readOnly = true)
    public CategoryArticle findCategoryArticleBydId(Integer id) {
        return categoryArticleDAO.findById(id);
    }

    @Transactional(readOnly = true)
    public List<CategoryArticle> findCategoriesArticlesBy(Company company, Status[] status) {
        return categoryArticleDAO.findCategoriesArticlesBy(company, status);
    }

    @Transactional
    public void saveOrUpdate(CategoryArticle categoryArticle) throws Exception {
        categoryArticleDAO.save(categoryArticle);
    }
    
    @Transactional
    public void changeStatus(CategoryArticle categoryArticle, Status status) throws Exception {
        categoryArticle.setStatus(status);
        saveOrUpdate(categoryArticle);
    }
    
    @Transactional(readOnly = true)
    public List<CategoryArticle> findByCompany(Company company, String nameLike){
        return categoryArticleDAO.findByCompany(company, nameLike);
    }

    @Transactional
    public void saveOrUpdate(Size size){
        sizeDAO.save(size);
    }

    @Transactional
    public void changeStatus(Size size, Status status) {
        size.setStatus(status);
        saveOrUpdate(size);
    }

    @Transactional(readOnly = true)
    public List<Size> findSizesBy(Company company) {
        return sizeDAO.findByCompany(company);
    }

    @Transactional(readOnly = true)
    public Size findSizeById(Integer id){
        return sizeDAO.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Size> findSizesByCompany(Company company, String nameLike){
        return sizeDAO.findByCompany(company, nameLike);
    }
}
