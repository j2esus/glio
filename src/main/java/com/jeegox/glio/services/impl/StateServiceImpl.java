package com.jeegox.glio.services.impl;

import com.jeegox.glio.dao.StateDAO;
import com.jeegox.glio.entities.State;
import com.jeegox.glio.services.StateService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author j2esus
 */
@Service
public class StateServiceImpl implements StateService{
    @Autowired
    private StateDAO stateDAO;

    @Transactional(readOnly = true)
    @Override
    public List<State> findAll() {
        return stateDAO.findAll();
    }
}
