package com.jeegox.glio.controllers.aim;

import com.jeegox.glio.controllers.BaseController;
import com.jeegox.glio.dto.TaskDTO;
import com.jeegox.glio.entities.admin.User;
import com.jeegox.glio.entities.aim.Aim;
import com.jeegox.glio.entities.aim.Project;
import com.jeegox.glio.entities.aim.Task;
import com.jeegox.glio.enumerators.Priority;
import com.jeegox.glio.enumerators.Status;
import com.jeegox.glio.services.UserService;
import com.jeegox.glio.services.ProjectService;
import com.jeegox.glio.services.aim.TaskService;
import java.util.List;
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
@RequestMapping("/task/**")
public class TaskController extends BaseController {
    private final ProjectService projectService;
    private final UserService userService;
    private final TaskService taskService;

    @Autowired
    public TaskController(ProjectService projectService, UserService userService,
            TaskService taskService) {
        this.projectService = projectService;
        this.userService = userService;
        this.taskService = taskService;
    }

    @RequestMapping("init")
    public String index(Model model, HttpServletRequest request) {
        List<Project> projects = projectService.findByCompany(getCurrentCompany(request), new Status[]{Status.ACTIVE});
        Priority[] priorities = Priority.values();
        model.addAttribute("projects", projects);
        model.addAttribute("priorities", priorities);
        model.addAttribute("status", new Status[]{
            Status.PENDING,
            Status.IN_PROCESS,
            Status.PAUSED,
            Status.FINISHED});
        return "task/init";
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
    public ResponseEntity<Task> toFinishTask(HttpServletRequest request, @RequestParam Integer idTask) {
        return ResponseEntity.ok(taskService.finishTask(taskService.findById(idTask)));
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
    
    @RequestMapping(value = "toCancelTask", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Void> toCancelTask(HttpServletRequest request, @RequestParam Integer idTask) {
        taskService.cancelFinishedTask(taskService.findById(idTask));
        return ResponseEntity.ok().build();
    }
    
    @RequestMapping(value = "saveTask", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Task> saveTask(HttpServletRequest request, @RequestParam Integer id, @RequestParam String name,
            @RequestParam String description, @RequestParam Priority priority,
            @RequestParam Integer estimated, @RequestParam String username, @RequestParam Integer idAim){
        Aim aim = projectService.findAimBydId(idAim);
        User user = userService.findById(username);
        Task task = new Task();
        task.setName(name);
        task.setDescription(description);
        task.setStatus(Status.PENDING);
        task.setPriority(priority);
        task.setEstimatedTime(estimated);
        task.setUserOwner(user);
        task.setUserRequester(getCurrentUser(request));
        task.setFather(aim);
        taskService.createNewTask(task);
        return ResponseEntity.ok(task);
    }
    
    @RequestMapping(value = "countActiveTasksByUserOwner", method = RequestMethod.POST)
    @ResponseBody
    public Long countActiveTasksByUserOwner(HttpServletRequest request) {
        return projectService.countActiveTasksByUserOwner(getCurrentUser(request));
    }
    
    @RequestMapping(value = "countActiveAimsByUserOwner", method = RequestMethod.POST)
    @ResponseBody
    public Long countActiveAimsByUserOwner(HttpServletRequest request) {
        return projectService.countActiveAimsByUserOwner(getCurrentUser(request));
    }
    
    @RequestMapping(value = "countActiveProjectsByUserOwner", method = RequestMethod.POST)
    @ResponseBody
    public Long countActiveProjectsByUserOwner(HttpServletRequest request) {
        return projectService.countActiveProjectsByUserOwner(getCurrentUser(request));
    }
    
    @RequestMapping(value = "findTasks", method = RequestMethod.POST)
    @ResponseBody
    public List<Task> findTasks(HttpServletRequest request, @RequestParam String query,
            @RequestParam("priorities[]") Priority[] priorities, @RequestParam Integer idProject,
            @RequestParam Integer idAim,
            @RequestParam("status[]") Status[] status) {
        return projectService.findByUser(getCurrentUser(request), status, query, 
                priorities, idProject, idAim);
    }
    
    @RequestMapping(value = "findSummaryTime", method = RequestMethod.POST)
    @ResponseBody
    public TaskDTO findSummaryTime(@RequestParam Integer idTask) {
        return projectService.findSummaryTime(idTask);
    }
    
    @RequestMapping(value = "countFinishedByUserOwner", method = RequestMethod.POST)
    @ResponseBody
    public Long countFinishedByUserOwner(HttpServletRequest request) {
        return projectService.countFinishedByUserOwner(getCurrentUser(request));
    }
}
