package com.jeegox.glio.services.expenses;

import com.jeegox.glio.dto.GeneralCategoryDTO;
import com.jeegox.glio.dto.MonthDTO;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.expenses.Category;
import com.jeegox.glio.entities.expenses.Expense;
import com.jeegox.glio.entities.expenses.Subcategory;
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

    List<String> yearsExpenses();

    List<MonthDTO> getMonthAmounts(Integer year);

    List<GeneralCategoryDTO> findDataCategory(Company company, Integer year);

    List<GeneralCategoryDTO> findDataCategory(Company company, Integer year, Integer month);

    void saveOrUpdate(Category category);

    Category findCategoryBy(Integer id);

    List<Category> findBy(Company company, Status[] estatus, String name);

    void changeStatus(Category category, Status status) throws Exception;

    void saveOrUpdate(Subcategory subcategory);

    Subcategory findSubcategoryBy(Integer id);

    List<Subcategory> findBy(Category category, Status[] estatus, String name);

    void changeStatus(Subcategory subcategory, Status status) throws Exception;

}
