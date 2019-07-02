package com.jeegox.glio.controllers;

import com.jeegox.glio.entities.admin.OptionMenu;
import com.jeegox.glio.entities.admin.Session;
import com.jeegox.glio.enumerators.Status;
import com.jeegox.glio.services.admin.CompanyService;
import com.jeegox.glio.services.admin.SessionService;
import com.jeegox.glio.services.admin.UserService;
import com.jeegox.glio.util.Constants;
import com.jeegox.glio.util.Util;
import com.jeegox.glio.util.VerifyUtils;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(Model model) {
        model.addAttribute("message", "");
        return "register";
    }
    
    @RequestMapping(value = "/thanks", method = RequestMethod.GET)
    public String thanks(Model model) {
        model.addAttribute("username", "");
        return "thanks";
    }
    

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView toRegister(HttpServletRequest request, @RequestParam String companyName,
            @RequestParam String username, @RequestParam String email, @RequestParam String password) {
        try {
            String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
            String user = this.companyService.register(gRecaptchaResponse, companyName, username, email, password);
            ModelAndView mv = new ModelAndView("thanks");
            mv.getModelMap().addAttribute("username", user);
            return mv;
        } catch (Exception e) {
            ModelAndView mv = new ModelAndView("register");
            mv.getModelMap().addAttribute("message", e.getMessage());
            mv.getModelMap().addAttribute("company", companyName);
            mv.getModelMap().addAttribute("username", username);
            mv.getModelMap().addAttribute("email", email);
            mv.getModelMap().addAttribute("password", password);
            return mv;
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
