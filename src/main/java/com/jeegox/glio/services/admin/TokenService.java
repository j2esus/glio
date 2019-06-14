package com.jeegox.glio.services.admin;

import com.jeegox.glio.dto.GenericResponse;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.admin.Token;
import com.jeegox.glio.entities.admin.User;
import com.jeegox.glio.enumerators.Status;
import java.util.List;

/**
 *
 * @author j2esus
 */
public interface TokenService {
    
    List<Token> findAll();
    
    Token find(User user);

    List<Token> findByUser(User user);
    
    Token find(Status status, String token);
    
    List<Token> findByCompany(Company company);
    
    void changeStatus(Token token,Status status) throws Exception;
    
    Token findById(Integer id);
    
    String generateToken(User user);
    
    GenericResponse validateToken(String token);
}
