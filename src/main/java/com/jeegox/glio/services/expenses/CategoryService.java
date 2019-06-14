package com.jeegox.glio.services.expenses;

import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.expenses.Category;
import com.jeegox.glio.enumerators.Status;
import java.util.List;

/**
 *
 * @author j2esus
 */
public interface CategoryService {
    
    void saveOrUpdate(Category category);
    
    Category findBy(Integer id);
    
    List<Category> findBy(Company company, Status[] estatus, String name);
    
    void changeStatus(Category category, Status status) throws Exception;
    
}
