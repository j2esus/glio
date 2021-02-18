package com.jeegox.glio.dao.aim.impl;

import com.jeegox.glio.dao.aim.TimeDAO;
import com.jeegox.glio.dao.hibernate.GenericDAOImpl;
import com.jeegox.glio.entities.aim.Task;
import com.jeegox.glio.entities.aim.Time;
import org.springframework.stereotype.Repository;

@Repository
public class TimeDAOImpl extends GenericDAOImpl<Time,Integer> implements TimeDAO{

    @Override
    public Time findCurrentTime(Task task) {
        String query = " select time "+
                " from Time time "+
                " where time.father = :task "+
                " and time.initDate is not null "+
                " and time.endDate is null ";

        return (Time)sessionFactory.getCurrentSession().createQuery(query).
                setParameter("task", task).getResultList().
                stream().findFirst().orElse(null);
    }
    
}
