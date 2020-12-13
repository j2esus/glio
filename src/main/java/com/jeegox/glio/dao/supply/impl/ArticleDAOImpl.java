package com.jeegox.glio.dao.supply.impl;

import com.jeegox.glio.dao.hibernate.GenericDAOImpl;
import com.jeegox.glio.dao.supply.ArticleDAO;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.supply.Article;
import com.jeegox.glio.enumerators.Status;
import java.util.List;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class ArticleDAOImpl extends GenericDAOImpl<Article, Integer> implements ArticleDAO {

    @Override
    public List<Article> findByCompany(Company company) {
        String qry = " select a "
                + " from Article a "
                + " where a.father = :company "
                + " and a.status <> :status  ";
        Query query = sessionFactory.getCurrentSession().createQuery(qry);
        query.setParameter("company", company);
        query.setParameter("status", Status.DELETED);
        return query.list();
    }

    @Override
    public Article findBySkuAndStockRequired(Company company, String sku) {
        String query = " select a "+
                " from Article a "+
                " where a.father = :company "+
                " and a.status = :status "+
                " and a.requiredStock = :requiredStock "+
                " and a.sku = :sku ";
        return (Article)sessionFactory.getCurrentSession().createQuery(query)
                .setParameter("company", company)
                .setParameter("status", Status.ACTIVE)
                .setParameter("requiredStock", true)
                .setParameter("sku", sku)
                .uniqueResultOptional().orElse(null);
    }

}
