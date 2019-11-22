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

/**
 *
 * @author j2esus
 */
@Repository
public class ProjectDAOImpl extends GenericDAOImpl<Project,Integer> implements ProjectDAO{

    @Override
    public List<Project> findByCompany(Company company) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select p ");
        sb.append(" from Project p ");
        sb.append(" join p.father u ");
        sb.append(" where u.father = :company ");
        sb.append(" and p.status != :status  ");
        Query q = sessionFactory.getCurrentSession().createQuery(sb.toString());
        q.setParameter("company", company);
        q.setParameter("status", Status.DELETED);
        return q.list();
    }

    @Override
    public List<Project> findBy(User user, String name, Status status, String description) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select p ");
        sb.append(" from Project p ");
        sb.append(" where p.father = :user ");
        sb.append(" and upper(p.name) like :name ");
        sb.append(" and upper(p.description) like :description ");
       
        if(status != null)
            sb.append(" and p.status = :status  ");
        else
            sb.append(" and p.status != :status  ");
        
        Query q = sessionFactory.getCurrentSession().createQuery(sb.toString());
        q.setParameter("user", user);
        q.setParameter("name", "%"+name.toUpperCase()+"%");
        q.setParameter("description", "%"+description.toUpperCase()+"%");
        
        if(status != null)
            q.setParameter("status", status);
        else
            q.setParameter("status", Status.DELETED);
        
        return q.list();
    }

    @Override
    public List<Project> findBy(User user, String query, Status[] status) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select p ");
        sb.append(" from Project p ");
        sb.append(" where p.father = :user ");
        sb.append(" and ( upper(p.name) like :query or upper(p.description) like :query )");
        sb.append(" and p.status in ( :status ) ");
        
        Query q = sessionFactory.getCurrentSession().createQuery(sb.toString());
        q.setParameter("user", user);
        q.setParameter("query", "%"+query.toUpperCase()+"%");
        q.setParameterList("status", status);
        return q.list();
    }

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
    public List<Project> findBy(Company company, String query, Status[] status) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select p ");
        sb.append(" from Project p ");
        sb.append(" join p.father u ");
        sb.append(" where u.father = :company ");
        sb.append(" and ( upper(p.name) like :query or upper(p.description) like :query )");
        sb.append(" and p.status in ( :status ) ");
        
        Query q = sessionFactory.getCurrentSession().createQuery(sb.toString());
        q.setParameter("company", company);
        q.setParameter("query", "%"+query.toUpperCase()+"%");
        q.setParameterList("status", status);
        return q.list();
    }

    @Override
    public List<Project> findBy(Company company, Status[] status) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select p ");
        sb.append(" from Project p ");
        sb.append(" join p.father u ");
        sb.append(" where u.father = :company ");
        sb.append(" and p.status in ( :status ) ");
        
        Query q = sessionFactory.getCurrentSession().createQuery(sb.toString());
        q.setParameter("company", company);
        q.setParameterList("status", status);
        return q.list();
    }
    
}
