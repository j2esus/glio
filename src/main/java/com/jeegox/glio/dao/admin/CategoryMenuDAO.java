package com.jeegox.glio.dao.admin;

import com.jeegox.glio.dao.hibernate.GenericDAO;
import com.jeegox.glio.entities.admin.CategoryMenu;
import com.jeegox.glio.entities.admin.UserType;
import java.util.List;

public interface CategoryMenuDAO extends GenericDAO<CategoryMenu, Integer>{
    List<CategoryMenu> findBy(UserType userType);
}
