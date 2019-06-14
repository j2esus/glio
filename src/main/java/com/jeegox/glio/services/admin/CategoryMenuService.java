package com.jeegox.glio.services.admin;

import com.jeegox.glio.dto.admin.CategoryMenuDTO;
import com.jeegox.glio.entities.admin.CategoryMenu;
import com.jeegox.glio.entities.admin.UserType;
import java.util.List;

/**
 *
 * @author j2esus
 */
public interface CategoryMenuService {
    
    List<CategoryMenu> findAll();
    
    List<CategoryMenu> findBy(UserType userType);
    
    List<CategoryMenuDTO> findByDTO(UserType userType);
    
    CategoryMenu findById(Integer id);
}
