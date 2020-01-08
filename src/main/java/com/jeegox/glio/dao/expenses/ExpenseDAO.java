package com.jeegox.glio.dao.expenses;

import com.jeegox.glio.dao.hibernate.GenericDAO;
import com.jeegox.glio.dto.GeneralCategoryDTO;
import com.jeegox.glio.dto.MonthDTO;
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
public interface ExpenseDAO extends GenericDAO<Expense, Integer>{
    
    List<Expense> findBy(Company company, Status[] estatus, Integer idCategory, 
            Integer idSubcategory, Date initDate, Date endDate);
    
    List<GeneralCategoryDTO> findDataCategory(Company company);
    
    List<GeneralCategoryDTO> findDataSubcategory(Category category);
    
    List<String> yearsExpenses(Company company);
    
    List<MonthDTO> getMonthAmounts(Company company, Integer year);
    
    List<GeneralCategoryDTO> findDataCategory(Company company, Integer year);
    
    List<GeneralCategoryDTO> findDataCategory(Company company, Integer year, Integer month);
}
