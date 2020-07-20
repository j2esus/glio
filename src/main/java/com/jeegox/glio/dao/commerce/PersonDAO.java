package com.jeegox.glio.dao.commerce;

import com.jeegox.glio.dao.hibernate.GenericDAO;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.commerce.Person;
import com.jeegox.glio.enumerators.PersonType;
import com.jeegox.glio.enumerators.Status;
import java.util.List;

public interface PersonDAO extends GenericDAO<Person, Integer> {

    List<Person> findBy(Company company, PersonType personType, String name, String email,
                        String phone, String rfc, Status[] status);
}
