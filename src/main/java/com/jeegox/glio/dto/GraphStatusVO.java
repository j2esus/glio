package com.jeegox.glio.dto;

import com.jeegox.glio.enumerators.Status;
import java.io.Serializable;

public class GraphStatusVO implements Serializable{
    private Status status;
    private Long quantity;

    public GraphStatusVO(){
        
    }
    
    public GraphStatusVO(Status status, Long quantity){
        this.status = status;
        this.quantity = quantity;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
    
}
