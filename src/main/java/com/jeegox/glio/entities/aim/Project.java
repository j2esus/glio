package com.jeegox.glio.entities.aim;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.jeegox.glio.entities.admin.User;
import com.jeegox.glio.entities.util.JComplexEntity;
import com.jeegox.glio.enumerators.Status;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

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
    @Column(name = "id_project", nullable = false)
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

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "init_date", nullable = false)
    @Temporal(TemporalType.DATE)
    public Date getInitDate() {
        return initDate;
    }

    public void setInitDate(Date initDate) {
        this.initDate = initDate;
    }

    @Column(name = "end_date", nullable = false)
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
    @Override
    public User getFather() {
        return father;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Project)) return false;
        Project project = (Project) o;
        return Objects.equal(id, project.id) &&
                Objects.equal(name, project.name) &&
                Objects.equal(description, project.description) &&
                Objects.equal(initDate, project.initDate) &&
                Objects.equal(endDate, project.endDate) &&
                Objects.equal(father, project.father) &&
                status == project.status;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, name, description, initDate, endDate, status, father);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("description", description)
                .add("initDate", initDate)
                .add("endDate", endDate)
                .add("status", status)
                .add("father", father)
                .toString();
    }
}
