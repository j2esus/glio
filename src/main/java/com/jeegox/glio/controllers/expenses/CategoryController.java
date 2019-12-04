package com.jeegox.glio.controllers.expenses;

import com.jeegox.glio.controllers.BaseController;
import com.jeegox.glio.entities.expenses.Category;
import com.jeegox.glio.entities.expenses.Subcategory;
import com.jeegox.glio.enumerators.Status;
import com.jeegox.glio.services.ExpenseService;
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
@RequestMapping("/category/**")
public class CategoryController extends BaseController{
    @Autowired
    private ExpenseService expenseService;
    
    @RequestMapping("init")
    public String index(Model model){
        Status[] status = {Status.ACTIVE, Status.INACTIVE};
        model.addAttribute("status", status);
        return "category/init";
    }
    
    @RequestMapping(value = "findCategories", method = RequestMethod.POST)
    @ResponseBody
    public List<Category> findCategories(HttpServletRequest request, @RequestParam String name){
        return expenseService.findBy(getCurrentCompany(request), new Status[]{Status.ACTIVE, Status.INACTIVE}, name);
    }
    
    @RequestMapping(value = "saveCategory", method = RequestMethod.POST)
    @ResponseBody
    public String saveCategory(HttpServletRequest request, @RequestParam Integer id, @RequestParam String name,@RequestParam Status status){
        try{
            expenseService.saveOrUpdate(new Category(id.equals(0) ? null : id, name, status, getCurrentCompany(request)));
            return "OK";
        }catch(Exception e){
            return e.getMessage();
        }
    }
    
    @RequestMapping(value = "deleteCategory", method = RequestMethod.POST)
    @ResponseBody
    public String deleteCategory(HttpServletRequest request,@RequestParam Integer id){
        try{
            this.expenseService.changeStatus(expenseService.findCategoryBy(id), Status.DELETED);
            return "OK";
        }catch(Exception e){
            return e.getMessage();
        }
    }
    
    @RequestMapping(value = "findSubcategories", method = RequestMethod.POST)
    @ResponseBody
    public List<Subcategory> findSubcategories(HttpServletRequest request, @RequestParam Integer idCategory, @RequestParam String name){
        Category category = this.expenseService.findCategoryBy(idCategory);
        return expenseService.findBy(category, new Status[]{Status.ACTIVE,Status.INACTIVE}, name);
    }

    @RequestMapping(value = "saveSubcategory", method = RequestMethod.POST)
    @ResponseBody
    public String saveSubcategory(HttpServletRequest request, @RequestParam Integer id, @RequestParam String name,@RequestParam Status status,
            @RequestParam Integer idCategory){
        try{
            expenseService.saveOrUpdate(new Subcategory(id.equals(0) ? null : id, name, status, expenseService.findCategoryBy(idCategory)));
            return "OK";
        }catch(Exception e){
            return e.getMessage();
        }
    }
    
    @RequestMapping(value = "deleteSubcategory", method = RequestMethod.POST)
    @ResponseBody
    public String deleteSubcategory(HttpServletRequest request,@RequestParam Integer id){
        try{
            this.expenseService.changeStatus(expenseService.findSubcategoryBy(id), Status.DELETED);
            return "OK";
        }catch(Exception e){
            return e.getMessage();
        }
    }
}
