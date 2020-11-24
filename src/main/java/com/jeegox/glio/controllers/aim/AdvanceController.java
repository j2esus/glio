package com.jeegox.glio.controllers.aim;

import com.jeegox.glio.controllers.BaseController;
import com.jeegox.glio.entities.aim.Aim;
import com.jeegox.glio.entities.aim.Project;
import com.jeegox.glio.entities.aim.Task;
import com.jeegox.glio.enumerators.Status;
import com.jeegox.glio.services.ProjectService;
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
@RequestMapping("/advance/**")
public class AdvanceController extends BaseController{
    private final ProjectService projectService;

    @Autowired
    public AdvanceController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @RequestMapping("init")
    public String index(Model model,HttpServletRequest request){
        return "advance/init";
    }
    
    @RequestMapping(value = "findProjects", method = RequestMethod.POST)
    @ResponseBody
    public List<Project> findProjects(HttpServletRequest request, @RequestParam String query){
        return projectService.findByCompany(getCurrentCompany(request), query, new Status[]{Status.ACTIVE});
    }
    
    @RequestMapping(value = "countTasksGroupedByStatus_project", method = RequestMethod.POST)
    @ResponseBody
    public Map<Status, Long> countTasksGroupedByStatus_project(@RequestParam Integer idProject){
        return projectService.countTasksGroupedByStatus(projectService.findBydId(idProject));
    }
    
    @RequestMapping(value = "findAims", method = RequestMethod.POST)
    @ResponseBody
    public List<Aim> findAims(@RequestParam Integer idProject){
        return projectService.findBy(projectService.findBydId(idProject), new Status[]{Status.ACTIVE, Status.FINISHED});
    }
    
    @RequestMapping(value = "findTasks", method = RequestMethod.POST)
    @ResponseBody
    public List<Task> findTasks(@RequestParam Integer idAim){
        Aim aim = projectService.findAimBydId(idAim);
        return projectService.findBy(aim, new Status[]{Status.PENDING, Status.FINISHED, Status.IN_PROCESS, Status.PAUSED, Status.ACCEPTED});
    }

    @RequestMapping(value = "countTasksGroupedByStatus_aim", method = RequestMethod.POST)
    @ResponseBody
    public Map<Status, Long> countTasksGroupedByStatus_aim(@RequestParam Integer idAim){
        return projectService.countTasksGroupedByStatus(projectService.findAimBydId(idAim));
    }

}
