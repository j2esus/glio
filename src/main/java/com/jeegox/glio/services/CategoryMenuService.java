package com.jeegox.glio.services;

import com.jeegox.glio.dao.admin.CategoryMenuDAO;
import com.jeegox.glio.dao.admin.OptionMenuDAO;
import com.jeegox.glio.dto.admin.CategoryMenuDTO;
import com.jeegox.glio.entities.admin.CategoryMenu;
import com.jeegox.glio.entities.admin.OptionMenu;
import com.jeegox.glio.entities.admin.UserType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryMenuService {
    private final CategoryMenuDAO categoryMenuDAO;
    private final OptionMenuDAO optionMenuDAO;
    
    @Autowired
    public CategoryMenuService(CategoryMenuDAO categoryMenuDAO, OptionMenuDAO optionMenuDAO){
        this.categoryMenuDAO = categoryMenuDAO;
        this.optionMenuDAO = optionMenuDAO;
    }
    
    @Transactional(readOnly = true)
    public List<CategoryMenu> findCategoryAll() {
        return categoryMenuDAO.findAll();
    }

    @Transactional(readOnly = true)
    public CategoryMenu findById(Integer id) {
        return categoryMenuDAO.findById(id);
    }
    
    @Transactional(readOnly = true)
    public List<OptionMenu> findOptionAll() {
        return optionMenuDAO.findAll();
    }
}
