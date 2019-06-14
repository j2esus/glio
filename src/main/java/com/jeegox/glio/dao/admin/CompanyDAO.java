package com.jeegox.glio.dao.admin;

import com.jeegox.glio.dao.hibernate.GenericDAO;
import com.jeegox.glio.entities.admin.Company;
import java.util.List;

/**
 *
 * @author j2esus
 */
public interface CompanyDAO extends GenericDAO<Company,Integer>{
    
    List<Company> findByName(String name);
    
    Company findBy(String name);
}
