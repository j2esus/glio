package com.jeegox.glio.entities.expenses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.util.JComplexEntity;
import com.jeegox.glio.enumerators.Status;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author j2esus
 */
@Entity
@Table(name = "category")
@JsonIgnoreProperties({"father"})
public class Category extends JComplexEntity<Integer, Company> implements Serializable{
    private String name;
    private Status status;
    
    public Category(){
        
    }
    
    public Category(Integer id, String name, Status status, Company company){
        this.id = id;
        this.name = name;
        this.status = status;
        this.father = company;
    }

    @Id
    @Column(name = "id_category")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Override
    public Integer getId() {
        return id;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "status")
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
