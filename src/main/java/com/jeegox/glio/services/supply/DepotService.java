package com.jeegox.glio.services.supply;

import com.jeegox.glio.dao.supply.DepotDAO;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.supply.Depot;
import com.jeegox.glio.enumerators.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class DepotService {
    private final DepotDAO depotDAO;

    @Autowired
    public DepotService(DepotDAO depotDAO){
        this.depotDAO = depotDAO;
    }

    @Transactional(readOnly = true)
    public Depot findById(Integer id){
        return depotDAO.findById(id);
    }

    @Transactional
    public void delete(Depot depot){
        depot.setStatus(Status.DELETED);
        depotDAO.save(depot);
    }

    @Transactional
    public void saveOrUpdate(Depot depot){
        depotDAO.save(depot);
    }

    @Transactional(readOnly = true)
    public List<Depot> findByNameAndStatus(Company company, String name, Status status){
        if(status != null)
            return depotDAO.findByNameAndStatus(company, name, status);
        return depotDAO.findByName(company, name);
    }

    @Transactional(readOnly = true)
    public Long countByCompany(Company company){
        return depotDAO.countByCompany(company);
    }
}
