package com.jeegox.glio.dto.admin;

import com.jeegox.glio.entities.admin.OptionMenu;
import com.jeegox.glio.enumerators.Status;
import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author j2esus
 */
public class CategoryMenuDTO implements Serializable{
    private Integer id;
    private String name;
    private Integer order;
    private Status status;
    private String icon;
    private Set<OptionMenu> optionsMenus = new TreeSet<>();
    
    public CategoryMenuDTO(){
        
    }
    
    public CategoryMenuDTO(Integer id, String name, Integer order, Status status, String icon){
        this.id = id;
        this.name = name;
        this.order = order;
        this.status = status;
        this.icon = icon;
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
}
