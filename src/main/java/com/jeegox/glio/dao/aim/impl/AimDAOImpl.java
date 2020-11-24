package com.jeegox.glio.dao.aim.impl;

import com.jeegox.glio.dao.aim.AimDAO;
import com.jeegox.glio.dao.hibernate.GenericDAOImpl;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.aim.Aim;
import com.jeegox.glio.entities.aim.Project;
import com.jeegox.glio.enumerators.Status;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class AimDAOImpl extends GenericDAOImpl<Aim,Integer> implements AimDAO{

    @Override
    public List<Aim> findByCompany(Company company) {
        String query = " select a "+
                " from Aim a "+
                " join a.father p "+
                " join p.father u "+
                " where u.father = :company "+
                " and a.status <> :status ";

        return sessionFactory.getCurrentSession().createQuery(query).setParameter("company", company).
                setParameter("status", Status.DELETED).getResultList();
    }

    @Override
    public List<Aim> findByProject(Project project) {
        String query = " select a "+
                " from Aim a "+
                " where a.father = :project "+
                " and a.status <> :status ";

        return sessionFactory.getCurrentSession().createQuery(query).setParameter("project", project).
                setParameter("status", Status.DELETED).getResultList();
    }

    @Override
    public List<Aim> findByProject(Project project, Status[] status) {
        String query = " select a "+
                " from Aim a "+
                " where a.father = :project "+
                " and a.status in ( :status ) ";
        
        return sessionFactory.getCurrentSession().createQuery(query).setParameter("project", project).
                setParameterList("status", status).getResultList();
    }
    
}
