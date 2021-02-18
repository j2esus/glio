package com.jeegox.glio.dao.admin.impl;

import com.jeegox.glio.dao.admin.CategoryMenuDAO;
import com.jeegox.glio.dao.hibernate.GenericDAOImpl;
import com.jeegox.glio.entities.admin.CategoryMenu;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryMenuDAOImpl extends GenericDAOImpl<CategoryMenu,Integer> implements CategoryMenuDAO {

}
