package com.jeegox.glio.entities;

import com.jeegox.glio.entities.util.JComplexEntity;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "town")
public class Town extends JComplexEntity<Integer, State> implements Serializable{
    private String name;
    private String keyTown;
    
    @Id
    @Column(name = "id_town")
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

    @Column(name = "key_town")
    public String getKeyTown() {
        return keyTown;
    }

    public void setKeyTown(String keyTown) {
        this.keyTown = keyTown;
    }
    
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_state",referencedColumnName = "id_state", nullable = false)
    @Override
    public State getFather() {
        return father;
    }
}
