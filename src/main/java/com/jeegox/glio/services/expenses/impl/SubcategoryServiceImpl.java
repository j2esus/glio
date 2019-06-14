package com.jeegox.glio.services.expenses.impl;

import com.jeegox.glio.dao.expenses.SubcategoryDAO;
import com.jeegox.glio.entities.expenses.Category;
import com.jeegox.glio.entities.expenses.Subcategory;
import com.jeegox.glio.enumerators.Status;
import com.jeegox.glio.services.expenses.SubcategoryService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author j2esus
 */
@Service
public class SubcategoryServiceImpl implements SubcategoryService{
    @Autowired
    private SubcategoryDAO subcategoryDAO;

    @Transactional
    @Override
    public void saveOrUpdate(Subcategory subcategory) {
        subcategoryDAO.save(subcategory);
    }

    @Transactional(readOnly = true)
    @Override
    public Subcategory findBy(Integer id) {
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
