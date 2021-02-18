package com.jeegox.glio.dao.admin.impl;

import com.jeegox.glio.dao.admin.UserDAO;
import com.jeegox.glio.dao.hibernate.GenericDAOImpl;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.admin.User;
import com.jeegox.glio.enumerators.Status;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAOImpl extends GenericDAOImpl<User, Integer> implements UserDAO {

    @Override
    public User login(String username, String password) {
        String query = "select u "+
                " from User u "+
                " where u.username = :username "+
                " and u.password = :password  "+
                " and u.status = :status ";

        return (User)sessionFactory.getCurrentSession().createQuery(query).
                setParameter("username", username).
                setParameter("password", password).
                setParameter("status", Status.ACTIVE).
                getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public User login(String token) {
        String query = "select u "+
                " from Token t "+
                " join t.father u "+
                " where t.token = :token "+
                " and u.status = :status "+
                " and t.status = :status ";

        return (User)sessionFactory.getCurrentSession().createQuery(query).setParameter("token", token).
                setParameter("status", Status.ACTIVE).
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
