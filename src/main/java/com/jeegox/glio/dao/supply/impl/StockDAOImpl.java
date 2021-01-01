package com.jeegox.glio.dao.supply.impl;

import com.jeegox.glio.dao.hibernate.GenericDAOImpl;
import com.jeegox.glio.dao.supply.StockDAO;
import com.jeegox.glio.dto.supply.ArticleStockDTO;
import com.jeegox.glio.dto.supply.CategoryStockDTO;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.supply.Article;
import com.jeegox.glio.entities.supply.CategoryArticle;
import com.jeegox.glio.entities.supply.Depot;
import com.jeegox.glio.entities.supply.Stock;
import com.jeegox.glio.enumerators.Status;
import com.jeegox.glio.enumerators.StockType;
import org.springframework.stereotype.Repository;

import javax.persistence.Tuple;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public Long getTotalInByCompany(Company company) {
        String query = " select sum(stock.quantity) "+
                " from Stock stock "+
                " where stock.father = :company "+
                " and stock.stockType = :stockType ";
        return (Long)sessionFactory.getCurrentSession().createQuery(query)
                .setParameter("company", company)
                .setParameter("stockType", StockType.IN)
                .uniqueResultOptional().orElse(0L);
    }

    @Override
    public Long getTotalOutByCompany(Company company) {
        String query = " select sum(stock.quantity) "+
                " from Stock stock "+
                " where stock.father = :company "+
                " and stock.stockType = :stockType ";
        return (Long)sessionFactory.getCurrentSession().createQuery(query)
                .setParameter("company", company)
                .setParameter("stockType", StockType.OUT)
                .uniqueResultOptional().orElse(0L);
    }

    @Override
    public Double getTotalValueIn(Company company) {
        String query = " select sum(stock.quantity*article.price) "+
                " from Stock stock "+
                " join stock.article article "+
                " where stock.father = :company "+
                " and stock.stockType = :stockType ";
        return (Double)sessionFactory.getCurrentSession().createQuery(query)
                .setParameter("company", company)
                .setParameter("stockType", StockType.IN)
                .uniqueResultOptional().orElse(0D);
    }

    @Override
    public Double getTotalValueOut(Company company) {
        String query = " select sum(stock.quantity*article.price) "+
                " from Stock stock "+
                " join stock.article article "+
                " where stock.father = :company "+
                " and stock.stockType = :stockType ";
        return (Double)sessionFactory.getCurrentSession().createQuery(query)
                .setParameter("company", company)
                .setParameter("stockType", StockType.OUT)
                .uniqueResultOptional().orElse(0D);
    }

    @Override
    public List<CategoryStockDTO> getTotalStockGroupedByCategory(Company company) {
        String query = "select ca.id_category_article, ca.name," +
                " sum((select if(sum(s.quantity) is null, 0, sum(s.quantity)) from stock s where s.id_article = a.id_article and s.type = 'IN'))," +
                " sum((select if(sum(s.quantity) is null, 0, sum(s.quantity)) from stock s where s.id_article = a.id_article and s.type = 'OUT')) " +
                " from article a" +
                " inner join category_article ca" +
                " on (a.id_category_article = ca.id_category_article)" +
                " where ca.id_company = :company and ca.status <> :status" +
                " group by ca.id_category_article, ca.name ";

        return sessionFactory.getCurrentSession().createNativeQuery(query, Tuple.class)
                .setParameter("company", company)
                .setParameter("status", Status.DELETED.name())
                .getResultList().stream()
                .map(x-> new CategoryStockDTO((Integer)x.get(0), (String)x.get(1), (BigDecimal) x.get(2), (BigDecimal)x.get(3)))
                .collect(Collectors.toList());

    }

    @Override
    public List<CategoryStockDTO> getTotalValueStockGroupedByCategory(Company company) {
        String query = "select ca.id_category_article, ca.name," +
                " sum((select if(sum(s.quantity) is null, 0, sum(s.quantity*ar.price)) " +
                " from stock s inner join article ar on(ar.id_article=s.id_article) where s.id_article = a.id_article and s.type = 'IN'))," +
                " sum((select if(sum(s.quantity) is null, 0, sum(s.quantity*ar.price)) from stock s inner join article ar on(ar.id_article = s.id_article) where s.id_article = a.id_article and s.type = 'OUT')) " +
                " from article a" +
                " inner join category_article ca" +
                " on (a.id_category_article = ca.id_category_article)" +
                " where ca.id_company = :company and ca.status <> :status" +
                " group by ca.id_category_article, ca.name ";

        ;
        return sessionFactory.getCurrentSession().createNativeQuery(query, Tuple.class)
                .setParameter("company", company)
                .setParameter("status", Status.DELETED.name())
                .getResultList().stream()
                .map(x-> new CategoryStockDTO((Integer)x.get(0), (String)x.get(1), BigDecimal.valueOf((Double) x.get(2)),
                        BigDecimal.valueOf((Double) x.get(3))))
                .collect(Collectors.toList());
    }
}
