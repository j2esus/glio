package com.jeegox.glio.controllers.supply;

import com.jeegox.glio.controllers.BaseController;
import com.jeegox.glio.entities.supply.Article;
import com.jeegox.glio.entities.supply.Depot;
import com.jeegox.glio.entities.supply.Stock;
import com.jeegox.glio.enumerators.Status;
import com.jeegox.glio.enumerators.StockType;
import com.jeegox.glio.services.supply.ArticleService;
import com.jeegox.glio.services.supply.DepotService;
import com.jeegox.glio.services.supply.StockService;
import com.jeegox.glio.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/stockIn/**")
public class StockInController extends BaseController {
    private final DepotService depotService;
    private final ArticleService articleService;
    private final StockService stockService;

    @Autowired
    public StockInController(DepotService depotService, ArticleService articleService,
                             StockService stockService){
        this.depotService = depotService;
        this.articleService = articleService;
        this.stockService = stockService;
    }

    @RequestMapping("init")
    public String index(HttpServletRequest request, Model model){
        List<Depot> depots = depotService.findByNameAndStatus(getCurrentCompany(request), "", Status.ACTIVE);
        model.addAttribute("title", "Entradas");
        model.addAttribute("depots", depots);
        model.addAttribute("stockType", "stockIn");
        return "stockIn/init";
    }

    @ResponseBody
    @RequestMapping(value = "findBySkuAndStockRequired", method = RequestMethod.POST)
    public ResponseEntity<Article> findBySkuAndStockRequired(HttpServletRequest request, @RequestParam String sku){
        return Optional.ofNullable(articleService.findBySkuAndStockRequired(getCurrentCompany(request), sku))
                .map(article->ResponseEntity.ok(article))
                .orElseGet(()->new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @ResponseBody
    @RequestMapping(value = "executeMovement", method = RequestMethod.POST)
    public ResponseEntity<Void> executeMovement(HttpServletRequest request, @RequestParam Integer idArticle,
                                   @RequestParam Integer idDepot, @RequestParam Integer quantity,
                                   @RequestParam String description){
        stockService.add(new Stock(Util.getCurrentDate(), getCurrentUser(request), depotService.findById(idDepot),
                articleService.findBydId(idArticle), quantity, description, StockType.IN, getCurrentCompany(request)));
        return ResponseEntity.ok().build();
    }
}
