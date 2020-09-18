package com.jeegox.glio.dao.admin;

import com.jeegox.glio.dao.hibernate.GenericDAO;
import com.jeegox.glio.entities.admin.Company;

public interface CompanyDAO extends GenericDAO<Company,Integer>{
    
    Company findByName(String name);
}
