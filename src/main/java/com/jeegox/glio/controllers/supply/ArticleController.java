package com.jeegox.glio.controllers.supply;

import com.jeegox.glio.controllers.BaseController;
import com.jeegox.glio.entities.supply.Article;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/article/**")
public class ArticleController extends BaseController{
    @Autowired
    private SupplyService supplyService;
    
    @RequestMapping("init")
    public String index(Model model){
        Status[] status = {Status.ACTIVE, Status.INACTIVE};
        Unity[] unities = Unity.values();
        model.addAttribute("status", status);
        model.addAttribute("unities", unities);
        return "article/init";
    }
    
    @RequestMapping("findArticles")
    @ResponseBody
    public List<Article> findArticles(HttpServletRequest request){
        return supplyService.findArticlesBy(getCurrentCompany(request), new Status[]{Status.ACTIVE, Status.INACTIVE});
    }
    
    @RequestMapping("deleteArticle")
    @ResponseBody
    public String deleteArticle(HttpServletRequest request,@RequestParam Integer id){
        try{
            supplyService.changeStatus(supplyService.findBydId(id), Status.DELETED);
            return "OK";
        }catch(Exception e){
            return e.getMessage();
        }
    }
    
    @RequestMapping("saveArticle")
    @ResponseBody
    public String saveArticle(HttpServletRequest request, @RequestParam Integer id, @RequestParam String name,
            @RequestParam String sku, @RequestParam String description, @RequestParam Double cost,
            @RequestParam Double price,
            @RequestParam Status status, @RequestParam Unity unity, @RequestParam Integer idCategoryArticle){
        try{
            this.supplyService.saveOrUpdate(new Article(id.equals(0) ? null : id, name, sku, description, cost, 
                    price, status, unity, getCurrentCompany(request), supplyService.findCategoryArticleBydId(idCategoryArticle)));
            return "OK";
        }catch(Exception e){
            return e.getMessage();
        }
    }
    
    @RequestMapping(value = "findByCompany", method = RequestMethod.POST)
    @ResponseBody
    public List<CategoryArticle> findByCompany(HttpServletRequest request, @RequestParam String name){
        return supplyService.findByCompany(getCurrentCompany(request), name);
    }
}
