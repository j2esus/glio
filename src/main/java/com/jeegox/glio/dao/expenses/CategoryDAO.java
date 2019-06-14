package com.jeegox.glio.dao.expenses;

import com.jeegox.glio.dao.hibernate.GenericDAO;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.expenses.Category;
import com.jeegox.glio.enumerators.Status;
import java.util.List;

/**
 *
 * @author j2esus
 */
public interface CategoryDAO extends GenericDAO<Category, Integer>{
    
    List<Category> findBy(Company company, Status[] estatus, String name);
    
}
