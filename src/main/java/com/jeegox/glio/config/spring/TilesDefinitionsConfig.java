package com.jeegox.glio.config.spring;

import com.jeegox.glio.config.general.DataConfig;
import java.util.HashMap;
import java.util.Map;
import org.apache.tiles.Attribute;
import org.apache.tiles.Definition;
import org.apache.tiles.definition.DefinitionsFactory;
import org.apache.tiles.request.Request;

public class TilesDefinitionsConfig implements DefinitionsFactory {
    private static final Map<String, Definition> TILES_DEFINITIONS = new HashMap<>();
    private static final Attribute BASE_TEMPLATE = new Attribute("/WEB-INF/views/layout/default.jsp");
    private static final Attribute BASE_TEMPLATE_WITHOUT_HEADER = new Attribute("/WEB-INF/views/layout/defaultWithoutHeader.jsp");
    private static final Attribute HEADER_TEMPLATE = new Attribute("/WEB-INF/views/layout/emptyHeader.jsp");
    private static final Attribute EMPTY_TEMPLATE = new Attribute("/WEB-INF/views/layout/empty.jsp");

    @Override
    public Definition getDefinition(String string, Request rqst) {
        return TILES_DEFINITIONS.get(string);
    }
    
    public static void addDefinitions(){
        DataConfig.PAGES.forEach((page) -> {
            switch (page.getLayout()) {
                case EMPTY:
                    addEmpty(page.getName(),page.getUrl());
                    break;
                case DEFAULT:
                    addDefault(page.getName(), page.getUrl());
                    break;
                case DEFAULT_WITHOUT_HEADER:
                    addDefaultWithoutHeader(page.getName(), page.getUrl());
                    break;
                default:
                    addWithHeader(page.getName(), page.getUrl());
                    break;
            }
        });
    }
    
    private static void addDefault(String name, String body) {
        Map<String, Attribute> attributes = new HashMap<>();
        attributes.put("resources", new Attribute("/WEB-INF/views/layout/resources.jsp"));
        attributes.put("header", new Attribute("/WEB-INF/views/layout/header.jsp"));
        attributes.put("title", new Attribute(DataConfig.TITLE));
        attributes.put("body", new Attribute(body));
        TILES_DEFINITIONS.put(name, new Definition(name, BASE_TEMPLATE, attributes));
    }
    
    private static void addEmpty(String name, String body) {
        Map<String, Attribute> attributes = new HashMap<>();
        attributes.put("resources", new Attribute("/WEB-INF/views/layout/resources.jsp"));
        attributes.put("body", new Attribute(body));
        attributes.put("title", new Attribute(DataConfig.TITLE));
        
        TILES_DEFINITIONS.put(name, new Definition(name, EMPTY_TEMPLATE, attributes));
    }
    
    private static void addWithHeader(String name, String body) {
        Map<String, Attribute> attributes = new HashMap<>();
        attributes.put("title", new Attribute(DataConfig.TITLE));
        attributes.put("resources", new Attribute("/WEB-INF/views/layout/resources.jsp"));
        attributes.put("body", new Attribute(body));
        TILES_DEFINITIONS.put(name, new Definition(name, HEADER_TEMPLATE, attributes));
    }
    
    private static void addDefaultWithoutHeader(String name, String body) {
        Map<String, Attribute> attributes = new HashMap<>();
        attributes.put("title", new Attribute(DataConfig.TITLE));
        attributes.put("resources", new Attribute("/WEB-INF/views/layout/resources.jsp"));
        attributes.put("body", new Attribute(body));
        TILES_DEFINITIONS.put(name, new Definition(name, BASE_TEMPLATE_WITHOUT_HEADER, attributes));
    }
    
}
