package com.jeegox.glio.services.admin.impl;

import com.jeegox.glio.dao.admin.CategoryMenuDAO;
import com.jeegox.glio.dto.admin.CategoryMenuDTO;
import com.jeegox.glio.entities.admin.CategoryMenu;
import com.jeegox.glio.entities.admin.OptionMenu;
import com.jeegox.glio.entities.admin.UserType;
import com.jeegox.glio.services.admin.CategoryMenuService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author j2esus
 */
@Service
public class CategoryMenuServiceImpl implements CategoryMenuService {
    @Autowired
    private CategoryMenuDAO categoryMenuDAO;

    @Transactional(readOnly = true)
    @Override
    public List<CategoryMenu> findAll() {
        return categoryMenuDAO.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public List<CategoryMenu> findBy(UserType userType) {
        return categoryMenuDAO.findBy(userType);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CategoryMenuDTO> findByDTO(UserType userType) {
        List<CategoryMenuDTO> categories = new ArrayList<>();
        Set<OptionMenu> options = userType.getOptions();
        Iterator<OptionMenu> it = options.iterator();
        
        Map<Integer, CategoryMenuDTO> mapCategory = new HashMap<>();
        
        while(it.hasNext()){
            OptionMenu op = it.next();
            CategoryMenu catMenu = op.getFather();
            
            if(!mapCategory.containsKey(catMenu.getId())){
                CategoryMenuDTO categoryMenuDTO = new CategoryMenuDTO(catMenu.getId(),catMenu.getName(), 
                        catMenu.getOrder(), catMenu.getStatus(), catMenu.getIcon());
                categoryMenuDTO.getOptionsMenus().add(op);
                mapCategory.put(catMenu.getId(), categoryMenuDTO);
            }else{
                CategoryMenuDTO categoryMenuDTO = mapCategory.get(catMenu.getId());
                categoryMenuDTO.getOptionsMenus().add(op);
            }
        }
        
        for (Map.Entry<Integer, CategoryMenuDTO> entry : mapCategory.entrySet()){
            categories.add(entry.getValue());
        }
        return categories;
    }

    @Transactional(readOnly = true)
    @Override
    public CategoryMenu findById(Integer id) {
        return categoryMenuDAO.findById(id);
    }
    
}
