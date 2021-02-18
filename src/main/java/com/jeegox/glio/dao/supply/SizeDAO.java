package com.jeegox.glio.dao.supply;

import com.jeegox.glio.dao.hibernate.GenericDAO;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.supply.Size;

import java.util.List;

public interface SizeDAO extends GenericDAO<Size, Integer> {

    List<Size> findByCompany(Company company);

    List<Size> findByCompany(Company company, String nameLike);
}
