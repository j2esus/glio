package com.jeegox.glio.entities.supply;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.util.JComplexEntity;
import com.jeegox.glio.enumerators.Status;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "depot")
@JsonIgnoreProperties({"father","hibernateLazyInitializer", "handler"})
public class Depot extends JComplexEntity<Integer, Company> implements Serializable {
    private String name;
    private Status status;

    public Depot(){

    }

    public Depot(Integer id, String name, Status status, Company father) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.father = father;
    }

    @Id
    @Column(name = "id_depot", nullable = false)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Depot)) return false;
        Depot depot = (Depot) o;
        return Objects.equal(id, depot.id) &&
                Objects.equal(name, depot.name) &&
                Objects.equal(father, depot.father) &&
                status == depot.status;
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
                .add("father", father)
                .toString();
    }
}
