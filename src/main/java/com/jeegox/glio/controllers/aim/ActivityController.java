package com.jeegox.glio.controllers.aim;

import com.jeegox.glio.controllers.BaseController;
import com.jeegox.glio.entities.aim.Aim;
import com.jeegox.glio.entities.aim.Project;
import com.jeegox.glio.enumerators.Status;
import com.jeegox.glio.services.UserService;
import com.jeegox.glio.services.ProjectService;
import com.jeegox.glio.util.Util;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/activity/**")
public class ActivityController extends BaseController {
    private final UserService userService;
    private final ProjectService projectService;

    @Autowired
    public ActivityController(UserService userService, ProjectService projectService) {
        this.userService = userService;
        this.projectService = projectService;
    }

    @RequestMapping("init")
    public String index(Model model, HttpServletRequest request) {
        List<Project> projects = projectService.findByCompany(getCurrentCompany(request),
                new Status[]{Status.ACTIVE, Status.INACTIVE, Status.FINISHED});
        model.addAttribute("projects", projects);
        return "activity/init";
    }

    @RequestMapping(value = "findActivityData", method = RequestMethod.POST)
    @ResponseBody
    public List<Map> findActivityData(HttpServletRequest request, @RequestParam String initDate,
            @RequestParam String endDate, @RequestParam Integer idProject, @RequestParam Integer idAim) {
        return userService.findActivityData(getCurrentCompany(request), Util.stringToDate(initDate, "yyyy-MM-dd"),
                Util.stringToDate(endDate, "yyyy-MM-dd"), idProject, idAim);
    }
    
    @RequestMapping(value = "findAims", method = RequestMethod.POST)
    @ResponseBody
    public List<Aim> findAims(HttpServletRequest request, @RequestParam Integer idProject){
        return projectService.findBy(projectService.findBydId(idProject));
    }
}
