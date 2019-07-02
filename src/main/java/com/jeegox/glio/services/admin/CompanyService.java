package com.jeegox.glio.services.admin;

import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.enumerators.Status;
import java.util.List;

/**
 *
 * @author j2esus
 */
public interface CompanyService {
    
    List<Company> findAll();
    
    void changeStatus(Company company,Status status) throws Exception;
    
    void saveOrUpdate(Company company) throws Exception;
    
    List<Company> findByName(String name);
    
    Company findBydId(Integer id);
    
    String register(String captchaResponse, String companyName, String username, String email, String password) throws Exception;
    
    Company findBy(String name);
    
}
