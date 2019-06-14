package com.jeegox.glio.entities.expenses;

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
@Table(name = "subcategory")
public class Subcategory extends JComplexEntity<Integer, Category> implements Serializable {
    private String name;
    private Status status;
    
    public Subcategory(){
        
    }
    
    public Subcategory(Integer id, String name, Status status, Category category){
        this.id = id;
        this.name = name;
        this.status = status;
        this.father = category;
    }

    @Id
    @Column(name = "id_subcategory")
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

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_category", referencedColumnName = "id_category", nullable = false)
    @Override
    public Category getFather() {
        return father;
    }
}
