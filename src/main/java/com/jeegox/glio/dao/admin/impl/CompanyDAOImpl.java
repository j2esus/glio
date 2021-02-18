package com.jeegox.glio.dao.admin.impl;

import com.jeegox.glio.dao.admin.CompanyDAO;
import com.jeegox.glio.dao.hibernate.GenericDAOImpl;
import com.jeegox.glio.entities.admin.Company;
import org.springframework.stereotype.Repository;

@Repository
public class CompanyDAOImpl extends GenericDAOImpl<Company,Integer> implements CompanyDAO{

    @Override
    public Company findByName(String name) {
        String qry = " select c "+
                " from Company c "+
                " where upper(c.name) = :name ";
        return (Company)sessionFactory.getCurrentSession().createQuery(qry).setParameter("name", name.toUpperCase()).
                getResultList().stream().findFirst().orElse(null);
    }
}
