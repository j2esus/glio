package com.jeegox.glio.entities.util;

import java.io.Serializable;

/**
 *
 * @author j2esus
 */
public abstract class JEntity <T extends Serializable> implements Serializable{
    protected T id;
	
    public abstract T getId();
	
    public void setId(T id) {
        this.id = id;
    }   
}
