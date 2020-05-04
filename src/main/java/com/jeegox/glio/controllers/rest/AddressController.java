package com.jeegox.glio.controllers.rest;

import com.jeegox.glio.entities.State;
import com.jeegox.glio.entities.Suburb;
import com.jeegox.glio.entities.Town;
import com.jeegox.glio.services.AddressService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/json/address/**")
public class AddressController {
    @Autowired
    private AddressService addressService;
    
    @RequestMapping(value = "findAllStates", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public List<State> findAllStates(){
        return addressService.findAll();
    }
    
    @RequestMapping(value = "findTownByState", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public List<Town> findTownByState(@RequestParam Integer idState){
        return addressService.findByState(idState);
    }
    
    @RequestMapping(value = "findSuburbByTown", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public List<Suburb> findSuburbByTown(@RequestParam Integer idTown){
        return addressService.findByTown(idTown);
    }
    
    @RequestMapping(value = "findSuburbByCp", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public List<Suburb> findSuburbByCp(@RequestParam String cp){
        return addressService.findByTown(cp);
    }
}
