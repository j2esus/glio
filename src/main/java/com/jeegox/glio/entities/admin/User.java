package com.jeegox.glio.entities.admin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jeegox.glio.entities.util.JComplexEntity;
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
import com.jeegox.glio.enumerators.Status;

@Entity
@Table(name = "user")
@JsonIgnoreProperties({"father","hibernateLazyInitializer", "handler"})
public class User extends JComplexEntity<Integer, Company> implements Serializable{
    private String username;
    private String password;
    private String name;
    private Status status;
    private UserType userType;
    private Boolean onlyOneAccess;
    private String email;
    
    public User(){
        
    }
    
    public User(Integer id, String username, String password, String name, Status status, 
            UserType userType, Boolean onlyOneAccess, Company company, String email){
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.status = status;
        this.userType = userType;
        this.onlyOneAccess = onlyOneAccess;
        this.father = company;
        this.email = email;
    }

    @Id
    @Column(name = "id_user")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user_type", referencedColumnName = "id_user_type", nullable = false)
    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    @Column(name = "only_one_access", nullable = false)
    public Boolean getOnlyOneAccess() {
        return onlyOneAccess;
    }

    public void setOnlyOneAccess(Boolean onlyOneAccess) {
        this.onlyOneAccess = onlyOneAccess;
    }

    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
