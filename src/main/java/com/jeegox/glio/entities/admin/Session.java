package com.jeegox.glio.entities.admin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

/**
 *
 * @author j2esus
 */
@Entity
@Table(name = "session")
@JsonIgnoreProperties({"father"})
public class Session extends JComplexEntity<Integer, User> implements Serializable{
    private String session;
    private Status status;
    private Timestamp initDate;
    private Timestamp endDate;
    
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
}
