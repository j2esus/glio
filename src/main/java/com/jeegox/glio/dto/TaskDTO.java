package com.jeegox.glio.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author j2esus
 */
public class TaskDTO implements Serializable {
    private Integer idTask;
    private String name;
    private Integer priority;
    private Integer idUserOwner;
    private Integer idUserRequester;
    private Integer estimatedTime;
    private BigDecimal realTime;

    public Integer getIdTask() {
        return idTask;
    }

    public void setIdTask(Integer idTask) {
        this.idTask = idTask;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getIdUserOwner() {
        return idUserOwner;
    }

    public void setIdUserOwner(Integer idUserOwner) {
        this.idUserOwner = idUserOwner;
    }

    public Integer getIdUserRequester() {
        return idUserRequester;
    }

    public void setIdUserRequester(Integer idUserRequester) {
        this.idUserRequester = idUserRequester;
    }

    public Integer getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(Integer estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public BigDecimal getRealTime() {
        return realTime;
    }

    public void setRealTime(BigDecimal realTime) {
        this.realTime = realTime;
    }
}
