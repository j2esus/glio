package com.jeegox.glio.dao.supply;

import com.jeegox.glio.dao.hibernate.GenericDAO;
import com.jeegox.glio.entities.supply.Article;
import com.jeegox.glio.entities.supply.Depot;
import com.jeegox.glio.entities.supply.Stock;

public interface StockDAO extends GenericDAO<Stock, Integer> {

    Long getTotalIn(Article article, Depot depot);

    Long getTotalOut(Article article, Depot depot);

}
