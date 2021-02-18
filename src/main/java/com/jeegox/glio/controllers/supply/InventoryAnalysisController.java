package com.jeegox.glio.controllers.supply;

import com.jeegox.glio.controllers.BaseController;
import com.jeegox.glio.dto.supply.CategoryStockDTO;
import com.jeegox.glio.entities.supply.Depot;
import com.jeegox.glio.services.supply.ArticleService;
import com.jeegox.glio.services.supply.DepotService;
import com.jeegox.glio.services.supply.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/inventoryAnalysis/**")
public class InventoryAnalysisController extends BaseController {
    private final DepotService depotService;
    private final ArticleService articleService;
    private final StockService stockService;

    @Autowired
    public InventoryAnalysisController(DepotService depotService, ArticleService articleService,
                                       StockService stockService){
        this.depotService = depotService;
        this.articleService = articleService;
        this.stockService = stockService;
    }

    @RequestMapping("init")
    public String index(){
        return "inventoryAnalysis/init";
    }

    @RequestMapping(value = "countDepots", method = RequestMethod.POST)
    @ResponseBody
    public Long countDepots(HttpServletRequest request){
        return depotService.countByCompany(getCurrentCompany(request));
    }

    @RequestMapping(value = "countArticles", method = RequestMethod.POST)
    @ResponseBody
    public Long countArticles(HttpServletRequest request){
        return articleService.countWithStockRequired(getCurrentCompany(request));
    }

    @RequestMapping(value = "countArticleUnities", method = RequestMethod.POST)
    @ResponseBody
    public Long countArticleUnities(HttpServletRequest request){
        return stockService.countArticleUnities(getCurrentCompany(request));
    }

    @RequestMapping(value = "getTotalStockValue", method = RequestMethod.POST)
    @ResponseBody
    public Double getTotalStockValue(HttpServletRequest request){
        return stockService.getTotalStockValue(getCurrentCompany(request));
    }

    @RequestMapping(value = "getTotalStockGroupedByCategory", method = RequestMethod.POST)
    @ResponseBody
    public List<CategoryStockDTO> getTotalStockGroupedByCategory(HttpServletRequest request){
        return stockService.getTotalStockGroupedByCategory(getCurrentCompany(request));
    }

    @RequestMapping(value = "getTotalValueStockGroupedByCategory", method = RequestMethod.POST)
    @ResponseBody
    public List<CategoryStockDTO> getTotalValueStockGroupedByCategory(HttpServletRequest request){
        return stockService.getTotalValueStockGroupedByCategory(getCurrentCompany(request));
    }
}
