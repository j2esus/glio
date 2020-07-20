package com.jeegox.glio.entities.commerce;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.util.JComplexEntity;
import com.jeegox.glio.enumerators.PersonType;
import com.jeegox.glio.enumerators.Status;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "person")
@JsonIgnoreProperties({"father","hibernateLazyInitializer", "handler"})
public class Person extends JComplexEntity<Integer, Company> implements Serializable {
    private String name;
    private String email;
    private String phone;
    private String rfc;
    private PersonType personType;
    private Status status;

    public Person(){

    }

    public Person(Integer id, String name, String email, String phone, String rfc, PersonType personType,
                  Status status, Company company){
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.rfc = rfc;
        this.personType = personType;
        this.status = status;
        this.father = company;
    }

    @Id
    @Column(name = "id_person", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Override
    public Integer getId() {
        return id;
    }

    @Column(name = "name", nullable = false, length = 100)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "email", length = 100)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "phone", length = 10)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Column(name = "rfc", length = 30)
    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "person_type", length = 30, nullable = false)
    public PersonType getPersonType() {
        return personType;
    }

    public void setPersonType(PersonType personType) {
        this.personType = personType;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_company", referencedColumnName = "id_company", nullable = false)
    @Override
    public Company getFather() {
        return father;
    }
}
