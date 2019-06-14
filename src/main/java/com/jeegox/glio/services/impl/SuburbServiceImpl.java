package com.jeegox.glio.services.impl;

import com.jeegox.glio.dao.SuburbDAO;
import com.jeegox.glio.entities.Suburb;
import com.jeegox.glio.services.SuburbService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author j2esus
 */
@Service
public class SuburbServiceImpl implements SuburbService {
    @Autowired
    private SuburbDAO suburbDAO;

    @Transactional(readOnly = true)
    @Override
    public List<Suburb> findByTown(Integer idTown) {
        return suburbDAO.findByTown(idTown);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Suburb> findByTown(String cp) {
        return suburbDAO.findByTown(cp);
    }
    
}
