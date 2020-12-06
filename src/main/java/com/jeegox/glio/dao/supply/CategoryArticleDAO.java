package com.jeegox.glio.dao.supply;

import com.jeegox.glio.dao.hibernate.GenericDAO;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.supply.CategoryArticle;
import java.util.List;

public interface CategoryArticleDAO extends GenericDAO<CategoryArticle, Integer>{
    
    List<CategoryArticle> findByCompany(Company company);
    
    List<CategoryArticle> findByCompany(Company company, String nameLike);
}
