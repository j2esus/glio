package com.jeegox.glio.dao.commerce.impl;

import com.jeegox.glio.dao.commerce.AddressDAO;
import com.jeegox.glio.dao.hibernate.GenericDAOImpl;
import com.jeegox.glio.entities.commerce.Address;
import com.jeegox.glio.entities.commerce.Person;
import com.jeegox.glio.enumerators.Status;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AddressDAOImpl extends GenericDAOImpl<Address, Integer> implements AddressDAO {

    @Override
    public List<Address> findBy(Person person, Status[] status) {
        String qry = " select a "+
                " from Address a "+
                " where a.father = :person "+
                " and a.status in ( :status ) ";
        Query query = sessionFactory.getCurrentSession().createQuery(qry);
        query.setParameter("person", person);
        query.setParameterList("status", status);
        return query.list();
    }

    @Override
    public List<Address> findBy(Person person, Boolean defaultt) {
        String qry = " select a "+
                " from Address a "+
                " where a.father = :person "+
                " and a.defaultt = :defaultt ";
        Query query = sessionFactory.getCurrentSession().createQuery(qry);
        query.setParameter("person", person);
        query.setParameter("defaultt", defaultt);
        return query.list();
    }
}
