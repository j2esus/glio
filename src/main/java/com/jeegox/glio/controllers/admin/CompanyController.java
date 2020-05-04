package com.jeegox.glio.controllers.admin;

import com.jeegox.glio.controllers.BaseController;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.enumerators.Status;
import com.jeegox.glio.services.CompanyService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/company/**")
public class CompanyController extends BaseController{
    @Autowired
    private CompanyService companyService;
    
    @RequestMapping("")
    public String index(Model model){
        Status[] status = {Status.ACTIVE, Status.DELETED, Status.INACTIVE};
        model.addAttribute("status", status);
        return "company/init";
    }
    
    @RequestMapping("findCompanies")
    @ResponseBody
    public List<Company> findCompanies(){
        return companyService.findAll();
    }
    
    @RequestMapping("deleteCompany")
    @ResponseBody
    public String deleteCompany(HttpServletRequest request,@RequestParam Integer id){
        try{
            this.companyService.changeStatus(companyService.findBydId(id), Status.DELETED);
            return "OK";
        }catch(Exception e){
            return e.getMessage();
        }
    }
    
    @RequestMapping("saveCompany")
    @ResponseBody
    public String saveCompany(HttpServletRequest request, @RequestParam Integer id, @RequestParam String name,
            @RequestParam String description,@RequestParam Status status,@RequestParam Integer totalUser){
        try{
            this.companyService.saveOrUpdate(new Company(id.equals(0) ? null : id, name, description, status, totalUser));
            return "OK";
        }catch(Exception e){
            return e.getMessage();
        }
    }
    
}
