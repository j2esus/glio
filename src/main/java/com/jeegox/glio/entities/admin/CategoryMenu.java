package com.jeegox.glio.entities.admin;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.jeegox.glio.entities.util.JEntity;
import com.jeegox.glio.enumerators.Status;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name = "category_menu")
public class CategoryMenu extends JEntity<Integer> implements Serializable {
    private String name;
    private Integer order;
    private Status status;
    private String icon;
    private String clazz;
    private List<OptionMenu> optionsMenus = new ArrayList<>();

    public CategoryMenu(){

    }

    public CategoryMenu(Integer id, String name, Integer order, Status status, String icon, String clazz) {
        this.id = id;
        this.name = name;
        this.order = order;
        this.status = status;
        this.icon = icon;
        this.clazz = clazz;
    }

    @Id
    @Column(name = "id_category_menu")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Override
    public Integer getId() {
        return id;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "order_category_menu")
    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Column(name = "icon")
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @OrderBy(value = "order")
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "father")
    public List<OptionMenu> getOptionsMenus() {
        return optionsMenus;
    }

    public void setOptionsMenus(List<OptionMenu> optionsMenus) {
        this.optionsMenus = optionsMenus;
    }

    @Column(name = "class")
    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoryMenu)) return false;
        CategoryMenu that = (CategoryMenu) o;
        return Objects.equal(id, that.id) &&
                Objects.equal(name, that.name) &&
                Objects.equal(order, that.order) &&
                status == that.status &&
                Objects.equal(icon, that.icon) &&
                Objects.equal(clazz, that.clazz);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, name, order, status, icon, clazz);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("order", order)
                .add("status", status)
                .add("icon", icon)
                .add("clazz", clazz)
                .toString();
    }
}
