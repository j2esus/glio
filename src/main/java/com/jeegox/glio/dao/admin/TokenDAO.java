package com.jeegox.glio.dao.admin;

import com.jeegox.glio.dao.hibernate.GenericDAO;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.admin.Token;
import com.jeegox.glio.entities.admin.User;
import com.jeegox.glio.enumerators.Status;
import java.util.List;

/**
 *
 * @author j2esus
 */
public interface TokenDAO extends GenericDAO<Token,Integer>{
    
    Token find(User user);

    List<Token> findByUser(User user);
    
    Token find(Status status, String token);
    
    List<Token> findByCompany(Company company);
}
