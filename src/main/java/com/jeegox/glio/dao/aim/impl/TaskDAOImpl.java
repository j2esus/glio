package com.jeegox.glio.dao.aim.impl;

import com.jeegox.glio.dao.aim.TaskDAO;
import com.jeegox.glio.dao.hibernate.GenericDAOImpl;
import com.jeegox.glio.dto.TaskDTO;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.admin.User;
import com.jeegox.glio.entities.aim.Aim;
import com.jeegox.glio.entities.aim.Project;
import com.jeegox.glio.entities.aim.Task;
import com.jeegox.glio.enumerators.Priority;
import com.jeegox.glio.enumerators.Status;
import com.jeegox.glio.util.Util;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class TaskDAOImpl extends GenericDAOImpl<Task,Integer> implements TaskDAO{

    @Override
    public List<Task> findByCompany(Company company) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select t ");
        sb.append(" from Task t ");
        sb.append(" join t.father a ");
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
    public List<Task> findBy(Aim aim) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select t ");
        sb.append(" from Task t ");
        sb.append(" join t.father a ");
        sb.append(" where t.father = :aim ");
        sb.append(" and t.status != :status  ");
        Query q = sessionFactory.getCurrentSession().createQuery(sb.toString());
        q.setParameter("aim", aim);
        q.setParameter("status", Status.DELETED);
        return q.list();
    }

    @Override
    public List<Task> findByProject(Project project) {
        String query = " select t "+
                " from Task t "+
                " join t.father a "+
                " where a.father = :project "+
                " and t.status <> :status ";

        return sessionFactory.getCurrentSession().createQuery(query).
                setParameter("project", project).
                setParameter("status", Status.DELETED).getResultList();
    }

    @Override
    public List<Task> findBy(User user, Status[] status, String query, Priority[] priorities) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select t ");
        sb.append(" from Task t ");
        sb.append(" where t.userOwner = :user ");
        sb.append(" and t.status in ( :status  ) ");
        sb.append(" and ( upper(t.name) like :query or upper(t.description) like :query )");
        sb.append(" and t.priority in ( :priorities )");
        Query q = sessionFactory.getCurrentSession().createQuery(sb.toString());
        q.setParameter("user", user);
        q.setParameterList("status", status);
        q.setParameter("query","%"+ query.toUpperCase()+"%");
        q.setParameterList("priorities", priorities);
        return q.list();
    }

    @Override
    public List<Task> findBy(Aim aim, Status[] status) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select t ");
        sb.append(" from Task t ");
        sb.append(" join t.father a ");
        sb.append(" where a = :aim ");
        sb.append(" and t.status not in ( :status  ) ");
        
        Query q = sessionFactory.getCurrentSession().createQuery(sb.toString());
        q.setParameter("aim", aim);
        q.setParameterList("status", status);
        
        return q.list();
    }

    @Override
    public Long count(User user, Status[] status) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select count(t) ");
        sb.append(" from Task t ");
        sb.append(" where t.userOwner = :user ");
        sb.append(" and t.status in ( :status  ) ");
        
        Query q = sessionFactory.getCurrentSession().createQuery(sb.toString());
        q.setParameter("user", user);
        q.setParameterList("status", status);
        
        List<Long> counts = q.list();
        if(counts.isEmpty())
            return 0L;
        else
            return counts.get(0);
    }

    @Override
    public Long count(User user, Status[] status, String query, Priority[] priorities) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select count(t) ");
        sb.append(" from Task t ");
        sb.append(" where t.userOwner = :user ");
        sb.append(" and t.status in ( :status  ) ");
        sb.append(" and ( upper(t.name) like :query or upper(t.description) like :query )");
        sb.append(" and t.priority in ( :priorities )");
        Query q = sessionFactory.getCurrentSession().createQuery(sb.toString());
        q.setParameter("user", user);
        q.setParameterList("status", status);
        q.setParameter("query","%"+ query.toUpperCase()+"%");
        q.setParameterList("priorities", priorities);
        List<Long> counts = q.list();
        if(counts.isEmpty())
            return 0L;
        else
            return counts.get(0);
    }

    @Override
    public Long count(Company company, Status[] status, String query, Priority[] priorities, Integer idProject) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select count(t) ");
        sb.append(" from Task t ");
        sb.append(" join t.userOwner u ");
        sb.append(" join t.father a ");
        sb.append(" join a.father p ");
        sb.append(" where u.father = :company ");
        sb.append(" and t.status in ( :status  ) ");
        sb.append(" and ( upper(t.name) like :query or upper(t.description) like :query or upper(u.username) like :query )");
        sb.append(" and t.priority in ( :priorities ) ");
        sb.append(" and a.status = :statusActive ");
        sb.append(" and p.status = :statusActive ");
        
        if(!idProject.equals(0))
            sb.append(" and p.id = :idProject ");
        
        Query q = sessionFactory.getCurrentSession().createQuery(sb.toString());
        q.setParameter("company", company);
        q.setParameterList("status", status);
        q.setParameter("query","%"+ query.toUpperCase()+"%");
        q.setParameterList("priorities", priorities);
        q.setParameter("statusActive", Status.ACTIVE);
        
        if(!idProject.equals(0))
            q.setParameter("idProject", idProject);
        
        List<Long> counts = q.list();
        if(counts.isEmpty())
            return 0L;
        else
            return counts.get(0);
    }

    @Override
    public List<Task> findBy(Company company, Status[] status, String query, Priority[] priorities, Integer idProject) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select t ");
        sb.append(" from Task t ");
        sb.append(" join t.userOwner u ");
        sb.append(" join t.father a ");
        sb.append(" join a.father p ");
        sb.append(" where u.father = :company ");
        sb.append(" and t.status in ( :status  ) ");
        sb.append(" and ( upper(t.name) like :query or upper(t.description) like :query or upper(u.username) like :query )");
        sb.append(" and t.priority in ( :priorities ) ");
        sb.append(" and a.status = :statusActive ");
        sb.append(" and p.status = :statusActive ");
        
        if(!idProject.equals(0))
            sb.append(" and p.id = :idProject ");
        
        sb.append(" order by t.priority ");
        Query q = sessionFactory.getCurrentSession().createQuery(sb.toString());
        q.setParameter("company", company);
        q.setParameterList("status", status);
        q.setParameter("query","%"+ query.toUpperCase()+"%");
        q.setParameterList("priorities", priorities);
        q.setParameter("statusActive", Status.ACTIVE);
        if(!idProject.equals(0))
            q.setParameter("idProject", idProject);
        return q.list();
    }

    @Override
    public List<TaskDTO> findBy(User userOwner, Status[] status, Date initDate, Date endDate, Integer idProject, Integer idAim) {
        List<TaskDTO> result = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append(" select tk.id_task, tk.name, priority, tk.id_user_owner, tk.id_user_requester, tk.estimated_time,  ");
        sb.append(" (SUM(TIMESTAMPDIFF(SECOND,tm.init_date,tm.end_date))/60)/60  real_time ");
        sb.append(" from task tk ");
        sb.append(" inner join aim a on(a.id_aim = tk.id_aim ) ");
        sb.append(" inner join project p on(p.id_project = a.id_project ) ");
        
        sb.append(" inner join time tm ");
        sb.append(" on(tk.id_task = tm.id_task) ");
        sb.append(" where tk.id_user_owner =  ").append(userOwner.getId());
        sb.append(" and tm.init_date between '").append(Util.dateToString(initDate,"yyyy-MM-dd")).append(" 00:00.00' and '").append(Util.dateToString(endDate,"yyyy-MM-dd")).append(" 23:59:59' ");
        sb.append(" and tk.status in ( ").append(Util.statusToString(status, ",")).append(" ) ");
        
        if(!idProject.equals(0))
            sb.append(" and p.id_project = ").append(idProject);
        
         if(!idAim.equals(0))
            sb.append(" and a.id_aim = ").append(idAim);
            
        sb.append(" group by tk.id_task ");
        
        List<Object[]> data = sessionFactory.getCurrentSession().createNativeQuery(sb.toString()).list();
        
        TaskDTO object = null;
        for (Object[] objects : data) {
            object = new TaskDTO();
            object.setIdTask((Integer)objects[0]);
            object.setName((String)objects[1]);
            object.setPriority((Integer)objects[2]);
            object.setIdUserOwner((Integer)objects[3]);
            object.setIdUserRequester((Integer)objects[4]);
            object.setEstimatedTime((Integer)objects[5]);
            object.setRealTime((BigDecimal)objects[6]);
            result.add(object);
        }
        return result;
    }
    
}
