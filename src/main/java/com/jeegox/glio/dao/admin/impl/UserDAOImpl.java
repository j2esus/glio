package com.jeegox.glio.dao.admin.impl;

import com.jeegox.glio.dao.admin.UserDAO;
import com.jeegox.glio.dao.hibernate.GenericDAOImpl;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.admin.User;
import com.jeegox.glio.enumerators.Status;
import java.util.List;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAOImpl extends GenericDAOImpl<User, Integer> implements UserDAO {

    //todo check this method because it might be divided in two different methods
    @Override
    public User login(String username, String password, String token) {
        String query = "select u "+
                " from Token t "+
                " right join t.father u "+
                " where ((u.username = :username "+
                " and u.password = :password ) "+
                " or t.token = :token ) "+
                " and u.status = :status ";

        return (User)sessionFactory.getCurrentSession().createQuery(query).setParameter("username", username).
                setParameter("password", password).setParameter("token", token).setParameter("status", Status.ACTIVE).
                getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public List<User> findByCompany(Company company) {
        String query = "select u "+
                " from User u "+
                " where u.father = :father "+
                " and u.status <> :status ";

        return sessionFactory.getCurrentSession().createQuery(query).setParameter("father", company).
                setParameter("status", Status.DELETED).getResultList();
    }

    @Override
    public Long count(Company company) {
        String query = "select count(u) "+
                " from User u "+
                " where u.father = :father "+
                " and u.status <> :status ";

        return (Long)sessionFactory.getCurrentSession().createQuery(query).setParameter("father", company).
                setParameter("status", Status.DELETED).getResultList().stream().findFirst().orElse(0);
    }

    @Override
    public User findByUsername(String username) {
        String query = "select u "+
                " from User u "+
                " where u.username = :username "+
                " and u.status <> :status ";

        return (User)sessionFactory.getCurrentSession().createQuery(query).setParameter("username", username).
                setParameter("status", Status.DELETED).getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public List<User> findByCompany(Company company, String nameLike) {
        String query = " select u "+
                " from User u "+
                " where u.father = :father "+
                " and u.status = :status "+
                " and upper(u.name) like :name ";
        return sessionFactory.getCurrentSession().createQuery(query).setParameter("father", company)
                .setParameter("status", Status.ACTIVE).setParameter("name", "%"+nameLike.toUpperCase()+"%").setMaxResults(10).getResultList();
    }
    
}
