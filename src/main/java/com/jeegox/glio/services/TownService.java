package com.jeegox.glio.services;

import com.jeegox.glio.entities.Town;
import java.util.List;

/**
 *
 * @author j2esus
 */
public interface TownService {
    
    List<Town> findByState(Integer idState);
}
