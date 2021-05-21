package com.jeegox.glio.services.admin;

import static com.google.common.truth.Truth.assertThat;
import com.jeegox.glio.dto.admin.CategoryMenuDTO;
import com.jeegox.glio.entities.admin.CategoryMenu;
import com.jeegox.glio.entities.admin.OptionMenu;
import com.jeegox.glio.enumerators.EntityType;
import com.jeegox.glio.enumerators.Status;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;

public class AppMenuServiceTest {
    private AppMenuService appMenuService;
    
    @Before
    public void setUp(){
        appMenuService = new AppMenuService();
    }
    
    @Test
    public void transformAllowedOptionsToMap_twoOptionsMenu_mapWithFourOptions(){
        CategoryMenu category = new CategoryMenu(2, "Tareas", 2, Status.ACTIVE, 
                "fa-list", "bg-warning");
        Set<OptionMenu> options = new HashSet<>();
        options.add(new OptionMenu(6, "Proyecto", 1, "/project/init", 
                Status.ACTIVE, "fa-cogs", EntityType.PUBLIC, category));
        options.add(new OptionMenu(7, "Tareas", 2, "/task/init", 
                Status.ACTIVE, "fa-tasks", EntityType.PUBLIC, category));
        
        Map<String, String> optionsMap = appMenuService.transformAllowedOptionsToMap(options);
        assertThat(optionsMap).isEqualTo(responseWithFourOptionsMap());
    }
    
    private Map<String, String> responseWithFourOptionsMap(){
        Map<String, String> responseOptions = new HashMap<>();
        responseOptions.put("project", "/project/init");
        responseOptions.put("task", "/task/init");
        responseOptions.put("resources", "");
        responseOptions.put("all", "");
        return responseOptions;
    }
    
    @Test
    public void transformAllowedOptionsToMap_threeOptionsMenu_mapWithFiveOptions() {
        CategoryMenu category = new CategoryMenu(2, "Tareas", 2, Status.ACTIVE, 
                "fa-list", "bg-warning");
        Set<OptionMenu> options = new HashSet<>();
        options.add(new OptionMenu(6, "Proyecto", 1, "/project/init", 
                Status.ACTIVE, "fa-cogs", EntityType.PUBLIC, category));
        options.add(new OptionMenu(7, "Tareas", 2, "/task/init", 
                Status.ACTIVE, "fa-tasks", EntityType.PUBLIC, category));
        options.add(new OptionMenu(8, "Avances", 3, "/advance/init", 
                Status.ACTIVE, "fa-pie-chart", EntityType.PUBLIC, category));
        
        Map<String, String> optionsMap = appMenuService.transformAllowedOptionsToMap(options);
        assertThat(optionsMap).isEqualTo(responseWithFiveOptionsMap());
    }

    private Map<String, String> responseWithFiveOptionsMap() {
        Map<String, String> responseOptions = new HashMap<>();
        responseOptions.put("project", "/project/init");
        responseOptions.put("task", "/task/init");
        responseOptions.put("advance", "/advance/init");
        responseOptions.put("resources", "");
        responseOptions.put("all", "");
        return responseOptions;
    }
    
    @Test 
    public void transformOptionsMenu_oneCategoryTwoOptions(){
        CategoryMenu category = new CategoryMenu(2, "Tareas", 2, Status.ACTIVE, 
                "fa-list", "bg-warning");
        Set<OptionMenu> options = new HashSet<>();
        options.add(new OptionMenu(6, "Proyecto", 1, "/project/init", 
                Status.ACTIVE, "fa-cogs", EntityType.PUBLIC, category));
        options.add(new OptionMenu(7, "Tareas", 2, "/task/init", 
                Status.ACTIVE, "fa-tasks", EntityType.PUBLIC, category));
        
        List<CategoryMenuDTO> optionsDTO = appMenuService.transformOptionsMenu(options);
        assertThat(optionsDTO).isEqualTo(responseWithOneCategoryAndTwoOptions(category));
    }
    
    private List<CategoryMenuDTO> responseWithOneCategoryAndTwoOptions(CategoryMenu categoryMenu){
        List<CategoryMenuDTO> categories = new ArrayList<>();
        CategoryMenuDTO categoryMenuDTO = new CategoryMenuDTO(2, "Tareas", 2, 
                Status.ACTIVE, "fa-list", "bg-warning");
        Set<OptionMenu> options = new HashSet<>();
        options.add(new OptionMenu(6, "Proyecto", 1, "/project/init",
                Status.ACTIVE, "fa-cogs", EntityType.PUBLIC, categoryMenu));
        options.add(new OptionMenu(7, "Tareas", 2, "/task/init",
                Status.ACTIVE, "fa-tasks", EntityType.PUBLIC, categoryMenu));
        categoryMenuDTO.setOptionsMenus(options);
        categories.add(categoryMenuDTO);
        return categories;
    }
    
    @Test
    public void transformOptionsMenu_twoCategoriesThreeOptions() {
        CategoryMenu category = new CategoryMenu(2, "Tareas", 2, Status.ACTIVE,
                "fa-list", "bg-warning");
        CategoryMenu category2 = new CategoryMenu(3, "Gastos", 3, Status.ACTIVE,
                "fa-usd", "bg-success");
        Set<OptionMenu> options = new HashSet<>();
        options.add(new OptionMenu(6, "Proyecto", 1, "/project/init",
                Status.ACTIVE, "fa-cogs", EntityType.PUBLIC, category));
        options.add(new OptionMenu(7, "Tareas", 2, "/task/init",
                Status.ACTIVE, "fa-tasks", EntityType.PUBLIC, category));
        options.add(new OptionMenu(11, "Categorías", 1, "/category/init",
                Status.ACTIVE, "fa-folder-open", EntityType.PUBLIC, category2));
        
        List<CategoryMenuDTO> optionsDTO = appMenuService.transformOptionsMenu(options);
        assertThat(optionsDTO).isEqualTo(responseWithTwoCategoryAndThreeOptions(category, category2));
    }

    private List<CategoryMenuDTO> responseWithTwoCategoryAndThreeOptions(
            CategoryMenu categoryMenu, CategoryMenu categoryMenu2) {
        List<CategoryMenuDTO> categories = new ArrayList<>();
        
        CategoryMenuDTO categoryMenuDTO = new CategoryMenuDTO(2, "Tareas", 2,
                Status.ACTIVE, "fa-list", "bg-warning");
        Set<OptionMenu> options = new HashSet<>();
        options.add(new OptionMenu(6, "Proyecto", 1, "/project/init",
                Status.ACTIVE, "fa-cogs", EntityType.PUBLIC, categoryMenu));
        options.add(new OptionMenu(7, "Tareas", 2, "/task/init",
                Status.ACTIVE, "fa-tasks", EntityType.PUBLIC, categoryMenu));
        categoryMenuDTO.setOptionsMenus(options);
        categories.add(categoryMenuDTO);
        
        CategoryMenuDTO categoryMenuDTO2 = new CategoryMenuDTO(3, "Gastos", 3,
                Status.ACTIVE, "fa-usd", "bg-success");
        Set<OptionMenu> options2 = new HashSet<>();
        options2.add(new OptionMenu(11, "Categorías", 1, "/category/init",
                Status.ACTIVE, "fa-folder-open", EntityType.PUBLIC, categoryMenu2));
        categoryMenuDTO2.setOptionsMenus(options2);
        
        categories.add(categoryMenuDTO2);
        return categories;
    }
}
