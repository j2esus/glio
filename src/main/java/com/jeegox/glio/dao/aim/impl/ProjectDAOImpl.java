package com.jeegox.glio.dao.aim.impl;

import com.jeegox.glio.dao.aim.ProjectDAO;
import com.jeegox.glio.dao.hibernate.GenericDAOImpl;
import com.jeegox.glio.dto.GraphStatusVO;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.admin.User;
import com.jeegox.glio.entities.aim.Project;
import com.jeegox.glio.enumerators.Status;
import java.util.List;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class ProjectDAOImpl extends GenericDAOImpl<Project,Integer> implements ProjectDAO{

    @Override
    public List<Project> findByCompany(Company company) {
        String query = " select p "+
                " from Project p "+
                " join p.father u "+
                " where u.father = :company "+
                " and p.status <> :status ";

        return sessionFactory.getCurrentSession().createQuery(query).setParameter("company", company).
                setParameter("status", Status.DELETED).getResultList();
    }

    @Override
    public List<Project> findByUser(User user, String value) {
        String query = " select p "+
                " from Project p "+
                " where p.father = :user "+
                " and ( upper(p.name) like :value or upper(p.description) like :value ) "+
                " and p.status <> :status ";
        
        return sessionFactory.getCurrentSession().createQuery(query).setParameter("user", user).
                setParameter("value", "%"+value.toUpperCase()+"%").
                setParameter("status", Status.DELETED).getResultList();
    }

    @Override
    public List<Project> findByUser(User user, String value, Status status) {
        String query = " select p "+
                " from Project p "+
                " where p.father = :user "+
                " and ( upper(p.name) like :value or upper(p.description) like :value )"+
                " and p.status = :status ";
        
        return sessionFactory.getCurrentSession().createQuery(query).setParameter("user", user).
                setParameter("value", "%"+value.toUpperCase()+"%").setParameter("status", status).getResultList();
    }

    //todo check if this method can be refactored by streams
    @Override
    public List<GraphStatusVO> findDataGraphProject(Integer idProject) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select new com.jeegox.glio.dto.GraphStatusVO(t.status, count(t)) ");
        sb.append(" from Task t ");
        sb.append(" join t.father a ");
        sb.append(" join a.father p ");
        sb.append(" where p.id = :idProject ");
        sb.append(" and t.status not in ( :idsStatus ) ");
        sb.append(" group by t.status ");
        
        Query q = sessionFactory.getCurrentSession().createQuery(sb.toString(), GraphStatusVO.class);
        q.setParameter("idProject", idProject);
        q.setParameterList("idsStatus",new Status[]{Status.DELETED, Status.INACTIVE});
        return q.list();
    }

    @Override
    public List<Project> findByCompany(Company company, String value, Status[] status) {
        String query = " select p "+
                " from Project p "+
                " join p.father u "+
                " where u.father = :company "+
                " and ( upper(p.name) like :value or upper(p.description) like :value )"+
                " and p.status in ( :status )";
        
        return sessionFactory.getCurrentSession().createQuery(query).setParameter("company", company).
                setParameter("value", "%"+value.toUpperCase()+"%").setParameterList("status", status).getResultList();
    }

    @Override
    public List<Project> findByCompany(Company company, Status[] status) {
        String query = " select p "+
                " from Project p "+
                " join p.father u "+
                " where u.father = :company "+
                " and p.status in ( :status )";

        return sessionFactory.getCurrentSession().createQuery(query).setParameter("company", company).
                setParameterList("status", status).getResultList();
    }
}
