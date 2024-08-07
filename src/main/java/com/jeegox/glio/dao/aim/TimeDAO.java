package com.jeegox.glio.dao.aim;

import com.jeegox.glio.dao.hibernate.GenericDAO;
import com.jeegox.glio.entities.aim.Task;
import com.jeegox.glio.entities.aim.Time;

public interface TimeDAO extends GenericDAO<Time,Integer>{
    
    Time findCurrentTime(Task task);
}
