package com.jeegox.glio.entities.aim;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.jeegox.glio.entities.util.JComplexEntity;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "time")
@JsonIgnoreProperties({"father","hibernateLazyInitializer", "handler"})
public class Time extends JComplexEntity<Integer, Task> implements Serializable{
    private Date initDate;
    private Date endDate;

    public Time() {
    }

    public Time(Integer id, Date initDate, Date endDate, Task father) {
        this.id = id;
        this.initDate = initDate;
        this.endDate = endDate;
        this.father = father;
    }

    @Id
    @Column(name = "id_time")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Override
    public Integer getId() {
        return id;
    }
    
    @Column(name = "init_date")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getInitDate() {
        return initDate;
    }

    public void setInitDate(Date initDate) {
        this.initDate = initDate;
    }

    @Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_task", referencedColumnName = "id_task", nullable = false)
    @Override
    public Task getFather() {
        return father;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Time)) return false;
        Time time = (Time) o;
        return Objects.equal(id, time.id) &&
                Objects.equal(initDate, time.initDate) &&
                Objects.equal(endDate, time.endDate) &&
                Objects.equal(father, time.father);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, initDate, endDate, father);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("initDate", initDate)
                .add("endDate", endDate)
                .add("father", father)
                .toString();
    }
}
