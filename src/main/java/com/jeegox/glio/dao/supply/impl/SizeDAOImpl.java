package com.jeegox.glio.dao.supply.impl;

import com.jeegox.glio.dao.hibernate.GenericDAOImpl;
import com.jeegox.glio.dao.supply.SizeDAO;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.supply.CategoryArticle;
import com.jeegox.glio.entities.supply.Size;
import com.jeegox.glio.enumerators.Status;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SizeDAOImpl extends GenericDAOImpl<Size, Integer> implements SizeDAO {

    @Override
    public List<Size> findSizesBy(Company company, Status[] status) {
        String qry = " select s "
                + " from Size s "
                + " where s.father = :company "
                + " and s.status in ( :status ) ";
        Query query = sessionFactory.getCurrentSession().createQuery(qry);
        query.setParameter("company", company);
        query.setParameterList("status", status);
        return query.list();
    }

    @Override
    public List<Size> findByCompany(Company company, String nameLike) {
        String qry = " select s "
                + " from Size s "
                + " where s.father = :company "
                + " and s.status = :status  "
                + " and upper(s.name) like :name ";
        Query query = sessionFactory.getCurrentSession().createQuery(qry);
        query.setParameter("company", company);
        query.setParameter("status", Status.ACTIVE);
        query.setParameter("name", "%" + nameLike.toUpperCase()+"%");
        query.setMaxResults(10);
        return query.list();
    }
}
