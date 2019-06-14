package com.jeegox.glio.services.admin;

import com.jeegox.glio.dto.admin.OptionMenuUserTypeDTO;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.admin.UserType;
import com.jeegox.glio.enumerators.Status;
import java.util.List;

/**
 *
 * @author j2esus
 */
public interface UserTypeService {
    
    List<UserType> findAll();
    
    List<UserType> findByCompany(Company company);
    
    UserType findById(Integer id);
    
    void changeStatus(UserType userType, Status status) throws Exception;
    
    void save(Integer id, String name, Status status, Company company) throws Exception;
    
    List<OptionMenuUserTypeDTO> findOptionsMenu(Integer idUserType);
    
    void saveOptions(Integer idUserType, String[] optionsAdd, String[] optionsDel) throws Exception;
}
