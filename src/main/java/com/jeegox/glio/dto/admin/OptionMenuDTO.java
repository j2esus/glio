package com.jeegox.glio.dto.admin;

import com.jeegox.glio.enumerators.Status;
import java.io.Serializable;
import java.util.Objects;

public class OptionMenuDTO implements Serializable{
    private Integer id;
    private String name;
    private Integer order;
    private String url;
    private Status status;
    private Integer idCategory;
    private String category;

    public OptionMenuDTO(Integer id, String name, Integer order, 
            String url, Status status, Integer idCategory, String category) {
        this.id = id;
        this.name = name;
        this.order = order;
        this.url = url;
        this.status = status;
        this.idCategory = idCategory;
        this.category = category;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(Integer idCategory) {
        this.idCategory = idCategory;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.id);
        hash = 29 * hash + Objects.hashCode(this.name);
        hash = 29 * hash + Objects.hashCode(this.order);
        hash = 29 * hash + Objects.hashCode(this.url);
        hash = 29 * hash + Objects.hashCode(this.status);
        hash = 29 * hash + Objects.hashCode(this.idCategory);
        hash = 29 * hash + Objects.hashCode(this.category);
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
        final OptionMenuDTO other = (OptionMenuDTO) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.url, other.url)) {
            return false;
        }
        if (!Objects.equals(this.category, other.category)) {
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
        if (!Objects.equals(this.idCategory, other.idCategory)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "OptionMenuDTO{" + "id=" + id + ", name=" + name + ", order=" + 
                order + ", url=" + url + ", status=" + status + ", idCategory=" + 
                idCategory + ", category=" + category + '}';
    }
    
    
}
