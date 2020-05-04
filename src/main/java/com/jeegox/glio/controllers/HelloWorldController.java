package com.jeegox.glio.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloWorldController {
    
    @RequestMapping("/hello")
    public String hello(Model model) {
        model.addAttribute("greeting", "Hello Spring MVC");
        return "helloworld";
    }
    
    @RequestMapping("/hello2")
    public String hello2(Model model) {
        model.addAttribute("greeting", "Hello Spring MVC 2");
        return "helloworld2";
    }
}
