package com.jeegox.glio.dao.admin;

import com.jeegox.glio.dao.hibernate.GenericDAO;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.admin.Session;
import com.jeegox.glio.entities.admin.User;
import java.util.List;

public interface SessionDAO extends GenericDAO<Session, Integer>{
    List<Session> findByCompany(Company company);
    
    List<Session> findByUser(User user);
    
    Session findOpenSession(User user);
    
    Session findBySession(String session);
}
