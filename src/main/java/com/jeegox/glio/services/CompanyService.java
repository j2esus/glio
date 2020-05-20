package com.jeegox.glio.services;

import com.jeegox.glio.dao.admin.CompanyDAO;
import com.jeegox.glio.dao.admin.OptionMenuDAO;
import com.jeegox.glio.dao.admin.UserDAO;
import com.jeegox.glio.dao.admin.UserTypeDAO;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.admin.User;
import com.jeegox.glio.entities.admin.UserType;
import com.jeegox.glio.enumerators.EntityType;
import com.jeegox.glio.enumerators.Status;
import com.jeegox.glio.util.Util;
import com.jeegox.glio.util.VerifyUtils;
import java.util.HashSet;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompanyService {

    private final CompanyDAO companyDAO;
    private final UserTypeDAO userTypeDAO;
    private final UserDAO userDAO;
    private final OptionMenuDAO optionMenuDAO;

    @Autowired
    public CompanyService(CompanyDAO companyDAO, UserTypeDAO userTypeDAO, UserDAO userDAO, OptionMenuDAO optionMenuDAO) {
        this.companyDAO = companyDAO;
        this.userTypeDAO = userTypeDAO;
        this.userDAO = userDAO;
        this.optionMenuDAO = optionMenuDAO;
    }
    
    @Transactional(readOnly = true)
    public List<Company> findAll() {
        return companyDAO.findAll();
    }

    @Transactional
    public void changeStatus(Company company, Status status) throws Exception {
        company.setStatus(status);
        companyDAO.save(company);
    }

    @Transactional
    public void saveOrUpdate(Company company) throws Exception {
        companyDAO.save(company);
    }

    @Transactional(readOnly = true)
    public List<Company> findByName(String name) {
        return companyDAO.findByName(name);
    }

    @Transactional(readOnly = true)
    public Company findBydId(Integer id) {
        return companyDAO.findById(id);
    }

    @Transactional
    public String register(String captchaResponse, String companyName, String username, String email, String password) throws Exception {
        try {

            boolean valid = VerifyUtils.verify(captchaResponse);
            if (!valid) {
                throw new Exception("Captcha incorrecto");
            }

            Company company = this.findBy(companyName);
            if (company != null) {
                throw new Exception("El nombre de la empresa ya se encuentra registrada, intente con otro nombre.");
            }

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
            String sUsername = username + "@" + companyName;
            User user = new User();
            user.setEmail(email);
            user.setFather(company);
            user.setName("");
            user.setUsername(sUsername);
            user.setOnlyOneAccess(Boolean.FALSE);
            user.setStatus(Status.ACTIVE);
            user.setUserType(userType);
            user.setPassword(Util.encodeSha256(password));

            userDAO.save(user);
            return sUsername;
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public Company findBy(String name) {
        return companyDAO.findBy(name);
    }
}
