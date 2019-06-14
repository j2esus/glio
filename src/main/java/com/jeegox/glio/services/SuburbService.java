package com.jeegox.glio.services;

import com.jeegox.glio.entities.Suburb;
import java.util.List;

/**
 *
 * @author j2esus
 */
public interface SuburbService {
    List<Suburb> findByTown(Integer idTown);
    
    List<Suburb> findByTown(String cp);
}
