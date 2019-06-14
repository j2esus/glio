package com.jeegox.glio.services.admin.impl;

import com.jeegox.glio.dao.admin.SessionDAO;
import com.jeegox.glio.entities.admin.Session;
import com.jeegox.glio.entities.admin.User;
import com.jeegox.glio.enumerators.Status;
import com.jeegox.glio.services.admin.SessionService;
import com.jeegox.glio.util.Util;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author j2esus
 */
@Service
public class SessionServiceImpl implements SessionService{
    @Autowired
    private SessionDAO sessionDAO;

    @Transactional(readOnly = true)
    @Override
    public List<Session> findAll() {
        return sessionDAO.findAll();
    }

    @Transactional
    @Override
    public void changeStatus(Session session, Status status) throws Exception {
        session.setStatus(status);
        session.setEndDate(Util.getCurrentCompleteDate());
        sessionDAO.save(session);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Session> findByUser(User user) {
        return sessionDAO.findByUser(user);
    }

    @Transactional(readOnly = true)
    @Override
    public Session findById(Integer id) {
        return sessionDAO.findById(id);
    }
    
}
