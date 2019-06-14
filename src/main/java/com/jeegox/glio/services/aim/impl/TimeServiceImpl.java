package com.jeegox.glio.services.aim.impl;

import com.jeegox.glio.dao.aim.TimeDAO;
import com.jeegox.glio.entities.aim.Task;
import com.jeegox.glio.entities.aim.Time;
import com.jeegox.glio.services.aim.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author j2esus
 */
@Repository
public class TimeServiceImpl implements TimeService {
    @Autowired
    private TimeDAO timeDAO;

    @Transactional
    @Override
    public void saveOrUpdate(Time time) {
        timeDAO.save(time);
    }

    @Transactional(readOnly = true)
    @Override
    public Time findById(Integer id) {
        return timeDAO.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Time findCurrentTime(Task task) {
        return timeDAO.findCurrentTime(task);
    }
}
