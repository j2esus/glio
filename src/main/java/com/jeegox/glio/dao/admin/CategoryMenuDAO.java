package com.jeegox.glio.dao.admin;

import com.jeegox.glio.dao.hibernate.GenericDAO;
import com.jeegox.glio.entities.admin.CategoryMenu;
import com.jeegox.glio.entities.admin.UserType;
import java.util.List;

/**
 *
 * @author j2esus
 */
public interface CategoryMenuDAO extends GenericDAO<CategoryMenu, Integer>{
    List<CategoryMenu> findBy(UserType userType);
}
