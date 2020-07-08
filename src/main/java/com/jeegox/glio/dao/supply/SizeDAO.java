package com.jeegox.glio.dao.supply;

import com.jeegox.glio.dao.hibernate.GenericDAO;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.supply.Size;
import com.jeegox.glio.enumerators.Status;

import java.util.List;

public interface SizeDAO extends GenericDAO<Size, Integer> {

    List<Size> findSizesBy(Company company, Status[] status);

    List<Size> findByCompany(Company company, String nameLike);
}
