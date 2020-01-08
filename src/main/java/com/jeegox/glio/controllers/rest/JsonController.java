package com.jeegox.glio.controllers.rest;

import com.jeegox.glio.dto.admin.UserResponse;
import com.jeegox.glio.dto.GenericResponse;
import com.jeegox.glio.services.UserService;
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
@RequestMapping("/json/**")
public class JsonController {
    @Autowired
    private UserService userService;
    
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public UserResponse login(@RequestParam String username, @RequestParam String password){
        return userService.getToken(username, password);
    }
    
    @RequestMapping(value = "validateToken", method = RequestMethod.POST)
    @ResponseBody
    public GenericResponse validateToken(@RequestParam String token){
        return userService.validateToken(token);
    }
}
