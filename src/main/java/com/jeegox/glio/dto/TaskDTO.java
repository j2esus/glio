package com.jeegox.glio.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class TaskDTO implements Serializable {
    private Integer idTask;
    private String name;
    private Integer priority;
    private Integer idUserOwner;
    private Integer idUserRequester;
    private Integer estimatedTime;
    private BigDecimal realTime;

    public TaskDTO() {
    }

    public TaskDTO(Integer idTask, String name, Integer priority, Integer idUserOwner,
            Integer idUserRequester, Integer estimatedTime, BigDecimal realTime) {
        this.idTask = idTask;
        this.name = name;
        this.priority = priority;
        this.idUserOwner = idUserOwner;
        this.idUserRequester = idUserRequester;
        this.estimatedTime = estimatedTime;
        this.realTime = realTime;
    }
    
    

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

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.idTask);
        hash = 67 * hash + Objects.hashCode(this.name);
        hash = 67 * hash + Objects.hashCode(this.priority);
        hash = 67 * hash + Objects.hashCode(this.idUserOwner);
        hash = 67 * hash + Objects.hashCode(this.idUserRequester);
        hash = 67 * hash + Objects.hashCode(this.estimatedTime);
        hash = 67 * hash + Objects.hashCode(this.realTime);
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
        final TaskDTO other = (TaskDTO) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.idTask, other.idTask)) {
            return false;
        }
        if (!Objects.equals(this.priority, other.priority)) {
            return false;
        }
        if (!Objects.equals(this.idUserOwner, other.idUserOwner)) {
            return false;
        }
        if (!Objects.equals(this.idUserRequester, other.idUserRequester)) {
            return false;
        }
        if (!Objects.equals(this.estimatedTime, other.estimatedTime)) {
            return false;
        }
        if (!Objects.equals(this.realTime, other.realTime)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TaskDTO{" + "idTask=" + idTask + ", name=" + name + ", priority=" + priority 
                + ", idUserOwner=" + idUserOwner + ", idUserRequester=" + idUserRequester + ", estimatedTime=" 
                + estimatedTime + ", realTime=" + realTime + '}';
    }
    
    
}
