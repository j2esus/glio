package com.jeegox.glio.controllers;

import com.jeegox.glio.dto.admin.CategoryMenuDTO;
import com.jeegox.glio.entities.State;
import com.jeegox.glio.entities.Suburb;
import com.jeegox.glio.entities.Town;
import com.jeegox.glio.entities.admin.CategoryMenu;
import com.jeegox.glio.entities.admin.Token;
import com.jeegox.glio.entities.admin.User;
import com.jeegox.glio.entities.aim.Aim;
import com.jeegox.glio.enumerators.Status;
import com.jeegox.glio.services.AddressService;
import com.jeegox.glio.services.CategoryMenuService;
import com.jeegox.glio.services.UserService;
import com.jeegox.glio.services.ProjectService;
import com.jeegox.glio.util.Constants;
import com.jeegox.glio.util.Util;

import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/all/**")
public class AllController extends BaseController {
    private final CategoryMenuService categoryMenuService;
    private final UserService userService;
    private final ProjectService projectService;
    private final AddressService addressService;

    @Autowired
    public AllController(CategoryMenuService categoryMenuService, UserService userService, ProjectService projectService,
                         AddressService addressService) {
        this.categoryMenuService = categoryMenuService;
        this.userService = userService;
        this.projectService = projectService;
        this.addressService = addressService;
    }

    @RequestMapping("dash")
    public String dash(HttpServletRequest request, Model model){
        User user = getCurrentUser(request);
        List<CategoryMenuDTO> categoriesMenu = categoryMenuService.findByDTO(user.getUserType());
        HttpSession httpSession = request.getSession(false);
        httpSession.setAttribute(Constants.Security.CATEGORY_LIST, categoriesMenu);
        return "all/dash";
    }
    
    @RequestMapping("configuration")
    public String configuration(HttpServletRequest request, Model model){
        User user = getCurrentUser(request);
        model.addAttribute("name", user.getName());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("idUser", user.getId());
        model.addAttribute("username", user.getUsername().split("@")[0]);
        model.addAttribute("company", user.getUsername().split("@")[1]);
        return "all/configuration";
    }
    
    @RequestMapping("changeUserData")
    @ResponseBody
    public String changeUserData(HttpServletRequest request, @RequestParam String username,
            @RequestParam String name,
            @RequestParam Integer idUser,
            @RequestParam String email){
        try{
            User user = getCurrentUser(request);
            user = userService.changeUserData(idUser, username.trim()+"@"+user.getUsername().split("@")[1], 
                    name.trim(), email.trim());
            updateSession(request, user);
            return "OK";
        }catch(Exception e){
            return e.getMessage();
        }
    }
    
    @RequestMapping("changePassword")
    @ResponseBody
    public String changePassword(HttpServletRequest request,@RequestParam String password,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            @RequestParam Integer idUser){
        try{
            User user = getCurrentUser(request);
            userService.changePassword(user, idUser,Util.encodeSha256(password), Util.encodeSha256(newPassword),
                    Util.encodeSha256(confirmPassword));
            return "OK";
        }catch(Exception e){
            return e.getMessage();
        }
    }
    
    @RequestMapping("findTokensUser")
    @ResponseBody
    public List<Token> findTokensUser(HttpServletRequest request){
        return userService.findByUser(getCurrentUser(request));
    }
    
    @RequestMapping("deleteToken")
    @ResponseBody
    public String deleteToken(@RequestParam Integer id){
        try{
            userService.changeStatus(userService.findTokenById(id), Status.DELETED);
            return "OK";
        }catch(Exception e){
            return e.getMessage();
        }
    }
    
    @RequestMapping(name = "module", method = RequestMethod.GET)
    public String module(Model model, @RequestParam Integer id,HttpServletRequest request){
        CategoryMenu cm = this.categoryMenuService.findById(id);
        HttpSession httpSession = request.getSession(false);
        List<CategoryMenuDTO> categoriesMenu = (List<CategoryMenuDTO>)httpSession.getAttribute(Constants.Security.CATEGORY_LIST);

        CategoryMenuDTO category = categoriesMenu.stream().filter(x -> Objects.equals(x.getId(), id)).
                findFirst().orElse(null);

        model.addAttribute("moduleName", cm != null ? cm.getName(): "");
        httpSession.setAttribute(Constants.Security.MENU, category != null ? category.getOptionsMenus(): new HashSet<>());
        return "all/module";
    }
    
    @RequestMapping(name = "countTasksInProcess", method = RequestMethod.POST)
    @ResponseBody
    public Long countTasksInProcess(HttpServletRequest request){
        return projectService.countTasksInProcess(getCurrentUser(request));
    }

    @RequestMapping("findAllStates")
    @ResponseBody
    public List<State> findAllStates(){
        return addressService.findAll();
    }

    @RequestMapping("findTowns")
    @ResponseBody
    public List<Town> findAllStates(@RequestParam Integer idState){
        return addressService.findByState(idState);
    }

    @RequestMapping("findSuburbs")
    @ResponseBody
    public List<Suburb> findSuburbs(@RequestParam Integer idTown){
        return addressService.findByTown(idTown);
    }

    @RequestMapping("findSuburbsByZipcode")
    @ResponseBody
    public List<Suburb> findSuburbsByZipcode(@RequestParam String cp){
        return addressService.findByTown(cp);
    }

    @RequestMapping("findSuburbsByZipCodeAndName")
    @ResponseBody
    public ResponseEntity<Suburb> findSuburbsByZipCodeAndName(@RequestParam String zipcode, @RequestParam String name){
        return Optional.ofNullable(addressService.findBy(zipcode, name).get()).map(suburb->ResponseEntity.ok().body(suburb))
                .orElseGet(()->new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @RequestMapping(value = "findAims", method = RequestMethod.POST)
    @ResponseBody
    public List<Aim> findAims(HttpServletRequest request, @RequestParam Integer idProject) {
        return projectService.findBy(projectService.findBydId(idProject));
    }
}
