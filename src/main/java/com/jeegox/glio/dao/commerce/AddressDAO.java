package com.jeegox.glio.dao.commerce;

import com.jeegox.glio.dao.hibernate.GenericDAO;
import com.jeegox.glio.entities.commerce.Address;
import com.jeegox.glio.entities.commerce.Person;
import com.jeegox.glio.enumerators.Status;
import java.util.List;

public interface AddressDAO extends GenericDAO<Address, Integer> {

    List<Address> findBy(Person person, Status[] status);

    List<Address> findBy(Person person, Boolean defaultt);
}
