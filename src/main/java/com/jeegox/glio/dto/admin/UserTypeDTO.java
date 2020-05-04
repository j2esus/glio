package com.jeegox.glio.dto.admin;

import com.jeegox.glio.entities.admin.UserType;
import java.io.Serializable;

public class UserTypeDTO implements Serializable{
    private Integer id;
    private String name;
    private String status;
    
    public UserTypeDTO(){
        
    }
    
    public UserTypeDTO(UserType userType){
        this.id = userType.getId();
        this.name = userType.getName();
        this.status = userType.getStatus().name();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
