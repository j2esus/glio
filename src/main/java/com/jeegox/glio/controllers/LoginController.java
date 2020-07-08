package com.jeegox.glio.controllers;

import com.jeegox.glio.entities.admin.OptionMenu;
import com.jeegox.glio.entities.admin.Session;
import com.jeegox.glio.enumerators.Status;
import com.jeegox.glio.services.CompanyService;
import com.jeegox.glio.services.UserService;
import com.jeegox.glio.util.Constants;
import com.jeegox.glio.util.Util;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
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

@Controller
public class LoginController {
    private final UserService userService;
    private final CompanyService companyService;

    @Autowired
    public LoginController(UserService userService, CompanyService companyService) {
        this.userService = userService;
        this.companyService = companyService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("message", "");
        return "index";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(Model model) {
        model.addAttribute("message", "");
        return "register";
    }
    
    @RequestMapping(value = "/thanks", method = RequestMethod.GET)
    public String thanks(Model model) {
        model.addAttribute("user", "");
        return "thanks";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView toRegister(HttpServletRequest request, @RequestParam String companyName,
            @RequestParam String username, @RequestParam String email, @RequestParam String password) {
        try {
            String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
            String user = this.companyService.register(gRecaptchaResponse, companyName, username, email, password);
            ModelAndView mv = new ModelAndView("thanks");
            mv.getModelMap().addAttribute("user", user.split("@")[0]);
            mv.getModelMap().addAttribute("company", user.split("@")[1]);
            return mv;
        } catch (Exception e) {
            ModelAndView mv = new ModelAndView("register");
            mv.getModelMap().addAttribute("message", e.getMessage());
            mv.getModelMap().addAttribute("company", companyName);
            mv.getModelMap().addAttribute("user", username);
            mv.getModelMap().addAttribute("email", email);
            mv.getModelMap().addAttribute("password", password);
            return mv;
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(@RequestParam String company, @RequestParam String user, @RequestParam String password, HttpServletRequest request) {
        ModelAndView mv = null;
        try {
            String username = user.trim()+"@"+company.trim();
            Session session = userService.login(username, Util.encodeSha256(password.trim()), "");
            if (session != null) {
                Set<OptionMenu> options = session.getFather().getUserType().getOptions();

                Map<String, String> mapaOptios = options.stream()
                        .collect(Collectors.toMap(x -> x.getUrl().
                                replace("/init","").substring(1), x -> x.getUrl()));

                mapaOptios.put("resources", "");
                mapaOptios.put("all", "");
                HttpSession httpSession = request.getSession(false);
                httpSession.setAttribute(Constants.Security.USER_SESSION, session);
                httpSession.setAttribute(Constants.Security.OPTIONS_MAP, mapaOptios);
                mv = new ModelAndView("redirect:all/dash");
                return mv;
            }
            mv = new ModelAndView("index");
            mv.getModelMap().addAttribute("message", "Los datos de acceso son incorrectos");
            mv.getModelMap().addAttribute("company", company);
            mv.getModelMap().addAttribute("user", user);
            return mv;
        } catch (Exception e) {
            mv = new ModelAndView("index");
            mv.getModelMap().addAttribute("message", e.getMessage());
            mv.getModelMap().addAttribute("company", company);
            mv.getModelMap().addAttribute("user", user);
            return mv;
        }
    }

    @RequestMapping("/logout")
    @ResponseBody
    public String logout(HttpServletRequest request) {
        try {
            HttpSession httpSession = request.getSession(false);
            Session session = (Session) httpSession.getAttribute(Constants.Security.USER_SESSION);
            userService.changeStatus(session, Status.CLOSED);
            httpSession.invalidate();
            return "OK";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
