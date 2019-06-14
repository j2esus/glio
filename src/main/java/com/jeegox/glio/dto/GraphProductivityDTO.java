package com.jeegox.glio.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author j2esus
 */
public class GraphProductivityDTO implements Serializable{
    private Integer idUser;
    private String username;
    private BigDecimal quantity;
    
    public GraphProductivityDTO(){
        
    }

    public GraphProductivityDTO(Integer idUser, String username, BigDecimal quantity) {
        this.idUser = idUser;
        this.username = username;
        this.quantity = quantity;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }
}
