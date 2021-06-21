package com.jeegox.glio.services.admin;

import com.jeegox.glio.dao.admin.OptionMenuDAO;
import com.jeegox.glio.dto.admin.CategoryMenuDTO;
import com.jeegox.glio.dto.admin.OptionMenuDTO;
import com.jeegox.glio.entities.admin.CategoryMenu;
import com.jeegox.glio.entities.admin.OptionMenu;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppMenuService {
    private final OptionMenuDAO optionMenuDAO;
    
    @Autowired
    public AppMenuService(OptionMenuDAO optionMenuDAO){
        this.optionMenuDAO = optionMenuDAO;
    }

    public Map<String, String> transformAllowedOptionsToMap(Set<OptionMenu> options){
        Map<String, String> mapaOptions = options.stream()
                .collect(Collectors.toMap(x -> x.getUrl().
                replace("/init", "").substring(1), x -> x.getUrl()));
        mapaOptions.put("resources", "");
        mapaOptions.put("all", "");
        return mapaOptions;
    }
    
    public List<CategoryMenuDTO> transformOptionsMenu(Set<OptionMenu> options){
        List<CategoryMenuDTO> categories = new ArrayList<>();
        Iterator<OptionMenu> it = options.iterator();

        Map<Integer, CategoryMenuDTO> mapCategory = new HashMap<>();

        while (it.hasNext()) {
            OptionMenu op = it.next();
            CategoryMenu catMenu = op.getFather();

            if (!mapCategory.containsKey(catMenu.getId())) {
                CategoryMenuDTO categoryMenuDTO = new CategoryMenuDTO(catMenu.getId(), catMenu.getName(),
                        catMenu.getOrder(), catMenu.getStatus(), catMenu.getIcon(), catMenu.getClazz());
                categoryMenuDTO.getOptionsMenus().add(op);
                mapCategory.put(catMenu.getId(), categoryMenuDTO);
            } else {
                CategoryMenuDTO categoryMenuDTO = mapCategory.get(catMenu.getId());
                categoryMenuDTO.getOptionsMenus().add(op);
            }
        }

        for (Map.Entry<Integer, CategoryMenuDTO> entry : mapCategory.entrySet()) {
            categories.add(entry.getValue());
        }
        return categories;
    }
    
    @Transactional
    public List<OptionMenuDTO> getPublicOptions(){
        return optionMenuDAO.getPublicOptions();
    }
    
    @Transactional
    public List<OptionMenu> findByIds(Integer[] ids) {
        return optionMenuDAO.findByIds(ids);
    }
}
