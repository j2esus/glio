package com.jeegox.glio.services;

import com.jeegox.glio.dao.commerce.AddressDAO;
import com.jeegox.glio.dao.commerce.PersonDAO;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.commerce.Address;
import com.jeegox.glio.entities.commerce.Person;
import com.jeegox.glio.enumerators.PersonType;
import com.jeegox.glio.enumerators.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PersonService {
    private final PersonDAO personDAO;
    private final AddressDAO addressDAO;

    @Autowired
    public PersonService(PersonDAO personDAO, AddressDAO addressDAO){
        this.personDAO = personDAO;
        this.addressDAO = addressDAO;
    }

    @Transactional
    public void save(Person person){
        personDAO.save(person);
    }

    @Transactional(readOnly = true)
    public Person findPersonById(Integer id){
        return personDAO.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Person> findBy(Company company, PersonType personType, String name, String email,
                               String phone, String rfc, Status[] status){
        return personDAO.findBy(company, personType, name, email, phone, rfc, status);
    }

    @Transactional
    public void changeStatus(Person person, Status status){
        person.setStatus(status);
        save(person);
    }

    @Transactional
    public void save(Address address){
        addressDAO.save(address);
    }

    @Transactional(readOnly = true)
    public Address findAddressById(Integer id){
        return addressDAO.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Address> findBy(Person person, Status[] status){
        return addressDAO.findBy(person, status);
    }

    @Transactional
    public void changeStatus(Address address, Status status){
        address.setStatus(status);
        save(address);
    }

    @Transactional
    public void setDefaultAddress(Integer id){
        Address defaultAddress = addressDAO.findById(id);
        List<Address> addresses = addressDAO.findBy(defaultAddress.getFather(), true);
        for(Address address: addresses){
            address.setDefaultt(false);
            addressDAO.save(address);
        }
        defaultAddress.setDefaultt(true);
        addressDAO.save(defaultAddress);
    }
}
