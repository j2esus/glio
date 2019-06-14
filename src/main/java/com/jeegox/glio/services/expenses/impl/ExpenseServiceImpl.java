package com.jeegox.glio.services.expenses.impl;

import com.jeegox.glio.dao.expenses.ExpenseDAO;
import com.jeegox.glio.dto.GeneralCategoryDTO;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.expenses.Category;
import com.jeegox.glio.entities.expenses.Expense;
import com.jeegox.glio.enumerators.Status;
import com.jeegox.glio.services.expenses.ExpenseService;
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
    
}
