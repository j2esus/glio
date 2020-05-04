package com.jeegox.glio.dao.expenses.impl;

import com.jeegox.glio.dao.expenses.SubcategoryDAO;
import com.jeegox.glio.dao.hibernate.GenericDAOImpl;
import com.jeegox.glio.entities.expenses.Category;
import com.jeegox.glio.entities.expenses.Subcategory;
import com.jeegox.glio.enumerators.Status;
import java.util.List;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class SubcategoryDAOImpl extends GenericDAOImpl<Subcategory, Integer> implements SubcategoryDAO{

    @Override
    public List<Subcategory> findBy(Category category, Status[] estatus, String name) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select s ");
        sb.append(" from Subcategory s ");
        sb.append(" where s.father = :category ");
        sb.append(" and s.status in ( :status ) ");
        sb.append(" and s.name like  :name ");
        Query q = sessionFactory.getCurrentSession().createQuery(sb.toString());
        q.setParameter("category", category);
        q.setParameter("name", "%"+name+"%");
        q.setParameterList("status", estatus);
        return q.list();
    }
    
}
