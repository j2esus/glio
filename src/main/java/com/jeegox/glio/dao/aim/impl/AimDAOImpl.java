package com.jeegox.glio.dao.aim.impl;

import com.jeegox.glio.dao.aim.AimDAO;
import com.jeegox.glio.dao.hibernate.GenericDAOImpl;
import com.jeegox.glio.dto.GraphStatusVO;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.aim.Aim;
import com.jeegox.glio.entities.aim.Project;
import com.jeegox.glio.enumerators.Status;
import java.util.List;
import org.hibernate.query.Query;
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

    //todo check this method because it might to be changed with lambdas functions
    @Override
    public List<GraphStatusVO> findDataGraphAim(Integer idAim) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select new com.jeegox.glio.dto.GraphStatusVO(t.status, count(t) ) ");
        sb.append(" from Task t ");
        sb.append(" join t.father a ");
        sb.append(" where a.id = :idAim ");
        sb.append(" and a.status not in ( :idsStatus ) and t.status not in ( :idsStatus ) ");
        sb.append(" group by t.status ");
        
        Query q = sessionFactory.getCurrentSession().createQuery(sb.toString(), GraphStatusVO.class);
        q.setParameter("idAim", idAim);
        q.setParameterList("idsStatus",new Status[]{Status.DELETED, Status.INACTIVE});
        return q.list();
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
