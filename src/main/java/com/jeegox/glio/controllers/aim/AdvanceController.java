package com.jeegox.glio.controllers.aim;

import com.jeegox.glio.controllers.BaseController;
import com.jeegox.glio.dto.GraphStatusVO;
import com.jeegox.glio.entities.aim.Aim;
import com.jeegox.glio.entities.aim.Project;
import com.jeegox.glio.entities.aim.Task;
import com.jeegox.glio.enumerators.Status;
import com.jeegox.glio.services.aim.AimService;
import com.jeegox.glio.services.aim.ProjectService;
import com.jeegox.glio.services.aim.TaskService;
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

/**
 *
 * @author j2esus
 */
@Controller
@RequestMapping("/advance/**")
public class AdvanceController extends BaseController{
    @Autowired
    private ProjectService projectService;
    @Autowired
    private AimService aimService;
    @Autowired
    private TaskService taskService;
    
    @RequestMapping("init")
    public String index(Model model,HttpServletRequest request){
        return "advance/init";
    }
    
    @RequestMapping(value = "findProjects", method = RequestMethod.POST)
    @ResponseBody
    public List<Project> findProjects(HttpServletRequest request, @RequestParam String query){
        return projectService.findBy(getCurrentCompany(request), query, new Status[]{Status.ACTIVE});
    }
    
    @RequestMapping(value = "findDataGraphProject", method = RequestMethod.POST)
    @ResponseBody
    public List<GraphStatusVO> findDataGraphProject(HttpServletRequest request, @RequestParam Integer idProject){
        return projectService.findDataGraphProject(idProject);
    }
    
    @RequestMapping(value = "findDataGraphAim", method = RequestMethod.POST)
    @ResponseBody
    public List<Map> findDataGraphAim(HttpServletRequest request, @RequestParam Integer idProject){
        return projectService.findDataGraphAim(idProject);
    }
    
    @RequestMapping(value = "findTasks", method = RequestMethod.POST)
    @ResponseBody
    public List<Task> findTasks(HttpServletRequest request, @RequestParam Integer idAim){
        Aim aim = this.aimService.findBydId(idAim);
        return taskService.findBy(aim, new Status[]{Status.DELETED, Status.INACTIVE});
    }
}
