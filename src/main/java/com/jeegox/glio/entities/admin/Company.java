package com.jeegox.glio.entities.admin;

import com.jeegox.glio.entities.util.JEntity;
import com.jeegox.glio.enumerators.Status;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author j2esus
 */
@Entity
@Table(name = "company")
public class Company extends JEntity<Integer> implements Serializable {
    private String name;
    private String description;
    private Status status;
    private Integer totalUser;
    
    public Company(){
        
    }
    
    public Company(Integer id, String name, String description, Status status, Integer totalUser){
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.totalUser = totalUser;
    }

    @Id
    @Column(name = "id_company")
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

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Column(name = "total_user")
    public Integer getTotalUser() {
        return totalUser;
    }

    public void setTotalUser(Integer totalUser) {
        this.totalUser = totalUser;
    }
}
