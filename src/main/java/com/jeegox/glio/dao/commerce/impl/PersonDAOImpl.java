package com.jeegox.glio.dao.commerce.impl;

import com.jeegox.glio.dao.commerce.PersonDAO;
import com.jeegox.glio.dao.hibernate.GenericDAOImpl;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.commerce.Person;
import com.jeegox.glio.enumerators.PersonType;
import com.jeegox.glio.enumerators.Status;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PersonDAOImpl extends GenericDAOImpl<Person, Integer> implements PersonDAO {
    @Override
    public List<Person> findBy(Company company, PersonType personType, String name, String email,
                               String phone, String rfc, Status[] status) {
        String qry = " select p "+
                " from Person p "+
                " where p.personType = :personType "+
                " and p.father = :company "+
                " and p.name like :name "+
                " and p.email like :email "+
                " and p.phone like :phone "+
                " and p.rfc like :rfc "+
                " and p.status in ( :status ) ";

        Query query = sessionFactory.getCurrentSession().createQuery(qry);
        query.setParameter("company", company);
        query.setParameter("personType", personType);
        query.setParameter("name","%" + name +"%");
        query.setParameter("email","%" + email +"%");
        query.setParameter("phone","%" + phone +"%");
        query.setParameter("rfc","%" + rfc +"%");
        query.setParameterList("status", status);
        return query.list();
    }
}
