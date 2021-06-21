package com.jeegox.glio.dao.admin.impl;

import com.jeegox.glio.dao.admin.UserTypeDAO;
import com.jeegox.glio.dao.hibernate.GenericDAOImpl;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.admin.UserType;
import com.jeegox.glio.enumerators.Status;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class UserTypeDAOImpl extends GenericDAOImpl<UserType,Integer> implements UserTypeDAO{

    @Override
    public List<UserType> findByCompany(Company company) {
        String query = " select u "+
                " from UserType u "+
                " where u.father = :father "+
                " and u.status <> :status ";

        return sessionFactory.getCurrentSession().createQuery(query).setParameter("father", company).
                setParameter("status", Status.DELETED).getResultList();
    }
}
