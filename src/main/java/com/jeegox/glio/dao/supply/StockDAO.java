package com.jeegox.glio.dao.supply;

import com.jeegox.glio.dao.hibernate.GenericDAO;
import com.jeegox.glio.dto.supply.ArticleStockDTO;
import com.jeegox.glio.dto.supply.CategoryStockDTO;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.supply.Article;
import com.jeegox.glio.entities.supply.CategoryArticle;
import com.jeegox.glio.entities.supply.Depot;
import com.jeegox.glio.entities.supply.Stock;
import java.util.List;

public interface StockDAO extends GenericDAO<Stock, Integer> {

    Long getTotalIn(Article article, Depot depot);

    Long getTotalOut(Article article, Depot depot);

    List<ArticleStockDTO> findAvailableStockGroupedByArticle(Company company, String articleCoincidence);

    List<ArticleStockDTO> findAvailableStockGroupedByArticle(Company company, String articleCoincidence, CategoryArticle category);

    Long getTotalInByCompany(Company company);

    Long getTotalOutByCompany(Company company);

    Double getTotalValueIn(Company company);

    Double getTotalValueOut(Company company);

    List<CategoryStockDTO> getTotalStockGroupedByCategory(Company company);

    List<CategoryStockDTO> getTotalValueStockGroupedByCategory(Company company);

}
