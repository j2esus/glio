package com.jeegox.glio.dao.supply;

import com.jeegox.glio.dao.hibernate.GenericDAO;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.supply.Depot;
import com.jeegox.glio.enumerators.Status;

import java.util.List;

public interface DepotDAO extends GenericDAO<Depot, Integer> {

    List<Depot> findByName(Company company, String name);

    List<Depot> findByNameAndStatus(Company company, String name, Status status);

    Long countByCompany(Company company);
}
