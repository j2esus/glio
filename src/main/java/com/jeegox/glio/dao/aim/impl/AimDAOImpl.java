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

/**
 *
 * @author j2esus
 */
@Repository
public class AimDAOImpl extends GenericDAOImpl<Aim,Integer> implements AimDAO{

    @Override
    public List<Aim> findByCompany(Company company) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select a ");
        sb.append(" from Aim a ");
        sb.append(" join a.father p ");
        sb.append(" join p.father u ");
        sb.append(" where u.father = :company ");
        sb.append(" and a.status != :status  ");
        Query q = sessionFactory.getCurrentSession().createQuery(sb.toString());
        q.setParameter("company", company);
        q.setParameter("status", Status.DELETED);
        return q.list();
    }

    @Override
    public List<Aim> findBy(Project project) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select a ");
        sb.append(" from Aim a ");
        sb.append(" where a.father = :project ");
        sb.append(" and a.status != :status  ");
        Query q = sessionFactory.getCurrentSession().createQuery(sb.toString());
        q.setParameter("project", project);
        q.setParameter("status", Status.DELETED);
        return q.list();
    }

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
    public List<Aim> findBy(Project project, Status[] status) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select a ");
        sb.append(" from Aim a ");
        sb.append(" join a.father p ");
        sb.append(" where p= :project ");
        sb.append(" and a.status not in ( :status )  ");
        
        Query q = sessionFactory.getCurrentSession().createQuery(sb.toString());
        q.setParameter("project", project);
        q.setParameterList("status", status);
        return q.list();
    }
    
}
