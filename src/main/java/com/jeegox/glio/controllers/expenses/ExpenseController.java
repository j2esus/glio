package com.jeegox.glio.controllers.expenses;

import com.jeegox.glio.controllers.BaseController;
import com.jeegox.glio.entities.expenses.Category;
import com.jeegox.glio.entities.expenses.Expense;
import com.jeegox.glio.entities.expenses.Subcategory;
import com.jeegox.glio.enumerators.Status;
import com.jeegox.glio.services.ExpenseService;
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

@Controller
@RequestMapping("/expense/**")
public class ExpenseController extends BaseController {
    private final ExpenseService expenseService;

    @Autowired
    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @RequestMapping("init")
    public String index(HttpServletRequest request, Model model){
        List<Category> categories = expenseService.findBy(getCurrentCompany(request), new Status[]{Status.ACTIVE}, "");
        model.addAttribute("categories", categories);
        return "expense/init";
    }
    
    @RequestMapping(value = "findSubcategories", method = RequestMethod.POST)
    @ResponseBody
    public List<Subcategory> findSubcategories(HttpServletRequest request, @RequestParam Integer idCategory){
        return expenseService.findBy(expenseService.findCategoryBy(idCategory), new Status[]{Status.ACTIVE}, "");
    }
    
    @RequestMapping(value = "saveExpense", method = RequestMethod.POST)
    @ResponseBody
    public String saveExpense(HttpServletRequest request, @RequestParam Integer id, @RequestParam Double amount, @RequestParam String description,
            @RequestParam Integer idSubcategory, @RequestParam String dateE){
        try{
            expenseService.saveOrUpdate(new Expense(id.equals(0) ? null : id, amount, description, expenseService.findSubcategoryBy(idSubcategory), 
            Util.stringToDate(dateE, "yyyy-MM-dd"), Status.ACTIVE, getCurrentUser(request), getCurrentCompany(request)));
            return "OK";
        }catch(Exception e){
            return e.getMessage();
        }
    }
    
    @RequestMapping(value = "findExpenses", method = RequestMethod.POST)
    @ResponseBody
    public List<Expense> findExpenses(HttpServletRequest request, @RequestParam String initDate,
            @RequestParam String endDate, @RequestParam Integer idCategory, @RequestParam Integer idSubcategory, @RequestParam String description){
        return expenseService.findBy(getCurrentCompany(request), new Status[]{Status.ACTIVE}, idCategory, idSubcategory, 
                Util.stringToDate(initDate, "yyyy-MM-dd"), Util.stringToDate(endDate, "yyyy-MM-dd"), description);
    }
    
    @RequestMapping("deleteExpense")
    @ResponseBody
    public String deleteExpense(HttpServletRequest request,@RequestParam Integer id){
        try{
            this.expenseService.changeStatus(expenseService.findBy(id), Status.DELETED);
            return "OK";
        }catch(Exception e){
            return e.getMessage();
        }
    }
    
}
