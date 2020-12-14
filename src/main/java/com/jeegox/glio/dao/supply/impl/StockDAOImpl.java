package com.jeegox.glio.dao.supply.impl;

import com.jeegox.glio.dao.hibernate.GenericDAOImpl;
import com.jeegox.glio.dao.supply.StockDAO;
import com.jeegox.glio.dto.supply.ArticleStockDTO;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.supply.Article;
import com.jeegox.glio.entities.supply.CategoryArticle;
import com.jeegox.glio.entities.supply.Depot;
import com.jeegox.glio.entities.supply.Stock;
import com.jeegox.glio.enumerators.Status;
import com.jeegox.glio.enumerators.StockType;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class StockDAOImpl extends GenericDAOImpl<Stock, Integer> implements StockDAO {

    @Override
    public Long getTotalIn(Article article, Depot depot) {
        String query = " select sum(stock.quantity) "+
                " from Stock stock "+
                " where stock.article = :article "+
                " and stock.depot = :depot "+
                " and stock.stockType = :stockType ";
        return (Long)sessionFactory.getCurrentSession().createQuery(query)
                .setParameter("article", article)
                .setParameter("depot", depot)
                .setParameter("stockType", StockType.IN)
                .uniqueResultOptional().orElse(0L);
    }

    @Override
    public Long getTotalOut(Article article, Depot depot) {
        String query = " select sum(stock.quantity) "+
                " from Stock stock "+
                " where stock.article = :article "+
                " and stock.depot = :depot "+
                " and stock.stockType = :stockType ";
        return (Long)sessionFactory.getCurrentSession().createQuery(query)
                .setParameter("article",article)
                .setParameter("depot", depot)
                .setParameter("stockType", StockType.OUT)
                .uniqueResultOptional().orElse(0L);
    }

    @Override
    public List<ArticleStockDTO> findAvailableStockGroupedByArticle(Company company, String articleCoincidence) {
        String query = "select new com.jeegox.glio.dto.supply.ArticleStockDTO(article.id, article.name, article.sku, article.unity, " +
                " categoryArticle.name, sizeArticle.name,"+
                " (select sum(stock.quantity) from Stock stock where stock.article = article and stock.stockType = 'IN'), "+
                " (select sum(stock.quantity) from Stock stock where stock.article = article and stock.stockType = 'OUT'))"+
                " from Article article "+
                " join article.categoryArticle categoryArticle "+
                " join article.size sizeArticle "+
                " where article.father = :company "+
                " and article.requiredStock = :requiredStock "+
                " and article.status <> :status "+
                " and (article.name like :articleCoincidence  or article.description like :articleCoincidence )";

        return sessionFactory.getCurrentSession()
                .createQuery(query, ArticleStockDTO.class)
                .setParameter("company", company)
                .setParameter("requiredStock", true)
                .setParameter("status", Status.DELETED)
                .setParameter("articleCoincidence", "%"+articleCoincidence+"%")
                .getResultList();
    }

    @Override
    public List<ArticleStockDTO> findAvailableStockGroupedByArticle(Company company, String articleCoincidence, CategoryArticle category) {
        String query = "select new com.jeegox.glio.dto.supply.ArticleStockDTO(article.id, article.name, article.sku, article.unity, " +
                " categoryArticle.name, sizeArticle.name,"+
                " (select sum(stock.quantity) from Stock stock where stock.article = article and stock.stockType = 'IN'), "+
                " (select sum(stock.quantity) from Stock stock where stock.article = article and stock.stockType = 'OUT'))"+
                " from Article article "+
                " join article.categoryArticle categoryArticle "+
                " join article.size sizeArticle "+
                " where article.father = :company "+
                " and article.requiredStock = :requiredStock "+
                " and article.status <> :status "+
                " and categoryArticle = :category "+
                " and (article.name like :articleCoincidence  or article.description like :articleCoincidence )";

        return sessionFactory.getCurrentSession()
                .createQuery(query, ArticleStockDTO.class)
                .setParameter("company", company)
                .setParameter("requiredStock", true)
                .setParameter("status", Status.DELETED)
                .setParameter("category", category)
                .setParameter("articleCoincidence", "%"+articleCoincidence+"%")
                .getResultList();
    }
}
