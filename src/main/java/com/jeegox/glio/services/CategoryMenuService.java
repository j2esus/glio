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

/**
 *
 * @author j2esus
 */
@Service
public class CategoryMenuService {
    @Autowired
    private CategoryMenuDAO categoryMenuDAO;
    @Autowired
    private OptionMenuDAO optionMenuDAO;
    
    @Transactional(readOnly = true)
    public List<CategoryMenu> findCategoryAll() {
        return categoryMenuDAO.findAll();
    }

    @Transactional(readOnly = true)
    public List<CategoryMenu> findBy(UserType userType) {
        return categoryMenuDAO.findBy(userType);
    }

    @Transactional(readOnly = true)
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
                        catMenu.getOrder(), catMenu.getStatus(), catMenu.getIcon(), catMenu.getClazz());
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
    public CategoryMenu findById(Integer id) {
        return categoryMenuDAO.findById(id);
    }
    
    @Transactional(readOnly = true)
    public List<OptionMenu> findOptionAll() {
        return optionMenuDAO.findAll();
    }
}
