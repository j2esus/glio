package com.jeegox.glio.entities.admin;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
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

    @Column(name = "name", unique = true, nullable = false)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Company)) return false;
        Company company = (Company) o;
        return Objects.equal(id, company.id) &&
                Objects.equal(name, company.name) &&
                Objects.equal(description, company.description) &&
                status == company.status &&
                Objects.equal(totalUser, company.totalUser);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, name, description, status, totalUser);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("description", description)
                .add("status", status)
                .add("totalUser", totalUser)
                .toString();
    }
}
