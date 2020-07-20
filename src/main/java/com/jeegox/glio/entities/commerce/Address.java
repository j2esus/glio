package com.jeegox.glio.entities.commerce;

import com.jeegox.glio.entities.util.JComplexEntity;
import com.jeegox.glio.enumerators.Status;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "address")
public class Address extends JComplexEntity<Integer, Person> implements Serializable {
    private String address;
    private String reference;
    private String zipcode;
    private String state;
    private String town;
    private String suburb;
    private Boolean defaultt;
    private Status status;

    public Address(){

    }

    public Address(Integer id, String address, String reference, String zipcode, String state, String town, String suburb,
                   Status status, Person person, Boolean defaultt) {
        this.id = id;
        this.address = address;
        this.reference = reference;
        this.zipcode = zipcode;
        this.state = state;
        this.town = town;
        this.suburb = suburb;
        this.status = status;
        this.defaultt = defaultt;
        this.father = person;
    }

    @Id
    @Column(name = "id_address")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Override
    public Integer getId() {
        return id;
    }

    @Column(name = "address", nullable = false)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name = "reference")
    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    @Column(name = "zipcode", nullable = false)
    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    @Column(name = "state", nullable = false)
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Column(name = "town", nullable = false)
    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    @Column(name = "suburb", nullable = false)
    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    @Column(name = "defaultt", nullable = false)
    public Boolean getDefaultt() {
        return defaultt;
    }

    public void setDefaultt(Boolean defaultt) {
        this.defaultt = defaultt;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_person", nullable = false, referencedColumnName = "id_person")
    @Override
    public Person getFather() {
        return father;
    }
}
