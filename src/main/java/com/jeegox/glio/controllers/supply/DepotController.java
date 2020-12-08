package com.jeegox.glio.controllers.supply;

import com.jeegox.glio.controllers.BaseController;
import com.jeegox.glio.entities.supply.Depot;
import com.jeegox.glio.enumerators.Status;
import com.jeegox.glio.services.supply.DepotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/depot/**")
public class DepotController extends BaseController {
    private final DepotService depotService;

    @Autowired
    public DepotController(DepotService depotService){
        this.depotService = depotService;
    }

    @RequestMapping("init")
    public String index(Model model){
        Status[] status = {Status.ACTIVE, Status.INACTIVE};
        model.addAttribute("status", status);
        return "depot/init";
    }

    @RequestMapping("findByNameAndStatus")
    @ResponseBody
    public List<Depot> findByNameAndStatus(HttpServletRequest request, @RequestParam String name,
                                           @RequestParam Status status){
        return depotService.findByNameAndStatus(getCurrentCompany(request), name, status);
    }

    @RequestMapping("delete")
    @ResponseBody
    public String delete(@RequestParam Integer id){
        try{
            depotService.delete(depotService.findById(id));
            return "OK";
        }catch(Exception e){
            return e.getMessage();
        }
    }

    @RequestMapping("save")
    @ResponseBody
    public String save(HttpServletRequest request, @RequestParam Integer id, @RequestParam String name,
                       @RequestParam Status status){
        try{
            depotService.saveOrUpdate(new Depot(id.equals(0) ? null : id, name, status, getCurrentCompany(request)));
            return "OK";
        }catch(Exception e){
            return e.getMessage();
        }
    }
}
