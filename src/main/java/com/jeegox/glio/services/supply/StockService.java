package com.jeegox.glio.services.supply;

import com.jeegox.glio.dao.supply.StockDAO;
import com.jeegox.glio.entities.supply.Article;
import com.jeegox.glio.entities.supply.Depot;
import com.jeegox.glio.entities.supply.Stock;
import com.jeegox.glio.enumerators.StockType;
import com.jeegox.glio.exceptions.BusinessException;
import com.jeegox.glio.exceptions.FunctionalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Objects;

@Service
public class StockService {
    private static StockDAO stockDAO;

    @Autowired
    public StockService(StockDAO stockDAO){
        this.stockDAO = stockDAO;
    }

    @Transactional
    public void add(Stock stock){
        validateStockExists(stock);
        validateStockTypeIn(stock.getStockType());
        validateNegativeQuantity(stock.getQuantity());
        stockDAO.save(stock);
    }

    private void validateStockExists(Stock stock){
        if(Objects.nonNull(stock.getId()))
            throw new FunctionalException("The stock should not have an id.");
    }

    private void validateStockTypeIn(StockType stockType){
        if(stockType != StockType.IN)
            throw new BusinessException("The type of the stock should be IN.");
    }

    private void validateNegativeQuantity(Integer quantity){
        if(quantity <= 0)
            throw new BusinessException("The quantity should not be negative or zero.");
    }

    @Transactional
    public Long getAvailableStock(Article article, Depot depot){
        return stockDAO.getTotalIn(article, depot) - stockDAO.getTotalOut(article, depot);
    }

    @Transactional
    public void take(Stock stock){
        validateStockExists(stock);
        validateStockTypeOut(stock.getStockType());
        validateNegativeQuantity(stock.getQuantity());
        validateAvailableStock(stock);
        stockDAO.save(stock);
    }

    private void validateStockTypeOut(StockType stockType) {
        if (stockType != StockType.OUT)
            throw new BusinessException("The type of the stock should be OUT.");
    }

    private void validateAvailableStock(Stock stock){
        if(stock.getQuantity() > getAvailableStock(stock.getArticle(), stock.getDepot()))
            throw new BusinessException("The quantity to take must to be less that available stock.");
    }
}
