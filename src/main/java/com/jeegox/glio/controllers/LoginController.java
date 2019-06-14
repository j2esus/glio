package com.jeegox.glio.controllers;

import com.jeegox.glio.entities.admin.OptionMenu;
import com.jeegox.glio.entities.admin.Session;
import com.jeegox.glio.enumerators.Status;
import com.jeegox.glio.services.admin.CompanyService;
import com.jeegox.glio.services.admin.SessionService;
import com.jeegox.glio.services.admin.UserService;
import com.jeegox.glio.util.Constants;
import com.jeegox.glio.util.Util;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author j2esus
 */
@Controller
public class LoginController {

    @Autowired
    private UserService userService;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private CompanyService companyService;

    @RequestMapping("/")
    public String index() {
        return "index";
    }
    
    @RequestMapping("/register")
    public String register() {
        return "register";
    }
    
    @RequestMapping(value = "/toRegister", method = RequestMethod.POST)
    @ResponseBody
    public String toRegister(@RequestParam String companyName, @RequestParam String username, @RequestParam String email, @RequestParam String password) {
        try{
            this.companyService.register(companyName, username, email, password);
            return "OK";
        }catch(Exception e){
            return e.getMessage();
        }
    }

    @RequestMapping("/login")
    @ResponseBody
    public String login(@RequestParam String username, @RequestParam String password, HttpServletRequest request) {
        try {
            Session session = userService.login(username, Util.encodeSha256(password), "");
            if (session != null) {
                Set<OptionMenu> options = session.getFather().getUserType().getOptions();
                Map<String, Integer> mapaOptios = new HashMap<>();
                Iterator<OptionMenu> it = options.iterator();
                OptionMenu item;
                while (it.hasNext()) {
                    item = it.next();
                    mapaOptios.put(item.getUrl().replace("/init", "").substring(1), 0);
                }
                mapaOptios.put("resources", 0);
                mapaOptios.put("all", 0);
                //options.stream().collect(Collectors.toMap(OptionMenu::getUrl, item -> item));
                HttpSession httpSession = request.getSession(false);
                httpSession.setAttribute(Constants.Security.USER_SESSION, session);
                httpSession.setAttribute(Constants.Security.OPTIONS_MAP, mapaOptios);
                return "OK";
            }
            return "ERROR";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @RequestMapping("/logout")
    @ResponseBody
    public String logout(HttpServletRequest request) {
        try {
            HttpSession httpSession = (HttpSession) request.getSession(false);
            Session session = (Session) httpSession.getAttribute(Constants.Security.USER_SESSION);
            sessionService.changeStatus(session, Status.CLOSED);
            httpSession.invalidate();
            return "OK";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
