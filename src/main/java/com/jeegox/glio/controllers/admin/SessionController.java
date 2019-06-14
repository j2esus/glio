package com.jeegox.glio.controllers.admin;

import com.jeegox.glio.controllers.BaseController;
import com.jeegox.glio.entities.admin.Session;
import com.jeegox.glio.enumerators.Status;
import com.jeegox.glio.services.admin.SessionService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author j2esus
 */
@Controller
@RequestMapping("/session/**")
public class SessionController extends BaseController {
    @Autowired
    private SessionService sessionService;
    
    @RequestMapping("init")
    public String index(){
        return "session/init";
    }
    
    @RequestMapping("findSessions")
    @ResponseBody
    public List<Session> findSessions(HttpServletRequest request){
        return sessionService.findByUser(getCurrentUser(request));
    }
    
    @RequestMapping("finishSession")
    @ResponseBody
    public String finishSession(HttpServletRequest request,@RequestParam Integer id){
        try{
            this.sessionService.changeStatus(sessionService.findById(id), Status.CLOSED);
            return "OK";
        }catch(Exception e){
            return e.getMessage();
        }
    }
}
