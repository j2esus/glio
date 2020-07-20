package com.jeegox.glio.controllers.commerce;

import com.jeegox.glio.controllers.BaseController;
import com.jeegox.glio.entities.commerce.Address;
import com.jeegox.glio.entities.commerce.Person;
import com.jeegox.glio.enumerators.PersonType;
import com.jeegox.glio.enumerators.Status;
import com.jeegox.glio.services.PersonService;
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

@Controller
@RequestMapping("/provider/")
public class ProviderController extends BaseController {
    private final PersonService personService;
    private final PersonType personType = PersonType.PROVIDER;

    @Autowired
    public ProviderController(PersonService personService){
        this.personService = personService;
    }

    @RequestMapping("init")
    public String index(HttpServletRequest request, Model model){
        Status[] status = new Status[]{Status.ACTIVE, Status.INACTIVE};
        model.addAttribute("title", "Proveedor");
        model.addAttribute("status", status);
        model.addAttribute("personType", "provider");
        return "client/init";
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> save(HttpServletRequest request, @RequestParam Integer id, @RequestParam String name,
                                       @RequestParam String email, @RequestParam String phone, @RequestParam String rfc,
                                       @RequestParam Status status){
        try{
            personService.save(new Person(id.equals(0) ? null : id, name, email, phone,
                    rfc, personType, status, getCurrentCompany(request)));
            return ResponseEntity.ok("OK");
        }catch(Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(value = "find", method = RequestMethod.POST)
    @ResponseBody
    public List<Person> find(HttpServletRequest request,@RequestParam String name,
                             @RequestParam String email, @RequestParam String phone, @RequestParam String rfc,
                             @RequestParam String status){
        if(status.equals(""))
            return personService.findBy(getCurrentCompany(request), personType, name, email, phone, rfc, new Status[]{Status.ACTIVE, Status.INACTIVE});
        return personService.findBy(getCurrentCompany(request), personType, name, email, phone, rfc, new Status[]{Status.valueOf(status)});
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public String delete(@RequestParam Integer id){
        try{
            personService.changeStatus(personService.findPersonById(id), Status.DELETED);
            return "OK";
        }catch(Exception e){
            return e.getMessage();
        }
    }

    @RequestMapping(value = "findAddress", method = RequestMethod.POST)
    @ResponseBody
    public List<Address> findAddress(@RequestParam Integer idPerson){
        return personService.findBy(personService.findPersonById(idPerson), new Status[]{Status.ACTIVE, Status.INACTIVE});
    }

    @RequestMapping(value = "deleteAddress", method = RequestMethod.POST)
    @ResponseBody
    public String deleteAddress(@RequestParam Integer id){
        try{
            personService.changeStatus(personService.findAddressById(id), Status.DELETED);
            return "OK";
        }catch(Exception e){
            return e.getMessage();
        }
    }

    @RequestMapping(value = "saveAddress", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> saveAddress(@RequestParam Integer id, @RequestParam String address,
                                              @RequestParam String reference, @RequestParam String zipcode, @RequestParam String state,
                                              @RequestParam String town, @RequestParam String suburb,
                                              @RequestParam Status status, @RequestParam Integer idPerson,
                                              @RequestParam Boolean defaultt){
        try{
            personService.save(new Address(id.equals(0) ? null : id, address, reference, zipcode, state, town, suburb, status,
                    personService.findPersonById(idPerson), defaultt));
            return ResponseEntity.ok("OK");
        }catch(Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(value = "setDefaultAddress", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Void> setDefaultAddress(@RequestParam Integer id){
        personService.setDefaultAddress(id);
        return ResponseEntity.ok().build();
    }
}
