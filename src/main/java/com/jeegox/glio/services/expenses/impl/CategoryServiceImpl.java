package com.jeegox.glio.services.expenses.impl;

import com.jeegox.glio.dao.expenses.CategoryDAO;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.expenses.Category;
import com.jeegox.glio.enumerators.Status;
import com.jeegox.glio.services.expenses.CategoryService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author j2esus
 */
@Service
public class CategoryServiceImpl implements CategoryService{
    @Autowired
    private CategoryDAO categoryDAO;

    @Transactional
    @Override
    public void saveOrUpdate(Category category) {
        categoryDAO.save(category);
    }

    @Transactional(readOnly = true)
    @Override
    public Category findBy(Integer id) {
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
    
}
