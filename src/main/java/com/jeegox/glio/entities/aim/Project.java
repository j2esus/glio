package com.jeegox.glio.entities.aim;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jeegox.glio.entities.admin.User;
import com.jeegox.glio.entities.util.JComplexEntity;
import com.jeegox.glio.enumerators.Status;
import java.io.Serializable;
import java.util.Date;
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
@Table(name = "project")
@JsonIgnoreProperties({"father","hibernateLazyInitializer", "handler"})
public class Project extends JComplexEntity<Integer, User> implements Serializable {
    private String name;
    private String description;
    private Date initDate;
    private Date endDate;
    private Status status;
    
    public Project(){
        
    }
    
    public Project(Integer id, String name, String description, Status status, Date initDate, Date endDate, User user){
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.initDate = initDate;
        this.endDate = endDate;
        this.father = user;
    }

    @Id
    @Column(name = "id_project")
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

    @Column(name = "init_date")
    public Date getInitDate() {
        return initDate;
    }

    public void setInitDate(Date initDate) {
        this.initDate = initDate;
    }

    @Column(name = "end_date")
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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
    @JoinColumn(name = "id_user",referencedColumnName = "id_user", nullable = false)
    @Override
    public User getFather() {
        return father;
    }    
    
}
