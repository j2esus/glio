package com.jeegox.glio.services.expenses;

import com.jeegox.glio.entities.expenses.Category;
import com.jeegox.glio.entities.expenses.Subcategory;
import com.jeegox.glio.enumerators.Status;
import java.util.List;

/**
 *
 * @author j2esus
 */
public interface SubcategoryService {
    
    void saveOrUpdate(Subcategory subcategory);
    
    Subcategory findBy(Integer id);
    
    List<Subcategory> findBy(Category category, Status[] estatus, String name);
    
    void changeStatus(Subcategory subcategory, Status status) throws Exception;
    
}
