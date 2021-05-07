package com.jeegox.glio.entities.aim;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.jeegox.glio.entities.admin.User;
import com.jeegox.glio.entities.util.JComplexEntity;
import com.jeegox.glio.enumerators.Priority;
import com.jeegox.glio.enumerators.Status;
import java.io.Serializable;
import javax.persistence.CascadeType;
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

@Entity
@Table(name = "task")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Task extends JComplexEntity<Integer, Aim> implements Serializable {
    private String name;
    private String description;
    private Status status;
    private Priority priority;
    private Integer estimatedTime;
    private User userRequester;
    private User userOwner;

    public Task(){
        
    }
            
    public Task(Integer id, String name, String description, Status status, Priority priority, Integer estimatedTime, User userRequester, User userOwner, Aim aim) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.estimatedTime = estimatedTime;
        this.userRequester = userRequester;
        this.userOwner = userOwner;
        this.father = aim;
    }
    
    @Id
    @Column(name = "id_task")
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

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "priority", nullable = false)
    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    @Column(name = "estimated_time", nullable = false)
    public Integer getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(Integer estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user_requester",referencedColumnName = "id_user", nullable = false)
    public User getUserRequester() {
        return userRequester;
    }

    public void setUserRequester(User userRequester) {
        this.userRequester = userRequester;
    }

    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user_owner",referencedColumnName = "id_user", nullable = true)
    public User getUserOwner() {
        return userOwner;
    }

    public void setUserOwner(User userOwner) {
        this.userOwner = userOwner;
    }
    
    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "id_aim",referencedColumnName = "id_aim", nullable = false)
    @Override
    public Aim getFather() {
        return father;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        return Objects.equal(id, task.id) &&
                Objects.equal(name, task.name) &&
                Objects.equal(description, task.description) &&
                status == task.status &&
                priority == task.priority &&
                Objects.equal(estimatedTime, task.estimatedTime) &&
                Objects.equal(userRequester, task.userRequester) &&
                Objects.equal(userOwner, task.userOwner) &&
                Objects.equal(father, task.father);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, name, description, status, priority, estimatedTime, userRequester, userOwner, father);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("description", description)
                .add("status", status)
                .add("priority", priority)
                .add("estimatedTime", estimatedTime)
                .add("userRequester", userRequester)
                .add("userOwner", userOwner)
                .add("father", father)
                .toString();
    }
}
