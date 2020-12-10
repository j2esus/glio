package com.jeegox.glio.dao.supply;

import com.jeegox.glio.dao.hibernate.GenericDAO;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.supply.Article;
import java.util.List;

public interface ArticleDAO extends GenericDAO<Article, Integer>{
    
    List<Article> findByCompany(Company company);

    Article findBySku(Company company, String sku);
}
