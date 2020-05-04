package com.jeegox.glio.services;

import com.jeegox.glio.dao.StateDAO;
import com.jeegox.glio.dao.SuburbDAO;
import com.jeegox.glio.dao.TownDAO;
import com.jeegox.glio.entities.State;
import com.jeegox.glio.entities.Suburb;
import com.jeegox.glio.entities.Town;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AddressService {
    @Autowired
    private StateDAO stateDAO;
    @Autowired
    private TownDAO townDAO;

    @Transactional(readOnly = true)
    public List<State> findAll() {
        return stateDAO.findAll();
    }
    
    @Autowired
    private SuburbDAO suburbDAO;

    @Transactional(readOnly = true)
    public List<Suburb> findByTown(Integer idTown) {
        return suburbDAO.findByTown(idTown);
    }

    @Transactional(readOnly = true)
    public List<Suburb> findByTown(String cp) {
        return suburbDAO.findByTown(cp);
    }
    
    @Transactional(readOnly = true)
    public List<Town> findByState(Integer idState) {
        return townDAO.findByState(idState);
    }
}
