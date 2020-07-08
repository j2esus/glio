package com.jeegox.glio.controllers.supply;

import com.jeegox.glio.controllers.BaseController;
import com.jeegox.glio.entities.supply.Size;
import com.jeegox.glio.enumerators.Status;
import com.jeegox.glio.services.SupplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/size/**")
public class SizeController extends BaseController {
    private final SupplyService supplyService;

    @Autowired
    public SizeController(SupplyService supplyService) {
        this.supplyService = supplyService;
    }

    @RequestMapping("init")
    public String index(Model model){
        Status[] status = {Status.ACTIVE, Status.INACTIVE};
        model.addAttribute("status", status);
        return "size/init";
    }

    @RequestMapping("findSizes")
    @ResponseBody
    public List<Size> findSizes(HttpServletRequest request){
        return supplyService.findSizesBy(getCurrentCompany(request), new Status[]{Status.ACTIVE, Status.INACTIVE});
    }

    @RequestMapping("delete")
    @ResponseBody
    public String delete(HttpServletRequest request,@RequestParam Integer id){
        try{
            supplyService.changeStatus(supplyService.findSizeById(id), Status.DELETED);
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
            this.supplyService.saveOrUpdate(new Size(id.equals(0) ? null : id, name, status, getCurrentCompany(request)));
            return "OK";
        }catch(Exception e){
            return e.getMessage();
        }
    }
}
