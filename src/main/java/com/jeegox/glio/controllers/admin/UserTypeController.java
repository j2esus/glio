package com.jeegox.glio.controllers.admin;

import com.jeegox.glio.controllers.BaseController;
import com.jeegox.glio.dto.admin.OptionMenuUserTypeDTO;
import com.jeegox.glio.entities.admin.UserType;
import com.jeegox.glio.enumerators.Status;
import com.jeegox.glio.services.UserService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/userType/**")
public class UserTypeController extends BaseController {
    @Autowired
    private UserService userService;
    
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
    
    @RequestMapping("saveOptions")
    @ResponseBody
    public String saveOptions(HttpServletRequest request, @RequestParam Integer idUserType, 
            @RequestParam String optionsAdd,
            @RequestParam String optionsDel){
        try{
            String[] optionsAddA = optionsAdd.split(",");
            String[] optionsDelA = optionsDel.split(",");
            userService.saveOptions(idUserType, optionsAddA, optionsDelA);
            return "OK";
        }catch(Exception e){
            return e.getMessage();
        }
    }
    
    @RequestMapping("findOptions")
    @ResponseBody
    public List<OptionMenuUserTypeDTO> findOptions(HttpServletRequest request, @RequestParam Integer idUserType){
        return userService.findOptionsMenu(idUserType);
    }
}
