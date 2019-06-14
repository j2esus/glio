package com.jeegox.glio.enumerators;

/**
 *
 * @author j2esus
 */
public enum UserTypeEnum {
    SUPER_ADMIN("SuperAdmin"),ADMIN("Admin");
    
    private String name;
    
    UserTypeEnum(String name){
        this.name = name;
    }
    
    public String getName(){
        return name;
    }
}
