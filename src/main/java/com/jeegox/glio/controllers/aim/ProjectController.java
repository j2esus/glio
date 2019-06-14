package com.jeegox.glio.controllers.aim;

import com.jeegox.glio.controllers.BaseController;
import com.jeegox.glio.entities.admin.User;
import com.jeegox.glio.entities.aim.Aim;
import com.jeegox.glio.entities.aim.Project;
import com.jeegox.glio.entities.aim.Task;
import com.jeegox.glio.enumerators.Priority;
import com.jeegox.glio.enumerators.Status;
import com.jeegox.glio.services.admin.UserService;
import com.jeegox.glio.services.aim.AimService;
import com.jeegox.glio.services.aim.ProjectService;
import com.jeegox.glio.services.aim.TaskService;
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
@RequestMapping("/project/**")
public class ProjectController extends BaseController{
    @Autowired
    private ProjectService projectService;
    @Autowired
    private AimService aimService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;
    
    @RequestMapping("init")
    public String index(Model model,HttpServletRequest request){
        Status[] status = {Status.ACTIVE, Status.INACTIVE};
        Status[] statusAim = {Status.ACTIVE, Status.INACTIVE, Status.FINISHED};
        Priority[] priorities = Priority.values();
        List<User> users = userService.findByCompany(getCurrentCompany(request));
        model.addAttribute("status", status);
        model.addAttribute("statusAim", statusAim);
        model.addAttribute("priorities", priorities);
        model.addAttribute("users", users);
        
        return "project/init";
    }
    
    //projects
    @RequestMapping(value = "findProjects", method = RequestMethod.POST)
    @ResponseBody
    public List<Project> findProjects(HttpServletRequest request, @RequestParam String name, @RequestParam String status,
            @RequestParam String description){
        return projectService.findBy(getCurrentUser(request), name, status.equals("")?null:Status.valueOf(status), description);
    }
    
    @RequestMapping(value = "deleteProject", method = RequestMethod.POST)
    @ResponseBody
    public String deleteProject(HttpServletRequest request,@RequestParam Integer id){
        try{
            this.projectService.changeStatus(projectService.findBydId(id), Status.DELETED);
            return "OK";
        }catch(Exception e){
            return e.getMessage();
        }
    }
    
    @RequestMapping(value = "saveProject", method = RequestMethod.POST)
    @ResponseBody
    public String saveProject(HttpServletRequest request, @RequestParam Integer id, @RequestParam String name,
            @RequestParam String description,@RequestParam Status status,@RequestParam String initDate, @RequestParam String endDate){
        try{
            this.projectService.saveOrUpdate(new Project(id.equals(0) ? null : id, name, description, status, 
                    Util.stringToDate(initDate, "yyyy-MM-dd"), Util.stringToDate(endDate, "yyyy-MM-dd"), getCurrentUser(request)));
            return "OK";
        }catch(Exception e){
            return e.getMessage();
        }
    }
    
    //aims
    @RequestMapping(value = "saveAim", method = RequestMethod.POST)
    @ResponseBody
    public String saveAim(HttpServletRequest request, @RequestParam Integer id, @RequestParam String name,
            @RequestParam String description,@RequestParam Status status,@RequestParam String initDate, 
            @RequestParam String endDate, @RequestParam Integer idProject){
        try{
            Project project = projectService.findBydId(idProject);
            this.aimService.saveOrUpdate(new Aim(id.equals(0) ? null : id, name, description, status, 
                    Util.stringToDate(initDate, "yyyy-MM-dd"), Util.stringToDate(endDate, "yyyy-MM-dd"),
                    getCurrentUser(request), project ));
            return "OK";
        }catch(Exception e){
            return e.getMessage();
        }
    }
    
    @RequestMapping(value = "findAims", method = RequestMethod.POST)
    @ResponseBody
    public List<Aim> findAims(HttpServletRequest request, @RequestParam Integer idProject){
        Project project = projectService.findBydId(idProject);
        return aimService.findBy(project);
    }
    
    @RequestMapping(value = "deleteAim", method = RequestMethod.POST)
    @ResponseBody
    public String deleteAim(HttpServletRequest request,@RequestParam Integer id){
        try{
            this.aimService.changeStatus(aimService.findBydId(id), Status.DELETED);
            return "OK";
        }catch(Exception e){
            return e.getMessage();
        }
    }
    
    
    //tasks
    @RequestMapping(value = "saveTask", method = RequestMethod.POST)
    @ResponseBody
    public String saveTask(HttpServletRequest request, @RequestParam Integer id, @RequestParam String name,
            @RequestParam String description, @RequestParam Priority priority,
            @RequestParam Integer estimated, @RequestParam String username, @RequestParam Integer idAim){
        try{
            Aim aim = aimService.findBydId(idAim);
            User user = userService.findById(username);
            Task task = taskService.findBydId(id);
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
            
            this.taskService.saveOrUpdate(task);
            return "OK";
        }catch(Exception e){
            return e.getMessage();
        }
    }
    
    @RequestMapping(value = "findTasks", method = RequestMethod.POST)
    @ResponseBody
    public List<Task> findTasks(HttpServletRequest request, @RequestParam Integer idAim){
        Aim aim = this.aimService.findBydId(idAim);
        return taskService.findBy(aim);
    }
    
    @RequestMapping(value = "deleteTask", method = RequestMethod.POST)
    @ResponseBody
    public String deleteTask(HttpServletRequest request,@RequestParam Integer id){
        try{
            this.taskService.changeStatus(taskService.findBydId(id), Status.DELETED);
            return "OK";
        }catch(Exception e){
            return e.getMessage();
        }
    }
}
