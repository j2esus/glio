package com.jeegox.glio.services.aim.impl;

import com.jeegox.glio.dao.aim.ProjectDAO;
import com.jeegox.glio.dto.GraphStatusVO;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.admin.User;
import com.jeegox.glio.entities.aim.Aim;
import com.jeegox.glio.entities.aim.Project;
import com.jeegox.glio.enumerators.Status;
import com.jeegox.glio.services.aim.AimService;
import com.jeegox.glio.services.aim.ProjectService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author j2esus
 */
@Service
public class ProjectServiceImpl implements ProjectService{
    @Autowired
    private ProjectDAO projectDAO;
    @Autowired
    private AimService aimService;

    @Transactional
    @Override
    public void saveOrUpdate(Project project) {
        projectDAO.save(project);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Project> findByCompany(Company company) {
        return projectDAO.findByCompany(company);
    }

    @Transactional(readOnly = true)
    @Override
    public Project findBydId(Integer id) {
        return projectDAO.findById(id);
    }

    @Transactional
    @Override
    public void changeStatus(Project project, Status status) throws Exception {
        project.setStatus(status);
        this.saveOrUpdate(project);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Project> findBy(User user, String name, Status status, String description) {
        return projectDAO.findBy(user, name, status, description);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Project> findBy(User user, String query, Status[] status) {
        return projectDAO.findBy(user, query, status);
    }

    @Transactional(readOnly = true)
    @Override
    public List<GraphStatusVO> findDataGraphProject(Integer idProject) {
        return projectDAO.findDataGraphProject(idProject);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Map> findDataGraphAim(Integer idProject) {
        List<Map> dataMap = new ArrayList<Map>();
        Project project = findBydId(idProject);
        List<Aim> aims = this.aimService.findBy(project, new Status[]{Status.DELETED, Status.INACTIVE});
        List<GraphStatusVO> graphs = null;
        Map<String, Object> map = null;
        for(Aim aim : aims){
            map = new HashMap<>();
            graphs = this.aimService.findDataGraphAim(aim.getId());
            map.put("aim", aim);
            map.put("data", graphs);
            dataMap.add(map);
        }
        return dataMap;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Project> findBy(Company company, String query, Status[] status) {
        return projectDAO.findBy(company, query, status);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Project> findBy(Company company, Status[] status) {
        return projectDAO.findBy(company, status);
    }
}
