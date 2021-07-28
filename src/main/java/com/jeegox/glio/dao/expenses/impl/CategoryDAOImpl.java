package com.jeegox.glio.dao.expenses.impl;

import com.jeegox.glio.dao.expenses.CategoryDAO;
import com.jeegox.glio.dao.hibernate.GenericDAOImpl;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.expenses.Category;
import com.jeegox.glio.enumerators.Status;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryDAOImpl extends GenericDAOImpl<Category, Integer> implements CategoryDAO{

    @Override
    public List<Category> findBy(Company company, Status[] estatus, String name) {
        String query = " select c "+
                " from Category c "+
                " where c.father = :company "+
                " and c.status in ( :status ) "+
                " and c.name like  :name ";
        return sessionFactory.getCurrentSession().createQuery(query)
                .setParameter("company", company)
                .setParameter("name", "%" + name + "%")
                .setParameterList("status", estatus)
                .getResultList();
    }
    
}
