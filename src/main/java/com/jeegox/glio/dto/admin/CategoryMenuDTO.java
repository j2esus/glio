package com.jeegox.glio.dto.admin;

import com.jeegox.glio.entities.admin.OptionMenu;
import com.jeegox.glio.enumerators.Status;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public class CategoryMenuDTO implements Serializable{
    private Integer id;
    private String name;
    private Integer order;
    private Status status;
    private String icon;
    private String clazz;
    private Set<OptionMenu> optionsMenus = new TreeSet<>();
    
    public CategoryMenuDTO(){
        
    }
    
    public CategoryMenuDTO(Integer id, String name, Integer order, Status status, String icon, String clazz){
        this.id = id;
        this.name = name;
        this.order = order;
        this.status = status;
        this.icon = icon;
        this.clazz = clazz;
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

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Set<OptionMenu> getOptionsMenus() {
        return optionsMenus;
    }

    public void setOptionsMenus(Set<OptionMenu> optionsMenus) {
        this.optionsMenus = optionsMenus;
    }
    
    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
        hash = 97 * hash + Objects.hashCode(this.name);
        hash = 97 * hash + Objects.hashCode(this.order);
        hash = 97 * hash + Objects.hashCode(this.status);
        hash = 97 * hash + Objects.hashCode(this.icon);
        hash = 97 * hash + Objects.hashCode(this.clazz);
        hash = 97 * hash + Objects.hashCode(this.optionsMenus);
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
        final CategoryMenuDTO other = (CategoryMenuDTO) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.icon, other.icon)) {
            return false;
        }
        if (!Objects.equals(this.clazz, other.clazz)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.order, other.order)) {
            return false;
        }
        if (this.status != other.status) {
            return false;
        }
        if (!Objects.equals(this.optionsMenus, other.optionsMenus)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CategoryMenuDTO{" + "id=" + id + ", name=" + name + ", order=" + order 
                + ", status=" + status + ", icon=" + icon + ", clazz=" + clazz + 
                ", optionsMenus=" + optionsMenus + '}';
    }
    
    
}
