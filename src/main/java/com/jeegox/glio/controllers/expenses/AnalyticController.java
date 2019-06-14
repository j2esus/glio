package com.jeegox.glio.controllers.expenses;

import com.jeegox.glio.controllers.BaseController;
import com.jeegox.glio.dto.GeneralCategoryDTO;
import com.jeegox.glio.entities.expenses.Category;
import com.jeegox.glio.enumerators.Status;
import com.jeegox.glio.services.expenses.CategoryService;
import com.jeegox.glio.services.expenses.ExpenseService;
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
@RequestMapping("/analytic/**")
public class AnalyticController extends BaseController{
    @Autowired
    private ExpenseService expenseService;
    @Autowired
    private CategoryService categoryService;
    
    @RequestMapping("init")
    public String index(Model model){
        
        return "analytic/init";
    }
    
    @RequestMapping(value = "findDataCategory", method = RequestMethod.POST)
    @ResponseBody
    public List<GeneralCategoryDTO> findDataCategory(HttpServletRequest request){
        return expenseService.findDataCategory(getCurrentCompany(request));
    }
    
    @RequestMapping(value = "findDataSubcategory", method = RequestMethod.POST)
    @ResponseBody
    public List<GeneralCategoryDTO> findDataSubcategory(HttpServletRequest request, @RequestParam Integer idCategory){
        return expenseService.findDataSubcategory(categoryService.findBy(idCategory));
    }
}
