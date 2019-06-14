package com.jeegox.glio.services.admin.impl;

import com.jeegox.glio.dao.admin.CompanyDAO;
import com.jeegox.glio.dao.admin.OptionMenuDAO;
import com.jeegox.glio.dao.admin.UserDAO;
import com.jeegox.glio.dao.admin.UserTypeDAO;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.admin.User;
import com.jeegox.glio.entities.admin.UserType;
import com.jeegox.glio.enumerators.EntityType;
import com.jeegox.glio.enumerators.Status;
import com.jeegox.glio.services.admin.CompanyService;
import com.jeegox.glio.util.Util;
import java.util.HashSet;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author j2esus
 */
@Service
public class CompanyServiceImpl implements CompanyService{
    @Autowired
    private CompanyDAO companyDAO;
    @Autowired
    private UserTypeDAO userTypeDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private OptionMenuDAO optionMenuDAO;
    

    @Transactional(readOnly = true)
    @Override
    public List<Company> findAll() {
        return companyDAO.findAll();
    }

    @Transactional
    @Override
    public void changeStatus(Company company, Status status) throws Exception {
        company.setStatus(status);
        this.saveOrUpdate(company);
    }

    @Transactional
    @Override
    public void saveOrUpdate(Company company) throws Exception {
        companyDAO.save(company);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Company> findByName(String name) {
        return companyDAO.findByName(name);
    }

    @Transactional(readOnly = true)
    @Override
    public Company findBydId(Integer id) {
        return companyDAO.findById(id);
    }

    @Transactional
    @Override
    public void register(String companyName, String username, String email, String password) throws Exception {
        try{
            Company company = this.findBy(companyName);
            if(company != null)
                throw new Exception("El nombre de la empresa ya se encuentra registrada, intente con otro nombre.");
            
            //company
            company = new Company();
            company.setName(companyName);
            company.setDescription("");
            company.setStatus(Status.ACTIVE);
            company.setTotalUser(3);
            this.saveOrUpdate(company);
            
            //user type
            UserType userType = new UserType();
            userType.setFather(company);
            userType.setName("admin");
            userType.setStatus(Status.ACTIVE);
            userType.setOptions(new HashSet(optionMenuDAO.findBy(EntityType.PUBLIC)));
            
            userTypeDAO.save(userType);
            //user
            User user = new User();
            user.setEmail(email);
            user.setFather(company);
            user.setName("");
            user.setUsername(username+"@"+companyName);
            user.setOnlyOneAccess(Boolean.FALSE);
            user.setStatus(Status.ACTIVE);
            user.setUserType(userType);
            user.setPassword(Util.encodeSha256(password));
            
            userDAO.save(user);
        }catch(Exception e){
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Company findBy(String name) {
        return companyDAO.findBy(name);
    }
    
}
