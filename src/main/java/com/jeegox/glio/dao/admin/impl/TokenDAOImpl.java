package com.jeegox.glio.dao.admin.impl;

import com.jeegox.glio.dao.admin.TokenDAO;
import com.jeegox.glio.dao.hibernate.GenericDAOImpl;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.admin.Token;
import com.jeegox.glio.entities.admin.User;
import com.jeegox.glio.enumerators.Status;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class TokenDAOImpl extends GenericDAOImpl<Token,Integer> implements TokenDAO{

    @Override
    public Token getActive(User user) {
        String query = " select t "+
                " from Token t "+
                " where t.father = :user "+
                " and t.status = :status ";

        return (Token)sessionFactory.getCurrentSession().createQuery(query).setParameter("user", user).
                setParameter("status", Status.ACTIVE).getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public List<Token> findByUser(User user) {
        String query = " select t "+
                " from Token t "+
                " where t.father = :user "+
                " and t.status <> :status ";

        return sessionFactory.getCurrentSession().createQuery(query).setParameter("user", user).
                setParameter("status", Status.DELETED).getResultList();
    }

    @Override
    public Token find(Status status, String token) {
        String query = " select t "+
                " from Token t "+
                " where t.token = :token "+
                " and t.status = :status ";
        return (Token)sessionFactory.getCurrentSession().createQuery(query).setParameter("token", token).
                setParameter("status", status).getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public List<Token> findByCompany(Company company) {
        String query = " select t "+
                " from Token t "+
                " join t.father u "+
                " where u.father = :company "+
                " and t.status <> :status ";

        return sessionFactory.getCurrentSession().createQuery(query).setParameter("company", company).
                setParameter("status", Status.DELETED).getResultList();
    }
    
}
