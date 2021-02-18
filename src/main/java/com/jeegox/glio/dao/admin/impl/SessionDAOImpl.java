package com.jeegox.glio.dao.admin.impl;

import com.jeegox.glio.dao.admin.SessionDAO;
import com.jeegox.glio.dao.hibernate.GenericDAOImpl;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.admin.Session;
import com.jeegox.glio.entities.admin.User;
import com.jeegox.glio.enumerators.Status;
import java.util.List;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class SessionDAOImpl extends GenericDAOImpl<Session, Integer> implements SessionDAO{
    
    @Override
    public List<Session> findByCompany(Company company) {
        String query = " select s " +
                " from Session s "+
                " join s.father u "+
                " where u.father = :company "+
                " and s.status <> :status ";

        return sessionFactory.getCurrentSession().createQuery(query).setParameter("company", company).
                setParameter("status", Status.DELETED).getResultList();
    }

    @Override
    public List<Session> findByUser(User user) {
        String query = " select s "+
                " from Session s "+
                " where s.father = :user "+
                " and s.status <> :status "+
                " order by s.initDate desc ";
        return sessionFactory.getCurrentSession().createQuery(query).setParameter("user", user).
                setParameter("status", Status.DELETED).getResultList();
    }

    @Override
    public Session findOpenSession(User user) {
        String query = " select s "+
                " from Session s "+
                " where s.father = :user "+
                " and s.status <> :status "+
                " and s.initDate is not null "+
                " and s.endDate is null ";

        return (Session)sessionFactory.getCurrentSession().createQuery(query).setParameter("user", user).
                setParameter("status", Status.CLOSED).getResultList().stream().findFirst().orElse(null);

    }
}
