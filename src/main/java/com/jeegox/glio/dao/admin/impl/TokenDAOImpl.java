package com.jeegox.glio.dao.admin.impl;

import com.jeegox.glio.dao.admin.TokenDAO;
import com.jeegox.glio.dao.hibernate.GenericDAOImpl;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.admin.Token;
import com.jeegox.glio.entities.admin.User;
import com.jeegox.glio.enumerators.Status;
import java.util.List;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author j2esus
 */
@Repository
public class TokenDAOImpl extends GenericDAOImpl<Token,Integer> implements TokenDAO{

    @Override
    public Token find(User user) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select t ");
        sb.append(" from Token t ");
        sb.append(" join t.father u ");
        sb.append(" where u.username = :username ");
        sb.append(" and t.status = :status  ");
        Query q = sessionFactory.getCurrentSession().createQuery(sb.toString());
        q.setParameter("username", user.getUsername());
        q.setParameter("status", Status.ACTIVE);
        List<Token> tokens = q.list();
        if(tokens.isEmpty())
            return null;
        else
            return tokens.get(0);
    }

    @Override
    public List<Token> findByUser(User user) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select t ");
        sb.append(" from Token t ");
        sb.append(" where t.father = :user ");
        sb.append(" and t.status != :status  ");
        Query q = sessionFactory.getCurrentSession().createQuery(sb.toString());
        q.setParameter("user", user);
        q.setParameter("status", Status.DELETED);
        return q.list();
    }

    @Override
    public Token find(Status status, String token) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select t ");
        sb.append(" from Token t ");
        sb.append(" where t.token = :token ");
        sb.append(" and t.status = :status");
        Query q = sessionFactory.getCurrentSession().createQuery(sb.toString());
        q.setParameter("token", token);
        q.setParameter("status", status);
        return (Token)q.uniqueResult();
    }

    @Override
    public List<Token> findByCompany(Company company) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select t ");
        sb.append(" from Token t ");
        sb.append(" join t.father u ");
        sb.append(" where u.father = :company ");
        sb.append(" and t.status != :status  ");
        Query q = sessionFactory.getCurrentSession().createQuery(sb.toString());
        q.setParameter("company", company);
        q.setParameter("status", Status.DELETED);
        return q.list();
    }
    
}
