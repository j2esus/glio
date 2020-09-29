package com.jeegox.glio.entities.aim;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jeegox.glio.entities.admin.User;
import com.jeegox.glio.entities.util.JComplexEntity;
import com.jeegox.glio.enumerators.Status;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "aim")
@JsonIgnoreProperties({"father", "user","hibernateLazyInitializer", "handler"})
public class Aim extends JComplexEntity<Integer, Project> implements Serializable{
    private String name;
    private String description;
    private Date initDate;
    private Date endDate;
    private Status status;
    private User user;
    
    public Aim(){
        
    }
    
    public Aim(Integer id, String name, String description, Status status, Date initDate, Date endDate, User user, Project project){
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.initDate = initDate;
        this.endDate = endDate;
        this.father = project;
        this.user = user;
    }
    
    @Id
    @Column(name = "id_aim")
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
    @Temporal(TemporalType.DATE)
    public Date getInitDate() {
        return initDate;
    }

    public void setInitDate(Date initDate) {
        this.initDate = initDate;
    }

    @Column(name = "end_date")
    @Temporal(TemporalType.DATE)
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
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_project",referencedColumnName = "id_project", nullable = false)
    @Override
    public Project getFather() {
        return father;
    }
}
