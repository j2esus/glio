package com.jeegox.glio.dao.expenses.impl;

import com.jeegox.glio.dao.expenses.SubcategoryDAO;
import com.jeegox.glio.dao.hibernate.GenericDAOImpl;
import com.jeegox.glio.entities.expenses.Category;
import com.jeegox.glio.entities.expenses.Subcategory;
import com.jeegox.glio.enumerators.Status;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class SubcategoryDAOImpl extends GenericDAOImpl<Subcategory, Integer> implements SubcategoryDAO{

    @Override
    public List<Subcategory> findBy(Category category, Status[] estatus, String name) {
        String query = " select s "+
                " from Subcategory s "+
                " where s.father = :category "+
                " and s.status in ( :status ) "+
                " and s.name like  :name ";
        return sessionFactory.getCurrentSession().createQuery(query)
                .setParameter("category", category)
                .setParameter("name", "%"+name+"%")
                .setParameterList("status", estatus)
                .getResultList();
    }
    
}
