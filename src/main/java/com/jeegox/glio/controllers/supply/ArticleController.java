package com.jeegox.glio.controllers.supply;

import com.jeegox.glio.controllers.BaseController;
import com.jeegox.glio.entities.supply.Article;
import com.jeegox.glio.entities.supply.CategoryArticle;
import com.jeegox.glio.entities.supply.Size;
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
    private final SupplyService supplyService;

    @Autowired
    public ArticleController(SupplyService supplyService) {
        this.supplyService = supplyService;
    }

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
        return supplyService.findArticlesBy(getCurrentCompany(request));
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
            @RequestParam Status status, @RequestParam Unity unity, @RequestParam Integer idCategoryArticle,
            @RequestParam Integer idSize, @RequestParam Boolean requiredStock){
        try{
            this.supplyService.saveOrUpdate(new Article(id.equals(0) ? null : id, name, sku, description, cost, 
                    price, status, unity, getCurrentCompany(request), supplyService.findCategoryArticleBydId(idCategoryArticle),
                    supplyService.findSizeById(idSize), requiredStock));
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

    @RequestMapping(value = "findSizesByCompany", method = RequestMethod.POST)
    @ResponseBody
    public List<Size> findSizesByCompany(HttpServletRequest request, @RequestParam String name){
        return supplyService.findSizesByCompany(getCurrentCompany(request), name);
    }
}
