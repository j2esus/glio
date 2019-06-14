package com.jeegox.glio.services.admin;

import com.jeegox.glio.entities.admin.Session;
import com.jeegox.glio.entities.admin.User;
import com.jeegox.glio.enumerators.Status;
import java.util.List;

/**
 *
 * @author j2esus
 */
public interface SessionService {
    
    List<Session> findAll();
    
    void changeStatus(Session session,Status status) throws Exception;
    
    List<Session> findByUser(User user);
    
    Session findById(Integer id);
}
