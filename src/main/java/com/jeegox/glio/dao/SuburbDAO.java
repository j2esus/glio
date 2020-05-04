package com.jeegox.glio.dao;

import com.jeegox.glio.dao.hibernate.GenericDAO;
import com.jeegox.glio.entities.Suburb;
import java.util.List;

public interface SuburbDAO extends GenericDAO<Suburb, Integer>{
    
    List<Suburb> findByTown(Integer idTown);
    
    List<Suburb> findByTown(String cp);
}
