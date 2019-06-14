package com.jeegox.glio.services.admin.impl;

import com.jeegox.glio.dao.admin.OptionMenuDAO;
import com.jeegox.glio.entities.admin.OptionMenu;
import com.jeegox.glio.services.admin.OptionMenuService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author j2esus
 */
@Service
public class OptionMenuServiceImpl implements OptionMenuService{
    @Autowired
    private OptionMenuDAO optionMenuDAO;
    
    @Transactional(readOnly = true)
    @Override
    public List<OptionMenu> findAll() {
        return optionMenuDAO.findAll();
    }
    
}
