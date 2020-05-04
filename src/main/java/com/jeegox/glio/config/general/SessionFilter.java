package com.jeegox.glio.config.general;

import com.jeegox.glio.entities.admin.Session;
import com.jeegox.glio.util.Constants;
import java.io.IOException;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SessionFilter implements Filter{
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        res.addHeader("X-FRAME-OPTIONS", "DENY");
        String path = req.getServletPath();
        if(DataConfig.isPrivate(path)){
            HttpSession httpSession = req.getSession(false);
            if(httpSession != null){
                Session session = (Session)httpSession.getAttribute(Constants.Security.USER_SESSION);
                if(session != null){
                    path = path.substring(1).split("/")[0];
                    Map<String, Integer> options = (Map)httpSession.getAttribute(Constants.Security.OPTIONS_MAP);
                    Integer op = options.get(path);
                    if(op == null){
                        res.sendRedirect(request.getServletContext().getContextPath()+"/forbidden");
                    }else{
                        chain.doFilter(request, response);
                    }
                }else{
                    res.sendRedirect(request.getServletContext().getContextPath());
                }
            }else{
                res.sendRedirect(request.getServletContext().getContextPath());
            }
        }else{
            chain.doFilter(request, response);
        }
    }
    
    @Override
    public void destroy() {
        
    }
}
