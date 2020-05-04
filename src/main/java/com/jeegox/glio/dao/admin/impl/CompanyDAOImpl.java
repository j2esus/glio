package com.jeegox.glio.dao.admin.impl;

import com.jeegox.glio.dao.admin.CompanyDAO;
import com.jeegox.glio.dao.hibernate.GenericDAOImpl;
import com.jeegox.glio.entities.admin.Company;
import java.util.List;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class CompanyDAOImpl extends GenericDAOImpl<Company,Integer> implements CompanyDAO{
    
    @Override
    public List<Company> findByName(String name) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select c ");
        sb.append(" from Company c ");
        sb.append(" where upper(c.name) like :name ");
        Query q = sessionFactory.getCurrentSession().createQuery(sb.toString());
        q.setParameter("name", "%"+name.toUpperCase()+"%");
        return q.list();
    }

    @Override
    public Company findBy(String name) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select c ");
        sb.append(" from Company c ");
        sb.append(" where upper(c.name) = :name ");
        Query q = sessionFactory.getCurrentSession().createQuery(sb.toString());
        q.setParameter("name", name.toUpperCase());
        List<Company> companies = q.list();
        if(companies.isEmpty())
            return null;
        return companies.get(0);
    }
}
