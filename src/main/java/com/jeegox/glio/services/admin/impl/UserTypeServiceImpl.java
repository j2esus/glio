package com.jeegox.glio.services.admin.impl;

import com.jeegox.glio.dao.admin.UserTypeDAO;
import com.jeegox.glio.dto.admin.OptionMenuUserTypeDTO;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.admin.OptionMenu;
import com.jeegox.glio.entities.admin.UserType;
import com.jeegox.glio.enumerators.Status;
import com.jeegox.glio.services.admin.UserTypeService;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author j2esus
 */
@Service
public class UserTypeServiceImpl implements UserTypeService {
    @Autowired
    private UserTypeDAO userTypeDAO;
    
    @Transactional(readOnly = true)
    @Override
    public List<UserType> findAll() {
        return userTypeDAO.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserType> findByCompany(Company company) {
        return userTypeDAO.findByCompany(company);
    }

    @Transactional(readOnly = true)
    @Override
    public UserType findById(Integer id) {
        return userTypeDAO.findById(id);
    }

    @Transactional
    @Override
    public void changeStatus(UserType userType, Status status) throws Exception {
        userType.setStatus(status);
        userTypeDAO.save(userType);
    }

    @Transactional
    @Override
    public void save(Integer id, String name, Status status, Company company) throws Exception {
        UserType userType = this.findById(id);
        if(userType == null){
            userType = new UserType(null, name, status, company);
        }else{
            userType.setName(name);
            userType.setStatus(status);
        }
        userTypeDAO.save(userType);
    }

    @Transactional(readOnly = true)
    @Override
    public List<OptionMenuUserTypeDTO> findOptionsMenu(Integer idUserType) {
        UserType userType = userTypeDAO.findById(idUserType);
        List<OptionMenuUserTypeDTO> options = userTypeDAO.findOptionsMenu();
        Set<OptionMenu> optionUser = userType.getOptions();
        Iterator<OptionMenu> it = optionUser.iterator();
        while(it.hasNext()){
            OptionMenu om = it.next();
            for(OptionMenuUserTypeDTO item : options){
                if(item.getIdOptionMenu().equals(om.getId())){
                    item.setAssigned(true);
                    continue;
                }
            }
            it.remove();
        }
        return options;
    }

    @Transactional
    @Override
    public void saveOptions(Integer idUserType, String[] optionsAdd, String[] optionsDel) throws Exception {
        if(optionsAdd.length > 0){
            for(String option: optionsAdd){
                if(userTypeDAO.findOption(idUserType, option).intValue() == 0)
                    userTypeDAO.addOption(idUserType, option);
            }
        }
        
        if(optionsDel.length > 0){
            userTypeDAO.deleteOptions(idUserType, optionsDel);
        }
    }
}
