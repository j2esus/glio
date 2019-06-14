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

/**
 *
 * @author j2esus
 */
@Repository
public class SessionDAOImpl extends GenericDAOImpl<Session, Integer> implements SessionDAO{
    
    @Override
    public List<Session> findByCompany(Company company) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select s ");
        sb.append(" from Session s ");
        sb.append(" join s.father u ");
        sb.append(" where u.father = :company ");
        sb.append(" and s.status != :status  ");
        Query q = sessionFactory.getCurrentSession().createQuery(sb.toString());
        q.setParameter("company", company);
        q.setParameter("status", Status.DELETED);
        return q.list();
    }

    @Override
    public List<Session> findByUser(User user) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select s ");
        sb.append(" from Session s ");
        sb.append(" where s.father = :user ");
        sb.append(" and s.status != :status  ");
        sb.append(" order by s.initDate ");
        Query q = sessionFactory.getCurrentSession().createQuery(sb.toString());
        q.setParameter("user", user);
        q.setParameter("status", Status.DELETED);
        return q.list();
    }

    @Override
    public Session findOpenSession(User user) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select s ");
        sb.append(" from Session s ");
        sb.append(" where s.father = :user ");
        sb.append(" and s.status != :status  ");
        sb.append(" and s.initDate is not null  ");
        sb.append(" and s.endDate is null  ");
        Query q = sessionFactory.getCurrentSession().createQuery(sb.toString());
        q.setParameter("user", user);
        q.setParameter("status", Status.CLOSED);
        List<Session> sessions = q.list();
        if(sessions.isEmpty())
            return null;
        return sessions.get(0);
    }

    @Override
    public Session findBySession(String session) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select s ");
        sb.append(" from Session s ");
        sb.append(" where s.session = :session ");
        Query q = sessionFactory.getCurrentSession().createQuery(sb.toString());
        q.setParameter("session", session);
        List<Session> sessions = q.list();
        if(sessions.isEmpty())
            return null;
        return sessions.get(0);
    }
}
