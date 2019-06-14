package com.jeegox.glio.dao.admin;

import com.jeegox.glio.dao.hibernate.GenericDAO;
import com.jeegox.glio.entities.admin.OptionMenu;
import com.jeegox.glio.enumerators.EntityType;
import java.util.List;

/**
 *
 * @author j2esus
 */
public interface OptionMenuDAO extends GenericDAO<OptionMenu,Integer>{
    
    List<OptionMenu> findBy(EntityType entityType);
}
