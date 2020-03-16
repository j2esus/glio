package com.jeegox.glio.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author j2esus
 */
public class MonthDTO implements Serializable{
    private Integer month;
    private String monthName;
    private Double amount;
    
    public MonthDTO(){
        
    }

    public MonthDTO(Integer month, String monthName, Double amount) {
        this.month = month;
        this.monthName = monthName;
        this.amount = amount;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.month);
        hash = 59 * hash + Objects.hashCode(this.monthName);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MonthDTO other = (MonthDTO) obj;
        if (!Objects.equals(this.monthName, other.monthName)) {
            return false;
        }
        if (!Objects.equals(this.month, other.month)) {
            return false;
        }
        return true;
    }

    
    
    
}
