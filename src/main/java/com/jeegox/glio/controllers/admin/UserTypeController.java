package com.jeegox.glio.controllers.admin;

import com.jeegox.glio.controllers.BaseController;
import com.jeegox.glio.dto.admin.OptionMenuDTO;
import com.jeegox.glio.entities.admin.OptionMenu;
import com.jeegox.glio.entities.admin.UserType;
import com.jeegox.glio.enumerators.Status;
import com.jeegox.glio.services.UserService;
import com.jeegox.glio.services.admin.AppMenuService;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/userType/**")
public class UserTypeController extends BaseController {
    private final UserService userService;
    private final AppMenuService appMenuService;

    @Autowired
    public UserTypeController(UserService userService, 
            AppMenuService appMenuService) {
        this.userService = userService;
        this.appMenuService = appMenuService;
    }

    @RequestMapping("init")
    public String index(Model model){
        Status[] status = {Status.ACTIVE, Status.INACTIVE};
        model.addAttribute("status", status);
        return "userType/init";
    }
    
    @RequestMapping("findUserTypes")
    @ResponseBody
    public List<UserType> findUserTypes(HttpServletRequest request){
        return userService.findUserTypeByCompany(getCurrentCompany(request));
    }
    
    @RequestMapping("deleteUserType")
    @ResponseBody
    public String deleteUserType(HttpServletRequest request,@RequestParam Integer id){
        try{
            userService.changeStatus(userService.findUserTypeById(id), Status.DELETED);
            return "OK";
        }catch(Exception e){
            return e.getMessage();
        }
    }
    
    @RequestMapping("saveUserType")
    @ResponseBody
    public String saveUserType(HttpServletRequest request, @RequestParam Integer id, @RequestParam String name,
            @RequestParam Status status){
        try{
            userService.save(id, name,status, getCurrentCompany(request));
            return "OK";
        }catch(Exception e){
            return e.getMessage();
        }
    }
    
    @RequestMapping(value = "saveOptions", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Void> saveOptions(HttpServletRequest request, @RequestParam Integer idUserType, 
            @RequestParam("options[]") Integer[] options){
        userService.saveOptions(userService.findUserTypeById(idUserType), 
                appMenuService.findByIds(options));
        return ResponseEntity.ok().build();
    }
    
    @RequestMapping(value = "getOptionsByUserType", method = RequestMethod.POST)
    @ResponseBody
    public Set<OptionMenu> getOptionsByUserType(HttpServletRequest request, @RequestParam Integer idUserType){
        return userService.findUserTypeById(idUserType).getOptions();
    }
    
    @RequestMapping(value = "getPublicOptions", method = RequestMethod.POST)
    @ResponseBody
    public List<OptionMenuDTO> getPublicOptions(HttpServletRequest request) {
        return appMenuService.getPublicOptions();
    }
}
