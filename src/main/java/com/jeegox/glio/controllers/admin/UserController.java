package com.jeegox.glio.controllers.admin;

import com.jeegox.glio.controllers.BaseController;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.admin.User;
import com.jeegox.glio.entities.admin.UserType;
import com.jeegox.glio.enumerators.Status;
import com.jeegox.glio.services.UserService;
import com.jeegox.glio.util.Util;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user/**")
public class UserController extends BaseController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("init")
    public String index(Model model, HttpServletRequest request){
        Status[] status = {Status.ACTIVE, Status.INACTIVE};
        Company company = getCurrentCompany(request);
        List<UserType> userTypes = userService.findUserTypeByCompany(company);
        model.addAttribute("status", status);
        model.addAttribute("userTypes", userTypes);
        model.addAttribute("company", company.getName());
        return "user/init";
    }    
    
    @RequestMapping("findUsers")
    @ResponseBody
    public List<User> findUsers(HttpServletRequest request){
        return userService.findByCompany(getCurrentCompany(request));
    }
    
    @RequestMapping("deleteUser")
    @ResponseBody
    public String deleteUser(HttpServletRequest request,@RequestParam Integer id){
        try{
            this.userService.changeStatus(userService.findById(id), Status.DELETED);
            return "OK";
        }catch(Exception e){
            return e.getMessage();
        }
    }
    
    @RequestMapping("saveUser")
    @ResponseBody
    public String saveUser(HttpServletRequest request, @RequestParam Integer id, @RequestParam String username,
            @RequestParam String password,
            @RequestParam String name ,@RequestParam Status status,
            @RequestParam Integer idUserType, @RequestParam Boolean onlyOneAccess,
            @RequestParam String email){
        try{
            UserType userType = userService.findUserTypeById(idUserType);
            User user = new User(id.equals(0) ? null :id, username, 
                    id.equals(0) ? Util.encodeSha256(password) : password, 
                    name, status, userType, onlyOneAccess, getCurrentCompany(request), email );
            this.userService.save(user);
            return "OK";
        }catch(Exception e){
            return e.getMessage();
        }
    }
}
