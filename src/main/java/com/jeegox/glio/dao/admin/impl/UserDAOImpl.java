package com.jeegox.glio.dao.admin.impl;

import com.jeegox.glio.dao.admin.UserDAO;
import com.jeegox.glio.dao.hibernate.GenericDAOImpl;
import com.jeegox.glio.entities.admin.Company;
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
public class UserDAOImpl extends GenericDAOImpl<User, Integer> implements UserDAO {

    @Override
    public User login(String username, String password, String token) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select u ");
        sb.append(" from Token t ");
        sb.append(" right join t.father u ");
        sb.append(" where ((u.username = :username ");
        sb.append(" and u.password = :password ) ");
        sb.append(" or t.token = :token ) ");
        sb.append(" and u.status = :status ");
        Query q = sessionFactory.getCurrentSession().createQuery(sb.toString());
        q.setParameter("username", username);
        q.setParameter("password", password);
        q.setParameter("token", token);
        q.setParameter("status", Status.ACTIVE);
        List<User> users = q.list();
        if (!users.isEmpty()) {
            return users.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<User> findByCompany(Company company) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select u ");
        sb.append(" from User u ");
        sb.append(" where u.father = :father ");
        sb.append(" and u.status != :status ");
        Query q = sessionFactory.getCurrentSession().createQuery(sb.toString());
        q.setParameter("father", company);
        q.setParameter("status", Status.DELETED);
        return q.list();
    }

    @Override
    public Long count(Company company) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select count(u) ");
        sb.append(" from User u ");
        sb.append(" where u.father = :father ");
        sb.append(" and u.status != :status ");
        Query q = sessionFactory.getCurrentSession().createQuery(sb.toString());
        q.setParameter("father", company);
        q.setParameter("status", Status.DELETED);
        return (Long) q.uniqueResult();
    }

    @Override
    public User findById(String username) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select u ");
        sb.append(" from User u ");
        sb.append(" where u.username = :username ");
        sb.append(" and u.status != :status ");
        Query q = sessionFactory.getCurrentSession().createQuery(sb.toString());
        q.setParameter("username", username);
        q.setParameter("status", Status.DELETED);
        List<User> users = q.list();
        if(users.isEmpty())
            return null;
        else
            return users.get(0);
    }

    @Override
    public List<User> findByCompany(Company company, String nameLike) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select u ");
        sb.append(" from User u ");
        sb.append(" where u.father = :father ");
        sb.append(" and u.status = :status ");
        sb.append(" and upper(u.name) like :name ");
        Query q = sessionFactory.getCurrentSession().createQuery(sb.toString());
        q.setParameter("father", company);
        q.setParameter("status", Status.ACTIVE);
        q.setParameter("name", "%"+nameLike.toUpperCase()+"%");
        q.setMaxResults(10);
        return q.list();
    }
    
}
