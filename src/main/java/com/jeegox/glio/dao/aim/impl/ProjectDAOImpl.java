package com.jeegox.glio.dao.aim.impl;

import com.jeegox.glio.dao.aim.ProjectDAO;
import com.jeegox.glio.dao.hibernate.GenericDAOImpl;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.admin.User;
import com.jeegox.glio.entities.aim.Project;
import com.jeegox.glio.enumerators.Status;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class ProjectDAOImpl extends GenericDAOImpl<Project,Integer> implements ProjectDAO{

    @Override
    public List<Project> findByCompany(Company company) {
        String query = " select p "+
                " from Project p "+
                " join p.father u "+
                " where u.father = :company "+
                " and p.status <> :status ";

        return sessionFactory.getCurrentSession().createQuery(query).setParameter("company", company).
                setParameter("status", Status.DELETED).getResultList();
    }

    @Override
    public List<Project> findByUser(User user, String value) {
        String query = " select p "+
                " from Project p "+
                " where p.father = :user "+
                " and ( upper(p.name) like :value or upper(p.description) like :value ) "+
                " and p.status <> :status ";
        
        return sessionFactory.getCurrentSession().createQuery(query).setParameter("user", user).
                setParameter("value", "%"+value.toUpperCase()+"%").
                setParameter("status", Status.DELETED).getResultList();
    }

    @Override
    public List<Project> findByUser(User user, String value, Status status) {
        String query = " select p "+
                " from Project p "+
                " where p.father = :user "+
                " and ( upper(p.name) like :value or upper(p.description) like :value )"+
                " and p.status = :status ";
        
        return sessionFactory.getCurrentSession().createQuery(query).setParameter("user", user).
                setParameter("value", "%"+value.toUpperCase()+"%").setParameter("status", status).getResultList();
    }

    @Override
    public List<Project> findByCompany(Company company, String value, Status[] status) {
        String query = " select p "+
                " from Project p "+
                " join p.father u "+
                " where u.father = :company "+
                " and ( upper(p.name) like :value or upper(p.description) like :value )"+
                " and p.status in ( :status )";
        
        return sessionFactory.getCurrentSession().createQuery(query).setParameter("company", company).
                setParameter("value", "%"+value.toUpperCase()+"%").setParameterList("status", status).getResultList();
    }

    @Override
    public List<Project> findByCompany(Company company, Status[] status) {
        String query = " select p "+
                " from Project p "+
                " join p.father u "+
                " where u.father = :company "+
                " and p.status in ( :status )";

        return sessionFactory.getCurrentSession().createQuery(query).setParameter("company", company).
                setParameterList("status", status).getResultList();
    }

    @Override
    public Long countActiveByUserOwner(User user) {
        String query = " select count( distinct p) "
                + " from Task t "
                + " join t.father a "
                + " join a.father p "
                + " where t.userOwner = :user "
                + " and t.status in ( :status )"
                + " and a.status = :activeStatus "
                + " and p.status = :activeStatus";

        return (Long) sessionFactory.getCurrentSession().createQuery(query).
                setParameter("user", user).
                setParameterList("status", new Status[]{
                    Status.PENDING,
                    Status.IN_PROCESS,
                    Status.PAUSED,
                    Status.FINISHED}).
                setParameter("activeStatus", Status.ACTIVE).
                getResultList().stream().
                findFirst().orElse(0);
    }
}
