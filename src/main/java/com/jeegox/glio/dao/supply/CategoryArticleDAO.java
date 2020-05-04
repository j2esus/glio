package com.jeegox.glio.dao.supply;

import com.jeegox.glio.dao.hibernate.GenericDAO;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.supply.CategoryArticle;
import com.jeegox.glio.enumerators.Status;
import java.util.List;

public interface CategoryArticleDAO extends GenericDAO<CategoryArticle, Integer>{
    
    List<CategoryArticle> findCategoriesArticlesBy(Company company, Status[] status);
    
    List<CategoryArticle> findByCompany(Company company, String nameLike);
}
