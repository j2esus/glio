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
import org.springframework.stereotype.Repository;

@Repository
public class TaskDAOImpl extends GenericDAOImpl<Task,Integer> implements TaskDAO{

    @Override
    public List<Task> findByAim(Aim aim) {
        String query = " select t "+
                " from Task t "+
                " where t.father = :aim "+
                " and t.status <> :status ";
        return sessionFactory.getCurrentSession().createQuery(query).
                setParameter("aim", aim).
                setParameter("status", Status.DELETED).getResultList();
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
    public Long count(User user, Status[] status) {
        String query = " select count(t) "+
                " from Task t "+
                " where t.userOwner = :user "+
                " and t.status in ( :status )";

        return (Long)sessionFactory.getCurrentSession().createQuery(query).
                setParameter("user", user).
                setParameterList("status", status).
                getResultList().stream().
                findFirst().orElse(0);
    }

    @Override
    public List<Task> findBy(Company company, Status[] status, String value, Priority[] priorities, Project project) {
        String query = " select t "+
                " from Task t "+
                " join t.userOwner user "+
                " join t.father aim "+
                " join aim.father project "+
                " where user.father = :company "+
                " and t.status in ( :status ) "+
                " and ( upper(t.name) like :value or upper(t.description) like :value or upper(user.username) like :value ) "+
                " and t.priority in ( :priorities ) "+
                " and aim.status = :activeStatus "+
                " and project.status = :activeStatus "+
                " and project = :project "+
                " order by t.priority ";

        return sessionFactory.getCurrentSession().createQuery(query).
                setParameter("company", company).
                setParameterList("status", status).
                setParameter("value", "%"+ value.toUpperCase()+"%").
                setParameterList("priorities", priorities).
                setParameter("activeStatus", Status.ACTIVE).
                setParameter("project", project).
                getResultList();
    }

    @Override
    public List<Task> findBy(Company company, Status[] status, String value, Priority[] priorities) {
        String query = " select t "+
                " from Task t "+
                " join t.userOwner user "+
                " join t.father aim "+
                " join aim.father project "+
                " where user.father = :company "+
                " and t.status in ( :status ) "+
                " and ( upper(t.name) like :value or upper(t.description) like :value or upper(user.username) like :value ) "+
                " and t.priority in ( :priorities ) "+
                " and aim.status = :activeStatus "+
                " and project.status = :activeStatus "+
                " order by t.priority ";

        return sessionFactory.getCurrentSession().createQuery(query).
                setParameter("company", company).
                setParameterList("status", status).
                setParameter("value", "%"+ value.toUpperCase()+"%").
                setParameterList("priorities", priorities).
                setParameter("activeStatus", Status.ACTIVE).
                getResultList();
    }

    //todo check this method because I think it could be refactored
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
