package com.jeegox.glio.dao.expenses;

import com.jeegox.glio.dao.hibernate.GenericDAO;
import com.jeegox.glio.entities.expenses.Category;
import com.jeegox.glio.entities.expenses.Subcategory;
import com.jeegox.glio.enumerators.Status;
import java.util.List;

public interface SubcategoryDAO extends GenericDAO<Subcategory, Integer>{
    
    List<Subcategory> findBy(Category category, Status[] estatus, String name);
    
}
