package com.jeegox.glio.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author j2esus
 */
@Controller
public class ErrorController {
    
    @RequestMapping("/404")
    public String page404(){
        return "404";
    }
    
    @RequestMapping("/400")
    public String page400(){
        return "400";
    }
    
    @RequestMapping("/403")
    public String page403(){
        return "403";
    }
    
    @RequestMapping("/forbidden")
    public String forbidden(){
        return "forbidden";
    }
}
