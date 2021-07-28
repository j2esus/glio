package com.jeegox.glio.entities.admin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.jeegox.glio.entities.util.JComplexEntity;
import com.jeegox.glio.enumerators.EntityType;
import com.jeegox.glio.enumerators.Status;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "option_menu")
@JsonIgnoreProperties("father")
public class OptionMenu extends JComplexEntity<Integer, CategoryMenu> implements Serializable, Comparable<OptionMenu>{
    private String name;
    private Integer order;
    private String url;
    private Status status;
    private String icon;
    private EntityType entityType;

    public OptionMenu() {
    }

    public OptionMenu(Integer id, String name, Integer order, String url, Status status, String icon, EntityType entityType,
                      CategoryMenu father) {
        this.id = id;
        this.name = name;
        this.order = order;
        this.url = url;
        this.status = status;
        this.icon = icon;
        this.entityType = entityType;
        this.father = father;
    }

    @Id
    @Column(name = "id_option_menu")
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

    @Column(name = "order_option_menu")
    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    @Column(name = "url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "entity_type", nullable = false)
    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    @Column(name = "icon")
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_category_menu",referencedColumnName = "id_category_menu", nullable = false)
    @Override
    public CategoryMenu getFather() {
        return father;
    }

    @Override
    public int compareTo(OptionMenu o) {
        return this.order-o.getOrder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OptionMenu)) return false;
        OptionMenu that = (OptionMenu) o;
        return Objects.equal(id, that.id) &&
                Objects.equal(name, that.name) &&
                Objects.equal(order, that.order) &&
                Objects.equal(url, that.url) &&
                status == that.status &&
                Objects.equal(icon, that.icon) &&
                entityType == that.entityType &&
                Objects.equal(father, that.father);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, name, order, url, status, icon, entityType, father);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("order", order)
                .add("url", url)
                .add("status", status)
                .add("icon", icon)
                .add("entityType", entityType)
                .add("father", father)
                .toString();
    }
}
