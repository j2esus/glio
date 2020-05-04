package com.jeegox.glio.dao.supply.impl;

import com.jeegox.glio.dao.hibernate.GenericDAOImpl;
import com.jeegox.glio.dao.supply.CategoryArticleDAO;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.supply.CategoryArticle;
import com.jeegox.glio.enumerators.Status;
import java.util.List;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryArticleDAOImpl extends GenericDAOImpl<CategoryArticle, Integer> implements CategoryArticleDAO {

    @Override
    public List<CategoryArticle> findCategoriesArticlesBy(Company company, Status[] status) {
        String qry = " select ca "
                + " from CategoryArticle ca "
                + " where ca.father = :company "
                + " and ca.status in ( :status ) ";
        Query query = sessionFactory.getCurrentSession().createQuery(qry);
        query.setParameter("company", company);
        query.setParameterList("status", status);

        return query.list();
    }

    @Override
    public List<CategoryArticle> findByCompany(Company company, String nameLike) {
        String qry = " select ca "
                + " from CategoryArticle ca "
                + " where ca.father = :company "
                + " and ca.status = :status  "
                + " and upper(ca.name) like :name ";
        Query query = sessionFactory.getCurrentSession().createQuery(qry);
        query.setParameter("company", company);
        query.setParameter("status", Status.ACTIVE);
        query.setParameter("name", "%" + nameLike.toUpperCase()+"%");
        query.setMaxResults(10);
        return query.list();
    }

}
