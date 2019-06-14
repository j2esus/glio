package com.jeegox.glio.entities.util;

import java.io.Serializable;

/**
 *
 * @author j2esus
 */
public abstract class JComplexEntity <T extends Serializable, S extends JEntity<?>> extends JEntity<T>
    implements Serializable {
    protected S father;
    
    public abstract S getFather();
    
    public void setFather(S father){
        this.father = father;
    }
}
