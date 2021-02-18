package com.jeegox.glio.dao.expenses.impl;

import com.jeegox.glio.dao.expenses.CategoryDAO;
import com.jeegox.glio.dao.hibernate.GenericDAOImpl;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.expenses.Category;
import com.jeegox.glio.enumerators.Status;
import java.util.List;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryDAOImpl extends GenericDAOImpl<Category, Integer> implements CategoryDAO{

    @Override
    public List<Category> findBy(Company company, Status[] estatus, String name) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select c ");
        sb.append(" from Category c ");
        sb.append(" where c.father = :company ");
        sb.append(" and c.status in ( :status ) ");
        sb.append(" and c.name like  :name ");
        Query q = sessionFactory.getCurrentSession().createQuery(sb.toString());
        q.setParameter("company", company);
        q.setParameter("name", "%"+name+"%");
        q.setParameterList("status", estatus);
        return q.list();
    }
    
}
