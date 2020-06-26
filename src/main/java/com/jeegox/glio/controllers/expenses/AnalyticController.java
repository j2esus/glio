package com.jeegox.glio.controllers.expenses;

import com.jeegox.glio.controllers.BaseController;
import com.jeegox.glio.dto.GeneralCategoryDTO;
import com.jeegox.glio.dto.MonthDTO;
import com.jeegox.glio.entities.expenses.Category;
import com.jeegox.glio.entities.expenses.Subcategory;
import com.jeegox.glio.enumerators.Status;
import com.jeegox.glio.services.ExpenseService;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.omg.CosNaming.NamingContextExtPackage.AddressHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/analytic/**")
public class AnalyticController extends BaseController{
    @Autowired
    private ExpenseService expenseService;
    
    @RequestMapping("init")
    public String index(Model model, HttpServletRequest request){
        List<Category> categories = expenseService.findBy(getCurrentCompany(request), new Status[]{Status.ACTIVE}, "");
        model.addAttribute("categories", categories);
        model.addAttribute("dates", expenseService.yearsExpenses(getCurrentCompany(request)));
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
        return expenseService.findDataSubcategory(expenseService.findCategoryBy(idCategory));
    }
    
    @RequestMapping(value = "getMonthAmounts", method = RequestMethod.POST)
    @ResponseBody
    public List<MonthDTO> getMonthAmounts(HttpServletRequest request, @RequestParam Integer year){
        return expenseService.getMonthAmounts(getCurrentCompany(request), year);
    }
    
    @RequestMapping(value = "findDataCategoryYear", method = RequestMethod.POST)
    @ResponseBody
    public List<GeneralCategoryDTO> findDataCategoryYear(HttpServletRequest request, @RequestParam Integer year){
        return expenseService.findDataCategory(getCurrentCompany(request), year);
    }
    
    @RequestMapping(value = "findDataCategoryYearMonth", method = RequestMethod.POST)
    @ResponseBody
    public List<GeneralCategoryDTO> findDataCategoryYearMonth(HttpServletRequest request, @RequestParam Integer year, @RequestParam Integer month){
        return expenseService.findDataCategory(getCurrentCompany(request), year, month);
    }
    
    @RequestMapping(value = "findDataSubcategoryYearMonth", method = RequestMethod.POST)
    @ResponseBody
    public List<GeneralCategoryDTO> findDataSubcategoryYearMonth(HttpServletRequest request, @RequestParam Integer idCategory,
            @RequestParam Integer year, @RequestParam Integer month){
        return expenseService.findDataSubcategory(expenseService.findCategoryBy(idCategory), year, month);
    }

    @RequestMapping(value = "findDataCategoryAllYears", method = RequestMethod.POST)
    @ResponseBody
    public Map<Integer, List<MonthDTO>> findDataCategoryAllYears(HttpServletRequest request, @RequestParam Integer idCategory, Integer idSubcategory){
        return expenseService.findDataCategoryAllYears(getCurrentCompany(request), idCategory, idSubcategory);
    }

    @RequestMapping(value = "findSubcategories", method = RequestMethod.POST)
    @ResponseBody
    public List<Subcategory> findSubcategories(HttpServletRequest request, @RequestParam Integer idCategory){
        return expenseService.findBy(expenseService.findCategoryBy(idCategory), new Status[]{Status.ACTIVE}, "");
    }
}
