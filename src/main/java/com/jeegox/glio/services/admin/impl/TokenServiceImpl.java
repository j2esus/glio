package com.jeegox.glio.services.admin.impl;

import com.jeegox.glio.dao.admin.TokenDAO;
import com.jeegox.glio.dto.GenericResponse;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.admin.Token;
import com.jeegox.glio.entities.admin.User;
import com.jeegox.glio.enumerators.Status;
import com.jeegox.glio.enumerators.StatusResponse;
import com.jeegox.glio.services.admin.TokenService;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author j2esus
 */
@Service
public class TokenServiceImpl implements TokenService{
    @Autowired
    private TokenDAO tokenDAO;

    @Transactional(readOnly = true)
    @Override
    public List<Token> findAll() {
        return tokenDAO.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Token find(User user) {
        return tokenDAO.find(user);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Token> findByUser(User user) {
        return tokenDAO.findByUser(user);
    }

    @Transactional(readOnly = true)
    @Override
    public Token find(Status status, String token) {
        return tokenDAO.find(status, token);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Token> findByCompany(Company company) {
        return tokenDAO.findByCompany(company);
    }

    @Transactional
    @Override
    public void changeStatus(Token token, Status status) throws Exception {
        token.setStatus(status);
        tokenDAO.save(token);
    }

    @Transactional(readOnly = true)
    @Override
    public Token findById(Integer id) {
        return tokenDAO.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public String generateToken(User user) {
        String sToken = "";
        Token token = tokenDAO.find(user);
        if(token == null){
            SecureRandom random = new SecureRandom();
            sToken = new BigInteger(130, random).toString(32);
            token = new Token();
            token.setStatus(Status.ACTIVE);
            token.setFather(user);
            token.setToken(sToken);
            tokenDAO.save(token);
        } else{
            sToken = token.getToken();
        }
        return sToken;
    }

    @Transactional(readOnly = true)
    @Override
    public GenericResponse validateToken(String token) {
        GenericResponse response = new GenericResponse();
        Token tokenO = tokenDAO.find(Status.ACTIVE, token);
        if(tokenO != null){
            response.setMessage("OK");
            response.setStatusResponse(StatusResponse.OK);
        }else{
            response.setMessage("El token is inválido.");
            response.setStatusResponse(StatusResponse.FAILURE);
        }
        return response;
    }
}
