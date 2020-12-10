package com.jeegox.glio.dao.supply.impl;

import com.jeegox.glio.dao.hibernate.GenericDAOImpl;
import com.jeegox.glio.dao.supply.StockDAO;
import com.jeegox.glio.entities.supply.Article;
import com.jeegox.glio.entities.supply.Depot;
import com.jeegox.glio.entities.supply.Stock;
import com.jeegox.glio.enumerators.StockType;
import org.springframework.stereotype.Repository;

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
}
