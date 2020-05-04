package com.jeegox.glio.dao.admin.impl;

import com.jeegox.glio.dao.admin.CategoryMenuDAO;
import com.jeegox.glio.dao.hibernate.GenericDAOImpl;
import com.jeegox.glio.entities.admin.CategoryMenu;
import com.jeegox.glio.entities.admin.UserType;
import java.util.List;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryMenuDAOImpl extends GenericDAOImpl<CategoryMenu,Integer> implements CategoryMenuDAO {

    @Override
    public List<CategoryMenu> findBy(UserType userType) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select distinct(c) ");
        sb.append(" from UserType u ");
        sb.append(" join u.options o ");
        sb.append(" join o.father c ");
        sb.append(" where u = :userType ");
        sb.append(" order by c.order ");
        Query q = sessionFactory.getCurrentSession().createQuery(sb.toString());
        q.setParameter("userType", userType);
        return q.list();
    }
}
