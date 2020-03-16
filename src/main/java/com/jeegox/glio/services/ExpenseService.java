package com.jeegox.glio.services;

import com.jeegox.glio.dao.expenses.CategoryDAO;
import com.jeegox.glio.dao.expenses.ExpenseDAO;
import com.jeegox.glio.dao.expenses.SubcategoryDAO;
import com.jeegox.glio.dto.GeneralCategoryDTO;
import com.jeegox.glio.dto.MonthDTO;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.expenses.Category;
import com.jeegox.glio.entities.expenses.Expense;
import com.jeegox.glio.entities.expenses.Subcategory;
import com.jeegox.glio.enumerators.Status;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author j2esus
 */
@Service
public class ExpenseService {

    @Autowired
    private ExpenseDAO expenseDAO;
    @Autowired
    private CategoryDAO categoryDAO;
    @Autowired
    private SubcategoryDAO subcategoryDAO;

    @Transactional
    public void saveOrUpdate(Expense expense) {
        expenseDAO.save(expense);
    }

    @Transactional(readOnly = true)
    public Expense findBy(Integer id) {
        return expenseDAO.findById(id);
    }

    @Transactional
    public void changeStatus(Expense expense, Status status) throws Exception {
        expense.setStatus(status);
        expenseDAO.save(expense);
    }

    @Transactional(readOnly = true)
    public List<Expense> findBy(Company company, Status[] estatus, Integer idCategory, Integer idSubcategory, 
            Date initDate, Date endDate, String description) {
        return expenseDAO.findBy(company, estatus, idCategory, idSubcategory, initDate, endDate, description);
    }

    @Transactional(readOnly = true)
    public List<GeneralCategoryDTO> findDataCategory(Company company) {
        return expenseDAO.findDataCategory(company);
    }

    @Transactional(readOnly = true)
    public List<GeneralCategoryDTO> findDataSubcategory(Category category) {
        return expenseDAO.findDataSubcategory(category);
    }

    @Transactional(readOnly = true)
    public List<String> yearsExpenses(Company company) {
        return expenseDAO.yearsExpenses(company);
    }

    @Transactional(readOnly = true)
    public List<MonthDTO> getMonthAmounts(Company company, Integer year) {
        return expenseDAO.getMonthAmounts(company, year);
    }

    @Transactional(readOnly = true)
    public List<GeneralCategoryDTO> findDataCategory(Company company, Integer year) {
        return expenseDAO.findDataCategory(company, year);
    }

    @Transactional(readOnly = true)
    public List<GeneralCategoryDTO> findDataCategory(Company company, Integer year, Integer month) {
        return expenseDAO.findDataCategory(company, year, month);
    }

    @Transactional
    public void saveOrUpdate(Category category) {
        categoryDAO.save(category);
    }

    @Transactional(readOnly = true)
    public Category findCategoryBy(Integer id) {
        return categoryDAO.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Category> findBy(Company company, Status[] estatus, String name) {
        return categoryDAO.findBy(company, estatus, name);
    }

    @Transactional
    public void changeStatus(Category category, Status status) throws Exception {
        category.setStatus(status);
        this.saveOrUpdate(category);
    }

    @Transactional
    public void saveOrUpdate(Subcategory subcategory) {
        subcategoryDAO.save(subcategory);
    }

    @Transactional(readOnly = true)
    public Subcategory findSubcategoryBy(Integer id) {
        return subcategoryDAO.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Subcategory> findBy(Category category, Status[] estatus, String name) {
        return subcategoryDAO.findBy(category, estatus, name);
    }

    @Transactional
    public void changeStatus(Subcategory subcategory, Status status) throws Exception {
        subcategory.setStatus(status);
        subcategoryDAO.save(subcategory);
    }

}
