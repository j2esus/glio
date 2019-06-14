package com.jeegox.glio.dao.admin;

import com.jeegox.glio.dao.hibernate.GenericDAO;
import com.jeegox.glio.dto.admin.OptionMenuUserTypeDTO;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.admin.UserType;
import java.util.List;

/**
 *
 * @author j2esus
 */
public interface UserTypeDAO extends GenericDAO<UserType, Integer>{
    
    List<UserType> findByCompany(Company company);
    
    List<OptionMenuUserTypeDTO> findOptionsMenu();
    
    void deleteOptions(Integer idUserType, String[] idsOptions);
    
    void addOption(Integer idUserType, String idOption);
    
    Integer findOption(Integer idUserType, String idOption);
}
