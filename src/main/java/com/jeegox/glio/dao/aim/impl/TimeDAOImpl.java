package com.jeegox.glio.dao.aim.impl;

import com.jeegox.glio.dao.aim.TimeDAO;
import com.jeegox.glio.dao.hibernate.GenericDAOImpl;
import com.jeegox.glio.entities.aim.Task;
import com.jeegox.glio.entities.aim.Time;
import java.util.List;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author j2esus
 */
@Repository
public class TimeDAOImpl extends GenericDAOImpl<Time,Integer> implements TimeDAO{

    @Override
    public Time findCurrentTime(Task task) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select t ");
        sb.append(" from Time t ");
        sb.append(" where t.father = :task ");
        sb.append(" and t.initDate is not null ");
        sb.append(" and t.endDate is null ");
        Query q = sessionFactory.getCurrentSession().createQuery(sb.toString());
        q.setParameter("task", task);
        
        List<Time> times = q.list();
        if(times.isEmpty())
            return null;
        return times.get(0);
    }
    
}
