package com.jeegox.glio.controllers.aim;

import com.jeegox.glio.controllers.BaseController;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.admin.User;
import com.jeegox.glio.entities.aim.Aim;
import com.jeegox.glio.entities.aim.Project;
import com.jeegox.glio.entities.aim.Task;
import com.jeegox.glio.enumerators.Priority;
import com.jeegox.glio.enumerators.Status;
import com.jeegox.glio.services.admin.UserService;
import com.jeegox.glio.services.ProjectService;
import java.util.HashMap;
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
@RequestMapping("/task/**")
public class TaskController extends BaseController {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserService userService;

    @RequestMapping("init")
    public String index(Model model, HttpServletRequest request) {
        List<Project> projects = projectService.findBy(getCurrentCompany(request), new Status[]{Status.ACTIVE});
        Priority[] priorities = Priority.values();
        model.addAttribute("projects", projects);
        model.addAttribute("priorities", priorities);
        return "task/init";
    }

    @RequestMapping(value = "findTasksPending", method = RequestMethod.POST)
    @ResponseBody
    public Map findTasksPending(HttpServletRequest request, @RequestParam String query,
            @RequestParam("priorities[]") Priority[] priorities, @RequestParam Integer idProject) {
        Company company = getCurrentCompany(request);
        Map<String, Object> response = new HashMap<>();
        response.put("count", projectService.count(company, new Status[]{Status.PENDING}, query, priorities, idProject));
        response.put("actives", projectService.findBy(company, new Status[]{Status.PENDING}, query, priorities, idProject));
        return response;
    }

    @RequestMapping(value = "findTasksInProcess", method = RequestMethod.POST)
    @ResponseBody
    public Map findTasksInProcess(HttpServletRequest request, @RequestParam String query,
            @RequestParam("priorities[]") Priority[] priorities, @RequestParam Integer idProject) {
        Company company = getCurrentCompany(request);
        Map<String, Object> response = new HashMap<>();
        response.put("count", projectService.count(company, new Status[]{Status.PAUSED, Status.IN_PROCESS}, query, priorities, idProject));
        response.put("inProcess", projectService.findBy(company, new Status[]{Status.PAUSED, Status.IN_PROCESS}, query, priorities, idProject));
        return response;
    }

    @RequestMapping(value = "findTasksFinished", method = RequestMethod.POST)
    @ResponseBody
    public Map findTasksFinished(HttpServletRequest request, @RequestParam String query,
            @RequestParam("priorities[]") Priority[] priorities, @RequestParam Integer idProject) {
        Company company = getCurrentCompany(request);
        Map<String, Object> response = new HashMap<>();
        response.put("count", projectService.count(company, new Status[]{Status.FINISHED}, query, priorities, idProject));
        response.put("finished", projectService.findBy(company, new Status[]{Status.FINISHED}, query, priorities, idProject));
        return response;
    }

    @RequestMapping(value = "toStartTask", method = RequestMethod.POST)
    @ResponseBody
    public String toStartTask(HttpServletRequest request, @RequestParam Integer idTask) {
        try {
            User user = getCurrentUser(request);
            projectService.work(user, idTask, Status.IN_PROCESS);
            return "OK";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @RequestMapping(value = "toFinishTask", method = RequestMethod.POST)
    @ResponseBody
    public String toFinishTask(HttpServletRequest request, @RequestParam Integer idTask) {
        try {
            User user = getCurrentUser(request);
            projectService.work(user, idTask, Status.FINISHED);
            return "OK";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @RequestMapping(value = "toPauseTask", method = RequestMethod.POST)
    @ResponseBody
    public String toPauseTask(HttpServletRequest request, @RequestParam Integer idTask) {
        try {
            User user = getCurrentUser(request);
            projectService.work(user, idTask, Status.PAUSED);
            return "OK";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @RequestMapping(value = "toRestarTask", method = RequestMethod.POST)
    @ResponseBody
    public String toRestarTask(HttpServletRequest request, @RequestParam Integer idTask) {
        try {
            User user = getCurrentUser(request);
            projectService.work(user, idTask, Status.IN_PROCESS);
            return "OK";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @RequestMapping(value = "toAcceptedTask", method = RequestMethod.POST)
    @ResponseBody
    public String toAcceptedTask(HttpServletRequest request, @RequestParam Integer idTask) {
        try {
            Task task = projectService.findTaskBydId(idTask);
            task.setStatus(Status.ACCEPTED);
            projectService.saveOrUpdate(task);
            return "OK";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    
    @RequestMapping(value = "toCancelTask", method = RequestMethod.POST)
    @ResponseBody
    public String toCancelTask(HttpServletRequest request, @RequestParam Integer idTask) {
        try {
            Task task = projectService.findTaskBydId(idTask);
            task.setStatus(Status.PAUSED);
            projectService.saveOrUpdate(task);
            return "OK";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    
    @RequestMapping(value = "findAim", method = RequestMethod.POST)
    @ResponseBody
    public List<Aim> findAim(HttpServletRequest request, @RequestParam Integer idProject) {
        return projectService.findBy(projectService.findBydId(idProject));
    }
    
    @RequestMapping(value = "saveTask", method = RequestMethod.POST)
    @ResponseBody
    public String saveTask(HttpServletRequest request, @RequestParam Integer id, @RequestParam String name,
            @RequestParam String description, @RequestParam Priority priority,
            @RequestParam Integer estimated, @RequestParam String username, @RequestParam Integer idAim){
        try{
            Aim aim = projectService.findAimBydId(idAim);
            User user = userService.findById(username);
            Task task = projectService.findTaskBydId(id);
            if(task == null){
                task = new Task(null, name, description, Status.PENDING, priority,
                    estimated, getCurrentUser(request), user, aim );
            }else{
                task.setName(name);
                task.setDescription(description);
                task.setPriority(priority);
                task.setEstimatedTime(estimated);
                task.setUserOwner(user);
                task.setUserRequester(getCurrentUser(request));
                task.setFather(aim);
            }
            this.projectService.saveOrUpdate(task);
            return "OK";
        }catch(Exception e){
            return e.getMessage();
        }
    }
}
