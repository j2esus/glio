package com.jeegox.glio.entities.admin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.jeegox.glio.entities.util.JComplexEntity;
import com.jeegox.glio.enumerators.Status;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name = "user_type")
@JsonIgnoreProperties({"father","options"})
public class UserType extends JComplexEntity<Integer, Company> implements Serializable {
    private String name;
    private Status status;
    private Set<OptionMenu> options = new HashSet<>();
    
    public UserType(){
        
    }
    
    public UserType(Integer id, String name, Status status, Company company){
        this.id = id;
        this.name = name;
        this.status = status;
        this.father = company;
    }

    @Id
    @Column(name = "id_user_type")
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

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
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

    @OrderBy(value = "order")
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "user_type_option", joinColumns = {
            @JoinColumn(name = "id_user_type", nullable = false) },
            inverseJoinColumns = { @JoinColumn(name = "id_option_menu",
                            nullable = false) })
    public Set<OptionMenu> getOptions() {
        return options;
    }

    public void setOptions(Set<OptionMenu> options) {
        this.options = options;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserType)) return false;
        UserType userType = (UserType) o;
        return Objects.equal(id, userType.id) &&
                Objects.equal(name, userType.name) &&
                status == userType.status &&
                Objects.equal(father, userType.father);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, name, status, father);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("status", status)
                .add("options", options)
                .add("father", father)
                .toString();
    }
}
