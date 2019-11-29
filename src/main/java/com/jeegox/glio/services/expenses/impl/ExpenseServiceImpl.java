package com.jeegox.glio.services.expenses.impl;

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
import com.jeegox.glio.services.expenses.ExpenseService;
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
public class ExpenseServiceImpl implements ExpenseService{
    @Autowired
    private ExpenseDAO expenseDAO;
    @Autowired
    private CategoryDAO categoryDAO;
    @Autowired
    private SubcategoryDAO subcategoryDAO;

    @Transactional
    @Override
    public void saveOrUpdate(Expense expense) {
        expenseDAO.save(expense);
    }

    @Transactional(readOnly = true)
    @Override
    public Expense findBy(Integer id) {
        return expenseDAO.findById(id);
    }

    @Transactional
    @Override
    public void changeStatus(Expense expense, Status status) throws Exception {
        expense.setStatus(status);
        expenseDAO.save(expense);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Expense> findBy(Company company, Status[] estatus, Integer idCategory, Integer idSubcategory, Date initDate, Date endDate) {
        return expenseDAO.findBy(company, estatus, idCategory, idSubcategory, initDate, endDate);
    }

    @Transactional(readOnly = true)
    @Override
    public List<GeneralCategoryDTO> findDataCategory(Company company) {
        return expenseDAO.findDataCategory(company);
    }

    @Transactional(readOnly = true)
    @Override
    public List<GeneralCategoryDTO> findDataSubcategory(Category category) {
        return expenseDAO.findDataSubcategory(category);
    }

    @Transactional(readOnly = true)
    @Override
    public List<String> yearsExpenses() {
        return expenseDAO.yearsExpenses();
    }

    @Transactional(readOnly = true)
    @Override
    public List<MonthDTO> getMonthAmounts(Integer year) {
        List<MonthDTO> monthAmounts = new ArrayList<>();
        String[] months = {"Enero","Febrero","Marzo", "Abril", "Mayo", "Junio", 
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        
        for(int i = 0; i < months.length; i++){
            monthAmounts.add(new MonthDTO(i, months[i], 0D));
        }
        for(MonthDTO item: expenseDAO.getMonthAmounts(year)){
            monthAmounts.get(item.getMonth()-1).setAmount(item.getAmount());
        }
        return monthAmounts;
    }

    @Transactional(readOnly = true)
    @Override
    public List<GeneralCategoryDTO> findDataCategory(Company company, Integer year) {
        return expenseDAO.findDataCategory(company, year);
    }

    @Transactional(readOnly = true)
    @Override
    public List<GeneralCategoryDTO> findDataCategory(Company company, Integer year, Integer month) {
        return expenseDAO.findDataCategory(company, year, month);
    }
    
    @Transactional
    @Override
    public void saveOrUpdate(Category category) {
        categoryDAO.save(category);
    }

    @Transactional(readOnly = true)
    @Override
    public Category findCategoryBy(Integer id) {
        return categoryDAO.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Category> findBy(Company company, Status[] estatus, String name) {
        return categoryDAO.findBy(company, estatus, name);
    }

    @Transactional
    @Override
    public void changeStatus(Category category, Status status) throws Exception {
        category.setStatus(status);
        this.saveOrUpdate(category);
    }
    
    @Transactional
    @Override
    public void saveOrUpdate(Subcategory subcategory) {
        subcategoryDAO.save(subcategory);
    }

    @Transactional(readOnly = true)
    @Override
    public Subcategory findSubcategoryBy(Integer id) {
        return subcategoryDAO.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Subcategory> findBy(Category category, Status[] estatus, String name) {
        return subcategoryDAO.findBy(category, estatus, name);
    }

    @Transactional
    @Override
    public void changeStatus(Subcategory subcategory, Status status) throws Exception {
        subcategory.setStatus(status);
        subcategoryDAO.save(subcategory);
    }
}
