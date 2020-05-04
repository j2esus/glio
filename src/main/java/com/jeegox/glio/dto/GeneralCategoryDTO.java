package com.jeegox.glio.dto;

import java.io.Serializable;

public class GeneralCategoryDTO implements Serializable {
    private Integer id;
    private String name;
    private Double amount;
    
    public GeneralCategoryDTO(){
        
    }
    
    public GeneralCategoryDTO(Integer id, String name, Double amount){
        this.id = id;
        this.name = name;
        this.amount = amount;
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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
    
    
}
