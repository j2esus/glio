package com.jeegox.glio.services.expenses;

import com.jeegox.glio.dto.GeneralCategoryDTO;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.expenses.Category;
import com.jeegox.glio.entities.expenses.Expense;
import com.jeegox.glio.enumerators.Status;
import java.util.Date;
import java.util.List;

/**
 *
 * @author j2esus
 */
public interface ExpenseService {
    
    void saveOrUpdate(Expense expense);
    
    Expense findBy(Integer id);
    
    void changeStatus(Expense expense, Status status) throws Exception;
    
    List<Expense> findBy(Company company, Status[] estatus, Integer idCategory, 
            Integer idSubcategory, Date initDate, Date endDate);
    
    List<GeneralCategoryDTO> findDataCategory(Company company);
    
    List<GeneralCategoryDTO> findDataSubcategory(Category category);
    
}
