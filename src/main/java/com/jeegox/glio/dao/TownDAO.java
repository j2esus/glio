package com.jeegox.glio.dao;

import com.jeegox.glio.dao.hibernate.GenericDAO;
import com.jeegox.glio.entities.Town;
import java.util.List;

/**
 *
 * @author j2esus
 */
public interface TownDAO extends GenericDAO<Town, Integer>{
    
    List<Town> findByState(Integer idState);
}
