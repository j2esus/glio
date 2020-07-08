package com.jeegox.glio.entities.supply;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.util.JComplexEntity;
import com.jeegox.glio.enumerators.Status;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "size")
@JsonIgnoreProperties({"father","hibernateLazyInitializer", "handler"})
public class Size extends JComplexEntity<Integer, Company> implements Serializable {
    private String name;
    private Status status;

    public Size(){

    }

    public Size(Integer id, String name, Status status, Company company) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.father = company;
    }

    @Id
    @Column(name = "id_size", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Override
    public Integer getId() {
        return id;
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
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
