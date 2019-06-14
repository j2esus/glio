package com.jeegox.glio.services.aim;

import com.jeegox.glio.entities.aim.Task;
import com.jeegox.glio.entities.aim.Time;

/**
 *
 * @author j2esus
 */
public interface TimeService {
    
    void saveOrUpdate(Time time);
    
    Time findById(Integer id);
    
    Time findCurrentTime(Task task);
    
}
