package com.jeegox.glio.entities.admin;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.jeegox.glio.entities.util.JComplexEntity;
import com.jeegox.glio.enumerators.Status;
import java.io.Serializable;
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
@Table(name = "token")
public class Token extends JComplexEntity<Integer, User> implements Serializable{
    private String token;
    private Status status;

    public Token() {
    }

    public Token(Integer id, String token, Status status, User father) {
        this.id = id;
        this.token = token;
        this.status = status;
        this.father = father;
    }

    @Id
    @Column(name = "id_token")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Override
    public Integer getId() {
        return id;
    }

    @Column(name = "token", nullable = false)
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Enumerated(EnumType.STRING)
    @Column(name="status",nullable=false)
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user",referencedColumnName = "id_user",nullable=false)
    @Override
    public User getFather() {
        return father;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Token)) return false;
        Token token1 = (Token) o;
        return Objects.equal(id, token1.id) &&
                Objects.equal(token, token1.token) &&
                status == token1.status &&
                Objects.equal(father, token1.father);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, token, status, father);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("token", token)
                .add("status", status)
                .add("father", father)
                .toString();
    }
}
