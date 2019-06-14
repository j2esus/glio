package com.jeegox.glio.services.impl;

import com.jeegox.glio.dao.TownDAO;
import com.jeegox.glio.entities.Town;
import com.jeegox.glio.services.TownService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author j2esus
 */
@Service
public class TownServiceImpl implements TownService{
    @Autowired
    private TownDAO townDAO;

    @Transactional(readOnly = true)
    @Override
    public List<Town> findByState(Integer idState) {
        return townDAO.findByState(idState);
    }
}
