package com.jeegox.glio.controllers.admin;

import com.jeegox.glio.controllers.BaseController;
import com.jeegox.glio.entities.admin.Token;
import com.jeegox.glio.enumerators.Status;
import com.jeegox.glio.services.UserService;
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
@RequestMapping("/token/**")
public class TokenController extends BaseController{
    @Autowired
    private UserService userService;
 
    @RequestMapping("init")
    public String index(){
        return "token/init";
    }
    
    @RequestMapping("findTokens")
    @ResponseBody
    public List<Token> findTokens(HttpServletRequest request){
        return userService.findTokenByCompany(getCurrentCompany(request));
    }
    
    @RequestMapping("deleteToken")
    @ResponseBody
    public String deleteToken(HttpServletRequest request,@RequestParam Integer id){
        try{
            userService.changeStatus(userService.findTokenById(id), Status.DELETED);
            return "OK";
        }catch(Exception e){
            return e.getMessage();
        }
    }
}
