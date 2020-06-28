package com.jeegox.glio.controllers;

import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.admin.Session;
import com.jeegox.glio.entities.admin.User;
import com.jeegox.glio.util.Constants;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class BaseController {
    
    public User getCurrentUser(HttpServletRequest request){
        HttpSession httpSession = request.getSession(false);
        Session session = (Session)httpSession.getAttribute(Constants.Security.USER_SESSION);
        return session.getFather();
    }
    
    public Company getCurrentCompany(HttpServletRequest request){
        HttpSession httpSession = request.getSession(false);
        Session session = (Session)httpSession.getAttribute(Constants.Security.USER_SESSION);
        User user = session.getFather();
        return user.getFather();
    }
    
    public void updateSession(HttpServletRequest request, Session session){
        HttpSession httpSession = request.getSession(false);
        httpSession.setAttribute(Constants.Security.USER_SESSION, session);
    }
    
    public void updateSession(HttpServletRequest request, User user){
        HttpSession httpSession = request.getSession(false);
        Session session = (Session)httpSession.getAttribute(Constants.Security.USER_SESSION);
        session.setFather(user);
        httpSession.setAttribute(Constants.Security.USER_SESSION, session);
    }
}
