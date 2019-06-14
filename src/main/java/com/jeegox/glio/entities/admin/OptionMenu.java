package com.jeegox.glio.entities.admin;

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

/**
 *
 * @author j2esus
 */
@Entity
@Table(name = "option_menu")
public class OptionMenu extends JComplexEntity<Integer, CategoryMenu> implements Serializable, Comparable<OptionMenu>{
    private String name;
    private Integer order;
    private String url;
    private Status status;
    private String icon;
    private EntityType entityType;
    
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
}
