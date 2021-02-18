package com.jeegox.glio.entities.admin;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.jeegox.glio.entities.util.JComplexEntity;
import com.jeegox.glio.enumerators.Status;
import java.io.Serializable;
import java.sql.Timestamp;
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
@Table(name = "session")
public class Session extends JComplexEntity<Integer, User> implements Serializable{
    private String session;
    private Status status;
    private Timestamp initDate;
    private Timestamp endDate;

    public Session() {
    }

    public Session(Integer id, String session, Status status, Timestamp initDate, Timestamp endDate, User father) {
        this.id = id;
        this.session = session;
        this.status = status;
        this.initDate = initDate;
        this.endDate = endDate;
        this.father = father;
    }

    @Id
    @Column(name = "id_session")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Override
    public Integer getId() {
        return id;
    }

    @Column(name="session", nullable=false)
    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }
    
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user",referencedColumnName = "id_user", nullable=false)
    @Override
    public User getFather() {
        return father;
    }
    
    @Enumerated(EnumType.STRING)
    @Column(name="status", nullable=false)
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    
    @Column(name = "init_date")
    public Timestamp getInitDate() {
        return initDate;
    }

    public void setInitDate(Timestamp initDate) {
        this.initDate = initDate;
    }

    @Column(name = "end_date")
    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Session)) return false;
        Session session1 = (Session) o;
        return Objects.equal(id, session1.id) &&
                Objects.equal(session, session1.session) &&
                status == session1.status &&
                Objects.equal(initDate, session1.initDate) &&
                Objects.equal(endDate, session1.endDate) &&
                Objects.equal(father, session1.father);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, session, status, initDate, endDate, father);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("session", session)
                .add("status", status)
                .add("initDate", initDate)
                .add("endDate", endDate)
                .add("father", father)
                .toString();
    }
}
