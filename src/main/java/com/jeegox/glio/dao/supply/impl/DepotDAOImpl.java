package com.jeegox.glio.dao.supply.impl;

import com.jeegox.glio.dao.hibernate.GenericDAOImpl;
import com.jeegox.glio.dao.supply.DepotDAO;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.supply.Depot;
import com.jeegox.glio.enumerators.Status;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DepotDAOImpl extends GenericDAOImpl<Depot, Integer> implements DepotDAO {

    @Override
    public List<Depot> findByName(Company company, String name) {
        String query = " select depot "+
                " from Depot depot "+
                " where depot.father = :company "+
                " and depot.name like :name "+
                " and depot.status <> :status ";
        return sessionFactory.getCurrentSession().createQuery(query)
                .setParameter("company", company)
                .setParameter("name", "%"+name+"%")
                .setParameter("status", Status.DELETED)
                .getResultList();
    }

    @Override
    public List<Depot> findByNameAndStatus(Company company, String name, Status status) {
        String query = " select depot "+
                " from Depot depot "+
                " where depot.father = :company "+
                " and depot.name like :name "+
                " and depot.status = :status ";
        return sessionFactory.getCurrentSession().createQuery(query)
                .setParameter("company", company)
                .setParameter("name", "%"+name+"%")
                .setParameter("status", status)
                .getResultList();
    }
}
