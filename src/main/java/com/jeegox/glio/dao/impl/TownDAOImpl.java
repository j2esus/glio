package com.jeegox.glio.dao.impl;

import com.jeegox.glio.dao.TownDAO;
import com.jeegox.glio.dao.hibernate.GenericDAOImpl;
import com.jeegox.glio.entities.Town;
import java.util.List;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class TownDAOImpl extends GenericDAOImpl<Town, Integer> implements TownDAO{

    @Override
    public List<Town> findByState(Integer idState) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select t ");
        sb.append(" from Town t ");
        sb.append(" where t.father.id = :idState ");
        Query q = sessionFactory.getCurrentSession().createQuery(sb.toString());
        q.setParameter("idState", idState);
        return q.list();
    }
}
