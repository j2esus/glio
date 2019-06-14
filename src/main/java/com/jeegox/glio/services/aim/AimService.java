package com.jeegox.glio.services.aim;

import com.jeegox.glio.dto.GraphStatusVO;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.aim.Aim;
import com.jeegox.glio.entities.aim.Project;
import com.jeegox.glio.enumerators.Status;
import java.util.List;

/**
 *
 * @author j2esus
 */
public interface AimService {

    void saveOrUpdate(Aim aim);
    
    List<Aim> findByCompany(Company company);
    
    Aim findBydId(Integer id);
    
    List<Aim> findBy(Project project);
    
    void changeStatus(Aim aim, Status status) throws Exception;
    
    List<GraphStatusVO> findDataGraphAim(Integer idAim);
    
    List<Aim> findBy(Project project,Status[] status);
}
