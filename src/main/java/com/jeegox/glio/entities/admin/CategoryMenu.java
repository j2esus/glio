package com.jeegox.glio.entities.admin;

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

/**
 *
 * @author j2esus
 */
@Entity
@Table(name = "category_menu")
public class CategoryMenu extends JEntity<Integer> implements Serializable {
    private String name;
    private Integer order;
    private Status status;
    private String icon;
    private List<OptionMenu> optionsMenus = new ArrayList<>();
    
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
}
