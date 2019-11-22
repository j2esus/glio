package com.jeegox.glio.controllers.aim;

import com.jeegox.glio.controllers.BaseController;
import com.jeegox.glio.dto.GraphProductivityDTO;
import com.jeegox.glio.entities.aim.Project;
import com.jeegox.glio.enumerators.Status;
import com.jeegox.glio.services.admin.UserService;
import com.jeegox.glio.services.aim.ProjectService;
import com.jeegox.glio.util.Util;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author j2esus
 */
@Controller
@RequestMapping("/productivity/**")
public class ProductivityController extends BaseController {
    @Autowired
    private UserService userService;
    @Autowired
    private ProjectService projectService;
    
    @RequestMapping("init")
    public String index(Model model,HttpServletRequest request){
        List<Project> projects = projectService.findBy(getCurrentCompany(request), 
                new Status[]{Status.ACTIVE, Status.INACTIVE, Status.FINISHED});
        model.addAttribute("projects", projects);
        return "productivity/init";
    }
    
    @RequestMapping(value = "findDataGraphProductivity", method = RequestMethod.POST)
    @ResponseBody
    public List<GraphProductivityDTO> findDataGraphProductivity(HttpServletRequest request,
            @RequestParam String initDate, @RequestParam String endDate, @RequestParam Integer idProject){
        return userService.findDataGraphProductivity(getCurrentCompany(request),Util.stringToDate(initDate, "yyyy-MM-dd"),
                Util.stringToDate(endDate, "yyyy-MM-dd"), idProject);
    }
}
