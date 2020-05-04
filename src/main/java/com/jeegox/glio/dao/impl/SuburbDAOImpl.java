package com.jeegox.glio.dao.impl;

import com.jeegox.glio.dao.SuburbDAO;
import com.jeegox.glio.dao.hibernate.GenericDAOImpl;
import com.jeegox.glio.entities.Suburb;
import java.util.List;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class SuburbDAOImpl extends GenericDAOImpl<Suburb, Integer> implements SuburbDAO{

    @Override
    public List<Suburb> findByTown(Integer idTown) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select s ");
        sb.append(" from Suburb s ");
        sb.append(" where s.father.id = :idTown ");
        Query q = sessionFactory.getCurrentSession().createQuery(sb.toString());
        q.setParameter("idTown", idTown);
        return q.list();
    }

    @Override
    public List<Suburb> findByTown(String cp) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select s ");
        sb.append(" from Suburb s ");
        sb.append(" where s.cp = :cp ");
        Query q = sessionFactory.getCurrentSession().createQuery(sb.toString());
        q.setParameter("cp", cp);
        return q.list();
    }
    
}
