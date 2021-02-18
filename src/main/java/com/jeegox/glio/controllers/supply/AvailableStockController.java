package com.jeegox.glio.controllers.supply;

import com.jeegox.glio.controllers.BaseController;
import com.jeegox.glio.dto.supply.ArticleStockDTO;
import com.jeegox.glio.services.SupplyService;
import com.jeegox.glio.services.supply.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/availableStock/**")
public class AvailableStockController extends BaseController {
    private final StockService stockService;
    private final SupplyService supplyService;

    @Autowired
    public AvailableStockController(StockService stockService, SupplyService supplyService){
        this.stockService = stockService;
        this.supplyService = supplyService;
    }

    @RequestMapping("init")
    public String index(Model model, HttpServletRequest request){
        model.addAttribute("categories", supplyService.findCategoriesArticlesBy(getCurrentCompany(request)));
        return "availableStock/init";
    }

    @ResponseBody
    @RequestMapping(value = "findAvailableStockGroupedByArticle", method = RequestMethod.POST)
    public List<ArticleStockDTO> findAvailableStockGroupedByArticle(HttpServletRequest request,
            @RequestParam String articleCoincidence, @RequestParam Integer idCategory){
        return stockService.findAvailableStockGroupedByArticle(getCurrentCompany(request), articleCoincidence,
                supplyService.findCategoryArticleBydId(idCategory));
    }
}
