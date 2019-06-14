package com.jeegox.glio.services.aim.impl;

import com.jeegox.glio.dao.aim.AimDAO;
import com.jeegox.glio.dto.GraphStatusVO;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.aim.Aim;
import com.jeegox.glio.entities.aim.Project;
import com.jeegox.glio.enumerators.Status;
import com.jeegox.glio.services.aim.AimService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author j2esus
 */
@Service
public class AimServiceImpl implements AimService{
    @Autowired
    private AimDAO aimDAO;

    @Transactional
    @Override
    public void saveOrUpdate(Aim aim) {
        aimDAO.save(aim);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Aim> findByCompany(Company company) {
        return aimDAO.findByCompany(company);
    }

    @Transactional(readOnly = true)
    @Override
    public Aim findBydId(Integer id) {
        return aimDAO.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Aim> findBy(Project project) {
        return aimDAO.findBy(project);
    }

    @Transactional
    @Override
    public void changeStatus(Aim aim, Status status) throws Exception {
        aim.setStatus(status);
        this.saveOrUpdate(aim);
    }

    @Transactional(readOnly = true)
    @Override
    public List<GraphStatusVO> findDataGraphAim(Integer idAim) {
        return aimDAO.findDataGraphAim(idAim);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Aim> findBy(Project project, Status[] status) {
        return aimDAO.findBy(project, status);
    }
    
}
