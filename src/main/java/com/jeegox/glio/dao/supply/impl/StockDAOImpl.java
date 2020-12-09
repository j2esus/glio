package com.jeegox.glio.dao.supply.impl;

import com.jeegox.glio.dao.hibernate.GenericDAOImpl;
import com.jeegox.glio.dao.supply.StockDAO;
import com.jeegox.glio.entities.supply.Stock;
import org.springframework.stereotype.Repository;

@Repository
public class StockDAOImpl extends GenericDAOImpl<Stock, Integer> implements StockDAO {

}
