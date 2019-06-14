package com.jeegox.glio.controllers;

import com.jeegox.glio.services.admin.CompanyService;
import com.jeegox.glio.services.admin.UserService;
import com.jeegox.glio.services.admin.UserTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author j2esus
 */
@Controller
public class HelloWorldController {
    @Autowired
    private CompanyService companyService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserTypeService userTypeService;
    
    @RequestMapping("/hello")
    public String hello(Model model) {
        model.addAttribute("greeting", "Hello Spring MVC");
        model.addAttribute("empresas", companyService.findAll());
        model.addAttribute("usuarios", userService.findAll());
        model.addAttribute("tipos", userTypeService.findAll());
        return "helloworld";
    }
    
    @RequestMapping("/hello2")
    public String hello2(Model model) {
        model.addAttribute("greeting", "Hello Spring MVC 2");
        return "helloworld2";
    }
}
