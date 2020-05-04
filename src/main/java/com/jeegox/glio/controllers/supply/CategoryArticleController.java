package com.jeegox.glio.controllers.supply;

import com.jeegox.glio.controllers.BaseController;
import com.jeegox.glio.entities.supply.CategoryArticle;
import com.jeegox.glio.enumerators.Status;
import com.jeegox.glio.enumerators.Unity;
import com.jeegox.glio.services.SupplyService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/categoryArticle/**")
public class CategoryArticleController extends BaseController{
    @Autowired
    private SupplyService supplyService;
    
    @RequestMapping("init")
    public String index(Model model){
        Status[] status = {Status.ACTIVE, Status.INACTIVE};
        Unity[] unities = Unity.values();
        model.addAttribute("status", status);
        model.addAttribute("unities", unities);
        return "categoryArticle/init";
    }
    
    @RequestMapping("findCategoriesArticles")
    @ResponseBody
    public List<CategoryArticle> findCategoriesArticles(HttpServletRequest request){
        return supplyService.findCategoriesArticlesBy(getCurrentCompany(request), new Status[]{Status.ACTIVE, Status.INACTIVE});
    }
    
    @RequestMapping("deleteCategoryArticle")
    @ResponseBody
    public String deleteCategoryArticle(HttpServletRequest request,@RequestParam Integer id){
        try{
            supplyService.changeStatus(supplyService.findCategoryArticleBydId(id), Status.DELETED);
            return "OK";
        }catch(Exception e){
            return e.getMessage();
        }
    }
    
    @RequestMapping("saveCategoryArticle")
    @ResponseBody
    public String saveCategoryArticle(HttpServletRequest request, @RequestParam Integer id, @RequestParam String name,
            @RequestParam Status status){
        try{
            this.supplyService.saveOrUpdate(new CategoryArticle(id.equals(0) ? null : id, name, status, getCurrentCompany(request)));
            return "OK";
        }catch(Exception e){
            return e.getMessage();
        }
    }
}
