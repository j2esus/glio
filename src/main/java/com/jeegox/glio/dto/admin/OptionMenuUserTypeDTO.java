package com.jeegox.glio.dto.admin;

import java.io.Serializable;

public class OptionMenuUserTypeDTO implements Serializable{
    private Integer idOptionMenu;
    private String optionMenuName;
    private Integer idCategoryOption;
    private String categoryOptionName;
    private boolean assigned;
    
    public OptionMenuUserTypeDTO(){
        
    }
    
    public OptionMenuUserTypeDTO(Integer idOptionMenu, String optionMenuName, Integer idCategoryOption,
            String categoryOptionName, boolean assigned){
        this.idOptionMenu = idOptionMenu;
        this.optionMenuName = optionMenuName;
        this.idCategoryOption = idCategoryOption;
        this.categoryOptionName = categoryOptionName;
        this.assigned = assigned;
    }

    public Integer getIdOptionMenu() {
        return idOptionMenu;
    }

    public void setIdOptionMenu(Integer idOptionMenu) {
        this.idOptionMenu = idOptionMenu;
    }

    public String getOptionMenuName() {
        return optionMenuName;
    }

    public void setOptionMenuName(String optionMenuName) {
        this.optionMenuName = optionMenuName;
    }

    public Integer getIdCategoryOption() {
        return idCategoryOption;
    }

    public void setIdCategoryOption(Integer idCategoryOption) {
        this.idCategoryOption = idCategoryOption;
    }

    public String getCategoryOptionName() {
        return categoryOptionName;
    }

    public void setCategoryOptionName(String categoryOptionName) {
        this.categoryOptionName = categoryOptionName;
    }

    public boolean isAssigned() {
        return assigned;
    }

    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }
}
